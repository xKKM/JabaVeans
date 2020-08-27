package com.example.JabaVeans.dto.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderStringsTest {
    @Test
    void OrderStatesStaticArrayContainsRequiredValues() {
        Assertions.assertEquals(OrderStrings.orderStates[0],"Nowe");
        Assertions.assertEquals(OrderStrings.orderStates[1],"Realizowane");
        Assertions.assertEquals(OrderStrings.orderStates[2],"Wysłane");
        Assertions.assertEquals(OrderStrings.orderStates[3],"Dostarczone");
    }

    @Test
    void OrderWeightsStaticArrayContainsRequiredValues() {
        Assertions.assertEquals(OrderStrings.orderWeights[0],"100g");
        Assertions.assertEquals(OrderStrings.orderWeights[1],"250g");
        Assertions.assertEquals(OrderStrings.orderWeights[2],"500g");
        Assertions.assertEquals(OrderStrings.orderWeights[3],"1000g");
    }
}
