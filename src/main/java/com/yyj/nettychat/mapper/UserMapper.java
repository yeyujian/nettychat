package com.yyj.nettychat.mapper;

import com.yyj.nettychat.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    int insert(User user);

    int updateParameter(User user);

    int delete(String id);

    User selectById(String id);

    User selectByName(User user); //根据用户名或者邮箱查找用户

    User selectWithPass(User user); //根据用户名和密码查找用户

    List<User> selectLinkKey(String value);

}
