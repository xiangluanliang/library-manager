package com.guo.mapper;

import com.guo.domain.ReturnRecord;
import com.guo.domain.ReturnRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReturnRecordMapper {
    long countByExample(ReturnRecordExample example);

    int deleteByExample(ReturnRecordExample example);

    int deleteByPrimaryKey(Integer returnId);

    int insert(ReturnRecord record);

    int insertSelective(ReturnRecord record);

    List<ReturnRecord> selectByExample(ReturnRecordExample example);

    ReturnRecord selectByPrimaryKey(Integer returnId);

    int updateByExampleSelective(@Param("record") ReturnRecord record, @Param("example") ReturnRecordExample example);

    int updateByExample(@Param("record") ReturnRecord record, @Param("example") ReturnRecordExample example);

    int updateByPrimaryKeySelective(ReturnRecord record);

    int updateByPrimaryKey(ReturnRecord record);
}