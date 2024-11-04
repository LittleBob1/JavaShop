package com.awaken.kidsshop.modules.buyer.controller;

import com.awaken.kidsshop.modules.buyer.entity.Buyer;
import com.awaken.kidsshop.modules.buyer.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class BuyerController {
    private final BuyerService buyerService;

    @Autowired
    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }

    @GetMapping("/buyers")
    private ResponseEntity<?> getAllBuyers() {
        return ResponseEntity.ok(buyerService.allBuyers());
    }

    @PostMapping("/buyers/createNewBuyer")
    private ResponseEntity<?> createNewBuyer(@RequestBody Buyer buyer) {
        return ResponseEntity.ok(buyerService.createBuyer(buyer));
    }

    @PostMapping("/buyers/{buyerId}/delete")
    private ResponseEntity<?> deleteBuyer(@PathVariable Long buyerId) {
        try {
            boolean buyerIsDeleted = buyerService.deleteBuyer(buyerId);
            if(buyerIsDeleted) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body(false);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/buyers/{buyerId}/update")
    private ResponseEntity<?> updateBuyer(@PathVariable Long buyerId, @RequestBody Buyer buyer) {
        return ResponseEntity.ok(buyerService.updateBuyer(buyerId, buyer));
    }
}
