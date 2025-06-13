package com.guo.service;

import com.guo.domain.User;
import com.guo.domain.Vo.BorrowRecordVo;
import com.guo.domain.Vo.ReservationVo;
import com.guo.utils.page.Page;

import java.util.Date;
import java.util.List;

/**
 * 记录服务接口
 * 负责处理借阅、归还等所有记录相关的查询业务。
 */
public interface IRecordService {

    /**
     * (管理员功能) 分页查询所有的借阅记录。
     * @param pageNum 当前页码
     * @return 封装了完整借阅信息（包括用户和书名）的分页对象。
     */
    Page<BorrowRecordVo> findAllRecordsByPage(int pageNum);

    /**
     * (用户功能) 查询指定用户的所有借阅记录。
     * @param userId 用户ID
     * @return 包含完整借阅信息的列表。
     */
    List<BorrowRecordVo> findRecordsByUserId(int userId);

    /**
     * 创建一个新的图书预约记录
     * @param bookId 要预约的图书ID
     * @param currentUser 当前登录的用户
     * @return 成功返回true，失败（如已预约）返回false
     */
    boolean createReservation(int bookId, User currentUser);


    /**
     * 执行借书操作。
     * @param bookId 要借阅的图书ID
     * @param currentUser 当前操作的用户
     * @param dueDate 用户选择的应还日期
     * @return 借阅成功返回true，失败返回false。
     */
    boolean executeBorrow(int bookId, User currentUser, Date dueDate);


    Page<BorrowRecordVo> findUnreturnedRecordsByPage(int pageNum);

    boolean executeReturn(int borrowId);

    /**
     * 根据用户ID查找其所有的预约记录详情。
     * @param userId 用户ID
     * @return 预约记录VO列表
     */
    List<ReservationVo> findReservationsByUserId(int userId);

    /**
     * 用户取消一个等待中的预约。
     * @param reservationId 要取消的预约ID
     * @param userId 操作者（当前登录用户）的ID，用于安全校验
     * @return 取消成功返回true，失败返回false
     */
    boolean cancelReservation(int reservationId, int userId);

    Page<ReservationVo> findAllReservationsByPage(int pageNum);

}