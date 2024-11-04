package com.awaken.kidsshop.modules.size.repository;

import com.awaken.kidsshop.modules.size.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
}
