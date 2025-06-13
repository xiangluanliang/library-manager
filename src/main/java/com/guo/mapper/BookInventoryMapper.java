package com.guo.mapper;

import com.guo.domain.BookInventory;
import com.guo.domain.BookInventoryExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookInventoryMapper {
    long countByExample(BookInventoryExample example);

    int deleteByExample(BookInventoryExample example);

    int deleteByPrimaryKey(Integer inventoryId);

    int insert(BookInventory record);

    int insertSelective(BookInventory record);

    List<BookInventory> selectByExample(BookInventoryExample example);

    BookInventory selectByPrimaryKey(Integer inventoryId);

    int updateByExampleSelective(@Param("record") BookInventory record, @Param("example") BookInventoryExample example);

    int updateByExample(@Param("record") BookInventory record, @Param("example") BookInventoryExample example);

    int updateByPrimaryKeySelective(BookInventory record);

    int updateByPrimaryKey(BookInventory record);

    // 根据图书ID删除库存记录
    int deleteByBookId(@Param("bookId") int bookId);

    // 根据图书ID查询库存信息
    BookInventory selectByBookId(@Param("bookId") int bookId);

    /**
     * 根据book_id选择性地更新库存信息。
     * 只会更新record对象中值不为null的字段。
     * @param record 包含待更新库存信息的对象
     * @return 受影响的行数
     */
    int updateByBookIdSelective(BookInventory record);


    int incrementAvailableCopies(@Param("bookId") int bookId);
}