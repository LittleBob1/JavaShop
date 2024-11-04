package com.awaken.kidsshop.modules.buyer.service;

import com.awaken.kidsshop.modules.buyer.entity.Buyer;
import com.awaken.kidsshop.modules.buyer.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BuyerService {
    private final BuyerRepository buyerRepository;

    @Autowired
    public BuyerService(BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    public List<Buyer> allBuyers(){
        return buyerRepository.findAll();
    }

    public Buyer createBuyer(Buyer buyer){
        return buyerRepository.save(buyer);
    }

    public boolean deleteBuyer(Long buyerId) {
        if (buyerRepository.findById(buyerId).isPresent()) {
            buyerRepository.deleteById(buyerId);
            return true;
        }
        return false;
    }

    public Buyer updateBuyer(Long id, Buyer buyer){
        Buyer a = buyerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        a.setName(buyer.getName());
        a.setEmail(buyer.getEmail());
        a.setPhone(buyer.getPhone());
        return buyerRepository.save(a);
    }
}
