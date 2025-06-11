package com.guo.service;

import com.guo.domain.User;
// 导入你的分页工具类，如果路径不符请自行修改
import com.guo.utils.page.Page;

/**
 * 管理员服务接口
 * 定义了管理员专属的业务逻辑，例如对系统中的其他用户、图书等资源进行管理。
 */
public interface IAdminService {

    /**
     * (管理员操作) 分页查询系统中的所有用户。
     * @param pageNum 当前页码
     * @return 封装了用户列表和分页信息的用户Page对象。
     */
    Page<User> findAllUsersByPage(int pageNum);

    /**
     * (管理员操作) 新增一个用户。
     * @param newUser 包含新用户信息的用户对象
     * @return 添加成功返回true，失败（如用户名已存在）返回false。
     */
    boolean addNewUser(User newUser);

    /**
     * (管理员操作) 根据用户ID删除一个用户。
     * @param userId 要删除的用户ID
     * @return 删除成功返回true，失败返回false。
     */
    boolean deleteUserById(int userId);


    // ====================================================================
    // 旧IAdminService中的方法已被移除或转移：
    //
    // - adminIsExist(), adminLogin(): 被统一的IUserService.login()取代。
    // - addBook(), getBookCategories(), addBookCategory(): 这些功能应转移到IBookService和IBookCategoryService中。
    // - updateAdmin(): 管理员更新自己的信息，使用IUserService.updateUserProfile()即可。
    // ====================================================================
}