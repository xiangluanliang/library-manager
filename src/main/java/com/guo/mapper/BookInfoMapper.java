package com.guo.mapper;

import com.guo.domain.BookInfo;
import com.guo.domain.BookInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BookInfoMapper {
    long countByExample(BookInfoExample example);

    int deleteByExample(BookInfoExample example);

    int deleteByPrimaryKey(Integer bookId);

    int insert(BookInfo record);

    int insertSelective(BookInfo record);

    List<BookInfo> selectByExampleWithBLOBs(BookInfoExample example);

    List<BookInfo> selectByExample(BookInfoExample example);

    BookInfo selectByPrimaryKey(Integer bookId);

    int updateByExampleSelective(@Param("record") BookInfo record, @Param("example") BookInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") BookInfo record, @Param("example") BookInfoExample example);

    int updateByExample(@Param("record") BookInfo record, @Param("example") BookInfoExample example);

    int updateByPrimaryKeySelective(BookInfo record);

    int updateByPrimaryKeyWithBLOBs(BookInfo record);

    int updateByPrimaryKey(BookInfo record);
}