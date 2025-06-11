package com.guo.service.impl;

import com.guo.domain.BookInfo;
import com.guo.domain.BookInventory;
import com.guo.mapper.BookInfoMapper;
import com.guo.mapper.BookInventoryMapper;
import com.guo.service.IBookService;
import com.guo.utils.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 图书服务实现类
 */
@Service
public class BookServiceImpl implements IBookService {

    @Resource
    private BookInfoMapper bookInfoMapper;

    @Resource
    private BookInventoryMapper bookInventoryMapper;

    // @Resource
    // private BorrowRecordMapper borrowRecordMapper;

    /**
     * 根据关键词搜索图书并分页。
     * @param keyword 搜索关键词
     * @param pageNum 当前页码
     * @return 分页结果
     */
    @Override
    public Page<BookInfo> searchAndPaginate(String keyword, int pageNum) {
        // TODO: 这是解决N+1查询的关键。你需要在BookInfoMapper.xml中创建一个新的查询方法，
        //  例如 "searchBooksWithInventory"，该方法使用LEFT JOIN连接book_info和book_inventory表。
        //  SQL示例: SELECT bi.*, inv.available_copies FROM book_info bi LEFT JOIN book_inventory inv ON bi.book_id = inv.book_id
        //  WHERE bi.title LIKE #{keyword} OR bi.author LIKE #{keyword}
        //  LIMIT #{offset}, #{pageSize}

        System.out.println("正在搜索图书，分页和JOIN查询逻辑待Mapper层实现...");
        Page<BookInfo> page = new Page<>();
        // 1. 调用Mapper获取分页后的数据列表
        // List<BookInfo> books = bookInfoMapper.searchBooksWithInventory(keyword, (pageNum-1)*10, 10);
        // page.setList(books);
        // 2. 调用Mapper获取符合条件的总数
        // long totalCount = bookInfoMapper.countSearchedBooks(keyword);
        // page.setTotalCount(totalCount);
        // 3. 设置其他分页参数
        // ...
        return page; // 暂时返回空对象
    }

    /**
     * 新增一本图书，包含其库存信息。
     * 使用@Transactional注解确保操作的原子性。
     * @param bookInfo 包含图书基本信息的对象
     * @param totalCopies 要入库的总数量
     * @return 添加成功返回true
     */
    @Override
    @Transactional
    public boolean addNewBook(BookInfo bookInfo, int totalCopies) {
        try {
            // 步骤1: 插入图书基本信息。使用insertSelective更安全。
            // 插入后，MyBatis会自动将生成的主键ID回填到bookInfo对象中。
            bookInfoMapper.insertSelective(bookInfo);

            // 步骤2: 检查是否成功获取到主键ID
            if (bookInfo.getBookId() == null || bookInfo.getBookId() == 0) {
                // 如果没有获取到ID，说明插入失败，抛出异常以回滚事务
                throw new RuntimeException("插入book_info表失败，未能获取自增ID。");
            }

            // 步骤3: 创建并插入库存信息
            BookInventory inventory = new BookInventory();
            inventory.setBookId(bookInfo.getBookId());
            inventory.setTotalCopies(totalCopies);
            inventory.setAvailableCopies(totalCopies); // 初始时，可借数量等于总数量
            // inventory.setLocation("默认位置"); // 可以设置一个默认位置
            bookInventoryMapper.insert(inventory);

            return true;
        } catch (Exception e) {
            // 捕获任何异常，打印日志，然后事务会自动回滚
            // 在实际项目中，应使用更专业的日志框架，如SLF4J
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
        // TODO: 实现完整的删除业务逻辑
        // 1. 检查这本书是否还有未归还的借阅记录，如果有，则不允许删除。
        // long outstandingBorrows = borrowRecordMapper.countOutstandingByBookId(bookId);
        // if (outstandingBorrows > 0) {
        //     System.out.println("无法删除，该书尚有未归还的借阅记录。");
        //     return false;
        // }

        // 2. 删除库存记录。为了避免外键约束问题，先删除子表记录。
        //    你需要在BookInventoryMapper中创建一个deleteByBookId方法。
        // bookInventoryMapper.deleteByBookId(bookId);

        // 3. 删除图书基本信息
        int affectedRows = bookInfoMapper.deleteByPrimaryKey(bookId);

        return affectedRows > 0;
    }

    /**
     * 根据ID查找一本图书。
     * @param bookId 图书ID
     * @return BookInfo对象
     */
    @Override
    public BookInfo findBookById(int bookId) {
        return bookInfoMapper.selectByPrimaryKey(bookId);
    }
}