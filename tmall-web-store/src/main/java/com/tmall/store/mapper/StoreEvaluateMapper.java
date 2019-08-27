package com.tmall.store.mapper;

import org.apache.ibatis.annotations.Param;

import com.tmall.store.entity.dto.StoreEvaluateDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface StoreEvaluateMapper {

    StoreEvaluateDTO getStoreEvaluate(@Param("storeId") int storeId);

}
