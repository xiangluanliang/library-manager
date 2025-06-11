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
    @Override
    public Page<BorrowRecordVo> findAllRecordsByPage(int pageNum) {
        // TODO: 这是解决N+1查询的关键。你需要在BorrowRecordMapper.xml中创建一个新的查询和对应的ResultMap。
        //  1. 创建一个新的<resultMap id="BorrowRecordVoResultMap" type="com.guo.domain.Vo.BorrowRecordVo">
        //  2. 在ResultMap中，将user.user_name映射到userName属性，将book_info.title映射到bookTitle属性。
        //  3. 创建一个新的<select id="selectAllWithDetailsByPage">，使用LEFT JOIN连接borrow_record, user, book_info三张表。

        System.out.println("正在查询所有借阅记录，分页和JOIN查询逻辑待Mapper层实现...");
        Page<BorrowRecordVo> page = new Page<>();
        // int pageSize = 10;
        // int offset = (pageNum - 1) * pageSize;
        // List<BorrowRecordVo> records = borrowRecordMapper.selectAllWithDetailsByPage(offset, pageSize);
        // long totalCount = borrowRecordMapper.countAll();
        // page.setList(records);
        // page.setTotalCount(totalCount);
        // ...
        return page;
    }

    /**
     * 查询指定用户的所有借阅记录。
     */
    @Override
    public List<BorrowRecordVo> findRecordsByUserId(int userId) {
        // TODO: 与上面类似，在BorrowRecordMapper.xml中创建一个新的查询方法 findWithDetailsByUserId。
        //  它也使用JOIN联表查询，但会根据传入的userId进行过滤。

        System.out.println("正在查询用户 " + userId + " 的借阅记录，JOIN查询逻辑待Mapper层实现...");
        // return borrowRecordMapper.findWithDetailsByUserId(userId);
        return null; // 暂时返回null
    }
}