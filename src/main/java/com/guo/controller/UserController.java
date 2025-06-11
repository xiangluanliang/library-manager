package com.guo.controller;

import com.guo.domain.User;
import com.guo.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 统一用户认证与普通用户功能控制器
 */
@Controller
public class UserController {

    @Resource
    private IUserService userService;

    // 预留借阅记录服务的注入点
    // @Resource
    // private IBorrowRecordService borrowRecordService;

    /**
     * 显示根路径或登录页面
     * @return 登录页面视图名
     */
    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    /**
     * 处理统一登录请求
     * @param username 用户名
     * @param password 原始密码
     * @param session HttpSession对象，用于存储用户会话
     * @param redirectAttributes 用于在重定向时传递闪存属性（如错误信息）
     * @return 登录成功后的主页视图名（重定向），或失败后返回登录页
     */
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        // 调用Service层进行登录验证
        User user = userService.login(username, password);

        if (user != null) {
            // 登录成功，将完整的用户信息存入session
            session.setAttribute("user", user);

            // 根据角色判断重定向到哪个主页
            if ("admin".equals(user.getRole())) {
                return "redirect:/admin/index";
            } else {
                return "redirect:/user/index";
            }
        } else {
            // 登录失败，通过RedirectAttributes添加错误信息，然后重定向回登录页
            redirectAttributes.addFlashAttribute("loginError", "用户名或密码错误！");
            return "redirect:/";
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
     * 查看当前用户的借阅记录
     * @param model Model对象，用于向视图传递数据
     * @param session HttpSession对象，用于获取当前登录用户
     * @return 用户借阅记录页面视图名
     */
    @GetMapping("/user/records")
    public String showUserBorrowingRecords(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        // TODO: 调用 borrowRecordService.findByUserId(currentUser.getUserId()) 获取借阅记录列表
        // List<BorrowRecord> records = borrowRecordService.findByUserId(currentUser.getUserId());
        // model.addAttribute("borrowingRecords", records);

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

    /**
     * 显示可借阅的图书列表及借书页面
     * @return 借书页面视图名
     */
    @GetMapping("/user/borrow")
    public String showBorrowPage() {
        // 这个页面通常会显示所有可借阅的图书列表
        // TODO: 调用 bookService获取图书列表并添加到Model中
        return "user/borrowingBooks";
    }

    /**

     * 显示还书页面
     * @return 还书页面视图名
     */
    @GetMapping("/user/return")
    public String showReturnPage() {
        // 这个页面通常会显示当前用户已借阅且未归还的图书列表
        // TODO: 调用borrowRecordService获取记录并添加到Model中
        return "user/returnBooks";
    }
}