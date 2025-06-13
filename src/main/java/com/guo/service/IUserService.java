package com.guo.service;

import com.guo.domain.User;

/**
 * 用户服务接口
 * 定义了所有与用户认证和普通用户自身相关的业务逻辑。
 */
public interface IUserService{

    /**
     * 用户登录验证。
     * @param username 用户名
     * @param password 原始密码
     * @return 如果验证成功，返回完整的User对象；否则返回null。
     */
    User login(String username, String password);

    /**
     * 通过用户ID查找用户。
     * @param id 用户ID
     * @return 找到的User对象，如果不存在则返回null。
     */
    User findUserById(int id);

    /**
     * 更新用户自己的个人信息。
     * Service层不应依赖HttpServletRequest，所以移除该参数。
     * @param userToUpdate 包含待更新信息的用户对象
     * @return 更新成功返回true，失败返回false。
     */
    boolean updateUserProfile(User userToUpdate);


        /**
     * 验证用户密码
     * @param userId 用户ID
     * @param password 待验证的密码
     * @return 密码是否正确
     */
    boolean verifyPassword(Long userId, String password);

    /**
     * 根据ID删除用户（注销账号）
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteUserById(Long userId);

    /**
     * 用户借阅一本书。
     * @param bookId 准备借阅的图书ID
     * @param userId 执行操作的用户ID
     * @return 借阅成功返回true，失败返回false。
     */
    boolean borrowBook(int bookId, int userId);
    


    // ====================================================================
    // 以下方法已从IUserService中移除，因为它们不属于核心用户服务职责：
    //
    // - findUserByUserName(String userName): 已被login方法内部消化，无需对外部暴露。
    // - findAllDepts(): Department实体已废弃。
    // - userLogin(String userName, String password): 被新的login方法取代。
    // - updateUser(User user, HttpServletRequest request): 被新的updateUserProfile方法取代。
    // - findAllBorrowingBooks(HttpServletRequest request): 功能将由新的IBorrowRecordService处理。
    // - userReturnBook(int bookId, HttpServletRequest request): 被新的returnBook方法取代。
    // - userBorrowingBook(int bookId, HttpServletRequest request): 被新的borrowBook方法取代。
    // - findUserByPage(int pageNum): 这是管理员功能，应放在AdminService中。
    // - insertUser(User user): 这是管理员功能，应放在AdminService中。
    // - deleteUserById(int userId): 这是管理员功能，应放在AdminService中。
    // ====================================================================
}
