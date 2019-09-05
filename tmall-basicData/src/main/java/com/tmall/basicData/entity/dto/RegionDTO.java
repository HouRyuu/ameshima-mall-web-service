package com.tmall.basicData.entity.dto;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class RegionDTO {

    private short regionLevel;
    private String regionCode;
    private String regionName;
    private String parentCode;

    public RegionDTO() {
    }

    public RegionDTO(short regionLevel) {
        this.regionLevel = regionLevel;
    }

    public RegionDTO(String parentCode) {
        this.parentCode = parentCode;
    }

    public short getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(short regionLevel) {
        this.regionLevel = regionLevel;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
