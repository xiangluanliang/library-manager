package com.guo.domain.Vo;

import com.guo.domain.BookInfo;

public class BookInfoVo extends BookInfo {

    private Integer availableCopies;
    private Integer totalCopies;

    // --- Getters and Setters ---

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }
}