package com.guo.mapper;

import com.guo.domain.BookInfo;
import com.guo.domain.BookInfoExample;
import com.guo.domain.Vo.BookInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    /**
     * 高级搜索的Mapper方法
     */
    List<BookInfoVo> searchAdvanced(@Param("field") String field, @Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 与高级搜索配套的数量统计方法
     */
    int countAdvanced(@Param("field") String field, @Param("keyword") String keyword);

    /**
     * 根据关键词统计符合条件的图书数量
     * @param keyword 搜索关键词
     * @return 符合条件的图书数量
     */
    int countSearchedBooks(@Param("keyword") String keyword);


}