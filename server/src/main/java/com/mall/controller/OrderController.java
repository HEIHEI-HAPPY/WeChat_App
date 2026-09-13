package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.dto.OrderCreateRequest;
import com.mall.dto.OrderDto;
import com.mall.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单
 * POST   /api/orders              从购物车创建订单
 * GET    /api/orders              当前用户订单列表
 * GET    /api/orders/{id}         订单详情
 * POST   /api/orders/{id}/cancel  取消订单
 * POST   /api/orders/{id}/pay     模拟支付（学习用）
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<OrderDto> create(@Valid @RequestBody OrderCreateRequest req, HttpServletRequest http) {
        Long userId = (Long) http.getAttribute("userId");
        return ApiResponse.ok(orderService.createFromCart(userId, req));
    }

    @GetMapping
    public ApiResponse<List<OrderDto>> list(HttpServletRequest http) {
        Long userId = (Long) http.getAttribute("userId");
        return ApiResponse.ok(orderService.listMine(userId));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderDto> detail(@PathVariable Long id, HttpServletRequest http) {
        Long userId = (Long) http.getAttribute("userId");
        return ApiResponse.ok(orderService.getById(userId, id));
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<OrderDto> cancel(@PathVariable Long id, HttpServletRequest http) {
        Long userId = (Long) http.getAttribute("userId");
        return ApiResponse.ok(orderService.cancel(userId, id));
    }

    @PostMapping("/{id}/pay")
    public ApiResponse<OrderDto> pay(@PathVariable Long id, HttpServletRequest http) {
        Long userId = (Long) http.getAttribute("userId");
        return ApiResponse.ok(orderService.mockPay(userId, id));
    }
}