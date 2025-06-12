package com.guo.controller;

import com.guo.domain.BookCategory;
import com.guo.domain.BookInfo;
import com.guo.domain.Vo.BookInfoVo;
import com.guo.service.IBookCategoryService;
import com.guo.service.IBookService;
import com.guo.utils.page.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

/**
 * 图书信息控制器
 * 负责处理图书的查询、展示以及管理员对图书和分类的管理操作。
 */
@Controller
@RequestMapping("/books") // 为所有图书相关请求添加"/books"前缀
public class BookController {


     @Resource
     private IBookService bookService;
     @Resource
     private IBookCategoryService bookCategoryService;


    /**
     * （用户功能）处理高级搜索请求并展示结果页面。
     * @param field    搜索字段 (从前端的<select name="field">获取)，默认为'title'
     * @param keyword  搜索关键词 (从前端的<input name="keyword">获取)
     * @param pageNum  当前页码 (从分页链接或表单获取)
     * @param source  请求来源，用户和管理员返回不同界面
     * @param model    用于向视图传递数据的Model对象
     * @return 返回到图书搜索结果页面的视图名
     */
    @GetMapping("/search")
    public String searchBooks(
            @RequestParam(value = "field", defaultValue = "title") String field,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "source", required = false) String source,
            Model model) {

        // 1. 调用Service层，执行真正的搜索和分页逻辑
        Page<BookInfoVo> bookPage = bookService.searchAndPaginate(field, keyword, pageNum);

        // 2. 将Service返回的结果和用户之前的搜索条件都放入Model中
        //    这样，前端Thymeleaf模板就可以通过 ${page}, ${searchField}, ${keyword} 来访问这些数据
        model.addAttribute("page", bookPage);             // 包含图书列表和分页信息的Page对象
        model.addAttribute("searchField", field);       // 用于回显用户选择的搜索字段
        model.addAttribute("keyword", keyword);         // 用于回显用户输入的关键词
        model.addAttribute("source", source);

        // 3. 返回逻辑视图名，Spring MVC会找到并渲染这个HTML文件
        if ("admin".equals(source)) {
            return "admin/showBooks";
        } else {
            return "user/findBook";
        }
    }


    // --- 以下为管理员专属功能 ---

    /**
     * (管理员功能) 处理添加新书的请求
     * @param bookInfo 包含新书信息的对象
     * @param redirectAttributes 用于重定向后显示提示
     * @return 重定向到图书管理页面
     */
    @PostMapping("/admin/add")
    public String addBook(BookInfoVo bookInfo, RedirectAttributes redirectAttributes) {

         boolean success = bookService.addNewBook(bookInfo,bookInfo.getTotalCopies());
         if (success) {
             redirectAttributes.addFlashAttribute("message", "图书《" + bookInfo.getTitle() + "》添加成功！");
         } else {
             redirectAttributes.addFlashAttribute("error", "添加失败，ISBN可能已存在。");
         }
        return "redirect:/admin/books"; // 操作结束后重定向到管理员的图书列表页
    }

    /**
     * (管理员功能) 处理删除图书的请求
     * @param bookId 要删除的图书ID
     * @return 操作结果
     */
    @PostMapping("/admin/delete")
    @ResponseBody
    public String deleteBook(@RequestParam("bookId") int bookId) {

        // 注意：删除前需要检查该书是否还有未归还的借阅记录
         boolean success = bookService.deleteBookById(bookId);
         return success ? "true" : "false";

    }


    // --- 图书分类管理 API ---

    /**
     * (API) 获取所有图书分类
     * @return 图书分类列表 (JSON格式)
     */
    @GetMapping("/categories/all")
    @ResponseBody
    public List<BookCategory> getAllBookCategories() {

         return bookCategoryService.findAll();
        // 暂时返回null
    }

    /**
     * (管理员功能) 添加新的图书分类
     * @param bookCategory 包含新分类信息的对象
     * @return 操作结果
     */
    @PostMapping("/categories/admin/add")
    @ResponseBody
    public String addBookCategory(BookCategory bookCategory) {

         boolean success = bookCategoryService.addCategory(bookCategory);
         return success ? "true" : "false";

    }

    /**
     * (管理员功能) 删除一个图书分类
     * @param categoryId 要删除的分类ID
     * @return 操作结果
     */
    @PostMapping("/categories/admin/delete")
    @ResponseBody
    public String deleteBookCategory(@RequestParam("categoryId") int categoryId) {

        // 注意：删除前需要检查该分类下是否还有图书
         boolean success = bookCategoryService.deleteCategoryById(categoryId);
         return success ? "true" : "false";

    }
}