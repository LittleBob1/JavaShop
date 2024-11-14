package com.awaken.kidsshop.modules.brand.controller;

import com.awaken.kidsshop.modules.brand.entity.Brand;
import com.awaken.kidsshop.modules.brand.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BrandController {
    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    private ResponseEntity<?> getAllBrands() {
        return ResponseEntity.ok(brandService.allBrands());
    }

    @PostMapping("/brands/createNewBrand")
    private ResponseEntity<?> createNewBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.createBrand(brand));
    }

    @PostMapping("/brands/{brandId}/delete")
    private ResponseEntity<?> deleteBrand(@PathVariable Long brandId) {
        try {
            boolean brandIsDeleted = brandService.deleteBrand(brandId);
            if(brandIsDeleted) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body(false);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/brands/{brandId}/update")
    private ResponseEntity<?> updateBrand(@PathVariable Long brandId, @RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.updateBrand(brandId, brand));
    }
}
