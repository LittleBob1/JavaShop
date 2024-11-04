package com.awaken.kidsshop.modules.brand.service;

import com.awaken.kidsshop.modules.brand.entity.Brand;
import com.awaken.kidsshop.modules.brand.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> allBrands(){
        return brandRepository.findAll();
    }

    public Brand createBrand(Brand brand){
        return brandRepository.save(brand);
    }

    public boolean deleteBrand(Long brandId) {
        if (brandRepository.findById(brandId).isPresent()) {
            brandRepository.deleteById(brandId);
            return true;
        }
        return false;
    }

    public Brand updateBrand(Long id, Brand brand){
        Brand a = brandRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        a.setName(brand.getName());
        return brandRepository.save(a);
    }
}
