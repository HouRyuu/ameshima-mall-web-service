package com.tmall.user.entity.po;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tmall.common.po.BasePO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Table(name="t_address")
public class AddressPO extends BasePO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer accountId;
    private String name;
    private String phone;
    private String provinceCode;
    private String province;
    private String cityCode;
    private String city;
    private String districtCode;
    private String district;
    private String detailedAddress;
    private short isDefault;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public short getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(short isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "AddressPO{" + "id=" + id + ", accountId=" + accountId + ", name='" + name + '\'' + ", phone='" + phone
                + '\'' + ", provinceCode='" + provinceCode + '\'' + ", province='" + province + '\'' + ", cityCode='"
                + cityCode + '\'' + ", city='" + city + '\'' + ", districtCode='" + districtCode + '\'' + ", district='"
                + district + '\'' + ", detailedAddress='" + detailedAddress + '\'' + ", isDefault=" + isDefault + '}';
    }
}
