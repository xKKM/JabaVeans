package com.example.JabaVeans.viewhelper;

import com.example.JabaVeans.dto.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TablePageTest {

    @Test
    void update_SetsPropertiesWithGivenSlice() {
        TablePage tablePage = new TablePage();
        tablePage.setSize(2);
        tablePage.setSearchStr("qqq");
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,0);
        Slice<Integer> slice = new SliceImpl<>(list,PageRequest.of(1,2),true);
        tablePage.update(slice);
        assertTrue(tablePage.isHasNext());
        assertTrue(tablePage.isHasPrev());
        assertEquals(tablePage.getSize(),tablePage.getLastSize());
        assertEquals(tablePage.getSearchStr(),tablePage.getLastSearchStr());
    }

    @Test
    void whenSizedNotEqualLastSize_updatePageNumberSetsCurrentPageTo0() {
        TablePage tablePage = new TablePage();
        tablePage.setCurrentPage(10);
        tablePage.setSize(10);
        tablePage.setLastSize(25);
        tablePage.updatePageNumber();
        assertEquals(0,tablePage.getCurrentPage());
    }

    @Test
    void whenSearchStrNotEqualLastSearchStr_updatePageNumberSetsCurrentPageTo0() {
        TablePage tablePage = new TablePage();
        tablePage.setCurrentPage(10);
        tablePage.setSearchStr("qqq");
        tablePage.setLastSearchStr("");
        tablePage.updatePageNumber();
        assertEquals(0,tablePage.getCurrentPage());
    }

    @Test
    void whenPageActionIsNext_AndHasNextIsTrue_And_FirstConditionIsFalse_updatePageNumberIncrementsCurrentPageValue() {
        TablePage tablePage = new TablePage();
        tablePage.setCurrentPage(1);
        tablePage.setHasNext(true);
        tablePage.setPageAction("Next");
        tablePage.updatePageNumber();
        assertEquals(2,tablePage.getCurrentPage());
    }

    @Test
    void whenPageActionIsNext_AndHasNextIsTrue_And_FirstConditionIsFalse_updatePageNumberDoesNotChangeCurrentPageValue() {
        TablePage tablePage = new TablePage();
        tablePage.setCurrentPage(1);
        tablePage.setHasNext(false);
        tablePage.setPageAction("Next");
        tablePage.updatePageNumber();
        assertEquals(1,tablePage.getCurrentPage());
    }

    @Test
    void whenPageActionIsPrev_AndHasPrevIsTrue_And_FirstConditionIsFalse_updatePageNumberDecrementsCurrentPageValue() {
        TablePage tablePage = new TablePage();
        tablePage.setCurrentPage(1);
        tablePage.setHasPrev(true);
        tablePage.setPageAction("Prev");
        tablePage.updatePageNumber();
        assertEquals(0,tablePage.getCurrentPage());
    }

    @Test
    void whenPageActionIsPrev_AndHasNextIsTrue_And_FirstConditionIsFalse_updatePageNumberDoesNotChangeCurrentPageValue() {
        TablePage tablePage = new TablePage();
        tablePage.setCurrentPage(1);
        tablePage.setHasPrev(false);
        tablePage.setPageAction("Prev");
        tablePage.updatePageNumber();
        assertEquals(1, tablePage.getCurrentPage());
    }
}