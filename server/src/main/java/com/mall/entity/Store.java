package com.mall.entity;

import jakarta.persistence.*;

/**
 * 门店
 */
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String address;

    /** 纬度 */
    private Double lat;

    /** 经度 */
    private Double lng;

    @Column(name = "business_hours", length = 500)
    private String businessHours;  // JSON: {"open":"08:00","close":"22:00"}

    /** 1=营业 0=关店 */
    private Integer status = 1;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

    public String getBusinessHours() { return businessHours; }
    public void setBusinessHours(String businessHours) { this.businessHours = businessHours; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}