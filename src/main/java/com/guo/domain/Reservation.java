package com.guo.domain;

import java.util.Date;

/**
 * Reservation 实体类，对应数据库中的 reservation 表。
 */
public class Reservation {

    private Integer reserveId;

    private Integer userId;

    private Integer bookId;

    private Integer borrowDurationDays;

    private Date reserveTime;

    private Date expireTime;

    private String status;

    // --- Getters and Setters ---

    public Integer getReserveId() {
        return reserveId;
    }

    public void setReserveId(Integer reserveId) {
        this.reserveId = reserveId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getBorrowDurationDays() {
        return borrowDurationDays;
    }

    public void setBorrowDurationDays(Integer borrowDurationDays) {
        this.borrowDurationDays = borrowDurationDays;
    }

    public Date getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(Date reserveTime) {
        this.reserveTime = reserveTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reserveId=" + reserveId +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", borrowDurationDays=" + borrowDurationDays +
                ", reserveTime=" + reserveTime +
                ", expireTime=" + expireTime +
                ", status='" + status + '\'' +
                '}';
    }
}