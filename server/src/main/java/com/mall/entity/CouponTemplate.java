package com.mall.entity;

import jakarta.persistence.*;

/**
 * 优惠券模板
 */
@Entity
@Table(name = "coupon_templates")
public class CouponTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /** 1=满减 2=折扣 */
    private Integer type;

    /** 满减额 / 折扣率（百分比） */
    private Integer amount;

    /** 满减门槛（分） */
    private Integer threshold;

    /** 领取后有效天数 */
    @Column(name = "valid_days")
    private Integer validDays;

    /** 1=启用 0=停发 */
    private Integer status = 1;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }

    public Integer getValidDays() { return validDays; }
    public void setValidDays(Integer validDays) { this.validDays = validDays; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}