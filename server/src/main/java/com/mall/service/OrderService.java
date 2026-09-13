package com.mall.service;

import com.mall.dto.*;
import com.mall.entity.Address;
import com.mall.entity.Order;
import com.mall.entity.OrderItem;
import com.mall.exception.ApiException;
import com.mall.repository.AddressRepository;
import com.mall.repository.OrderItemRepository;
import com.mall.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final CartService cartService;
    private final AddressRepository addressRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public OrderService(CartService cartService,
                        AddressRepository addressRepo,
                        OrderRepository orderRepo,
                        OrderItemRepository orderItemRepo) {
        this.cartService = cartService;
        this.addressRepo = addressRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    /**
     * 从购物车创建订单
     * 流程：取购物车 → 校验地址 → 写 order + order_items → 清空购物车
     */
    @Transactional
    public OrderDto createFromCart(Long userId, OrderCreateRequest req) {
        List<CartItemDto> cart = cartService.list(userId);
        if (cart == null || cart.isEmpty()) {
            throw new ApiException(400, "购物车为空，无法下单");
        }

        Address addr = addressRepo.findByIdAndUserId(req.getAddressId(), userId)
                .orElseThrow(() -> new ApiException(404, "地址不存在或不属于当前用户"));

        // 算总价
        int total = cart.stream().mapToInt(it -> it.getPriceSnapshot() * it.getQuantity()).sum();

        // 写订单主表
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotal(total);
        order.setStatus(0); // 待支付
        order.setPickupType(req.getPickupType() != null ? req.getPickupType() : 1);
        order.setAddressId(addr.getId());
        order.setRemark(req.getRemark());
        order = orderRepo.save(order);

        // 写订单明细
        List<OrderItem> items = new ArrayList<>();
        for (CartItemDto c : cart) {
            OrderItem oi = new OrderItem();
            oi.setOrderId(order.getId());
            oi.setGoodsId(c.getGoodsId());
            oi.setPriceSnapshot(c.getPriceSnapshot());
            oi.setQuantity(c.getQuantity());
            oi.setSkuSnapshot(c.getSku());
            items.add(oi);
        }
        orderItemRepo.saveAll(items);

        // 清空购物车
        cartService.clear(userId);

        // 返回详情
        return toDto(order, items, addr);
    }

    public List<OrderDto> listMine(Long userId) {
        return orderRepo.findByUserIdOrderByIdDesc(userId).stream()
                .map(o -> {
                    List<OrderItem> items = orderItemRepo.findByOrderId(o.getId());
                    Address addr = o.getAddressId() == null ? null
                            : addressRepo.findById(o.getAddressId()).orElse(null);
                    return toDto(o, items, addr);
                })
                .collect(Collectors.toList());
    }

    public OrderDto getById(Long userId, Long id) {
        Order o = orderRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException(404, "订单不存在"));
        List<OrderItem> items = orderItemRepo.findByOrderId(o.getId());
        Address addr = o.getAddressId() == null ? null
                : addressRepo.findById(o.getAddressId()).orElse(null);
        return toDto(o, items, addr);
    }

    /** 取消订单（仅待支付状态可取消）*/
    @Transactional
    public OrderDto cancel(Long userId, Long id) {
        Order o = orderRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException(404, "订单不存在"));
        if (o.getStatus() != 0) {
            throw new ApiException(400, "只有待支付订单可以取消");
        }
        o.setStatus(5);
        return getById(userId, id);
    }

    /**
     * 模拟支付（学习用，不接入真实微信支付）
     * 真实环境用 wx-pay V3 + 回调验签
     */
    @Transactional
    public OrderDto mockPay(Long userId, Long id) {
        Order o = orderRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException(404, "订单不存在"));
        if (o.getStatus() != 0) {
            throw new ApiException(400, "订单状态不是待支付，无法支付");
        }
        o.setStatus(1);                // 已支付
        o.setPaidAt(LocalDateTime.now());
        o.setTransactionId("MOCK_" + System.currentTimeMillis());
        // 真实环境：根据 pickupType 把状态推到「制作中 2」或「待取餐 3」
        // 学习阶段先保持「已支付」
        return getById(userId, id);
    }

    private String generateOrderNo() {
        return "M" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", new Random().nextInt(10000));
    }

    private OrderDto toDto(Order o, List<OrderItem> items, Address addr) {
        OrderDto d = new OrderDto();
        d.setId(o.getId());
        d.setOrderNo(o.getOrderNo());
        d.setTotal(o.getTotal());
        d.setStatus(o.getStatus());
        d.setPickupType(o.getPickupType());
        d.setStoreId(o.getStoreId());
        d.setAddressId(o.getAddressId());
        d.setRemark(o.getRemark());
        d.setPaidAt(o.getPaidAt());
        d.setTransactionId(o.getTransactionId());
        d.setCreatedAt(o.getCreatedAt());

        if (addr != null) {
            AddressDto ad = new AddressDto();
            ad.setId(addr.getId());
            ad.setName(addr.getName());
            ad.setPhone(addr.getPhone());
            ad.setProvince(addr.getProvince());
            ad.setCity(addr.getCity());
            ad.setDistrict(addr.getDistrict());
            ad.setDetail(addr.getDetail());
            d.setAddress(ad);
        }

        if (items != null) {
            List<OrderItemDto> itemDtos = items.stream().map(it -> {
                OrderItemDto x = new OrderItemDto();
                x.setId(it.getId());
                x.setGoodsId(it.getGoodsId());
                x.setPriceSnapshot(it.getPriceSnapshot());
                x.setQuantity(it.getQuantity());
                x.setSkuSnapshot(it.getSkuSnapshot());
                // 名称图片从 cart snapshot 取不出，这里简化：从 goods 快照反查没必要
                // 真实环境应该在 OrderItem 里存 goodsName/imageUrl 冗余
                return x;
            }).collect(Collectors.toList());
            d.setItems(itemDtos);
        }

        return d;
    }
}