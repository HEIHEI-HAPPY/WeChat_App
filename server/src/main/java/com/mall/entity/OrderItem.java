package com.mall.entity;

import jakarta.persistence.*;

/**
 * 订单商品明细
 */
@Entity
@Table(name = "order_items", indexes = {
        @Index(name = "idx_order_items_order", columnList = "order_id")
})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /** 下单时快照价（分）—— 商品改价不影响历史订单 */
    @Column(name = "price_snapshot", nullable = false)
    private Integer priceSnapshot;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "sku_snapshot", length = 1000)
    private String skuSnapshot;  // JSON

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public Integer getPriceSnapshot() { return priceSnapshot; }
    public void setPriceSnapshot(Integer priceSnapshot) { this.priceSnapshot = priceSnapshot; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getSkuSnapshot() { return skuSnapshot; }
    public void setSkuSnapshot(String skuSnapshot) { this.skuSnapshot = skuSnapshot; }
}