package com.guo.service.impl;

import com.guo.domain.User;
import com.guo.mapper.UserMapper;
import com.guo.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户服务实现类
 * 负责处理用户认证和普通用户相关的业务逻辑
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    // TODO: 后续实现具体业务时，注入新的Mapper
    // @Resource
    // private BorrowRecordMapper borrowRecordMapper;
    // @Resource
    // private BookInventoryMapper bookInventoryMapper;

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
     * 更新用户个人信息（由用户自己操作）
     * @param userToUpdate 包含待更新信息的用户对象
     * @return 更新成功返回true，否则返回false
     */
    @Override
    @Transactional // 建议更新操作都加上事务注解
    public boolean updateUserProfile(User userToUpdate) {
        // 使用 selective 方法，只会更新 userToUpdate 对象中不为null的字段
        // 这样可以避免将用户的密码、角色等重要信息意外置空
        int affectedRows = userMapper.updateByPrimaryKeySelective(userToUpdate);
        return affectedRows > 0;
    }


    /**
     * 用户借书（未来需要实现的业务）
     * @param bookId 图书ID
     * @param userId 用户ID
     * @return 借阅成功返回true，否则返回false
     */
    @Override
    @Transactional
    public boolean borrowBook(int bookId, int userId) {
        // TODO: 实现完整的借书业务逻辑
        // 1. 检查用户借书数量是否已达上限 (查询user表或borrow_record表)
        // 2. 检查图书库存是否充足 (查询book_inventory表的available_copies)
        // 3. 如果可以借，则更新book_inventory表的available_copies数量（减1）
        // 4. 在borrow_record表中插入一条新的借阅记录，状态为'borrowed'
        // 5. 如果以上任一步失败，则事务回滚，借书失败
        System.out.println("用户 " + userId + " 正在尝试借阅图书 " + bookId + "（逻辑待实现）");
        return false; // 暂时返回false
    }

    /**
     * 用户还书（未来需要实现的业务）
     * @param bookId 图书ID
     * @param userId 用户ID
     * @return 归还成功返回true，否则返回false
     */
    @Override
    @Transactional
    public boolean returnBook(int bookId, int userId) {
        // TODO: 实现完整的还书业务逻辑
        // 1. 查找该用户对应的该图书的、状态为'borrowed'或'overdue'的借阅记录 (查询borrow_record表)
        // 2. 如果找到记录，则更新该条记录的状态为'returned'
        // 3. 在return_record表中插入一条新的归还记录
        // 4. 更新book_inventory表的available_copies数量（加1）
        // 5. 如果有逾期，可能还需要处理罚金逻辑（查询overdue_record表）
        // 6. 如果以上任一步失败，则事务回滚，还书失败
        System.out.println("用户 " + userId + " 正在尝试归还图书 " + bookId + "（逻辑待实现）");
        return false; // 暂时返回false
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
