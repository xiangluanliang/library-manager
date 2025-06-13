package com.guo.service.impl;

import com.guo.domain.*;
import com.guo.domain.Vo.BorrowRecordVo;
import com.guo.domain.Vo.ReservationVo;
import com.guo.mapper.BookInventoryMapper;
import com.guo.mapper.BorrowRecordMapper;
import com.guo.mapper.ReservationMapper;
import com.guo.mapper.ReturnRecordMapper;
import com.guo.service.IRecordService;
import com.guo.utils.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
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
    @Resource
    private ReturnRecordMapper returnRecordMapper;

    @Resource
    private BookInventoryMapper bookInventoryMapper;


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
        page.setPageCount((int) Math.ceil((double) totalCount / pageSize));
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


    @Override
    public Page<BorrowRecordVo> findUnreturnedRecordsByPage(int pageNum) {
        Page<BorrowRecordVo> page = new Page<>();
        int pageSize = 10;
        int offset = (pageNum - 1) * pageSize;
        // 调用新的Mapper方法
        List<BorrowRecordVo> records = borrowRecordMapper.findUnreturnedWithDetailsByPage(offset, pageSize);
        long totalCount = borrowRecordMapper.countUnreturned();

        page.setList(records);
        page.setPageCount((int) Math.ceil((double) totalCount / pageSize));
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        return page;
    }

    @Override
    @Transactional
    public boolean executeReturn(int borrowId) {
        try {
            // 1. 查找原始借阅记录
            BorrowRecord record = borrowRecordMapper.selectByPrimaryKey(borrowId);
            if (record == null || "returned".equals(record.getStatus())) return false;

            // 2. 更新借阅记录状态为 "returned"
            record.setStatus("returned");
            borrowRecordMapper.updateByPrimaryKeySelective(record);

            // 3. 插入还书记录
            ReturnRecord returnRecord = new ReturnRecord();
            returnRecord.setBorrowId(borrowId);
            returnRecord.setReturnTime(new Date());
            returnRecordMapper.insert(returnRecord);

            // 4. 库存加1（注意：这里暂时不加，因为马上可能被预约者借走）
            // bookInventoryMapper.incrementAvailableCopies(record.getBookId());

            // 5. 【核心逻辑】检查这本书是否有等待中的预约
            Reservation nextReservation = reservationMapper.findNextPendingReservation(record.getBookId());

            if (nextReservation != null) {
                System.out.println("检测到预约，自动为用户 " + nextReservation.getUserId() + " 办理借阅...");
                // 如果有预约，直接为该用户创建一笔新的借阅记录

                // a. 创建新的借阅记录
                BorrowRecord newBorrow = new BorrowRecord();
                newBorrow.setUserId(nextReservation.getUserId());
                newBorrow.setBookId(record.getBookId());
                newBorrow.setBorrowTime(new Date());

                // b. 计算新的应还日期
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, nextReservation.getBorrowDurationDays());
                newBorrow.setDueTime(cal.getTime());

                newBorrow.setStatus("borrowed");
                borrowRecordMapper.insert(newBorrow);

                // c. 更新预约状态为 "fulfilled" (已完成)
                nextReservation.setStatus("fulfilled");
                reservationMapper.updateByPrimaryKeySelective(nextReservation);

                // d. 因为书马上又被借走了，所以库存实际上没有变化，不需要执行加1操作
            } else {
                // 如果没有预约，才真正地把库存加1
                System.out.println("未检测到预约，库存+1。");
                bookInventoryMapper.incrementAvailableCopies(record.getBookId());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("执行还书操作失败", e);
        }
    }

    /**
     * 根据用户ID查找其所有的预约记录详情。
     */
    @Override
    public List<ReservationVo> findReservationsByUserId(int userId) {
        // 直接调用我们刚刚在Mapper中创建的新方法
        return reservationMapper.findReservationsWithDetailsByUserId(userId);
    }

    /**
     * 用户取消一个等待中的预约。
     */
    @Override
    @Transactional // 更新操作，推荐使用事务
    public boolean cancelReservation(int reservationId, int userId) {
        // 1. 从数据库中获取该条预约记录的完整信息
        Reservation reservation = reservationMapper.selectByPrimaryKey(reservationId);

        // 2. 安全校验：
        //    a. 检查预约是否存在
        //    b. 检查这条预约记录是否属于当前操作的用户
        //    c. 检查预约状态是否为'pending'（只有等待中的预约才能被取消）
        if (reservation != null && reservation.getUserId().equals(userId) && "pending".equals(reservation.getStatus())) {

            // 3. 将状态更新为'canceled'
            Reservation reservationToUpdate = new Reservation();
            reservationToUpdate.setReserveId(reservationId);
            reservationToUpdate.setStatus("canceled");

            // 4. 执行选择性更新，只更新status字段
            int affectedRows = reservationMapper.updateByPrimaryKeySelective(reservationToUpdate);

            return affectedRows > 0;
        }

        // 如果校验不通过，直接返回失败
        System.out.println("取消预约失败：记录不存在，或用户无权操作，或状态不正确。");
        return false;
    }

    @Override
    public Page<ReservationVo> findAllReservationsByPage(int pageNum) {
        Page<ReservationVo> page = new Page<>();
        int pageSize = 10; // Assume 10 records per page
        int offset = (pageNum - 1) * pageSize;

        // 1. Call the mapper to get the paginated list of data
        List<ReservationVo> records = reservationMapper.findAllWithDetailsByPage(offset, pageSize);
        page.setList(records != null ? records : new ArrayList<>());

        // 2. Call the mapper to get the total count of records
        long totalCount = reservationMapper.countAllReservations();

        // 3. Assemble the rest of the Page object's properties
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        int pageCount = (int) Math.ceil((double) totalCount / pageSize);
        page.setPageCount(pageCount > 0 ? pageCount : 1);

        return page;
    }
}