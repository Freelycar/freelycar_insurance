package com.freelycar.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class TasUserInfo {
    /**
     * 主键 自增
     */
    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer id;

    /**
     * 公众号openId
     */
    private String openId;

    /**
     * 开发平台的unionId
     */
    private String unionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Override
    public String toString() {
        return "TasUserInfo{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", unionId='" + unionId + '\'' +
                '}';
    }
}
