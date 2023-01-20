package com.tmall.common.po;

import javax.persistence.Id;
import javax.persistence.Table;

import com.tmall.common.constants.TmallConstant;
import org.springframework.util.Assert;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Table(name = "t_global_config")
public class GlobalConfigPO extends BasePO {

    @Id
    private Integer id;
    private String category;
    private String name;
    private String value;

    public GlobalConfigPO() {
    }

    public GlobalConfigPO(String configName) {
        Assert.hasText(configName, "configName can not be empty.");
        String[] names = configName.split(TmallConstant.UNDERLINE);
        this.category = names[0];
        this.name = names[1];
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
