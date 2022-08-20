package com.tmall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.tmall.common.constants.CommonErrResult;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.PageResult;
import com.tmall.common.dto.PublicResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.order.constants.OrderConstants;
import com.tmall.order.entity.dto.OrderConditionDTO;
import com.tmall.order.entity.dto.OrderMQDTO;
import com.tmall.order.entity.po.OrderGoodsPO;
import com.tmall.order.entity.po.OrderLogisticsPO;
import com.tmall.order.entity.po.OrderPayPO;
import com.tmall.order.entity.vo.OrderDetailVO;
import com.tmall.order.keys.OrderKey;
import com.tmall.order.mapper.OrderGoodsMapper;
import com.tmall.order.mapper.OrderLogisticsMapper;
import com.tmall.order.mapper.OrderPayMapper;
import com.tmall.order.rabbitmq.MQSender;
import com.tmall.order.service.OrderService;
import com.tmall.order.utils.ConvertToVO;
import com.tmall.remote.goods.api.IGoodsService;
import com.tmall.remote.goods.dto.CartGoodsDTO;
import com.tmall.remote.goods.dto.OrderAddressDTO;
import com.tmall.remote.goods.utils.JsonUtils;
import com.tmall.remote.goods.vo.ShopCartVO;
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

    @Override
    public PublicResult<?> orderTuQueue(String cityCode, String address) {
        if (StringUtils.isBlank(cityCode) || StringUtils.isBlank(address)) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        OrderAddressDTO addressDTO = new OrderAddressDTO();
        addressDTO.setAccountId(LoginInfo.get().getAccountId());
        addressDTO.setCityCode(cityCode);
        PublicResult<List<ShopCartVO>> storeListRes = goodsService.goodsBySkus(addressDTO);
        if (storeListRes.getErrCode() != TmallConstant.NO) {
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
        Integer orderQueueState = redisClient.get(OrderKey.ORDER_MQ, parentOrderNo + TmallConstant.UNDERLINE + LoginInfo.get().getAccountId());
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
                .andCondition("is_delete=", TmallConstant.NO);
        List<OrderGoodsPO> goodsList = orderGoodsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(goodsList)) {
            return PublicResult.success(Collections.emptyList());
        }
        List<String> orderNoList = goodsList.stream().map(OrderGoodsPO::getOrderNo).collect(Collectors.toList());
        example = new Example(OrderPayPO.class);
        example.and().andIn("orderNo", orderNoList).andCondition("is_delete=", TmallConstant.NO);
        List<OrderPayPO> payList = orderPayMapper.selectByExample(example);
        example = new Example(OrderLogisticsPO.class);
        example.and().andIn("orderNo", orderNoList).andCondition("is_delete=", TmallConstant.NO);
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
                .andCondition("is_delete=", TmallConstant.NO);
        List<OrderGoodsPO> goodsList = orderGoodsMapper.selectByExample(example);
        List<String> orderNoList = goodsList.stream().map(OrderGoodsPO::getOrderNo).collect(Collectors.toList());
        example = new Example(OrderPayPO.class);
        example.and().andIn("orderNo", orderNoList).andCondition("is_delete=", TmallConstant.NO);
        List<OrderPayPO> payList = orderPayMapper.selectByExample(example);
        example = new Example(OrderLogisticsPO.class);
        example.and().andIn("orderNo", orderNoList).andCondition("is_delete=", TmallConstant.NO);
        List<OrderLogisticsPO> logisticsList = orderLogisticsMapper.selectByExample(example);
        return PublicResult.success(new PageResult<>(condition.getPageSize(), condition.getPageIndex(), orderPage.getTotal(), ConvertToVO.fromPO(goodsList, payList, logisticsList)));
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
                    .andEqualTo("orderState", TmallConstant.OrderStateEnum.DISPATCH.getState())
                    .andCondition("is_delete=", TmallConstant.NO);
            OrderGoodsPO orderGoodsPO = new OrderGoodsPO();
            orderGoodsPO.setOrderState(TmallConstant.OrderStateEnum.NO_COMMENT.getState());
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
            mqStateKey = orderMQ.getParentOrderNo() + TmallConstant.UNDERLINE + orderMQ.getAccountId();
            Integer mqStatus = redisClient.get(OrderKey.ORDER_MQ, mqStateKey);
            if (mqStatus == null || mqStatus.shortValue() != TmallConstant.NO) {
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
                orderGoods.setOrderState(TmallConstant.OrderStateEnum.NO_PAY.getState());
                orderGoods.setCreateTime(now);
                orderGoods.setIsDelete(TmallConstant.NO);
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
                orderLogistics.setLogisticsState(TmallConstant.LogisticsStateEnum.NO_DISPATCH.getState());
                orderLogistics.setCreateTime(now);
                orderLogistics.setIsDelete(TmallConstant.NO);
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
                dealPrice = dealPrice.add(goods.getPrice().multiply(new BigDecimal(goods.getAmount()).add(goods.getFreight())));
            }
            pay.setDealPrice(dealPrice);
            pay.setPayState(TmallConstant.PayStateEnum.DEFAULT.getState());
            pay.setCreateTime(now);
            pay.setIsDelete(TmallConstant.NO);
            return pay;
        }).collect(Collectors.toList());
        orderPayMapper.insertList(payList);
    }
}
