package com.ameshima.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ameshima.order.constants.OrderConstants;
import com.ameshima.order.entity.vo.OrderLogisticsVO;
import com.ameshima.order.keys.OrderKey;
import com.ameshima.order.rabbitmq.MQSender;
import com.ameshima.order.utils.PayPayUtil;
import com.ameshima.remote.goods.api.IGoodsService;
import com.ameshima.remote.goods.dto.CartGoodsDTO;
import com.ameshima.remote.goods.dto.OrderAddressDTO;
import com.ameshima.remote.goods.utils.JsonUtils;
import com.ameshima.remote.goods.vo.ShopCartVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ameshima.common.constants.CommonErrResult;
import com.ameshima.common.constants.PayErrResultEnum;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.dto.LoginInfo;
import com.ameshima.common.dto.PageResult;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.order.entity.dto.OrderConditionDTO;
import com.ameshima.order.entity.dto.OrderMQDTO;
import com.ameshima.order.entity.po.OrderGoodsPO;
import com.ameshima.order.entity.po.OrderLogisticsPO;
import com.ameshima.order.entity.po.OrderPayPO;
import com.ameshima.order.entity.vo.OrderDetailVO;
import com.ameshima.order.mapper.OrderGoodsMapper;
import com.ameshima.order.mapper.OrderLogisticsMapper;
import com.ameshima.order.mapper.OrderPayMapper;
import com.ameshima.order.service.OrderService;
import com.ameshima.order.utils.ConvertToVO;
import jp.ne.paypay.model.PaymentState;
import jp.ne.paypay.model.QRCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    public static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private IGoodsService goodsService;
    @Resource
    private MQSender mqSender;
    @Resource
    private RedisClient redisClient;
    @Resource
    private OrderGoodsMapper orderGoodsMapper;
    @Resource
    private OrderLogisticsMapper orderLogisticsMapper;
    @Resource
    private OrderPayMapper orderPayMapper;
    @Resource
    private PayPayUtil payPayUtil;

    @Override
    public PublicResult<?> orderTuQueue(String cityCode, String address) {
        if (StringUtils.isBlank(cityCode) || StringUtils.isBlank(address)) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        OrderAddressDTO addressDTO = new OrderAddressDTO();
        addressDTO.setAccountId(LoginInfo.get().getAccountId());
        addressDTO.setCityCode(cityCode);
        PublicResult<List<ShopCartVO>> storeListRes = goodsService.goodsBySkus(addressDTO);
        if (storeListRes.getErrCode() != MallConstant.NO) {
            return storeListRes;
        }
        List<ShopCartVO> storeList = storeListRes.getData();
        if (CollectionUtils.isEmpty(storeList)) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        OrderMQDTO order = new OrderMQDTO();
        order.setAccountId(LoginInfo.get().getAccountId());
        order.setAddress(address);
        order.setStoreGoodsList(storeList);
        String parentNo = mqSender.sendOrder(order);
        return parentNo != null ? PublicResult.success(parentNo) : PublicResult.error();
    }

    @Override
    public PublicResult<Integer> getOrderQueueState(String parentOrderNo) {
        if (StringUtils.isBlank(parentOrderNo)) {
            return PublicResult.error();
        }
        Integer orderQueueState = redisClient.get(OrderKey.ORDER_MQ, parentOrderNo + MallConstant.UNDERLINE + LoginInfo.get().getAccountId());
        if (orderQueueState == null) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        } else if (orderQueueState == OrderConstants.OrderMqState.ERROR.getState()) {
            return PublicResult.error();
        }
        return PublicResult.success(orderQueueState);
    }

    @Override
    public PublicResult<List<OrderDetailVO>> findOrderGoodsList(String parentOrderNo, short orderState) {
        if (StringUtils.isBlank(parentOrderNo) || orderState < 0 || orderState > 5) {
            return PublicResult.error();
        }
        Example example = new Example(OrderGoodsPO.class);
        example.and().andEqualTo("accountId", LoginInfo.get().getAccountId())
                .andEqualTo("parentOrderNo", parentOrderNo)
                .andEqualTo("orderState", orderState)
                .andEqualTo("isDelete", MallConstant.NO);
        List<OrderGoodsPO> goodsList = orderGoodsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(goodsList)) {
            return PublicResult.success(Collections.emptyList());
        }
        List<String> orderNoList = goodsList.stream().map(OrderGoodsPO::getOrderNo).collect(Collectors.toList());
        example = new Example(OrderPayPO.class);
        example.and().andIn("orderNo", orderNoList)
                .andEqualTo("isDelete", MallConstant.NO);
        List<OrderPayPO> payList = orderPayMapper.selectByExample(example);
        example = new Example(OrderLogisticsPO.class);
        example.and().andIn("orderNo", orderNoList)
                .andEqualTo("isDelete", MallConstant.NO);
        List<OrderLogisticsPO> logisticsList = orderLogisticsMapper.selectByExample(example);
        return PublicResult.success(ConvertToVO.fromPO(goodsList, payList, logisticsList));
    }

    @Override
    public PublicResult<PageResult<OrderDetailVO>> orderPage(OrderConditionDTO condition) {
        PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        List<String> parentNoList = orderGoodsMapper.parentNoList(condition);
        PageInfo<String> orderPage = new PageInfo<>(parentNoList);
        if (CollectionUtils.isEmpty(parentNoList)) {
            return PublicResult.success(new PageResult<>());
        }
        Example example = new Example(OrderGoodsPO.class);
        example.and().andIn("parentOrderNo", parentNoList)
                .andEqualTo("isDelete", MallConstant.NO);
        if (condition.getStoreId() != null) {
            example.and().andEqualTo("storeId", condition.getStoreId());
        }
        List<OrderGoodsPO> goodsList = orderGoodsMapper.selectByExample(example);
        List<String> orderNoList = goodsList.stream().map(OrderGoodsPO::getOrderNo).collect(Collectors.toList());
        example = new Example(OrderPayPO.class);
        example.and().andIn("orderNo", orderNoList).andEqualTo("isDelete", MallConstant.NO);
        List<OrderPayPO> payList = orderPayMapper.selectByExample(example);
        example = new Example(OrderLogisticsPO.class);
        example.and().andIn("orderNo", orderNoList).andEqualTo("isDelete", MallConstant.NO);
        List<OrderLogisticsPO> logisticsList = orderLogisticsMapper.selectByExample(example);
        return PublicResult.success(new PageResult<>(condition.getPageSize(), condition.getPageIndex(), orderPage.getTotal(), ConvertToVO.fromPO(goodsList, payList, logisticsList)));
    }

    @Override
    public PublicResult<String> createPayPayCode(String parentOrderNo, String orderNo) {
        OrderGoodsPO payInfo = orderGoodsMapper.getPayInfo(MallConstant.PayStateEnum.DEFAULT.getState(), parentOrderNo, orderNo);
        if (payInfo == null) {
            // 支払いすでに完了
            return PublicResult.error(PayErrResultEnum.DONE);
        }
        String paymentId = MallConstant.ZERO_STR.equals(orderNo) ? parentOrderNo : orderNo;
        if (MallConstant.ZERO_STR.equals(payInfo.getPrice().toString())) {
            // 支払い必要ないので、会計を完成する
            return payOrder(parentOrderNo, orderNo, MallConstant.PayWayEnum.PAYPAY, paymentId);
        }
        PublicResult<QRCodeResponse> result = payPayUtil.createQRCode(new PayPayUtil.OrderInfo(paymentId,
                payInfo.getPrice().intValue(), MallConstant.SITE_NAME + ":" + payInfo.getStoreName()));
        if (result.getErrCode() == PublicResult.OK_CODE) {
            return PublicResult.success(result.getData().getUrl());
        }
        // 既に二次元コードを生成した
        if (result.getErrResult() == PayErrResultEnum.DUPLICATE
                || result.getErrResult() == PayErrResultEnum.EXPIRY) {
            PublicResult<String> statusResult = payPayStatus(parentOrderNo, orderNo);
            if (statusResult.getErrCode() == PublicResult.OK_CODE && StringUtils.isBlank(statusResult.getData())) {
                return PublicResult.error(PayErrResultEnum.DONE);
            }
            // 会計まだ完了してなければ、二次元コードを削除して、顧客に改めて支払わせる
            payPayUtil.deleteQRCode(paymentId);
        }
        return PublicResult.error(result.getErrCode(), result.getErrMsg());
    }

    @Override
    public PublicResult<String> getPayPayStatus(String parentOrderNo, String orderNo) {
        OrderGoodsPO payInfo = orderGoodsMapper.getPayInfo(MallConstant.PayStateEnum.DEFAULT.getState(), parentOrderNo, orderNo);
        if (payInfo == null) {
            // 支払いすでに完了
            return PublicResult.error(PayErrResultEnum.DONE);
        }
        if (MallConstant.ZERO_STR.equals(payInfo.getPrice().toString())) {
            // 支払い必要ない
            return payOrder(parentOrderNo, orderNo, MallConstant.PayWayEnum.PAYPAY,
                    MallConstant.ZERO_STR.equals(orderNo) ? parentOrderNo : orderNo);
        }
        return payPayStatus(parentOrderNo, orderNo);
    }

    private PublicResult<String> payPayStatus(String parentOrderNo, String orderNo) {
        String paymentId = MallConstant.ZERO_STR.equals(orderNo) ? parentOrderNo : orderNo;
        PublicResult<String> payDetail = payPayUtil.getPayDetail(paymentId);
        if (PaymentState.StatusEnum.COMPLETED.getValue().equals(payDetail.getData())) {
            // 支払完了
            LOGGER.info("parentOrderNo=>{},orderNo=>{}.PayPayで支払完了.RedisからOrderKey.PAYPAY_CODE[_ID]:{}を削除",
                    parentOrderNo, orderNo, paymentId);
            payDetail = payOrder(parentOrderNo, orderNo, MallConstant.PayWayEnum.PAYPAY, payDetail.getErrMsg());
            if (payDetail.getErrCode() == PublicResult.OK_CODE) {
                redisClient.removeKey(OrderKey.PAYPAY_CODE, paymentId);
                redisClient.removeKey(OrderKey.PAYPAY_CODE_ID, paymentId);
            }
        }
        return payDetail;
    }

    private PublicResult<String> payOrder(String parentOrderNo, String orderNo, MallConstant.PayWayEnum payWayEnum, String payNo) {
        if (StringUtils.isBlank(parentOrderNo)) {
            return PublicResult.error();
        }
        int accountId = LoginInfo.get().getAccountId();
        try {
            if (payOrder(parentOrderNo, orderNo, accountId, payWayEnum, payNo)) {
                LOGGER.info("accountId=>{}はparentOrderNo=>{},orderNo=>{}は支払ったので、注文状態を次に変える", accountId, parentOrderNo, orderNo);
                return PublicResult.success();
            } else {
                return PublicResult.error(CommonErrResult.ERR_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("accountId=>％1$dはparentOrderNo=>%2$s,orderNo=>%3$s支払いが失敗", accountId, parentOrderNo, orderNo), e);
        }
        return PublicResult.error();
    }

    @Override
    public PublicResult<?> receiveConfirm(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            return PublicResult.error();
        }
        int accountId = LoginInfo.get().getAccountId();
        try {
            Example example = new Example(OrderGoodsPO.class);
            example.and().andEqualTo("accountId", accountId)
                    .andEqualTo("orderNo", orderNo)
                    .andEqualTo("orderState", MallConstant.OrderStateEnum.DISPATCH.getState())
                    .andEqualTo("isDelete", MallConstant.NO);
            OrderGoodsPO orderGoodsPO = new OrderGoodsPO();
            orderGoodsPO.setOrderState(MallConstant.OrderStateEnum.NO_COMMENT.getState());
            if (orderGoodsMapper.updateByExampleSelective(orderGoodsPO, example) > 0) {
                LOGGER.info("accountId=>{}はorderNo=>{}の商品は届いたので、注文状態を次に変える", accountId, orderNo);
                return PublicResult.success();
            } else {
                return PublicResult.error(CommonErrResult.ERR_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("accountId=>％1$dはorderNo=>%2$s届いた確認が失敗", accountId, orderNo), e);
        }
        return PublicResult.error();
    }

    @Override
    public void generateOrder(String orderStr) {
        LOGGER.info("注文のメッセージキューを届けた => {}", orderStr);
        String mqStateKey = null;
        try {
            JSONObject jsonObject = JSON.parseObject(orderStr);
            OrderMQDTO orderMQ = jsonObject.toJavaObject(OrderMQDTO.class);
            orderMQ.setStoreGoodsList(JsonUtils.parse(jsonObject.getJSONArray("storeGoodsList")));
            mqStateKey = orderMQ.getParentOrderNo() + MallConstant.UNDERLINE + orderMQ.getAccountId();
            Integer mqStatus = redisClient.get(OrderKey.ORDER_MQ, mqStateKey);
            if (mqStatus == null || mqStatus.shortValue() != MallConstant.NO) {
                LOGGER.error("注文のメッセージキューの状態は違う => {}", orderStr);
                return;
            }
            generateOrder(orderStr, orderMQ);
            if (redisClient.set(OrderKey.ORDER_MQ, mqStateKey, OrderConstants.OrderMqState.OK.getState())) {
                LOGGER.info("注文のメッセージキューを成功に受けた => {}", orderStr);
                return;
            }
        } catch (Exception e) {
            LOGGER.error(String.format("注文のクリエートはエラーが発生した => %s", orderStr), e);
        }
        if (mqStateKey != null) {
            redisClient.set(OrderKey.ORDER_MQ, mqStateKey, OrderConstants.OrderMqState.ERROR.getState());
        }
    }

    @Override
    public boolean orderGoodsExists(int goodsId, short status) {
        OrderGoodsPO orderGoodsPO = new OrderGoodsPO();
        orderGoodsPO.setGoodsId(goodsId);
        orderGoodsPO.setOrderState(status);
        orderGoodsPO.setIsDelete(MallConstant.NO);
        return orderGoodsMapper.selectCount(orderGoodsPO) > 0;
    }

    @Override
    public PublicResult<?> delivery(OrderLogisticsVO logistics) {
        return deliveryWithTran(logistics) == MallConstant.YES ? PublicResult.success() : PublicResult.error();
    }

    @Transactional
    short deliveryWithTran(OrderLogisticsVO logistics) {
        Example example = new Example(OrderLogisticsPO.class);
        example.and().andEqualTo("orderNo", logistics.getOrderNo())
                .andEqualTo("logisticsState", MallConstant.LogisticsStateEnum.NO_DISPATCH.getState())
                // .andLike("targetAddress", MallConstant.PERSENT + logistics.getTargetAddress() + MallConstant.PERSENT)
                .andEqualTo("isDelete", MallConstant.NO);
        OrderLogisticsPO record = new OrderLogisticsPO();
        record.setLogisticsCompany(logistics.getLogisticsCompany());
        record.setTrackingNo(logistics.getTrackingNo());
        record.setLogisticsState(MallConstant.LogisticsStateEnum.DISPATCH.getState());
        if (orderLogisticsMapper.updateByExampleSelective(record, example) == 0) {
            return MallConstant.NO;
        }
        example = new Example(OrderGoodsPO.class);
        example.and().andEqualTo("orderNo", logistics.getOrderNo())
                .andEqualTo("storeId", LoginInfo.get().getStoreId())
                .andEqualTo("orderState", MallConstant.OrderStateEnum.NO_DISPATCH.getState())
                .andEqualTo("isDelete", MallConstant.NO);
        OrderGoodsPO orderGoods = new OrderGoodsPO();
        orderGoods.setOrderState(MallConstant.OrderStateEnum.DISPATCH.getState());
        if (orderGoodsMapper.updateByExampleSelective(orderGoods, example) == 0) {
            return MallConstant.NO;
        }
        return MallConstant.YES;
    }

    @Transactional
    void generateOrder(String orderStr, OrderMQDTO orderMQ) {
        createOrderGoods(orderMQ);
        LOGGER.info("注文したグッズのクリエート完了 => {}", orderStr);
        createOrderLogistics(orderMQ);
        LOGGER.info("注文した配送情報のクリエート完了 => {}", orderStr);
        createOrderPay(orderMQ);
        LOGGER.info("注文した支払いのクリエート完了 => {}", orderStr);
        goodsService.skuOrdered(orderMQ.getAccountId());
        LOGGER.info("注文したグッズの在庫を減らして未注文のキャッシュの削除完了 => {}", orderStr);
    }

    private void createOrderGoods(OrderMQDTO orderMQ) {
        List<ShopCartVO> storeGoodsList = orderMQ.getStoreGoodsList();
        List<OrderGoodsPO> orderGoodsList = new ArrayList<>();
        String parentOrderNo = orderMQ.getParentOrderNo();
        Date now = new Date();
        for (ShopCartVO storeGoods : storeGoodsList) {
            orderGoodsList.addAll(storeGoods.getGoodsList().stream().map(goods -> {
                OrderGoodsPO orderGoods = new OrderGoodsPO();
                orderGoods.setParentOrderNo(parentOrderNo);
                orderGoods.setOrderNo(storeGoods.getOrderNo());
                orderGoods.setAccountId(orderMQ.getAccountId());
                orderGoods.setStoreId(storeGoods.getStoreId());
                orderGoods.setStoreName(storeGoods.getStoreName());
                orderGoods.setGoodsId(goods.getGoodsId());
                orderGoods.setImgUrl(goods.getImgUrl());
                orderGoods.setGoodsName(goods.getName());
                orderGoods.setSkuId(goods.getSkuId());
                orderGoods.setAttrsJson(goods.getAttrsJson());
                orderGoods.setPrice(goods.getPrice());
                orderGoods.setMarketPrice(goods.getMarketPrice());
                orderGoods.setGoodsLocation(goods.getLocation());
                orderGoods.setOrderNum(goods.getAmount());
                orderGoods.setFreight(goods.getFreight());
                orderGoods.setOrderState(MallConstant.OrderStateEnum.NO_PAY.getState());
                orderGoods.setCreateTime(now);
                orderGoods.setIsDelete(MallConstant.NO);
                return orderGoods;
            }).collect(Collectors.toList()));
        }
        orderGoodsMapper.insertList(orderGoodsList);
    }

    private void createOrderLogistics(OrderMQDTO orderMQ) {
        Table<Integer, String, OrderLogisticsPO> orderLogMap = HashBasedTable.create();
        Date now = new Date();
        orderMQ.getStoreGoodsList().forEach(store -> store.getGoodsList().forEach(goods -> {
            // 每个门店每个城市只生成一条物流记录
            if (!orderLogMap.contains(store.getStoreId(), goods.getLocation())) {
                OrderLogisticsPO orderLogistics = new OrderLogisticsPO();
                orderLogistics.setOrderNo(store.getOrderNo());
                orderLogistics.setGoodsLocation(goods.getLocation());
                orderLogistics.setTargetAddress(orderMQ.getAddress());
                orderLogistics.setLogisticsState(MallConstant.LogisticsStateEnum.NO_DISPATCH.getState());
                orderLogistics.setCreateTime(now);
                orderLogistics.setIsDelete(MallConstant.NO);
                orderLogMap.put(store.getStoreId(), goods.getLocation(), orderLogistics);
            }
        }));
        orderLogisticsMapper.insertList(new ArrayList<>(orderLogMap.values()));
    }

    private void createOrderPay(OrderMQDTO orderMQ) {
        Date now = new Date();
        List<OrderPayPO> payList = orderMQ.getStoreGoodsList().stream().map(store -> {
            OrderPayPO pay = new OrderPayPO();
            pay.setAccountId(orderMQ.getAccountId());
            pay.setOrderNo(store.getOrderNo());
            BigDecimal dealPrice = new BigDecimal(0);
            // 合計＝単価＊数量＋送料
            for (CartGoodsDTO goods : store.getGoodsList()) {
                dealPrice = dealPrice.add(goods.getPrice().multiply(new BigDecimal(goods.getAmount()))).add(goods.getFreight());
            }
            pay.setDealPrice(dealPrice);
            pay.setPayState(MallConstant.PayStateEnum.DEFAULT.getState());
            pay.setCreateTime(now);
            pay.setIsDelete(MallConstant.NO);
            return pay;
        }).collect(Collectors.toList());
        orderPayMapper.insertList(payList);
    }

    @Transactional
    boolean payOrder(String parentOrderNo, String orderNo, int accountId, MallConstant.PayWayEnum payWay, String payNo) {
        OrderPayPO orderPayPO = new OrderPayPO();
        orderPayPO.setPayState(MallConstant.PayStateEnum.DONE.getState());
        orderPayPO.setPayWay(payWay.getCode());
        orderPayPO.setPayNo(payNo);
        // 支払う状態変更
        //　parentOrderNoで全部の注文を支払い
        if (!MallConstant.ZERO_STR.equals(parentOrderNo) && MallConstant.ZERO_STR.equals(orderNo)) {
            orderPayPO.setAccountId(accountId);
            orderPayPO.setOrderNo(parentOrderNo);
            if (orderPayMapper.payByParentOrderNo(orderPayPO) < 1) {
                return false;
            }
        } else {
            Example example = new Example(OrderPayPO.class);
            example.and().andEqualTo("orderNo", orderNo)
                    .andEqualTo("accountId", accountId)
                    .andEqualTo("payState", MallConstant.PayStateEnum.DEFAULT.getState())
                    .andEqualTo("isDelete", MallConstant.NO);
            if (orderPayMapper.updateByExampleSelective(orderPayPO, example) < 1) {
                return false;
            }
        }
        // 注文状態変更
        Example example = new Example(OrderGoodsPO.class);
        example.and().andEqualTo("accountId", accountId)
                .andEqualTo("orderState", MallConstant.OrderStateEnum.NO_PAY.getState())
                .andEqualTo("isDelete", MallConstant.NO);
        if (!MallConstant.ZERO_STR.equals(parentOrderNo)) {
            example.and().andEqualTo("parentOrderNo", parentOrderNo);
        }
        if (!MallConstant.ZERO_STR.equals(orderNo)) {
            example.and().andEqualTo("orderNo", orderNo);
        }
        OrderGoodsPO orderGoodsPO = new OrderGoodsPO();
        orderGoodsPO.setOrderState(MallConstant.OrderStateEnum.NO_DISPATCH.getState());
        return orderGoodsMapper.updateByExampleSelective(orderGoodsPO, example) > 0;
    }

}
