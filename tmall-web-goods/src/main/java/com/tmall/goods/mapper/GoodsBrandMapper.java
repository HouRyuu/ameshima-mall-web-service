package com.tmall.goods.mapper;

import java.util.List;

import com.tmall.goods.entity.dto.GoodsBrandDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsBrandMapper {

    int getStoreBrandCount(GoodsBrandDTO queryParam);

    List<GoodsBrandDTO> findStoreBrands(GoodsBrandDTO queryParam);

}
