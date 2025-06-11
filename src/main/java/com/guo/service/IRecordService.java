package com.guo.service;

import com.guo.domain.Vo.BorrowRecordVo;
import com.guo.utils.page.Page;
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
}