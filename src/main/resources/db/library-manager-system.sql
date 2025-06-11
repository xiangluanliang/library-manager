-- =================================================================
-- Library Management System Initialization Script
-- Author: Yhr
-- Version: 2.0
-- Description: Creates a modern library database schema and populates it with sample data.
-- =================================================================

-- 禁用外键检查，以便我们可以按任意顺序删除和创建表
SET FOREIGN_KEY_CHECKS = 0;

-- 删除所有旧表（如果存在），确保一个干净的开始
DROP TABLE IF EXISTS `operation_log`,
    `reservation`,
    `renew_record`,
    `overdue_record`,
    `return_record`,
    `borrow_record`,
    `book_status`,
    `book_inventory`,
    `book_info`,
    `book_category`,
    `user`;

-- 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 1. 用户表 (user)
-- 存储系统用户，包括普通用户和管理员
-- ----------------------------
CREATE TABLE `user` (
                        `user_id` int(11) NOT NULL AUTO_INCREMENT,
                        `user_name` varchar(20) NOT NULL,
                        `user_pwd` varchar(64) NOT NULL COMMENT '建议存储加密后的密码，例如使用BCrypt',
                        `user_email` varchar(50) NOT NULL,
                        `max_borrow` int(11) NOT NULL DEFAULT 5 COMMENT '最大可借阅数量',
                        `role` ENUM('user', 'admin') NOT NULL DEFAULT 'user' COMMENT '角色：普通用户或管理员',
                        `creation_time` datetime DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`user_id`),
                        UNIQUE KEY `idx_user_name` (`user_name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 插入示例用户数据
INSERT INTO
    `user` (
    `user_id`,
    `user_name`,
    `user_pwd`,
    `user_email`,
    `max_borrow`,
    `role`
)
VALUES (
           1,
           'admin',
           'admin',
           'admin@example.com',
           99,
           'admin'
       ),
       (
           2,
           'zhangsan',
           '123456',
           'zhangsan@example.com',
           5,
           'user'
       ),
       (
           3,
           'lisi',
           '123456',
           'lisi@example.com',
           5,
           'user'
       ),
       (
           4,
           'wangwu',
           '123456',
           'wangwu@example.com',
           3,
           'user'
       );

-- ----------------------------
-- 2. 图书分类表 (book_category)
-- 支持多级分类
-- ----------------------------
CREATE TABLE `book_category` (
                                 `category_id` int(11) NOT NULL AUTO_INCREMENT,
                                 `category_name` varchar(20) NOT NULL,
                                 `parent_id` int(11) DEFAULT NULL COMMENT '父分类ID，用于实现多级分类',
                                 PRIMARY KEY (`category_id`),
                                 CONSTRAINT `fk_category_parent` FOREIGN KEY (`parent_id`) REFERENCES `book_category` (`category_id`) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 插入示例分类数据
INSERT INTO
    `book_category` (
    `category_id`,
    `category_name`,
    `parent_id`
)
VALUES (1, '文学', NULL),
       (2, '计算机科学', NULL),
       (3, '历史', NULL),
       (4, '中国文学', 1),
       (5, '外国文学', 1),
       (6, '编程语言', 2),
       (7, '数据库', 2);

-- ----------------------------
-- 3. 图书基础信息表 (book_info)
-- 存储图书的静态信息，如书名、作者等
-- ----------------------------
CREATE TABLE `book_info` (
                             `book_id` int(11) NOT NULL AUTO_INCREMENT,
                             `isbn` varchar(20) NOT NULL,
                             `title` varchar(100) NOT NULL,
                             `author` varchar(50) NOT NULL,
                             `publisher` varchar(50) NOT NULL,
                             `publish_date` date DEFAULT NULL,
                             `description` text,
                             `cover_url` varchar(255) DEFAULT NULL COMMENT '图书封面图片URL',
                             `category_id` int(11) NOT NULL,
                             `price` decimal(10, 2) NOT NULL,
                             PRIMARY KEY (`book_id`),
                             UNIQUE KEY `idx_isbn` (`isbn`),
                             CONSTRAINT `fk_book_category` FOREIGN KEY (`category_id`) REFERENCES `book_category` (`category_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 插入示例图书信息
INSERT INTO
    `book_info` (
    `book_id`,
    `isbn`,
    `title`,
    `author`,
    `publisher`,
    `publish_date`,
    `description`,
    `category_id`,
    `price`
)
VALUES (
           101,
           '9787532722699',
           '三体',
           '刘慈欣',
           '重庆出版社',
           '2008-01-01',
           '一部史诗级的科幻小说，讲述了地球文明在宇宙中的兴衰历程。',
           4,
           23.00
       ),
       (
           102,
           '9787111558422',
           '深入理解Java虚拟机',
           '周志明',
           '机械工业出版社',
           '2019-12-01',
           '全面深入地讲解Java虚拟机，是Java开发者的必读之作。',
           6,
           129.00
       ),
       (
           103,
           '9787115440237',
           'MySQL必知必会',
           'Ben Forta',
           '人民邮电出版社',
           '2017-01-01',
           '一本非常适合初学者的MySQL入门书籍。',
           7,
           49.00
       ),
       (
           104,
           '9787020024759',
           '平凡的世界',
           '路遥',
           '人民文学出版社',
           '1990-03-01',
           '全景式地表现中国当代城乡社会生活的长篇小说。',
           4,
           68.00
       ),
       (
           105,
           '9787544270878',
           '百年孤独',
           '加西亚·马尔克斯',
           '南海出版公司',
           '2011-06-01',
           '魔幻现实主义文学的代表作，描写了布恩迪亚家族七代人的传奇故事。',
           5,
           39.50
       );

-- ----------------------------
-- 4. 图书库存表 (book_inventory)
-- 管理每本书的数量和位置
-- ----------------------------
CREATE TABLE `book_inventory` (
                                  `inventory_id` int(11) NOT NULL AUTO_INCREMENT,
                                  `book_id` int(11) NOT NULL,
                                  `total_copies` int(11) NOT NULL,
                                  `available_copies` int(11) NOT NULL,
                                  `location` varchar(50) DEFAULT NULL COMMENT '存放位置，如A区-03架-02层',
                                  PRIMARY KEY (`inventory_id`),
                                  UNIQUE KEY `idx_book_id` (`book_id`),
                                  CONSTRAINT `fk_inventory_book` FOREIGN KEY (`book_id`) REFERENCES `book_info` (`book_id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 插入示例库存数据
INSERT INTO
    `book_inventory` (
    `book_id`,
    `total_copies`,
    `available_copies`,
    `location`
)
VALUES (101, 5, 4, 'A-01-01'), -- 《三体》有5本，4本可借
       (102, 3, 2, 'B-02-03'), -- 《深入理解Java虚拟机》有3本，2本可借
       (103, 10, 10, 'B-02-05'), -- 《MySQL必知必会》有10本，全部可借
       (104, 8, 8, 'A-01-02'), -- 《平凡的世界》有8本，全部可借
       (105, 4, 4, 'A-03-01');
-- 《百年孤独》有4本，全部可借

-- ----------------------------
-- 5. 借阅记录表 (borrow_record)
-- 记录每一次有效的借书行为
-- ----------------------------
CREATE TABLE `borrow_record` (
                                 `borrow_id` int(11) NOT NULL AUTO_INCREMENT,
                                 `user_id` int(11) NOT NULL,
                                 `book_id` int(11) NOT NULL,
                                 `borrow_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `due_time` datetime NOT NULL COMMENT '应还日期',
                                 `status` ENUM(
                                     'borrowed',
                                     'returned',
                                     'overdue'
                                     ) NOT NULL DEFAULT 'borrowed' COMMENT '当前借阅状态',
                                 PRIMARY KEY (`borrow_id`),
                                 CONSTRAINT `fk_borrow_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
                                 CONSTRAINT `fk_borrow_book` FOREIGN KEY (`book_id`) REFERENCES `book_info` (`book_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 插入示例借阅数据
-- 假设当前时间是 2025-06-11
-- 张三借了一本《三体》，尚未归还
INSERT INTO
    `borrow_record` (
    `borrow_id`,
    `user_id`,
    `book_id`,
    `borrow_time`,
    `due_time`,
    `status`
)
VALUES (
           501,
           2,
           101,
           '2025-06-01 10:00:00',
           '2025-07-01 10:00:00',
           'borrowed'
       );

-- 李四借了一本《深入理解Java虚拟机》，尚未归还
INSERT INTO
    `borrow_record` (
    `borrow_id`,
    `user_id`,
    `book_id`,
    `borrow_time`,
    `due_time`,
    `status`
)
VALUES (
           502,
           3,
           102,
           '2025-06-05 14:30:00',
           '2025-07-05 14:30:00',
           'borrowed'
       );

-- 张三之前借过一本《MySQL必知必会》，并且已经归还
INSERT INTO
    `borrow_record` (
    `borrow_id`,
    `user_id`,
    `book_id`,
    `borrow_time`,
    `due_time`,
    `status`
)
VALUES (
           503,
           2,
           103,
           '2025-05-10 09:00:00',
           '2025-06-10 09:00:00',
           'returned'
       );

-- 王五借了一本书，并且已经逾期
INSERT INTO
    `borrow_record` (
    `borrow_id`,
    `user_id`,
    `book_id`,
    `borrow_time`,
    `due_time`,
    `status`
)
VALUES (
           504,
           4,
           102,
           '2025-04-20 11:00:00',
           '2025-05-20 11:00:00',
           'overdue'
       );

-- ----------------------------
-- 6. 归还记录表 (return_record)
-- 记录每一次还书行为
-- ----------------------------
CREATE TABLE `return_record` (
                                 `return_id` int(11) NOT NULL AUTO_INCREMENT,
                                 `borrow_id` int(11) NOT NULL,
                                 `return_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `operator_id` int(11) DEFAULT NULL COMMENT '操作员ID，可以是管理员',
                                 PRIMARY KEY (`return_id`),
                                 UNIQUE KEY `idx_borrow_id` (`borrow_id`),
                                 CONSTRAINT `fk_return_borrow` FOREIGN KEY (`borrow_id`) REFERENCES `borrow_record` (`borrow_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 插入示例归还数据
-- 对应 borrow_id 为 503 的归还记录
INSERT INTO
    `return_record` (
    `return_id`,
    `borrow_id`,
    `return_time`,
    `operator_id`
)
VALUES (
           601,
           503,
           '2025-06-08 17:00:00',
           1
       );

-- ----------------------------
-- 7. 预约记录表 (reservation)
-- 当图书不可借时，用户可以进行预约
-- ----------------------------
CREATE TABLE `reservation` (
                               `reserve_id` int(11) NOT NULL AUTO_INCREMENT,
                               `user_id` int(11) NOT NULL,
                               `book_id` int(11) NOT NULL,
                               `reserve_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               `expire_time` datetime NOT NULL COMMENT '预约到期时间',
                               `status` enum(
                                   'pending',
                                   'fulfilled',
                                   'canceled',
                                   'expired'
                                   ) NOT NULL DEFAULT 'pending',
                               PRIMARY KEY (`reserve_id`),
                               CONSTRAINT `fk_reserve_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
                               CONSTRAINT `fk_reserve_book` FOREIGN KEY (`book_id`) REFERENCES `book_info` (`book_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 插入示例预约数据
-- 王五预约了《三体》，因为张三还没还
INSERT INTO
    `reservation` (
    `reserve_id`,
    `user_id`,
    `book_id`,
    `reserve_time`,
    `expire_time`,
    `status`
)
VALUES (
           801,
           4,
           101,
           '2025-06-10 11:00:00',
           '2025-06-13 11:00:00',
           'pending'
       );

-- ----------------------------
-- 8. 系统操作日志表 (operation_log)
-- 记录关键操作，用于审计和追踪
-- ----------------------------
CREATE TABLE `operation_log` (
                                 `log_id` int(11) NOT NULL AUTO_INCREMENT,
                                 `user_id` int(11) DEFAULT NULL COMMENT '操作发起人ID',
                                 `operation_type` varchar(20) NOT NULL,
                                 `operation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `target_id` int(11) DEFAULT NULL COMMENT '被操作对象ID',
                                 `target_type` varchar(20) DEFAULT NULL COMMENT '被操作对象类型，如 book, user',
                                 `details` text COMMENT '操作详情',
                                 PRIMARY KEY (`log_id`),
                                 CONSTRAINT `fk_log_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- 插入示例日志数据
INSERT INTO
    `operation_log` (
    `user_id`,
    `operation_type`,
    `target_id`,
    `target_type`,
    `details`
)
VALUES (
           1,
           'ADD_BOOK',
           105,
           'book_info',
           '管理员添加了新书《百年孤独》。'
       ),
       (
           2,
           'BORROW_BOOK',
           101,
           'book_info',
           '用户张三借阅了《三体》。'
       ),
       (
           3,
           'BORROW_BOOK',
           102,
           'book_info',
           '用户李四借阅了《深入理解Java虚拟机》。'
       ),
       (
           1,
           'RETURN_BOOK',
           103,
           'book_info',
           '管理员为用户张三办理了《MySQL必知必会》的归还手续。'
       );