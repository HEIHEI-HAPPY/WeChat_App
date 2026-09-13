package com.mall.service;

import com.mall.dto.UserDto;
import com.mall.entity.WxUser;
import com.mall.exception.ApiException;
import com.mall.repository.WxUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeService {

    private final WxUserRepository userRepo;

    public MeService(WxUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public UserDto getMe(Long userId) {
        WxUser u = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(404, "用户不存在"));
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setOpenid(u.getOpenid());
        dto.setNickname(u.getNickname());
        dto.setAvatarUrl(u.getAvatarUrl());
        dto.setPhone(u.getPhone());
        dto.setLevel(u.getLevel());
        dto.setPoints(u.getPoints());
        return dto;
    }

    @Transactional
    public UserDto updateMe(Long userId, UserDto patch) {
        WxUser u = userRepo.findById(userId)
                .orElseThrow(() -> new ApiException(404, "用户不存在"));
        if (patch.getNickname() != null) u.setNickname(patch.getNickname());
        if (patch.getAvatarUrl() != null) u.setAvatarUrl(patch.getAvatarUrl());
        if (patch.getPhone() != null) u.setPhone(patch.getPhone());
        userRepo.save(u);
        return getMe(userId);
    }
}