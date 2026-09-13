package com.mall.dto;

/**
 * 订单明细
 */
public class OrderItemDto {
    private Long id;
    private Long goodsId;
    private String goodsName;
    private String imageUrl;
    private Integer priceSnapshot;
    private Integer quantity;
    private String skuSnapshot;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String goodsName) { this.goodsName = goodsName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getPriceSnapshot() { return priceSnapshot; }
    public void setPriceSnapshot(Integer priceSnapshot) { this.priceSnapshot = priceSnapshot; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getSkuSnapshot() { return skuSnapshot; }
    public void setSkuSnapshot(String skuSnapshot) { this.skuSnapshot = skuSnapshot; }
}