package com.mall.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 商品表
 * 见 PROJECT_MAP.md 第五章 goods
 */
@Entity
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;

    /** 价格存「分」，前端展示时 /100。PROJECT_MAP 全局规则要求 */
    @Column(name = "base_price", nullable = false)
    private Integer basePrice;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(length = 1000)
    private String description;

    @Column(length = 2000)
    private String specs;   // JSON 字符串：杯型/温度/糖度/奶类

    @Column(length = 2000)
    private String addons;  // JSON 字符串：加料选项

    private Integer stock = 0;

    @Column(length = 500)
    private String sellTime; // JSON 字符串：售卖时段

    /** 1=上架 0=下架 */
    private Integer status = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() { this.createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getBasePrice() { return basePrice; }
    public void setBasePrice(Integer basePrice) { this.basePrice = basePrice; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSpecs() { return specs; }
    public void setSpecs(String specs) { this.specs = specs; }

    public String getAddons() { return addons; }
    public void setAddons(String addons) { this.addons = addons; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getSellTime() { return sellTime; }
    public void setSellTime(String sellTime) { this.sellTime = sellTime; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}