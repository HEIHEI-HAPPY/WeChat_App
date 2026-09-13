package com.mall.dto;

/**
 * /api/me 返回的用户信息
 */
public class UserDto {

    private Long id;
    private String openid;
    private String nickname;
    private String avatarUrl;
    private String phone;
    private Integer level;
    private Integer points;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOpenid() { return openid; }
    public void setOpenid(String openid) { this.openid = openid; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}