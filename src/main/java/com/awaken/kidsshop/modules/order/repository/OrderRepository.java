package com.awaken.kidsshop.modules.order.repository;

import com.awaken.kidsshop.modules.order.entity.Order;
import com.awaken.kidsshop.modules.sales.SalesCountByDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderBySalesDateDesc();

    @Query(value = "SELECT DATE_TRUNC('day', sales_date) as salesDay, COUNT(*) as salesCount " +
            "FROM orders " +
            "GROUP BY DATE_TRUNC('day', sales_date) " +
            "ORDER BY salesDay",
            nativeQuery = true)
    List<SalesCountByDay> findSalesCountByDay();
}

