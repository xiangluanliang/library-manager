package com.guo.controller;

import com.guo.domain.BookCategory;
import com.guo.domain.User;
import com.guo.domain.Vo.BookInfoVo;
import com.guo.domain.Vo.BorrowRecordVo;
import com.guo.domain.Vo.ReservationVo;
import com.guo.service.*;
import com.guo.utils.page.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 管理员功能控制器
 * 负责处理所有管理员专属的页面导航和操作请求。
 */
@Controller
@RequestMapping("/admin") // 为所有方法添加"/admin"前缀
public class AdminController {

    @Resource
    private IUserService userService;
    @Resource
    private IAdminService adminService;

    @Resource
    private IBookService bookService;
    @Resource
    private IRecordService recordService;
    @Resource
    private IBookCategoryService bookCategoryService;


    /**
     * 显示管理员主页
     * @return 管理员主页视图
     */
    @GetMapping("/index")
    public String showAdminIndex() {
        return "admin/index";
    }

    // --- 用户管理 ---

    /**
     * 分页显示所有用户列表
     * @param pageNum 当前页码，默认为第一页
     * @param model Model对象
     * @return 显示用户列表的视图
     */
    @GetMapping("/users")
    public String showUsersPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model) {
         Page<User> userPage = adminService.findAllUsersByPage(pageNum);
         model.addAttribute("page", userPage);
        return "admin/showUsers";
    }

    /**
     * 显示新增用户页面
     * @return 新增用户视图
     */
    @GetMapping("/users/add")
    public String showAddUserPage() {
        return "admin/addUser";
    }

    /**
     * 处理新增用户请求
     * @param newUser 包含新用户信息的对象
     * @param redirectAttributes 用于重定向后显示提示信息
     * @return 重定向到用户列表页面
     */
    @PostMapping("/users/add")
    public String addUser(User newUser, RedirectAttributes redirectAttributes) {
         boolean success = adminService.addNewUser(newUser);
         if (success) {
             redirectAttributes.addFlashAttribute("message", "用户添加成功！");
         } else {
             redirectAttributes.addFlashAttribute("error", "添加失败，用户名可能已存在。");
         }
        return "redirect:/admin/users";
    }

    /**
     * 处理删除用户请求
     * @param userId 要删除的用户ID
     * @return 操作结果（JSON格式）
     */
    @PostMapping("/users/delete")
    @ResponseBody
    public String deleteUser(@RequestParam("userId") int userId) {
        boolean success = adminService.deleteUserById(userId);
        return success ? "true" : "false";
    }


    // --- 图书管理 ---

    /**
     * 显示新增图书页面
     * @return 新增图书视图
     */
    @GetMapping("/books/add")
    public String showAddBookPage() {
        return "admin/addBook";
    }

    /**
     * 显示编辑书籍页面
     * @param id 从URL路径中获取的书籍ID
     * @param model Model对象，用于向视图传递数据
     * @return 编辑书籍页面的视图名
     */
    @GetMapping("/books/edit/{id}")
    public String showEditBookPage(@PathVariable("id") int id, Model model) {
        // 1. 根据ID调用Service查找书籍的完整信息（包含库存）
        BookInfoVo bookVo = bookService.findBookById(id);

        // 2. 将查找到的书籍对象放入Model中
        model.addAttribute("book", bookVo);

        // 3. 同时获取所有分类，用于填充下拉框
         List<BookCategory> categories = bookCategoryService.findAll();
         model.addAttribute("categories", categories);

        return "admin/editBook"; // 返回一个新的编辑页面
    }

    /**
     * 处理编辑书籍的表单提交
     * @param bookInfo 包含更新后书籍信息的对象
     * @param redirectAttributes 用于重定向后显示提示信息
     * @return 重定向到图书管理页面
     */
    @PostMapping("/books/edit")
    public String updateBook(BookInfoVo bookInfo, RedirectAttributes redirectAttributes) {
        // 调用Service层执行更新操作
        boolean success = bookService.updateBook(bookInfo);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "图书《" + bookInfo.getTitle() + "》信息更新成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "更新失败！");
        }

        // 操作结束后，重定向回图书列表页面
        return "redirect:/books/search?source=admin";
    }

    // --- 分类管理 ---

    /**
     * 显示分类管理页面
     * @return 分类管理视图
     */
    @GetMapping("/categories")
    public String showCategoryPage(Model model) {
         List<BookCategory> categoryTree = bookCategoryService.findAll();
         model.addAttribute("categoryTree", categoryTree);
        return "admin/addCategory";
    }

    // --- 记录管理 ---


    /**
     * 显示所有借阅记录
     * @param borrowPageNum 当前页码
     * @param reservePageNum 当前页码
     * @param model Model对象
     * @return 显示所有借阅记录的视图
     */
    @GetMapping("/records/borrowing")
    public String showAllBorrowingRecords(@RequestParam(value = "borrowPageNum", defaultValue = "1") int borrowPageNum,
                                          @RequestParam(value = "reservePageNum", defaultValue = "1") int reservePageNum,
                                          Model model) {

        // 1. 查询借阅记录的分页数据
        Page<BorrowRecordVo> borrowPage = recordService.findAllRecordsByPage(borrowPageNum);
        model.addAttribute("borrowPage", borrowPage);

        // 2. 查询预约记录的分页数据
        //    (假设IReservationService中也有一个分页查询所有预约的方法)
        Page<ReservationVo> reservePage = recordService.findAllReservationsByPage(reservePageNum);
        model.addAttribute("reservePage", reservePage);

        return "admin/allBorrowingBooksRecord";
    }

    // 为“待还处理”页面提供数据
    @GetMapping("/records/return")
    public String showReturnProcessingPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model) {
        Page<BorrowRecordVo> recordPage = recordService.findUnreturnedRecordsByPage(pageNum);
        model.addAttribute("page", recordPage);
        return "admin/returnProcessing"; // 返回新页面
    }

    // 处理“回收”按钮的AJAX请求
    @PostMapping("/records/return/execute")
    @ResponseBody
    public String executeReturnBook(@RequestParam("borrowId") int borrowId) {
        boolean success = recordService.executeReturn(borrowId);
        return success ? "true" : "false";
    }


    // --- 管理员个人信息 ---

    /**
     * 显示当前管理员的个人信息页面
     * @return 个人信息视图
     */
    @GetMapping("/profile")
    public String showAdminProfilePage(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        System.out.println(currentUser.getUserName());
        // 从数据库重新获取最新的用户信息，防止session中数据过时
        User freshUser = userService.findUserById(currentUser.getUserId());
        model.addAttribute("profileUser", freshUser);
        return "admin/adminInfo";
    }

    /**
     * 处理更新管理员个人信息的请求
     * @param adminToUpdate 包含更新信息的对象
     * @param session HttpSession对象，用于更新会话中的信息
     * @param redirectAttributes 用于重定向后显示提示信息
     * @return 操作结果
     */
    @PostMapping("/profile/update")
    @ResponseBody
    public String updateAdminProfile(User adminToUpdate, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentAdmin = (User) session.getAttribute("user");
        adminToUpdate.setUserId(currentAdmin.getUserId());

         boolean success = userService.updateUserProfile(adminToUpdate);
         if(success) {
            session.setAttribute("user", userService.findUserById(currentAdmin.getUserId()));
            return "true";
         }
         return "false";
    }
}