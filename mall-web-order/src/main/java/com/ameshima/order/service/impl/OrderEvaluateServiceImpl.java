package com.ameshima.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ameshima.common.constants.CommonErrResult;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.dto.LoginInfo;
import com.ameshima.common.dto.LoginUser;
import com.ameshima.common.dto.PageResult;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.order.entity.po.OrderEvaluatePO;
import com.ameshima.order.entity.po.OrderGoodsPO;
import com.ameshima.order.entity.vo.OrderEvaluateVO;
import com.ameshima.order.mapper.OrderEvaluateMapper;
import com.ameshima.order.mapper.OrderGoodsMapper;
import com.ameshima.order.service.OrderEvaluateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Service
public class OrderEvaluateServiceImpl implements OrderEvaluateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEvaluateServiceImpl.class);

    @Resource
    private OrderEvaluateMapper orderEvaluateMapper;
    @Resource
    private OrderGoodsMapper orderMapper;

    @Override

    public int goodsEvaluateCount(int goodsId) {
        Example example = new Example(OrderEvaluatePO.class);
        example.and().andEqualTo("goodsId", goodsId).andCondition("is_delete=", MallConstant.NO);
        return orderEvaluateMapper.selectCountByExample(example);
    }

    @Override
    public PublicResult<?> create(OrderEvaluateVO evaluate) {
        if (evaluate == null || evaluate.getGoodsId() == null || evaluate.getSkuId() == null || StringUtils.isBlank(evaluate.getOrderNo())
                || StringUtils.isBlank(evaluate.getEvaluateText()) || evaluate.getDescScore() == null) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        LoginUser loginUser = LoginInfo.get();
        OrderEvaluatePO evaluatePO = new OrderEvaluatePO();
        evaluatePO.setAccountId(loginUser.getAccountId());
        evaluatePO.setAvatar(loginUser.getAvatar());
        evaluatePO.setNickName(loginUser.getNickName());
        evaluatePO.setOrderNo(evaluate.getOrderNo());
        evaluatePO.setGoodsId(evaluate.getGoodsId());
        evaluatePO.setSkuId(evaluate.getSkuId());
        evaluatePO.setDescScore(evaluate.getDescScore());
        evaluatePO.setEvaluateText(evaluate.getEvaluateText());
        try {
            if (orderComment(evaluatePO)) {
                return PublicResult.success();
            } else {
                return PublicResult.error(CommonErrResult.ERR_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("accountId=>%1$dは商品のコメント=>%2$sがエラーになった", loginUser.getAccountId(), JSON.toJSONString(evaluate)), e);
        }
        return PublicResult.error();
    }

    @Override
    public PublicResult<PageResult<OrderEvaluateVO>> evaluatePage(OrderEvaluateVO condition) {
        if (condition == null || condition.getGoodsId() == null) {
            return PublicResult.error();
        }
        PageHelper.startPage(condition.getPageIndex(), condition.getPageSize());
        PageInfo<OrderEvaluateVO> evaluatePage = new PageInfo<>(orderEvaluateMapper.evaluateList(condition));
        return PublicResult.success(new PageResult<>(condition.getPageSize(), condition.getPageIndex(), evaluatePage.getTotal(), evaluatePage.getList()));
    }

    @Transactional
    boolean orderComment(OrderEvaluatePO evaluatePO) {
        OrderGoodsPO order = new OrderGoodsPO();
        order.setOrderState(MallConstant.OrderStateEnum.DONE.getState());
        Example example = new Example(OrderGoodsPO.class);
        example.and().andEqualTo("orderNo", evaluatePO.getOrderNo())
                .andEqualTo("accountId", evaluatePO.getAccountId())
                .andEqualTo("skuId", evaluatePO.getSkuId())
                .andEqualTo("orderState", MallConstant.OrderStateEnum.NO_COMMENT.getState())
                .andCondition("is_delete=", MallConstant.NO);
        return orderMapper.updateByExampleSelective(order, example) > 0 && orderEvaluateMapper.insertSelective(evaluatePO) > 0;
    }
}
