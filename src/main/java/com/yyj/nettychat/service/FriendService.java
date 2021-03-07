package com.yyj.nettychat.service;

import com.yyj.nettychat.model.Friendship;
import com.yyj.nettychat.model.User;

import java.util.List;

public interface FriendService {

    /**
     * 添加好友
     *
     * @param friendship
     * @return
     */
    boolean addFriend(Friendship friendship);

    /**
     * 查找好友
     *
     * @param userid
     * @return
     */
    List<User> findFriendsByUserid(String userid);

    /**
     * 查找所有好友申请请求
     *
     * @param userid
     * @return
     */
    List<User> findFriendshipRequireByUserid(String userid);

    /**
     * 同意好友申请
     *
     * @param friendship
     * @return
     */
    boolean acceptFriendship(Friendship friendship);

    /**
     * 拒绝好友申请
     *
     * @param friendship
     * @return
     */
    boolean ignoreFriendship(Friendship friendship);

    /**
     *  判断好友关系
     * @param userid
     * @param friendid
     * @return
     */
    boolean isFriend(String userid, String friendid);
}
