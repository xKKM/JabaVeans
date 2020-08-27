package com.example.JabaVeans.dto.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductPricesTest {

    @Test
    void GetPriceReturnsCorrectValues() {
        assertEquals(ProductPrices.getPrice(0),ProductPrices.beans100);
        assertEquals(ProductPrices.getPrice(1),ProductPrices.beans250);
        assertEquals(ProductPrices.getPrice(2),ProductPrices.beans500);
        assertEquals(ProductPrices.getPrice(3),ProductPrices.beans1000);
        assertNull(ProductPrices.getPrice(4));
    }
}