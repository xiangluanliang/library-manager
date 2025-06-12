package com.guo.domain.Vo;

import com.guo.domain.BorrowRecord;
import java.util.Date;

/**
 * 借阅记录的视图对象
 * 用于封装一条完整的、包含用户和图书信息的借阅记录，以供前端展示。
 */
public class BorrowRecordVo extends BorrowRecord {

    private String userName;
    private String bookTitle;
    private Date returnTime;

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

    // 【新增】为returnTime字段添加getter和setter
    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }
}