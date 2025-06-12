package com.guo.service.impl;

import com.guo.domain.Vo.BorrowRecordVo;
import com.guo.mapper.BorrowRecordMapper;
import com.guo.service.IRecordService;
import com.guo.utils.page.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 记录服务实现类
 */
@Service
public class RecordServiceImpl implements IRecordService {

    @Resource
    private BorrowRecordMapper borrowRecordMapper;

    /**
     * 分页查询所有借阅记录。
     */
    /**
     * 分页查询所有借阅记录。
     */
    @Override
    public Page<BorrowRecordVo> findAllRecordsByPage(int pageNum) {
        int pageSize = 10;
        int offset = (pageNum - 1) * pageSize;

        // 1. 调用Mapper获取分页后的数据列表
        List<BorrowRecordVo> records = borrowRecordMapper.selectAllWithDetailsByPage(offset, pageSize);

        // 2. 调用Mapper获取符合条件的总数
        long totalCount = borrowRecordMapper.countAll();

        // 3. 设置分页对象
        Page<BorrowRecordVo> page = new Page<>();
        page.setList(records);
        //TODO:page.setTotalCount(totalCount);没有这个方法，这行代码的作用是将查询到的所有借阅记录的总数赋值给分页对象 Page 的 totalCount 字段。
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);

        return page;
    }

    /**
     * 查询指定用户的所有借阅记录。
     */
    @Override
    public List<BorrowRecordVo> findRecordsByUserId(int userId) {
        return borrowRecordMapper.findWithDetailsByUserId(userId);
    }
}