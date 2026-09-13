package com.mall.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车项（Redis 存储的实体）
 * 用 Serializable + 无 final 字段，确保 Jackson / JDK 序列化都能用
 */
public class CartItemDto implements Serializable {

    private String itemId;          // UUID
    private Long goodsId;
    private String goodsName;
    private String imageUrl;
    private Integer priceSnapshot;   // 加购时价格快照（分）
    private String sku;              // JSON：{"杯型":"中杯","温度":"热"}
    private Integer quantity;
    private Long storeId;
    private LocalDateTime addedAt;

    public CartItemDto() {}

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public Long getGoodsId() { return goodsId; }
    public void setGoodsId(Long goodsId) { this.goodsId = goodsId; }

    public String getGoodsName() { return goodsName; }
    public void setGoodsName(String goodsName) { this.goodsName = goodsName; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getPriceSnapshot() { return priceSnapshot; }
    public void setPriceSnapshot(Integer priceSnapshot) { this.priceSnapshot = priceSnapshot; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}