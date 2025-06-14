package com.guo.service.impl;

import com.guo.domain.BookInventory;
import com.guo.domain.BorrowRecord;
import com.guo.domain.User;
import com.guo.mapper.BookInventoryMapper;
import com.guo.mapper.BorrowRecordMapper;
import com.guo.mapper.UserMapper;
import com.guo.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户服务实现类
 * 负责处理用户认证和普通用户相关的业务逻辑
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;


     @Resource
     private BorrowRecordMapper borrowRecordMapper;
     @Resource
     private BookInventoryMapper bookInventoryMapper;

    /**
     * 用户登录的核心实现
     * @param username 用户名
     * @param password 用户输入的原始密码
     * @return 成功则返回User对象，失败则返回null
     */
    @Override
    public User login(String username, String password) {
        // --- ↓↓↓ 新增日志 ↓↓↓ ---
        System.out.println("=============================================");
        System.out.println("【日志】尝试登录，用户名: " + username);
        System.out.println("【日志】输入的密码: " + password);

        // 1. 根据用户名从数据库查找用户
        User userFromDb = userMapper.findByUsername(username);

        // --- ↓↓↓ 新增日志，这是回答你问题的关键！ ↓↓↓ ---
        System.out.println("【日志】从数据库查询到的用户对象: " + userFromDb);

        // 2. 检查用户是否存在
        if (userFromDb == null) {
            System.out.println("【日志】登录失败：用户名不存在。");
            System.out.println("=============================================");
            return null; // 用户名不存在
        }

        System.out.println("【日志】数据库中的密码: " + userFromDb.getUserPwd());

        // 3. 直接使用字符串的 .equals() 方法进行明文密码比较
        if (password.equals(userFromDb.getUserPwd())) {
            System.out.println("【日志】登录成功：密码匹配正确！");
            System.out.println("=============================================");
            // 密码正确，返回完整的用户信息
            return userFromDb;
        }

        System.out.println("【日志】登录失败：密码错误。");
        System.out.println("=============================================");
        // 密码错误，返回null
        return null;
    }

    /**
     * 根据用户ID查找用户
     * @param id 用户ID
     * @return User对象或null
     */
    @Override
    public User findUserById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 更新用户个人信息，并增加了对空密码字段的处理。
     * @param userToUpdate 包含待更新信息的用户对象
     * @return 更新成功返回true，否则返回false
     */
    @Override
    @Transactional
    public boolean updateUserProfile(User userToUpdate) {

        // 【关键修正】在更新前，检查密码字段
        // 如果用户提交的密码是空字符串，我们就把它设置为null
        if (userToUpdate.getUserPwd() != null && userToUpdate.getUserPwd().isEmpty()) {
            userToUpdate.setUserPwd(null);
        }

        // 执行选择性更新
        int affectedRows = userMapper.updateByPrimaryKeySelective(userToUpdate);

        return affectedRows > 0;
    }


    /**
     * 用户借书
     * @param bookId 图书ID
     * @param userId 用户ID
     * @return 借阅成功返回true，否则返回false
     */
    @Override
    @Transactional
    public boolean borrowBook(int bookId, int userId) {
        // 1. 检查用户借书数量是否已达上限 (查询borrow_record表)
        int borrowCount = borrowRecordMapper.countBorrowedBooksByUserId(userId);
        int borrowLimit = 3; // 假设用户最多可借3本书
        if (borrowCount >= borrowLimit) {
            System.out.println("用户 " + userId + " 借书数量已达上限，无法继续借书。");
            return false;
        }

        // 2. 检查图书库存是否充足 (查询book_inventory表的available_copies)
        BookInventory bookInventory = bookInventoryMapper.selectByBookId(bookId);
        if (bookInventory == null || bookInventory.getAvailableCopies() <= 0) {
            System.out.println("图书 " + bookId + " 库存不足，无法借阅。");
            return false;
        }

        // 3. 如果可以借，则更新book_inventory表的available_copies数量（减1）
        bookInventory.setAvailableCopies(bookInventory.getAvailableCopies() - 1);
        int updateCount = bookInventoryMapper.updateByPrimaryKeySelective(bookInventory);
        if (updateCount <= 0) {
            System.out.println("更新图书 " + bookId + " 库存失败，无法借阅。");
            return false;
        }

        // 4. 在borrow_record表中插入一条新的借阅记录，状态为'borrowed'
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUserId(userId);
        borrowRecord.setBookId(bookId);
        borrowRecord.setBorrowTime(new Date());
        // 假设借阅期限为30天
        long dueTime = System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000;
        borrowRecord.setDueTime(new Date(dueTime));
        borrowRecord.setStatus("borrowed");
        int insertCount = borrowRecordMapper.insertSelective(borrowRecord);
        if (insertCount <= 0) {
            System.out.println("插入借阅记录失败，无法借阅。");
            return false;
        }

        return true;
    }

        /**
     * 验证用户密码
     * @param userId 用户ID
     * @param password 待验证的密码
     * @return 密码是否正确
     */
    @Override
    public boolean verifyPassword(Long userId, String password) {
        // 1. 根据ID获取用户
        User user = userMapper.selectByPrimaryKey(Math.toIntExact(userId));
        if (user == null) {
            System.out.println("【密码验证】用户不存在，ID: " + userId);
            return false;
        }

        // 2. 比较密码（明文比较）
        boolean passwordValid = password.equals(user.getUserPwd());

        System.out.println("【密码验证】用户ID: " + userId +
                " | 密码匹配结果: " + passwordValid);

        return passwordValid;
    }


    /**
     * 根据ID删除用户（注销账号）
     * @param userId 用户ID
     * @return 是否删除成功
     */
    @Override
    public boolean deleteUserById(Long userId) {
        System.out.println("【账号注销】开始处理用户ID: " + userId);

        // 1. 检查用户是否存在
        User user = userMapper.selectByPrimaryKey(Math.toIntExact(userId));
        if (user == null) {
            System.out.println("【账号注销】失败：用户不存在，ID: " + userId);
            return false;
        }

        // 2. 检查是否有未归还的图书
        int borrowCount = borrowRecordMapper.countBorrowedBooksByUserId(Math.toIntExact(userId));
        if (borrowCount>0) {
            System.out.println("【账号注销】失败：用户还有未归还的图书，ID: " + userId);
            return false;
        }

        // 3. 删除用户（实际项目中建议软删除）
        int affectedRows = userMapper.deleteByPrimaryKey(Math.toIntExact(userId));
        boolean success = affectedRows > 0;

        System.out.println("【账号注销】结果: " + (success ? "成功" : "失败") +
                " | 影响行数: " + affectedRows);

        return success;
    }

    // --- 以下是旧文件中不再属于UserService职责的方法，已被移除 ---
    // - findAllDepts() -> 已删除
    // - findUserByPage() -> 应属于AdminService
    // - insertUser() -> 应属于AdminService
    // - deleteUserById() -> 应属于AdminService
    // - findUserByUserName() -> 已被login方法内部使用，不再需要暴露为public
}
