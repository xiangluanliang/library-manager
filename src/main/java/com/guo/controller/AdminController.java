package com.guo.controller;

import com.guo.domain.User;
// 预留未来需要用到的新实体类和Service
// import com.guo.domain.BookInfo;
// import com.guo.domain.BorrowRecord;
// import com.guo.service.IBookService;
// import com.guo.service.IRecordService;
import com.guo.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 管理员功能控制器
 * 负责处理所有管理员专属的页面导航和操作请求。
 */
@Controller
@RequestMapping("/admin") // 为所有方法添加"/admin"前缀
public class AdminController {

    @Resource
    private IUserService userService;

    // TODO: 后续需要注入其他重构后的Service
    // @Resource
    // private IBookService bookService;
    // @Resource
    // private IRecordService recordService;


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
        // TODO: 在IUserService或新的IAdminService中实现分页查询所有用户的逻辑
        // Page<User> userPage = userService.findAllUsersByPage(pageNum);
        // model.addAttribute("page", userPage);
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
        // TODO: 在IUserService或新的IAdminService中实现新增用户的逻辑，注意密码要加密
        // boolean success = userService.addNewUser(newUser);
        // if (success) {
        //     redirectAttributes.addFlashAttribute("message", "用户添加成功！");
        // } else {
        //     redirectAttributes.addFlashAttribute("error", "添加失败，用户名可能已存在。");
        // }
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
        // TODO: 在IUserService或新的IAdminService中实现删除用户的逻辑
        // boolean success = userService.deleteUserById(userId);
        // return success ? "true" : "false";
        return "true"; // 暂时返回true
    }


    // --- 图书管理 ---

    /**
     * 显示图书管理页面
     * @param pageNum 当前页码
     * @param model Model对象
     * @return 显示图书列表的视图
     */
    @GetMapping("/books")
    public String showBooksPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model) {
        // TODO: 在IBookService中实现分页查询所有图书的逻辑
        // Page<BookInfo> bookPage = bookService.findAllBooksByPage(pageNum);
        // model.addAttribute("page", bookPage);
        return "admin/showBooks";
    }

    /**
     * 显示新增图书页面
     * @return 新增图书视图
     */
    @GetMapping("/books/add")
    public String showAddBookPage() {
        // TODO: 查询所有图书分类并添加到Model中，供下拉框选择
        // List<BookCategory> categories = bookCategoryService.findAll();
        // model.addAttribute("categories", categories);
        return "admin/addBook";
    }


    // --- 分类管理 ---

    /**
     * 显示分类管理页面
     * @return 分类管理视图
     */
    @GetMapping("/categories")
    public String showCategoryPage(Model model) {
        // TODO: 在IBookCategoryService中实现查询所有分类的逻辑
        // List<BookCategory> categoryTree = bookCategoryService.findCategoryTree();
        // model.addAttribute("categoryTree", categoryTree);
        return "admin/addCategory";
    }

    // --- 记录管理 ---

    /**
     * 显示所有借阅记录
     * @param pageNum 当前页码
     * @param model Model对象
     * @return 显示所有借阅记录的视图
     */
    @GetMapping("/records/borrowing")
    public String showAllBorrowingRecords(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, Model model) {
        // TODO: 在新的IRecordService中实现分页查询所有借阅记录的逻辑
        // Page<BorrowRecordVo> recordPage = recordService.findAllBorrowRecordsByPage(pageNum);
        // model.addAttribute("page", recordPage);
        return "admin/allBorrowingBooksRecord";
    }


    // --- 管理员个人信息 ---

    /**
     * 显示当前管理员的个人信息页面
     * @return 个人信息视图
     */
    @GetMapping("/profile")
    public String showAdminProfilePage() {
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

        // TODO: 调用IUserService中的更新方法
        // boolean success = userService.updateUserProfile(adminToUpdate);
        // if(success) {
        //    session.setAttribute("user", userService.findUserById(currentAdmin.getUserId()));
        //    return "true";
        // }
        // return "false";
        return "true"; // 暂时返回true
    }
}