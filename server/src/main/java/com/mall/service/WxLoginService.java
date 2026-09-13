package com.mall.service;

import com.mall.dto.WxLoginResponse;
import com.mall.entity.WxUser;
import com.mall.exception.ApiException;
import com.mall.repository.WxUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 微信登录业务：换 openid → upsert 用户 → 签 token
 */
@Service
public class WxLoginService {

    private final WeChatService weChatService;
    private final WxUserRepository userRepo;
    private final JwtService jwtService;

    public WxLoginService(WeChatService weChatService,
                          WxUserRepository userRepo,
                          JwtService jwtService) {
        this.weChatService = weChatService;
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }

    @Transactional
    public WxLoginResponse login(String code) {
        if (code == null || code.isEmpty()) {
            throw new ApiException(400, "code 不能为空");
        }

        String openid = weChatService.code2openid(code);
        WxUser user = userRepo.findByOpenid(openid).orElseGet(() -> {
            WxUser u = new WxUser();
            u.setOpenid(openid);
            u.setNickname("咖啡新用户");
            return userRepo.save(u);
        });

        String token = jwtService.generate(user.getId(), openid);
        return new WxLoginResponse(
                token,
                user.getId(),
                user.getOpenid(),
                user.getNickname(),
                user.getAvatarUrl()
        );
    }
}