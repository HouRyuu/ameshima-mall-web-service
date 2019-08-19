package com.tmall.goods.mapper;

import java.util.List;

import com.tmall.goods.entity.dto.GuessLikeQueryDTO;
import org.apache.ibatis.annotations.Param;

import com.tmall.goods.entity.dto.GoodsGridDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsMapper {

    List<GoodsGridDTO> findPromote(@Param("promoteId") int promoteId);

    List<GoodsGridDTO> findByCategories(GuessLikeQueryDTO queryParam);

}
