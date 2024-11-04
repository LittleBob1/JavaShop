package com.awaken.kidsshop.modules.brand.repository;

import com.awaken.kidsshop.modules.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
