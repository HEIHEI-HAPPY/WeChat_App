package com.mall.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情（包含明细 + 地址）
 */
public class OrderDto {

    private Long id;
    private String orderNo;
    private Integer total;
    /** 0待支付 1已支付 2制作中 3待取餐 4已完成 5已取消 6已退款 */
    private Integer status;
    private Integer pickupType;
    private Long storeId;
    private Long addressId;
    private AddressDto address;
    private String remark;
    private LocalDateTime paidAt;
    private String transactionId;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getPickupType() { return pickupType; }
    public void setPickupType(Integer pickupType) { this.pickupType = pickupType; }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }

    public AddressDto getAddress() { return address; }
    public void setAddress(AddressDto address) { this.address = address; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
}