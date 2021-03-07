package com.yyj.nettychat.service;

import com.yyj.nettychat.model.Role;
import com.yyj.nettychat.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    /**
     * 根据用户名或邮箱查找用户
     *
     * @param name
     * @return
     */
    User getUserByName(String name);


    /**
     * 根据密码查找用户
     * @param user
     * @return
     */
    User getUserWithPass(User user);

    /**
     * 注册新用户
     *
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * 更新用户密码
     *
     * @param user
     * @return
     */
    boolean updatePassword(User user);

    /**
     * 更新昵称
     *
     * @param user
     * @return
     */
    boolean updateNickName(User user);

    /**
     * 更新用户头像
     *
     * @param file
     * @param user
     * @return
     */
    User upload(MultipartFile file, User user);

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    User findById(String id);
}
