package com.guo.mapper;

import com.guo.domain.BookInventory;
import com.guo.domain.BookInventoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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
}