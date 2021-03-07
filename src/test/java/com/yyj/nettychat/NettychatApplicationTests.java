package com.yyj.nettychat;

import com.alibaba.fastjson.JSON;
import com.yyj.nettychat.entity.ChatMessage;
import com.yyj.nettychat.entity.Message;
import com.yyj.nettychat.entity.MsgActionEnum;
import com.yyj.nettychat.entity.UserChannelMap;
import com.yyj.nettychat.mapper.FriendshipMapper;
import com.yyj.nettychat.mapper.GroupMapper;
import com.yyj.nettychat.mapper.RoleMapper;
import com.yyj.nettychat.model.*;
import com.yyj.nettychat.service.UserService;
import com.yyj.nettychat.util.FastDFSClient;
import com.yyj.nettychat.util.RedisUtils;
import com.yyj.nettychat.util.ZipUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NettychatApplicationTests {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    UserService userService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    FriendshipMapper friendshipMapper;

    @Autowired
    GroupMapper groupMapper;

    @Autowired
    FastDFSClient fastDFSClient;

    @Test
    public void contextLoads() {
        //System.out.println(userService.getUserByName("1"));
        //System.out.println(roleMapper.selectByRoleid("1"));
        // System.out.println(roleMapper.selectByRolename("user"));
        System.out.println(redisUtils.get("\n\n\n\n\n========\n\n\n\n"));
        System.out.println(redisUtils.get("shiro:session:d9ff9cbc-363b-4ded-857d-a0fbbd48a033"));
        System.out.println(redisUtils.get("shiro:session:d9ff9cbc-363b-4ded-857d-a0fbbd48a03"));
        System.out.println(redisUtils.get("hh"));
    }

    @Test
    public void test1() {
        ChatMessage chatmsg = new ChatMessage();
        chatmsg.setFriendid("11111");
        chatmsg.setGroupid("3333");
        chatmsg.setUserid("3333");
        chatmsg.setMessage("wdddfdf");
        Message msg = new Message();
        msg.setMessage(chatmsg);
//        msg.setType(3);
        msg.setExt("png");
        System.out.println(JSON.toJSONString(msg));
    }

    @Test
    public void test2() {
        ZipUtils zip = new ZipUtils();
        System.out.println("压缩前字符串:压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩" +
                "前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压" +
                "压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩" +
                "前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串缩前字符压缩前字符串压缩前字符串压缩前字符串压缩前字符串" +
                "串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串");
        String zipstr = zip.zip("压缩前字符串:压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩" +
                "前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压" +
                "压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩" +
                "前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串缩前字符压缩前字符串压缩前字符串压缩前字符串压缩前字符串" +
                "串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串压缩前字符串");
        System.out.println("压缩后字符串:");
        System.out.println(zipstr);
        System.out.println("解压后字符串：" + zip.unzip(zipstr));
    }

    @Test
    public void test3(){
        Friendship friendship=new Friendship();
        friendship.setCreatetime(new Date());
        friendship.setId("6");
        friendship.setFromid("1");
        friendship.setState(1);
        friendship.setToid("2");
        friendship.setToid(null);
        Group group=new Group();
        group.setId("1");
        group.setCreatetime(new Date());
        group.setMasterid("1");
        //groupMapper.insertGroup(group);
        System.out.println(groupMapper.seleteGroup("1"));
        GroupMember groupMember=new GroupMember();
        groupMember.setCreatetime(new Date());
        groupMember.setId("2");
        groupMember.setGroupid("1");
        User user=new User();
        user.setUserid("1");
        groupMember.setMember(user);
        //System.out.println(groupMapper.insertMember(groupMember));
        System.out.println(groupMapper.seleteMember(groupMember));
        System.out.println(groupMapper.selectGroupsByuserid("1"));
    }

    @Test
    public void test4(){
        System.out.println(groupMapper.seleteMembers("1288095591954481152"));
    }
}
