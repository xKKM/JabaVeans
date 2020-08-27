package com.example.JabaVeans.dto.helper;

import java.math.BigDecimal;

public class ProductPrices {
    public static final BigDecimal beans100 = BigDecimal.valueOf(4.19);
    public static final BigDecimal beans250 = BigDecimal.valueOf(9.99);
    public static final BigDecimal beans500 = BigDecimal.valueOf(18.99);
    public static final BigDecimal beans1000 = BigDecimal.valueOf(36.99);

    public static BigDecimal getPrice(int i) {
        switch (i) {
            case 0: return beans100;
            case 1: return beans250;
            case 2: return beans500;
            case 3: return beans1000;
            default: return null;
        }
    }
}
