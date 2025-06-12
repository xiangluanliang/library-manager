package com.guo.service.impl;

import com.guo.domain.BookInfo;
import com.guo.domain.BookInfoExample;
import com.guo.domain.BookInventory;
import com.guo.domain.Vo.BookInfoVo;
import com.guo.mapper.BookInfoMapper;
import com.guo.mapper.BookInventoryMapper;
import com.guo.mapper.BorrowRecordMapper;
import com.guo.service.IBookService;
import com.guo.utils.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书服务实现类
 */
@Service
public class BookServiceImpl implements IBookService {

    @Resource
    private BookInfoMapper bookInfoMapper;

    @Resource
    private BookInventoryMapper bookInventoryMapper;

     @Resource
    private BorrowRecordMapper borrowRecordMapper;

    /**
     * 根据关键词搜索图书并分页。
     * @param field 搜索字段
     * @param keyword 搜索关键词
     * @param pageNum 当前页码
     * @return 分页结果
     */
    /**
     * 根据关键词搜索图书并分页。
     * @param keyword 搜索关键词
     * @param pageNum 当前页码
     * @return 分页结果
     */
    @Override
    public Page<BookInfoVo> searchAndPaginate(String field, String keyword, int pageNum) {
        System.out.println("【Service层】正在执行高级搜索: 字段=" + field + ", 关键词=" + keyword + ", 页码=" + pageNum);
        Page<BookInfoVo> page = new Page<>();
        page.setList(new ArrayList<>());
        int pageSize = 10;
        int offset = (pageNum - 1) * pageSize;
        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? "%" + keyword.trim() + "%" : null;
//         1. 调用Mapper获取分页后的数据列表
        List<BookInfoVo> books = bookInfoMapper.searchAdvanced(field, searchKeyword, offset, pageSize);
        if (books != null) {
            page.setList(books);
        }
//         2. 调用Mapper获取符合条件的总数
        int totalCount = bookInfoMapper.countAdvanced(field, searchKeyword);
        page.setPageCount(totalCount);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPageCount((int) Math.ceil((double) totalCount / pageSize));
        if (page.getPageCount() == 0) {
            page.setPageCount(1);
        }

        return page;
    }

    @Override
    @Transactional
    public boolean addNewBook(BookInfo bookInfo, int totalCopies) {
        try {
            // 【新增】步骤1: 检查ISBN是否已存在
            BookInfoExample example = new BookInfoExample();
            example.createCriteria().andIsbnEqualTo(bookInfo.getIsbn());
            long existingBooks = bookInfoMapper.countByExample(example);

            if (existingBooks > 0) {
                System.out.println("【日志】添加失败：ISBN " + bookInfo.getIsbn() + " 已存在。");
                return false; // 直接返回失败，不尝试插入
            }

            // 步骤2: 插入图书基本信息
            bookInfoMapper.insertSelective(bookInfo);
            System.out.println("【日志】新增图书后获取到的ID: " + bookInfo.getBookId());

            // 步骤3: 检查是否成功获取到主键ID
            if (bookInfo.getBookId() == null || bookInfo.getBookId() == 0) {
                throw new RuntimeException("插入book_info表失败，未能获取自增ID。");
            }

            // 步骤4: 创建并插入库存信息
            BookInventory inventory = new BookInventory();
            inventory.setBookId(bookInfo.getBookId());
            inventory.setTotalCopies(totalCopies);
            inventory.setAvailableCopies(totalCopies);
            inventory.setLocation("A-01-01");
            System.out.println("【日志】准备插入的库存信息: " + inventory.toString());

            int affectedRows = bookInventoryMapper.insertSelective(inventory);

            if (affectedRows <= 0) {
                throw new RuntimeException("插入book_inventory表失败。");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据ID删除一本图书。
     * @param bookId 要删除的图书ID
     * @return 删除成功返回true
     */
    @Override
    @Transactional
    public boolean deleteBookById(int bookId) {
        // 1. 检查这本书是否还有未归还的借阅记录，如果有，则不允许删除。
        long outstandingBorrows = borrowRecordMapper.countOutstandingByBookId(bookId);
        if (outstandingBorrows > 0) {
            System.out.println("无法删除，该书尚有未归还的借阅记录。");
            return false;
        }

        // 2. 删除库存记录。为了避免外键约束问题，先删除子表记录。
        bookInventoryMapper.deleteByBookId(bookId);

        // 3. 删除图书基本信息
        int affectedRows = bookInfoMapper.deleteByPrimaryKey(bookId);

        return affectedRows > 0;
    }


    /**
     * 根据ID查找一本图书。
     * @param bookId 图书ID
     * @return BookInfoVo对象
     */
    @Override
    public BookInfoVo findBookById(int bookId) {
        return bookInfoMapper.selectVoByPrimaryKey(bookId);
    }

    @Override
    @Transactional
    public boolean updateBook(BookInfoVo bookVo) {
        try {
            // 步骤1: 更新图书基本信息 (book_info表)
            // BookInfoVo继承自BookInfo，所以可以直接传入
            int bookInfoRows = bookInfoMapper.updateByPrimaryKeySelective(bookVo);

            // 步骤2: 更新库存信息 (book_inventory表)
            BookInventory inventory = new BookInventory();
            inventory.setBookId(bookVo.getBookId());
            inventory.setTotalCopies(bookVo.getTotalCopies());
            inventory.setAvailableCopies(bookVo.getAvailableCopies());
            // 你可以根据需要更新location等其他字段

            // 使用updateByBookId这个自定义方法，避免主键问题
            int inventoryRows = bookInventoryMapper.updateByBookIdSelective(inventory);

            // 只有两个更新都至少影响了一行（或按你的业务逻辑判断），才算成功
            return bookInfoRows > 0 && inventoryRows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}