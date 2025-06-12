package com.guo.service.impl;

import com.guo.domain.*;
import com.guo.domain.Vo.BorrowRecordVo;
import com.guo.mapper.BorrowRecordMapper;
import com.guo.mapper.ReservationMapper;
import com.guo.service.IRecordService;
import com.guo.utils.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * 记录服务实现类
 */
@Service
public class RecordServiceImpl implements IRecordService {

    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    @Resource
    private ReservationMapper reservationMapper;

    /**
     * 分页查询所有借阅记录。
     */
    /**
     * 分页查询所有借阅记录。
     */
    @Override
    public Page<BorrowRecordVo> findAllRecordsByPage(int pageNum) {
        int pageSize = 10;
        int offset = (pageNum - 1) * pageSize;

        // 1. 调用Mapper获取分页后的数据列表
        List<BorrowRecordVo> records = borrowRecordMapper.selectAllWithDetailsByPage(offset, pageSize);

        // 2. 调用Mapper获取符合条件的总数
        long totalCount = borrowRecordMapper.countAll();

        // 3. 设置分页对象
        Page<BorrowRecordVo> page = new Page<>();
        page.setList(records);
        //TODO:page.setTotalCount(totalCount);没有这个方法，这行代码的作用是将查询到的所有借阅记录的总数赋值给分页对象 Page 的 totalCount 字段。
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);

        return page;
    }

    /**
     * 查询指定用户的所有借阅记录。
     */
    @Override
    public List<BorrowRecordVo> findRecordsByUserId(int userId) {
        System.out.println("=============================================");
        System.out.println("【日志】尝试登录，用户名: " + userId);
        return borrowRecordMapper.findWithDetailsByUserId(userId);
    }

    /**
     * 执行借书操作，包含完整的业务规则校验。
     */
    @Override
    @Transactional
    public boolean executeBorrow(int bookId, User currentUser, Date dueDate) {
        try {
            // 业务规则1：检查该书是否还有库存可借
            // 我们需要一个方法来安全地更新库存（例如使用乐观锁或悲观锁），
            // 这里为了简化，我们直接尝试将库存减1，并检查受影响的行数。
            int updatedRows = borrowRecordMapper.decrementAvailableCopies(bookId);

            if (updatedRows <= 0) {
                System.out.println("借阅失败：图书ID " + bookId + " 库存不足或不存在。");
                return false; // 库存不足或更新失败
            }

            // 业务规则2：检查用户借书数量是否已达上限
            BorrowRecordExample example = new BorrowRecordExample();
            example.createCriteria()
                    .andUserIdEqualTo(currentUser.getUserId())
                    .andStatusEqualTo("borrowed"); // 只统计状态为'borrowed'的记录

            long currentBorrows = borrowRecordMapper.countByExample(example);
            if (currentBorrows >= currentUser.getMaxBorrow()) {
                System.out.println("借阅失败：用户 " + currentUser.getUserName() + " 已达到最大借阅数量。");
                // **重要**：因为上一步已经将库存减1，这里必须抛出异常来回滚事务！
                throw new RuntimeException("已达到最大借阅数量");
            }

            // 所有检查通过，创建新的借阅记录
            BorrowRecord newRecord = new BorrowRecord();
            newRecord.setBookId(bookId);
            newRecord.setUserId(currentUser.getUserId());
            newRecord.setBorrowTime(new Date()); // 借阅时间为当前时间
            newRecord.setDueTime(dueDate);       // 应还日期为前端传入的日期
            newRecord.setStatus("borrowed");

            // 插入借阅记录
            borrowRecordMapper.insertSelective(newRecord);

            System.out.println("用户 " + currentUser.getUserName() + " 成功借阅图书ID " + bookId);
            return true;

        } catch (Exception e) {
            // 捕获任何异常（包括我们自己抛出的异常），打印日志。
            // @Transactional注解会确保数据库操作被回滚。
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean createReservation(int bookId, User currentUser) {
        // 业务规则：检查该用户是否已经预约了这本书且预约仍在有效期内
        ReservationExample example = new ReservationExample();
        example.createCriteria()
                .andUserIdEqualTo(currentUser.getUserId())
                .andBookIdEqualTo(bookId)
                .andStatusEqualTo("pending");

        long existingReservations = reservationMapper.countByExample(example);
        if (existingReservations > 0) {
            System.out.println("预约失败：用户 " + currentUser.getUserName() + " 已预约过此书。");
            return false; // 用户已预约，不允许重复
        }

        // 创建新的预约对象
        Reservation reservation = new Reservation();
        reservation.setUserId(currentUser.getUserId());
        reservation.setBookId(bookId);
        reservation.setReserveTime(new Date()); // 预约时间为当前时间

        // 设置预约有效期，例如3天后过期
        Instant expireInstant = Instant.now().plus(3, ChronoUnit.DAYS);
        reservation.setExpireTime(Date.from(expireInstant));
        reservation.setStatus("pending");

        // 插入数据库
        int affectedRows = reservationMapper.insertSelective(reservation);
        return affectedRows > 0;
    }
}