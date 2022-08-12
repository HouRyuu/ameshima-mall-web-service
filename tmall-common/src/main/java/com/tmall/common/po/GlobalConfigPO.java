package com.tmall.common.po;

import javax.persistence.Id;
import javax.persistence.Table;

import com.tmall.common.constants.TmallConstant;
import org.springframework.util.Assert;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
