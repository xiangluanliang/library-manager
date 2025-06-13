package com.guo.controller;

import com.guo.domain.User;
import com.guo.domain.Vo.BookInfoVo;
import com.guo.domain.Vo.BorrowRecordVo;
import com.guo.domain.Vo.ReservationVo;
import com.guo.service.IBookService;
import com.guo.service.IRecordService;
import com.guo.service.IUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 统一用户认证与普通用户功能控制器
 */
@Controller
public class UserController {

    @Resource
    private IUserService userService;

     @Resource
     private IBookService bookService;

    @Resource
    private IRecordService recordService;


    /**
     * 当用户访问根路径时，显示登录页面。
     * @return 返回登录页的视图名 "index"
     */
    @GetMapping("/")
    public String showLoginPage() {
        return "index";
    }

    /**
     * 处理统一登录请求
     * @param username 从表单接收的用户名
     * @param password 从表单接收的密码
     * @param session  用于在登录成功后存储用户信息
     * @param model    用于在登录失败时向页面传递错误信息
     * @return 字符串，根据结果决定是重定向还是返回登录页
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {

        // --- 这里是我们熟悉的日志，现在它应该会被打印出来了 ---
        System.out.println("=============================================");
        System.out.println("【手动登录】尝试登录，用户名: " + username);

        // 调用我们早已写好的Service层login方法
        User user = userService.login(username, password);

        // 判断Service层返回的结果
        if (user != null) {
            // --- 登录成功 ---
            System.out.println("【手动登录】登录成功！用户角色: " + user.getRole());

            // 1. 将完整的用户信息存入Session，以便后续页面使用
            session.setAttribute("user", user);

            // 2. 根据角色判断应该跳转到哪个页面
            if ("admin".equals(user.getRole())) {
                // 如果是管理员，重定向到管理员主页
                return "redirect:/admin/index";
            } else {
                // 如果是普通用户，重定向到用户主页
                return "redirect:/user/index";
            }
        } else {
            // --- 登录失败 ---
            System.out.println("【手动登录】登录失败：用户名或密码错误。");

            // 1. 通过Model向前端页面传递一个错误信息
            model.addAttribute("loginError", "用户名或密码错误！");

            // 2. 返回到登录页面，让用户重新登录
            return "index";
        }
    }

    /**
     * 处理统一登出请求
     * @param session HttpSession对象
     * @return 重定向到登录页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 销毁session，清除所有会话属性
        session.invalidate();
        return "redirect:/";
    }

    // ==========================================================
    // ==                  普通用户专属功能区域                  ==
    // ==========================================================

    /**
     * 返回普通用户的主页
     * @return 用户主页视图名
     */
    @GetMapping("/user/index")
    public String userIndex() {
        return "user/index";
    }

    /**
     * 显示特定图书的借阅/预约确认页面。
     * @param bookId 从请求URL中获取的书籍ID
     * @param model Model对象，用于向视图传递数据
     * @return 借阅/预约页面的视图名
     */
    @GetMapping("/user/borrow")
    public String showBorrowPage(@RequestParam("bookId") int bookId, Model model) {

        // 1. 根据 bookId 调用 BookService 查找完整的图书信息（包括库存）
        //    这里我们假设 IBookService 中有一个 findBookWithInventoryById 方法
        BookInfoVo bookVo = bookService.findBookById(bookId); // 假设findBookById返回的是带库存的VO

        // 2. 将查找到的图书对象放入Model中，以便前端页面可以访问
        //    在HTML中，你就可以通过 ${book.title}, ${book.availableCopies} 来使用它
        model.addAttribute("book", bookVo);

        // 3. 返回对应的视图名
        return "user/borrowingBooks";
    }

    /**
     * 查看当前用户的借阅记录。
     * @param model Model对象，用于向视图传递数据
     * @param session HttpSession对象，用于获取当前登录用户
     * @return 用户借阅记录页面视图名
     */
    @GetMapping("/user/records")
    public String showUserBorrowingRecords(Model model, HttpSession session) {
        // 1. 从session中获取当前登录的用户对象
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            // 如果用户未登录，重定向到登录页面
            return "redirect:/";
        }

        // 安全检查，如果用户未登录则直接跳转回登录页
        if (currentUser == null) {
            return "redirect:/";
        }

        // 2. 调用 RecordService，根据用户ID查询其所有的借阅记录
        //    这个方法需要你在IRecordService和其实现类中创建
        List<BorrowRecordVo> recordList = recordService.findRecordsByUserId(currentUser.getUserId());

        System.out.println("=============================================");
        System.out.println("【日志】尝试登录，用户名: " + currentUser.getUserName());

        // 3. 将查询到的记录列表放入Model中
        //    在HTML中，你就可以通过 th:each="record : ${recordList}" 来遍历
        model.addAttribute("recordList", recordList);

        // 4. 返回对应的视图名
        return "user/borrowingBooksRecord";
    }

    /**
     * 显示用户的个人信息页面
     * @param model Model对象
     * @param session HttpSession对象
     * @return 个人信息页面视图名
     */
    @GetMapping("/user/profile")
    public String showUserProfilePage(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        // 从数据库重新获取最新的用户信息，防止session中数据过时
        User freshUser = userService.findUserById(currentUser.getUserId());
        model.addAttribute("profileUser", freshUser);
        return "user/userMessage";
    }

    /**
     * 处理用户更新个人信息的请求
     * @param userToUpdate 包含更新信息的用户对象，由Spring自动封装
     * @param session HttpSession对象
     * @param redirectAttributes 用于重定向后显示成功或失败信息
     * @return 重定向到个人信息页面
     */
    @PostMapping("/user/profile/update")
    public String updateUserProfile(User userToUpdate, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        // 安全起见，确保用户只能修改自己的信息
        userToUpdate.setUserId(currentUser.getUserId());

        boolean success = userService.updateUserProfile(userToUpdate); // 你需要在Service层实现这个方法

        if (success) {
            redirectAttributes.addFlashAttribute("message", "信息更新成功！");
            // 更新session中的用户信息
            session.setAttribute("user", userService.findUserById(currentUser.getUserId()));
        } else {
            redirectAttributes.addFlashAttribute("error", "信息更新失败！");
        }

        return "redirect:/user/profile";
    }



    // ==========================================================   
    // ==                  账号注销功能                         ==
    // ==========================================================

    /**
     * 显示账号注销确认页面
     * @return 注销确认页面视图名
     */
    @GetMapping("/user/delete-account")
    public String showDeleteAccountPage() {
        return "user/deleteAccount";
    }

    /**
     * 处理账号注销请求
     * @param password 用户输入的密码用于确认
     * @param session 当前会话
     * @param redirectAttributes 重定向属性，用于传递消息
     * @return 重定向到登录页面
     */
    @PostMapping("/user/delete-account")
    public String deleteUserAccount(@RequestParam("password") String password,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {

        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            // 用户未登录，重定向到登录页面
            return "redirect:/";
        }

        // 验证密码是否正确
        boolean passwordValid = userService.verifyPassword(Long.valueOf(currentUser.getUserId()), password);

        if (passwordValid) {
            // 执行账号注销
            boolean deleted = userService.deleteUserById(Long.valueOf(currentUser.getUserId()));

            if (deleted) {
                // 账号删除成功，使会话失效
                session.invalidate();
                redirectAttributes.addFlashAttribute("message", "您的账号已成功注销");
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("error", "账号注销失败，请稍后再试");
                return "redirect:/user/delete-account";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "密码错误，请重新输入");
            return "redirect:/user/delete-account";
        }
    }

    /**
     * 处理确认借书的表单提交
     * @param bookId 从表单隐藏域获取
     * @param dueDate 从表单日期选择器获取
     * @param session 用于获取当前用户
     * @param redirectAttributes 用于重定向后显示提示信息
     * @return 重定向视图
     */
    @PostMapping("/user/borrow/execute")
    public String executeBorrow(@RequestParam("bookId") int bookId,
                                @RequestParam("dueDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dueDate,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/"; // 用户未登录
        }

        boolean success = recordService.executeBorrow(bookId, currentUser, dueDate);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "借阅成功！请在应还日期前归还。");
            return "redirect:/user/records"; // 借阅成功，跳转到我的借阅记录页面
        } else {
            redirectAttributes.addFlashAttribute("error", "借阅失败！该书可能已被借出或您已达到借阅上限。");
            return "redirect:/books/search"; // 借阅失败，返回图书搜索页
        }
    }

    /**
     * 处理图书预约请求
     * @param bookId 从表单提交的图书ID
     * @param session 用于获取当前登录用户
     * @param redirectAttributes 用于在重定向后显示提示信息
     * @return 重定向到图书搜索页面
     */
    @PostMapping("/user/reserve/execute")
    public String executeReservation(@RequestParam("bookId") int bookId,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/";
        }

        // 调用 recordService 的方法
        boolean success = recordService.createReservation(bookId, currentUser);

        if (success) {
            redirectAttributes.addFlashAttribute("message", "图书预约成功！");
        } else {
            redirectAttributes.addFlashAttribute("error", "预约失败，您可能已预约过此书。");
        }

        return "redirect:/books/search";
    }

    /**
     * 【新增】显示当前用户的预约记录页面
     * @param session 用于获取当前登录用户
     * @param model   用于向视图传递数据
     * @return “我的预约”页面的视图名
     */
    @GetMapping("/user/reservations") // 使用复数形式，更符合RESTful风格
    public String showMyReservations(HttpSession session, Model model) {
        // 1. 从session中获取当前登录的用户对象
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/"; // 用户未登录，重定向到首页
        }

        // 2. 调用ReservationService，根据用户ID查询其所有的预约记录详情
        //    这个方法需要你在IReservationService和其实现类中创建
        List<ReservationVo> reservationList = recordService.findReservationsByUserId(currentUser.getUserId());

        // 3. 将查询到的记录列表放入Model中
        //    在HTML中，你就可以通过 th:each="reservation : ${reservations}" 来遍历
        model.addAttribute("reservations", reservationList);

        // 4. 返回对应的视图名
        return "user/myReservations";
    }

    /**
     * 【新增】处理取消预约的AJAX请求
     * @param reservationId 要取消的预约ID
     * @param session       用于验证操作的合法性
     * @return 操作结果字符串 "true" 或 "false"
     */
    @PostMapping("/user/reservations/cancel")
    @ResponseBody
    public String cancelReservation(@RequestParam("reservationId") int reservationId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "false"; // 未登录，无法操作
        }

        // 调用Service层执行取消操作，Service内部会校验该预约是否属于当前用户
        boolean success = recordService.cancelReservation(reservationId, currentUser.getUserId());
        return success ? "true" : "false";
    }
}
