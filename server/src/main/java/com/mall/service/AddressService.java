package com.mall.service;

import com.mall.dto.AddressDto;
import com.mall.dto.AddressRequest;
import com.mall.entity.Address;
import com.mall.exception.ApiException;
import com.mall.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository repo;

    public AddressService(AddressRepository repo) {
        this.repo = repo;
    }

    public List<AddressDto> list(Long userId) {
        return repo.findByUserIdOrderByIsDefaultDescIdDesc(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressDto create(Long userId, AddressRequest req) {
        // 如果设为默认，把其他默认去掉
        if (req.getIsDefault() != null && req.getIsDefault() == 1) {
            clearDefault(userId);
        }
        Address a = new Address();
        a.setUserId(userId);
        copyTo(a, req);
        return toDto(repo.save(a));
    }

    @Transactional
    public AddressDto update(Long userId, Long id, AddressRequest req) {
        Address a = repo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException(404, "地址不存在"));
        if (req.getIsDefault() != null && req.getIsDefault() == 1) {
            clearDefault(userId);
        }
        copyTo(a, req);
        return toDto(repo.save(a));
    }

    @Transactional
    public void delete(Long userId, Long id) {
        Address a = repo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ApiException(404, "地址不存在"));
        repo.delete(a);
    }

    public AddressDto getDefault(Long userId) {
        return repo.findByUserIdOrderByIsDefaultDescIdDesc(userId).stream()
                .filter(a -> a.getIsDefault() != null && a.getIsDefault() == 1)
                .findFirst()
                .map(this::toDto)
                .orElseGet(() -> repo.findByUserIdOrderByIsDefaultDescIdDesc(userId).stream()
                        .findFirst()
                        .map(this::toDto)
                        .orElse(null));
    }

    private void clearDefault(Long userId) {
        List<Address> list = repo.findByUserIdOrderByIsDefaultDescIdDesc(userId);
        list.stream()
                .filter(a -> a.getIsDefault() != null && a.getIsDefault() == 1)
                .forEach(a -> {
                    a.setIsDefault(0);
                    repo.save(a);
                });
    }

    private void copyTo(Address a, AddressRequest req) {
        a.setName(req.getName());
        a.setPhone(req.getPhone());
        a.setProvince(req.getProvince());
        a.setCity(req.getCity());
        a.setDistrict(req.getDistrict());
        a.setDetail(req.getDetail());
        a.setIsDefault(req.getIsDefault() != null ? req.getIsDefault() : 0);
    }

    private AddressDto toDto(Address a) {
        AddressDto d = new AddressDto();
        d.setId(a.getId());
        d.setName(a.getName());
        d.setPhone(a.getPhone());
        d.setProvince(a.getProvince());
        d.setCity(a.getCity());
        d.setDistrict(a.getDistrict());
        d.setDetail(a.getDetail());
        d.setIsDefault(a.getIsDefault() == null ? 0 : a.getIsDefault());
        return d;
    }
}