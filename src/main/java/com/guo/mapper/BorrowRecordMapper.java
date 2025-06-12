package com.guo.mapper;

import com.guo.domain.BorrowRecord;
import com.guo.domain.BorrowRecordExample;
import java.util.List;

import com.guo.domain.Vo.BorrowRecordVo;
import org.apache.ibatis.annotations.Param;

public interface BorrowRecordMapper {
    long countByExample(BorrowRecordExample example);

    int deleteByExample(BorrowRecordExample example);

    int deleteByPrimaryKey(Integer borrowId);

    int insert(BorrowRecord record);

    int insertSelective(BorrowRecord record);

    List<BorrowRecord> selectByExample(BorrowRecordExample example);

    BorrowRecord selectByPrimaryKey(Integer borrowId);

    int updateByExampleSelective(@Param("record") BorrowRecord record, @Param("example") BorrowRecordExample example);

    int updateByExample(@Param("record") BorrowRecord record, @Param("example") BorrowRecordExample example);

    int updateByPrimaryKeySelective(BorrowRecord record);

    int updateByPrimaryKey(BorrowRecord record);

    /**
     * 分页查询所有借阅记录并包含用户和图书详细信息
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 借阅记录列表
     */
    List<BorrowRecordVo> selectAllWithDetailsByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 根据用户ID查询借阅记录并包含用户和图书详细信息
     * @param userId 用户ID
     * @return 借阅记录列表
     */
    List<BorrowRecordVo> findWithDetailsByUserId(@Param("userId") int userId);

    /**
     * 统计所有借阅记录数量
     * @return 借阅记录总数
     */
    long countAll();


    // 统计某本书未归还的借阅记录数量
    long countOutstandingByBookId(@Param("bookId") int bookId);
}