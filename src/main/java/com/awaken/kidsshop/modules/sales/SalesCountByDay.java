package com.awaken.kidsshop.modules.sales;

import java.time.LocalDateTime;

public interface SalesCountByDay {
    LocalDateTime getSalesDay();
    Long getSalesCount();
}
