package com.guo.domain.Vo;

import com.guo.domain.Book;
import com.guo.domain.User;
import lombok.Data;

/**
 * @author guo
 * 添加视图层对象
 * 新增属性 user
 */
@Data
public class BorrowingBooksVo {
    private User user;
    private Book book;  //借阅书籍
    private String dateOfBorrowing;  //借书日期
    private String dateOfReturn;    //还书日期
}
