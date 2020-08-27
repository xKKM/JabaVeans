package com.example.JabaVeans.viewhelper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
public class TablePage {
    private int size;
    private int lastSize;
    private int currentPage;
    private boolean hasNext;
    private boolean hasPrev;
    private String pageAction;
    private String searchStr;
    private String lastSearchStr;

    public TablePage() {
        size = 10;
        lastSize = 10;
        currentPage = 0;
        hasNext = false;
        hasPrev = false;
        pageAction = "";
        searchStr="";
        lastSearchStr="";
    }
    public TablePage(Slice<?> slice) {
       this();
       update(slice);
    }
    //called after obtaining slice
    public void update(Slice<?> slice) {
        hasPrev = slice.hasPrevious();
        hasNext = slice.hasNext();
        lastSearchStr = searchStr;
        lastSize = size;
    }

    //called before obtaining slice
    public void updatePageNumber() {
        if (size != lastSize || !searchStr.equals(lastSearchStr) ) {
            currentPage = 0;
        } else {
            switch (pageAction) {
                case "Next":
                    if (hasNext) {
                        currentPage++;
                    }
                    break;
                case "Prev":
                    if (hasPrev) {
                        currentPage--;
                    }
                    break;
            }
        }
    }


}
