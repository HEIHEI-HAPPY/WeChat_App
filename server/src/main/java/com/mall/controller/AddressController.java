package com.mall.controller;

import com.mall.dto.AddressDto;
import com.mall.dto.AddressRequest;
import com.mall.dto.ApiResponse;
import com.mall.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址
 * GET    /api/addresses           列表
 * POST   /api/addresses           新增
 * PATCH  /api/addresses/{id}      编辑
 * DELETE /api/addresses/{id}      删除
 * GET    /api/addresses/default   当前默认地址
 */
@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ApiResponse<List<AddressDto>> list(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(addressService.list(userId));
    }

    @PostMapping
    public ApiResponse<AddressDto> create(@Valid @RequestBody AddressRequest body, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(addressService.create(userId, body));
    }

    @PatchMapping("/{id}")
    public ApiResponse<AddressDto> update(@PathVariable Long id,
                                          @Valid @RequestBody AddressRequest body,
                                          HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(addressService.update(userId, id, body));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> remove(@PathVariable Long id, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        addressService.delete(userId, id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/default")
    public ApiResponse<AddressDto> defaultAddress(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return ApiResponse.ok(addressService.getDefault(userId));
    }
}