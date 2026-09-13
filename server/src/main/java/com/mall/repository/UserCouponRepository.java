package com.mall.repository;

import com.mall.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    List<UserCoupon> findByUserIdAndStatus(Long userId, Integer status);
    List<UserCoupon> findByUserId(Long userId);
}