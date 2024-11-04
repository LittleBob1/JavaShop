package com.awaken.kidsshop.modules.product.controller;

import com.awaken.kidsshop.dto.ProductDTO;
import com.awaken.kidsshop.modules.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    private ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.allProducts());
    }

    @PostMapping("/products/createNewProduct")
    private ResponseEntity<?> createNewProduct(@RequestBody ProductDTO product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PostMapping("/products/{productId}/delete")
    private ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            boolean productIsDeleted = productService.deleteProduct(productId);
            if(productIsDeleted) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body(false);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/products/{productId}/update")
    private ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO product) {
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }
}
