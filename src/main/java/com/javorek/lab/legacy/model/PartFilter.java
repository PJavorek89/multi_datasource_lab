package com.javorek.lab.legacy.model;

import java.math.BigDecimal;
import java.util.List;

public record PartFilter(
        String category,        // null = nefiltrovat
        String plant,
        BigDecimal minPrice,
        List<String> partNos,
        String sort) {          // jen hodnoty z whitelistu, viz service
}
