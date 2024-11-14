package com.awaken.kidsshop.modules.productitem.controller;

import com.awaken.kidsshop.modules.productitem.controller.dto.ProductItemDTO;
import com.awaken.kidsshop.modules.productitem.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductItemController {
    private final ProductItemService productItemService;

    @Autowired
    public ProductItemController(ProductItemService productItemService) {
        this.productItemService = productItemService;
    }

    @GetMapping("/productsItems")
    private ResponseEntity<?> getAllProductItems() {
        return ResponseEntity.ok(productItemService.allProductItems());
    }

    @GetMapping("/productsItems/sortByQuantity")
    private ResponseEntity<?> getProductItemsByQuantity() {
        return ResponseEntity.ok(productItemService.productItemsByQuantity());
    }


    @PostMapping("/productsItems/createNewProductItem")
    private ResponseEntity<?> createNewProductItem(@RequestBody ProductItemDTO productItemDTO) {
        return ResponseEntity.ok(productItemService.createProductItem(productItemDTO));
    }

    @PostMapping("/productsItems/{productItemId}/delete")
    private ResponseEntity<?> deleteProductItem(@PathVariable Long productItemId) {
        try {
            boolean itemIsDeleted = productItemService.deleteProductItem(productItemId);
            if(itemIsDeleted) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body(false);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/productsItems/{productItemId}/update")
    private ResponseEntity<?> updateProductItem(@PathVariable Long productItemId, @RequestBody ProductItemDTO productItemDTO) {
        return ResponseEntity.ok(productItemService.updateProductItem(productItemId, productItemDTO));
    }
}
