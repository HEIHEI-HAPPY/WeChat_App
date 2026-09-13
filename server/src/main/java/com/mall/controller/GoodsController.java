package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.entity.Goods;
import com.mall.service.GoodsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品
 * GET    /api/goods       列表
 * GET    /api/goods/{id}  详情
 */
@RestController
@RequestMapping("/api")
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("/goods")
    public ApiResponse<List<Goods>> list() {
        return ApiResponse.ok(goodsService.list());
    }

    @GetMapping("/goods/{id}")
    public ApiResponse<Goods> detail(@PathVariable Long id) {
        return ApiResponse.ok(goodsService.getById(id));
    }
}