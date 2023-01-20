package com.tmall.basicData.entity.dto;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
