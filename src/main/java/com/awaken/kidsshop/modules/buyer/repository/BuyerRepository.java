package com.awaken.kidsshop.modules.buyer.repository;

import com.awaken.kidsshop.modules.buyer.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {
}
