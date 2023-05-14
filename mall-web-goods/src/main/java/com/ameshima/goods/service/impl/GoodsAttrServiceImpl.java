package com.ameshima.goods.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ameshima.common.constants.CommonErrResult;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.goods.entity.po.GoodsAttrMapPO;
import com.ameshima.goods.entity.po.GoodsAttrPO;
import com.ameshima.goods.mapper.GoodsAttrMapper;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.goods.entity.dto.GoodsAttrMapDTO;
import com.ameshima.goods.keys.GoodsKey;
import com.ameshima.goods.mapper.GoodsAttrMapMapper;
import com.ameshima.goods.service.GoodsAttrService;
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
public class GoodsAttrServiceImpl implements GoodsAttrService {

    @Resource
    private GoodsAttrMapper attrMapper;
    @Resource
    private GoodsAttrMapMapper attrMapMapper;
    @Resource
    private RedisClient redisClient;

    @Override
    public List<Map<String, Object>> findGoodsAttrList(int goodsId) {
        return redisClient.get(GoodsKey.GOODS_ATTRS, goodsId,
                () -> convertAttrs(attrMapMapper.findGoodsAttrList(goodsId)));
    }

    @Override
    public List<GoodsAttrMapDTO> findAttrList() {
        GoodsAttrPO attrPO = new GoodsAttrPO();
        attrPO.setIsDelete(MallConstant.NO);
        return attrMapper.select(attrPO).stream().map(attr -> new GoodsAttrMapDTO(attr.getId(), attr.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<GoodsAttrMapDTO> findAttrMapList(int goodsId) {
        return attrMapMapper.findGoodsAttrList(goodsId);
    }

    @Override
    public PublicResult<Integer> saveAttrMap(GoodsAttrMapDTO attrMap) {
        GoodsAttrMapPO attrMapPO = new GoodsAttrMapPO();
        attrMapPO.setId(attrMap.getId());
        attrMapPO.setGoodsId(attrMap.getGoodsId());
        attrMapPO.setAttrId(attrMap.getAttrId());
        attrMapPO.setTxtValue(attrMap.getTxtValue());
        // attrMapPO.setImgValue(attrMap.getImgValue());
        redisClient.removeKey(GoodsKey.GOODS_ATTRS, attrMapPO.getGoodsId());
        if (attrMap.getId() == null && attrMapMapper.insertSelective(attrMapPO) == 1) {
            return PublicResult.success(attrMapPO.getId());
        }
        Example example = new Example(GoodsAttrMapPO.class);
        example.and().andEqualTo("id", attrMapPO.getId())
                .andEqualTo("goodsId", attrMapPO.getGoodsId())
                .andEqualTo("isDelete", MallConstant.NO);
        if (attrMapMapper.updateByExampleSelective(attrMapPO, example) > 0) {
            return PublicResult.success(attrMapPO.getId());
        }
        return PublicResult.error();
    }

    @Override
    public PublicResult<?> deleteAttrMap(int id, int goodsId) {
        if (id < 1 && goodsId < 1) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        GoodsAttrMapPO attrMapPO = new GoodsAttrMapPO();
        attrMapPO.setIsDelete(MallConstant.YES);
        redisClient.removeKey(GoodsKey.GOODS_ATTRS, goodsId);
        Example example = new Example(GoodsAttrMapPO.class);
        example.and().andEqualTo("id", id)
                .andEqualTo("goodsId", goodsId)
                .andEqualTo("isDelete", MallConstant.NO);
        if (attrMapMapper.updateByExampleSelective(attrMapPO, example) > 0) {
            return PublicResult.success();
        }
        return PublicResult.error();
    }

    private List<Map<String, Object>> convertAttrs(List<GoodsAttrMapDTO> goodsAttrList) {
        List<Map<String, Object>> attrList = Lists.newArrayList();
        Map<String, Collection<GoodsAttrMapDTO>> attrMap = Multimaps.index(goodsAttrList, GoodsAttrMapDTO::getAttrName).asMap();
        Set<Map.Entry<String, Collection<GoodsAttrMapDTO>>> entrySet = attrMap.entrySet();
        Map<String, Object> tmp;
        for (Map.Entry<String, Collection<GoodsAttrMapDTO>> entry : entrySet) {
            tmp = Maps.newHashMap();
            tmp.put("key", entry.getKey());
            tmp.put("value", Lists.newArrayList(entry.getValue()));
            attrList.add(tmp);
        }
        return attrList;
    }
}
