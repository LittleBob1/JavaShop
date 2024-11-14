package com.awaken.kidsshop.modules.size.controller;

import com.awaken.kidsshop.modules.size.entity.Size;
import com.awaken.kidsshop.modules.size.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SizeController {
    private final SizeService sizeService;

    @Autowired
    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping("/sizes")
    private ResponseEntity<?> getAllSizes() {
        return ResponseEntity.ok(sizeService.allSizes());
    }

    @PostMapping("/sizes/createNewSize")
    private ResponseEntity<?> createNewSize(@RequestBody Size size) {
        return ResponseEntity.ok(sizeService.createSize(size));
    }

    @PostMapping("/sizes/{sizeId}/delete")
    private ResponseEntity<?> deleteSize(@PathVariable Long sizeId) {
        try {
            boolean sizeIsDeleted = sizeService.deleteSize(sizeId);
            if(sizeIsDeleted) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body(false);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/sizes/{sizeId}/update")
    private ResponseEntity<?> updateSize(@PathVariable Long sizeId, @RequestBody Size size) {
        return ResponseEntity.ok(sizeService.updateSize(sizeId, size));
    }
}
