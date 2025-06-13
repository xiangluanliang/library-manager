package com.guo.domain.Vo;

import com.guo.domain.Reservation;

/**
 * 预约记录的视图对象 (VO)
 * 继承自Reservation实体，并扩展了用于前端展示的额外字段。
 */
public class ReservationVo extends Reservation {

    /**
     * 【已有】从 book_info 表关联查询出的图书标题
     */
    private String bookTitle;

    /**
     * 【新增】从 user 表关联查询出的用户名
     */
    private String userName;


    // --- Getter and Setter ---

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    // 【新增】为userName字段添加getter和setter
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}