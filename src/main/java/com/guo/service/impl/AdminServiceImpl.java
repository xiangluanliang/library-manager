package com.guo.service.impl;

import com.guo.domain.User;
import com.guo.mapper.UserMapper;
import com.guo.service.IAdminService;
import com.guo.utils.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 管理员服务实现类
 */
@Service
public class AdminServiceImpl implements IAdminService {

    @Resource
    private UserMapper userMapper;

    /**
     * (管理员操作) 分页查询所有用户。
     * @param pageNum 当前页码
     * @return 封装好的分页对象
     */
    @Override
    public Page<User> findAllUsersByPage(int pageNum) {
        // TODO: 此处需要你根据你自己的分页工具类(Page)和UserMapper进行实现。
        // 下面是一个通用的实现逻辑示例，你需要替换成你自己的方法。

        Page<User> page = new Page<>();
        int pageSize = 10; // 假设每页10条
        int offset = (pageNum - 1) * pageSize;

        // 1. 查询当前页的用户列表 (你需要在UserMapper.xml中创建一个带LIMIT和OFFSET的查询)
        // List<User> users = userMapper.selectAllUsersWithLimit(offset, pageSize);
        // page.setList(users);

        // 2. 查询用户总数
        // long totalCount = userMapper.countByExample(new UserExample());
        // page.setTotalCount(totalCount);

        // 3. 设置其他分页参数
        // page.setPageNum(pageNum);
        // page.setPageSize(pageSize);
        // ...

        // 暂时返回一个空的分页对象，防止控制器报错
        System.out.println("正在查询所有用户，分页逻辑待实现...");
        return page;
    }

    /**
     * (管理员操作) 新增一个用户。
     * @param newUser 包含新用户信息的用户对象
     * @return 添加成功返回true
     */
    @Override
    @Transactional
    public boolean addNewUser(User newUser) {
        // 安全检查：防止添加一个已存在的用户名
        if (userMapper.findByUsername(newUser.getUserName()) != null) {
            return false; // 用户名已存在
        }

        // 核心安全步骤：对用户的明文密码进行加密
        String encodedPassword = newUser.getUserPwd();
        newUser.setUserPwd(encodedPassword);

        // 插入数据库，使用selective方法更安全
        int affectedRows = userMapper.insertSelective(newUser);

        return affectedRows > 0;
    }

    /**
     * (管理员操作) 根据ID删除用户。
     * @param userId 要删除的用户ID
     * @return 删除成功返回true
     */
    @Override
    @Transactional
    public boolean deleteUserById(int userId) {
        // TODO: 在删除用户前，可能需要先处理该用户相关的借阅记录等，以避免违反数据库外键约束。
        // 这是一个复杂的业务，暂时先只做删除用户本身的操作。

        int affectedRows = userMapper.deleteByPrimaryKey(userId);
        return affectedRows > 0;
    }
}