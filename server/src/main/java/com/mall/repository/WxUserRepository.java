package com.mall.repository;

import com.mall.entity.WxUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WxUserRepository extends JpaRepository<WxUser, Long> {
    Optional<WxUser> findByOpenid(String openid);
}