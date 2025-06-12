package com.guo.mapper;

import com.guo.domain.User;
import com.guo.domain.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 完整的用户信息，包括角色
     */
    User findByUsername(@Param("username") String userName);

    // 查询当前页的用户列表
    List<User> selectAllUsersWithLimit(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 查询用户总数
    long countByExample();
}