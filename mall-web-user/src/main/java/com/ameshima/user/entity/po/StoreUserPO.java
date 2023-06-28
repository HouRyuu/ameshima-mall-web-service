package com.ameshima.user.entity.po;

import com.ameshima.common.po.BasePO;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Table(name = "t_store_user")
public class StoreUserPO extends BasePO {

    @Id
    private Integer accountId;
    private Integer storeId;
    private String storeName;
    private String logo;
    private String businessLicense;

    public StoreUserPO() {
    }

    public StoreUserPO(Integer accountId, Integer storeId, String storeName, String businessLicense) {
        this.accountId = accountId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.businessLicense = businessLicense;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }
}
