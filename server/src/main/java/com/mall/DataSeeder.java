package com.mall;

import com.mall.entity.Goods;
import com.mall.repository.GoodsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动时灌入示例商品（仅当 goods 表为空时执行）
 * 见 PROJECT_MAP.md 阶段 1 Checklist
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final GoodsRepository goodsRepo;

    public DataSeeder(GoodsRepository goodsRepo) {
        this.goodsRepo = goodsRepo;
    }

    @Override
    public void run(String... args) {
        if (goodsRepo.count() > 0) {
            log.info("[DataSeeder] goods 表已有 {} 条数据，跳过 seed", goodsRepo.count());
            return;
        }

        log.info("[DataSeeder] 写入 5 条咖啡示例商品");

        Goods latte = new Goods();
        latte.setName("拿铁");
        latte.setCategory("拿铁");
        latte.setBasePrice(1800);  // 18 元
        latte.setDescription("经典意式拿铁，香醇顺滑");
        latte.setStock(100);
        latte.setSpecs("[{\"name\":\"杯型\",\"options\":[{\"label\":\"中杯\",\"extraPrice\":0},{\"label\":\"大杯\",\"extraPrice\":300}]},{\"name\":\"温度\",\"options\":[{\"label\":\"热\",\"extraPrice\":0},{\"label\":\"冰\",\"extraPrice\":0}]},{\"name\":\"糖度\",\"options\":[{\"label\":\"无糖\",\"extraPrice\":0},{\"label\":\"半糖\",\"extraPrice\":0},{\"label\":\"全糖\",\"extraPrice\":0}]}]");
        goodsRepo.save(latte);

        Goods americano = new Goods();
        americano.setName("美式");
        americano.setCategory("美式");
        americano.setBasePrice(1500);
        americano.setDescription("纯正意式浓缩 + 热水，提神醒脑");
        americano.setStock(100);
        goodsRepo.save(americano);

        Goods cappuccino = new Goods();
        cappuccino.setName("卡布奇诺");
        cappuccino.setCategory("拿铁");
        cappuccino.setBasePrice(1900);
        cappuccino.setDescription("浓缩 + 蒸汽牛奶 + 绵密奶泡");
        cappuccino.setStock(100);
        goodsRepo.save(cappuccino);

        Goods mocha = new Goods();
        mocha.setName("摩卡");
        mocha.setCategory("拿铁");
        mocha.setBasePrice(2200);
        mocha.setDescription("浓缩 + 巧克力 + 牛奶，甜品级享受");
        mocha.setStock(100);
        goodsRepo.save(mocha);

        Goods caramel = new Goods();
        caramel.setName("焦糖玛奇朵");
        caramel.setCategory("拿铁");
        caramel.setBasePrice(2300);
        caramel.setDescription("浓缩 + 香草 + 焦糖酱，回味悠长");
        caramel.setStock(100);
        goodsRepo.save(caramel);

        log.info("[DataSeeder] 完成，共 {} 条", goodsRepo.count());
    }
}