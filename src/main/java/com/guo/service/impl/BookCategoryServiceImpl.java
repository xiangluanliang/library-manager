package com.guo.service.impl;

import com.guo.domain.BookCategory;
import com.guo.domain.BookCategoryExample;
import com.guo.mapper.BookCategoryMapper;
import com.guo.service.IBookCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 图书分类服务实现类
 */
@Service
public class BookCategoryServiceImpl implements IBookCategoryService {

    @Resource
    private BookCategoryMapper bookCategoryMapper;

    // TODO: 后续为实现更复杂的业务逻辑（如删除分类前检查其下是否有书），可能需要注入BookInfoMapper
    // @Resource
    // private BookInfoMapper bookInfoMapper;

    /**
     * 获取所有图书分类。
     */
    @Override
    public List<BookCategory> findAll() {
        // 创建一个空的Example对象，表示查询所有记录
        return bookCategoryMapper.selectByExample(new BookCategoryExample());
    }

    /**
     * 根据ID查找分类。
     */
    @Override
    public BookCategory findById(int categoryId) {
        return bookCategoryMapper.selectByPrimaryKey(categoryId);
    }

    /**
     * 添加新分类。
     */
    @Override
    @Transactional
    public boolean addCategory(BookCategory category) {
        // 使用insertSelective更安全，它只插入非空字段
        int affectedRows = bookCategoryMapper.insertSelective(category);
        return affectedRows > 0;
    }

    /**
     * 更新分类。
     */
    @Override
    @Transactional
    public boolean updateCategory(BookCategory category) {
        int affectedRows = bookCategoryMapper.updateByPrimaryKeySelective(category);
        return affectedRows > 0;
    }

    /**
     * 根据ID删除分类。
     */
    @Override
    @Transactional
    public boolean deleteCategoryById(int categoryId) {
        // TODO: 实现更安全的删除逻辑。
        // 在真正删除分类前，应该先检查该分类下是否还有图书。
        // 1. 调用 bookInfoMapper.countByCategoryId(categoryId)
        // 2. 如果数量大于0，则不允许删除，直接返回false。
        // 3. 如果数量为0，才执行下面的删除操作。

        int affectedRows = bookCategoryMapper.deleteByPrimaryKey(categoryId);
        return affectedRows > 0;
    }
}