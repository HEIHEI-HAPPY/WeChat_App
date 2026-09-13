package com.mall.dto;

import jakarta.validation.constraints.NotNull;

/**
 * 创建订单请求
 * 默认从购物车拿所有条目；addressId 必填；remark 可选
 */
public class OrderCreateRequest {

    @NotNull(message = "请选择收货地址")
    private Long addressId;

    /** 1=到店自取 2=外卖配送 */
    private Integer pickupType = 1;

    private String remark;

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }

    public Integer getPickupType() { return pickupType; }
    public void setPickupType(Integer pickupType) { this.pickupType = pickupType; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}