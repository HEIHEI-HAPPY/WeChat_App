package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.dto.CartAddRequest;
import com.mall.dto.CartItemDto;
import com.mall.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 购物车
 * GET    /api/cart       当前用户的购物车
 * POST   /api/cart       加入购物车
 * PATCH  /api/cart/{id}  修改数量
 * DELETE /api/cart/{id}  删除
 * GET    /api/cart/count 角标数量
 * DELETE /api/cart       清空
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ApiResponse<List<CartItemDto>> list(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(cartService.list(userId));
    }

    @PostMapping
    public ApiResponse<CartItemDto> add(@Valid @RequestBody CartAddRequest body, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(cartService.add(userId, body));
    }

    @PatchMapping("/{itemId}")
    public ApiResponse<CartItemDto> update(@PathVariable String itemId,
                                          @RequestBody Map<String, Integer> body,
                                          HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        Integer qty = body == null ? null : body.get("quantity");
        return ApiResponse.ok(cartService.updateQuantity(userId, itemId, qty));
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse<?> remove(@PathVariable String itemId, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        cartService.remove(userId, itemId);
        return ApiResponse.ok(null);
    }

    @DeleteMapping
    public ApiResponse<?> clear(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        cartService.clear(userId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/count")
    public ApiResponse<Integer> count(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(cartService.count(userId));
    }
}