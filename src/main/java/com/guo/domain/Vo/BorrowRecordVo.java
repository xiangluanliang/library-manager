package com.guo.domain.Vo;

import com.guo.domain.BorrowRecord;

/**
 * 借阅记录的视图对象
 * 用于封装一条完整的、包含用户和图书信息的借阅记录，以供前端展示。
 */
public class BorrowRecordVo extends BorrowRecord {

    // 扩展的字段，用于存储关联表的信息
    private String userName;
    private String bookTitle;

    // --- Getter and Setter ---

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
}