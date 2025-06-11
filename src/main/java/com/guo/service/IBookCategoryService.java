package com.guo.service;

import com.guo.domain.BookCategory;
import java.util.List;

/**
 * 图书分类服务接口
 * 定义了对图书分类进行增、删、改、查等操作的业务逻辑。
 */
public interface IBookCategoryService {

    /**
     * 获取所有的图书分类。
     * 通常用于在添加/编辑图书时，填充分类下拉列表。
     * @return 图书分类的列表。
     */
    List<BookCategory> findAll();

    /**
     * 根据ID查找一个分类。
     * @param categoryId 分类ID
     * @return BookCategory对象，不存在则返回null。
     */
    BookCategory findById(int categoryId);

    /**
     * 添加一个新的图书分类。
     * @param category 包含新分类信息的对象
     * @return 添加成功返回true，失败返回false。
     */
    boolean addCategory(BookCategory category);

    /**
     * 更新一个已有的图书分类。
     * @param category 包含更新信息的对象
     * @return 更新成功返回true，失败返回false。
     */
    boolean updateCategory(BookCategory category);

    /**
     * 根据ID删除一个图书分类。
     * @param categoryId 要删除的分类ID
     * @return 删除成功返回true，失败返回false。
     */
    boolean deleteCategoryById(int categoryId);
}