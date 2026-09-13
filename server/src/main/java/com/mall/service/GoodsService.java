package com.mall.service;

import com.mall.entity.Goods;
import com.mall.exception.ApiException;
import com.mall.repository.GoodsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsService {

    private final GoodsRepository goodsRepo;

    public GoodsService(GoodsRepository goodsRepo) {
        this.goodsRepo = goodsRepo;
    }

    /** 商品列表：上架的，按 id 倒序 */
    public List<Goods> list() {
        return goodsRepo.findByStatusOrderByIdDesc(1);
    }

    public Goods getById(Long id) {
        return goodsRepo.findById(id)
                .orElseThrow(() -> new ApiException(404, "商品不存在"));
    }

    @Transactional
    public Goods create(Goods goods) {
        if (goods.getName() == null || goods.getName().isEmpty()) {
            throw new ApiException(400, "商品名不能为空");
        }
        if (goods.getBasePrice() == null || goods.getBasePrice() < 0) {
            throw new ApiException(400, "价格必须 ≥ 0");
        }
        goods.setStatus(1);
        return goodsRepo.save(goods);
    }
}