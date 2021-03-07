package com.yyj.nettychat.service.impl;

import com.yyj.nettychat.config.push.AsyncCenter;
import com.yyj.nettychat.entity.FriendPushEnum;
import com.yyj.nettychat.mapper.FriendshipMapper;
import com.yyj.nettychat.mapper.UserMapper;
import com.yyj.nettychat.model.Friendship;
import com.yyj.nettychat.model.User;
import com.yyj.nettychat.service.FriendService;
import com.yyj.nettychat.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendshipMapper friendshipMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private AsyncCenter asyncCenter;

    @Override
    public boolean addFriend(Friendship friendship) {
        boolean result = false;
        try {
            if (friendship.getFromid().equals(friendship.getToid())) { //检测是否自己添加自己
                return false;
            }
            if (friendshipMapper.selectFriendsByPrimaryKey(friendship).size() > 0) { //已经发送过添加好友申请或者已经是好友
                return false;
            }
            if (userMapper.selectById(friendship.getToid()) == null) { //检查用户是否存在
                return false;
            }
            friendship.setId(idWorker.nextId());
            result = friendshipMapper.insert(friendship) > 0;
            /**
             * 发送消息队列 待处理
             */
            if (result == true)
                asyncCenter.sendPush(FriendPushEnum.REQUIRE, friendship);
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException("添加好友请求失败");
        }
        return result;
    }

    @Override
    public List<User> findFriendsByUserid(String userid) {
        Friendship friendship = new Friendship();
        friendship.setFromid(userid);
        friendship.setToid(null);
        friendship.setState(1);
        List<User> friends = friendshipMapper.selectFriendsByPrimaryKey(friendship);
        return friends;
    }

    @Override
    public List<User> findFriendshipRequireByUserid(String userid) {
        Friendship friendship = new Friendship();
        friendship.setFromid(null);
        friendship.setToid(userid);
        friendship.setState(0);
        List<User> friends = friendshipMapper.selectFriendsByPrimaryKey(friendship);
        return friends;
    }

    @Override
    public boolean acceptFriendship(Friendship friendship) {
        boolean result = false;
        try {
            if (friendshipMapper.selectFriendsByPrimaryKey(friendship).size() > 0) { //已经是好友
                return false;
            }
            if (friendshipMapper.updateFriendshipState(friendship) <= 0) { //同意好友请求操作失败 不存在记录
                return false;
            }
            Friendship friendship1 = new Friendship();
            friendship1.setToid(friendship.getFromid());
            friendship1.setFromid(friendship.getToid());
            friendship1.setState(1);
            friendship1.setCreatetime(new Date());
            friendship1.setId(idWorker.nextId());
            if (friendshipMapper.insert(friendship1) <= 0) {
                throw new RuntimeException("同意好友请求操作失败");
            }
            /**
             * 发送消息队列 待处理
             */
            asyncCenter.sendPush(FriendPushEnum.ACCEPT, friendship1);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("同意好友请求操作失败");
        }
        return result;
    }

    @Override
    public boolean ignoreFriendship(Friendship friendship) {
        try {
            friendship.setState(0);
            List<User> friends = friendshipMapper.selectFriendsByPrimaryKey(friendship);
            if (friends == null) { //不存在好友关系或者好友请求
                return false;
            }
            if (friendshipMapper.delete(friendship) <= 0) {
                throw new RuntimeException("删除好友操作失败");
            }
            Friendship friendship1 = new Friendship();
            friendship1.setToid(friendship.getFromid());
            friendship1.setFromid(friendship.getToid());
            friendship1.setState(1);
            if (friendshipMapper.selectFriendsByPrimaryKey(friendship1).size() > 0) { //删除好友
                if (friendshipMapper.delete(friendship1) <= 0) {
                    throw new RuntimeException("删除好友操作失败");
                }
                asyncCenter.sendPush(FriendPushEnum.DELETE, friendship1);
            } else { //拒绝添加好友请求
                /**
                 * 发送消息队列 待处理
                 */
                asyncCenter.sendPush(FriendPushEnum.IGNORE, friendship1);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("删除好友操作失败");
        }
    }

    @Override
    public boolean isFriend(String userid, String friendid) {
        Friendship friendship = new Friendship();
        friendship.setFromid(userid);
        friendship.setToid(friendid);
        friendship.setState(1);
        return friendshipMapper.selectFriendsByPrimaryKey(friendship).size() > 0;
    }
}
