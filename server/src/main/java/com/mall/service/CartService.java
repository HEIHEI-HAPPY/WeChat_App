package com.mall.service;

import com.mall.dto.CartAddRequest;
import com.mall.dto.CartItemDto;
import com.mall.entity.Goods;
import com.mall.exception.ApiException;
import com.mall.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物车服务 —— 用 Redis Hash 存储
 *
 * Key 设计：cart:{userId}    (Hash)
 *   field: itemId (UUID)
 *   value: CartItemDto (JSON)
 *
 * 优点：
 * - 原子操作（HSET / HDEL / HINCRBY）
 * - 自动 TTL（7 天未访问过期）
 * - 后期切 MySQL / PostgreSQL 持久化只需重写本类，Controller 一行不改
 */
@Service
public class CartService {

    private static final long CART_TTL_DAYS = 7;

    private final RedisTemplate<String, Object> redis;
    private final GoodsRepository goodsRepo;

    @Autowired
    public CartService(RedisTemplate<String, Object> redis, GoodsRepository goodsRepo) {
        this.redis = redis;
        this.goodsRepo = goodsRepo;
    }

    private String key(Long userId) {
        return "cart:" + userId;
    }

    /** 列出购物车全部条目 */
    public List<CartItemDto> list(Long userId) {
        String k = key(userId);
        Map<Object, Object> entries = redis.opsForHash().entries(k);
        if (entries == null || entries.isEmpty()) return Collections.emptyList();

        return entries.values().stream()
                .map(v -> (CartItemDto) v)
                .sorted(Comparator.comparing(CartItemDto::getAddedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }

    /** 加入购物车 */
    public CartItemDto add(Long userId, CartAddRequest req) {
        Goods goods = goodsRepo.findById(req.getGoodsId())
                .orElseThrow(() -> new ApiException(404, "商品不存在"));
        if (goods.getStatus() != 1) {
            throw new ApiException(400, "商品已下架");
        }

        CartItemDto item = new CartItemDto();
        item.setItemId(UUID.randomUUID().toString());
        item.setGoodsId(goods.getId());
        item.setGoodsName(goods.getName());
        item.setImageUrl(goods.getImageUrl());
        item.setPriceSnapshot(goods.getBasePrice());
        item.setSku(req.getSku());
        item.setQuantity(req.getQuantity());
        item.setStoreId(req.getStoreId());
        item.setAddedAt(LocalDateTime.now());

        String k = key(userId);
        redis.opsForHash().put(k, item.getItemId(), item);
        redis.expire(k, java.time.Duration.ofDays(CART_TTL_DAYS));
        return item;
    }

    /** 修改数量（itemId 必须在该用户购物车里）*/
    public CartItemDto updateQuantity(Long userId, String itemId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new ApiException(400, "数量必须 ≥ 1");
        }
        String k = key(userId);
        Object raw = redis.opsForHash().get(k, itemId);
        if (raw == null) {
            throw new ApiException(404, "购物车里没有这一项");
        }
        CartItemDto item = (CartItemDto) raw;
        item.setQuantity(quantity);
        redis.opsForHash().put(k, itemId, item);
        return item;
    }

    /** 删除一项 */
    public void remove(Long userId, String itemId) {
        redis.opsForHash().delete(key(userId), itemId);
    }

    /** 清空整个购物车 */
    public void clear(Long userId) {
        redis.delete(key(userId));
    }

    /** 购物车里的商品件数（用于角标）*/
    public int count(Long userId) {
        Long n = redis.opsForHash().size(key(userId));
        return n == null ? 0 : n.intValue();
    }
}