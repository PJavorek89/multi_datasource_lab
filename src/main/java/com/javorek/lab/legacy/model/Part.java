package com.javorek.lab.legacy.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Part(String partNo,
                   String name,
                   String category,
                   String plant,
                   BigDecimal prive,
                   LocalDate createAt) {
}
