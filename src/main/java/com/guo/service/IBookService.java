package com.guo.service;

import com.guo.domain.BookInfo;
import com.guo.domain.Vo.BookInfoVo;
import com.guo.utils.page.Page;

/**
 * 图书服务接口
 * 定义了所有与图书相关的核心业务逻辑。
 */
public interface IBookService {

    /**
     * 根据关键词搜索图书并进行分页。
     * @param field 搜索字段
     * @param keyword 搜索关键词
     * @param pageNum 当前页码
     * @return 封装了图书列表和分页信息的Page对象。
     */
    Page<BookInfoVo> searchAndPaginate(String field, String keyword, int pageNum);

    /**
     * 新增一本图书。
     * 这个操作需要同时向book_info表和book_inventory表写入数据。
     * @param bookInfo 包含图书基本信息的对象
     * @param totalCopies 要入库的总数量
     * @return 添加成功返回true，失败返回false。
     */
    boolean addNewBook(BookInfo bookInfo, int totalCopies);

    /**
     * 根据ID删除一本图书。
     * @param bookId 要删除的图书ID
     * @return 删除成功返回true，失败返回false。
     */
    boolean deleteBookById(int bookId);

    /**
     * 根据ID查找一本图书的详细信息。
     * @param bookId 图书ID
     * @return BookInfo对象，不存在则返回null。
     */
    BookInfo findBookById(int bookId);

}