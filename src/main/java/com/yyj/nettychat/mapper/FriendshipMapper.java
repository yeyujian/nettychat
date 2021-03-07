package com.yyj.nettychat.mapper;

import com.yyj.nettychat.model.Friendship;
import com.yyj.nettychat.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FriendshipMapper {

    int updateFriendshipState(Friendship friendship);

    int delete(Friendship friendship);

    int insert(Friendship friendship);

    List<User> selectFriendsByPrimaryKey(Friendship friendship);

}
