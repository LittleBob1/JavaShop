package com.awaken.kidsshop.modules.productitem.repository;

import com.awaken.kidsshop.modules.productitem.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    List<ProductItem> findAllByOrderByQuantityDesc();
}
