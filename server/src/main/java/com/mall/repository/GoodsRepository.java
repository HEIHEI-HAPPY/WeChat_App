package com.mall.repository;

import com.mall.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    /** 上架的商品按 id 倒序（最新在前） */
    List<Goods> findByStatusOrderByIdDesc(Integer status);
}