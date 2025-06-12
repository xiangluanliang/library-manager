package com.guo.service.impl;

import com.guo.domain.BookCategory;
import com.guo.domain.BookCategoryExample;
import com.guo.mapper.BookCategoryMapper;
import com.guo.mapper.BookInfoMapper;
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


     @Resource
     private BookInfoMapper bookInfoMapper;

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
        // 检查该分类下是否还有图书
        int bookCount = bookInfoMapper.countByCategoryId(categoryId);
        if (bookCount > 0) {
            // 分类下有图书，不允许删除
            return false;
        }

        // 分类下没有图书，执行删除操作
        int affectedRows = bookCategoryMapper.deleteByPrimaryKey(categoryId);
        return affectedRows > 0;
    }
}