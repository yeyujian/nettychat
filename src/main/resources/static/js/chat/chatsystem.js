window.CHATSYSTEM = {
    _webSocket: null,
    _curFocus: {},
    USER_INFO: {
        userid: '',
        username: '',
        nickname: '',
        useremail: ''
    },
    SENDMSG: {
        createtime: null,
        message: null
    },
    _friends: {},
    _groups: {},
    sendMessage: function(obj = null) {
        var text = $('#input_box').val();
        // $('#input_box').val("");
        $('#input_box').attr("readonly", "readonly");
        var obj = obj ? obj : CHATSYSTEM.createMessage(text, CHATSYSTEM._curFocus.type == "friendid" ? 1 : 2);
        console.log("发送消息：");
        console.log(obj);
        this._webSocket.send(JSON.stringify(obj));
    },
    createMessage: function(message, type = 0, ext = null) { //创建消息体
        var SENDMSG = new Object();
        SENDMSG.type = type;
        SENDMSG.message = {
            userid: CHATSYSTEM.USER_INFO.userid,
            message: message
        };
        if (type == 1) {
            SENDMSG.message.friendid = CHATSYSTEM._curFocus.id;
        } else if (type == 2) {
            SENDMSG.message.groupid = CHATSYSTEM._curFocus.id;
        }
        SENDMSG.createtime = new Date();
        SENDMSG.message[this._curFocus.type] = this._curFocus.id;
        return SENDMSG;
    },
    showMemberMsg: function(key, groupid, isMaster = false) { //显示成员名片
        var member = null;
        var btnHTML = "";
        if (groupid == null) {
            member = CHATSYSTEM._friends[key];
        } else {
            member = CHATSYSTEM._groups[groupid].members[key];
        }
        if (key != CHATSYSTEM.USER_INFO.userid) {
            if (CHATSYSTEM._friends[key] == undefined) {
                btnHTML += `<a onclick="CHATSYSTEM.insertFriend('${key}')"><i class="layui-icon layui-icon-addition" style="font-size: 30px; color: #1E9FFF;"></i></a>`;
            } else {
                btnHTML += `<a onclick="CHATSYSTEM.deleteFriend('${key}')"><i class="layui-icon layui-icon-subtraction" style="font-size: 30px; color: #1E9FFF;"></i></a>`;
            }
        }
        if (isMaster) {
            btnHTML += `<a onclick="CHATSYSTEM.withdrawGroup('${groupid}','${key}')"><i class="layui-icon layui-icon-subtraction" style="font-size: 30px; color: red;"></i></a>`;
        }
        var index = layer.open({
            title: member.nickname,
            content: `
        <div class="own-head-content">
            <div class="own_head_top">
                <div class="own_head_text">
                    <p class="own_name">昵称：` + member.nickname + `</p>
                    <p class="own_numb">用户名：` + member.username + `</p>
                    
                </div>
                <img class="sculpture  border border-white" src="https://p.qqan.com/up/2020-6/2020061409134850890.jpg" alt="" />
            </div>
            <div class="own_head_bottom">
                <p><span>邮箱: </span>` + member.email + `</p>
                ` + btnHTML + `                
            </div>
        </div>`,
            btn: [],
            shade: 0
        });
    },
    insertFriend: function(id) { //添加好友
        if (!confirm("确定要添加好友吗")) return;
        $.ajax({
            url: "/friend/send/" + id,
            type: "GET",
            success: function(result) {
                if (result.status == 10200) {
                    alert("请求发送成功");
                } else {
                    alert(result.msg);
                }
            },
            error: function() {
                alert("请求发送失败");
            }
        });
    },
    deleteFriend: function(id) { //删除好友操作
        if (!confirm("确定要删除好友吗")) return;
        $.ajax({
            url: "/friend/delete/" + id,
            type: "DELETE",
            success: function(result) {
                if (result.status == 10200) {
                    alert("请求发送成功");
                    //删除好友列表对应id
                    $("#F-" + id).remove();
                } else {
                    alert(result.msg);
                }
            },
            error: function() {
                alert("请求发送失败");
            }
        });
    },
    createGroup: function(name) { //创建群组
        if (!confirm("确定要创建群组:" + name + " 吗")) return;
        $.ajax({
            url: "/group",
            type: "POST",
            data: { name: name },
            success: function(result) {
                if (result.status == 10200) {
                    alert("创建群组成功");
                    let group = result.data;
                    CHATSYSTEM._groups[group.id] = group; // 缓存群组信息
                    var members = group.members;
                    CHATSYSTEM._groups[group.id].members = {};
                    for (var member of members) {
                        CHATSYSTEM._groups[group.id].members[member.member.userid] = member.member;
                    }
                    $("#friendslist >p:eq(4)").after(`<a href="#" id="G-list-${group.id}" class="list-group-item list-group-item-action" onclick="CHATSYSTEM.groupListOnClickEvent('${group.id}')">
    <div class="chat_head border border-blue"><img src="${"/static/img/avatar.jpg"}" /></div>
    <div class="friend_text">
        <p class="friend_name">${group.name}</p>
    </div></a>`);
                    $(".window_body").append(
                        `<div id="window-body-group${group.id}" class="hide"></div>`
                    );
                } else {
                    alert(result.msg);
                }
            },
            error: function() {
                alert("请求发送失败");
            }
        });
    },
    insertGroup: function(id) { //添加群组
        if (!confirm("确定要加入群组吗")) return;
        $.ajax({
            url: "/groupmember/" + id,
            type: "POST",
            success: function(result) {
                if (result.status == 10200) {
                    alert("请求发送成功");
                } else {
                    alert(result.msg);
                }
            },
            error: function() {
                alert("请求发送失败");
            }
        });
    },
    withdrawGroup: function(groupid, memberid) { //退出/踢出群组
        if (!confirm("确定要退出/踢出群组吗？")) return;
        $.ajax({
            url: "/groupmember/" + groupid,
            type: "DELETE",
            data: { memberid: memberid },
            success: function(result) {
                if (result.status == 10200) {
                    alert("操作成功");
                    layer.closeAll();
                    $("#G-list-" + CHATSYSTEM._curFocus.id).remove();
                    CHATSYSTEM._groups[CHATSYSTEM._curFocus.id].members = undefined;
                } else {
                    alert("退出/踢出群组失败");
                }
            },
            error: function() {
                alert("退出/踢出群组失败");
            }
        });
    },
    deleteGroup: function(id) { //删除群组
        if (!confirm("确定要删除群组吗")) return;
        $.ajax({
            url: "/group/" + id,
            type: "DELETE",
            success: function(result) {
                if (result.status == 10200) {
                    alert("操作成功");
                    layer.closeAll();
                    $("#G-list-" + CHATSYSTEM._curFocus.id).remove();
                    CHATSYSTEM._groups[CHATSYSTEM._curFocus.id].members = undefined;
                } else {
                    alert("删除群组失败");
                }
            },
            error: function() {
                alert("删除群组失败");
            }
        });
    },
    windowTopBtnOnClick: function() { //查看群组信息  获取群组成员列表
        if (JSON.stringify(CHATSYSTEM._curFocus) == '{}') return;
        if (CHATSYSTEM._curFocus.type == "friendid") { //当前聊天窗口是好友聊天
            CHATSYSTEM.showMemberMsg(CHATSYSTEM._curFocus.id, null);
        } else { //当前聊天窗口是群聊
            var group = CHATSYSTEM._groups[CHATSYSTEM._curFocus.id];
            var membersHTML = ``;
            var isMaster = group.masterid == CHATSYSTEM.USER_INFO.userid;
            var flag = 0;
            for (var id in group.members) {
                membersHTML += `<div class="group-member-item" onclick="CHATSYSTEM.showMemberMsg('${group.members[id].userid}','${group.id}',true)">
                <img src="/static/img/avatar.jpg" />
                ` + (flag == 0 ? `<p class="member_name" style="color:red;">${group.members[id].nickname}</p>` : `<p class="member_name">${group.members[id].nickname}</p>`) + `</div>`;
                flag++;
            }
            layer.closeAll();
            var index = layer.open({
                title: "群组id:" + group.id,
                content: `
                <div class="window-sidebar">
        <div id="groupmember-lsit">` + membersHTML + `</div>
        <div class="btn-list">
        ` + (isMaster ? `<div class="btn-item"><button type="button" class="btn btn-outline-danger" onclick="CHATSYSTEM.deleteGroup('${group.id}')">删除群组</button></div>` :
                    `<div class="btn-item"><button type="button" class="btn btn-outline-danger" onclick="CHATSYSTEM.withdrawGroup('${group.id}','${group.members[id].userid}')">退出群组</button></div>`) + `
        </div>
        </div>`,
                btn: [],
                shade: 0,
                type: 1,
                offset: 'rt',
                area: ['305px', '100%']
            });

        }
    },
    initUserInfo: function() {
        this.USER_INFO.userid = document.getElementById("userid").innerText;
        this.USER_INFO.username = document.getElementById("username").innerText;
        this.USER_INFO.nickname = document.getElementById("nickname").innerText;
        this.USER_INFO.useremail = document.getElementById("useremail").innerText;
        return true;
    },
    friendListOnClickEvent: function(key) {
        layer.closeAll();
        $("#input_box").attr("readonly", false);
        $("#windows_name").text(CHATSYSTEM._friends[key].nickname);
        //将旧活跃隐藏
        if (JSON.stringify(CHATSYSTEM._curFocus) != '{}') {
            $("#window-body-" + (CHATSYSTEM._curFocus.type == "friendid" ? "friend" : "group") +
                CHATSYSTEM._curFocus.id).addClass(
                "hide");
            $((CHATSYSTEM._curFocus.type == "friendid" ? "#friendslist-" : "#grouplist-") +
                    CHATSYSTEM._curFocus.id)
                .removeClass("active");
        }
        //将新活跃显示
        $("#window-body-friend" + key).removeClass("hide");
        CHATSYSTEM._curFocus.type = "friendid";
        CHATSYSTEM._curFocus.id = key;
        $("#friendslist-" + key).addClass("active");
        if ($(`#friendslist-${key} span`).length > 0)
            $(`#friendslist-${key} span`).hide();
    },
    groupListOnClickEvent: function(key) {
        layer.closeAll();
        $("#input_box").attr("readonly", false);
        $("#windows_name").text(CHATSYSTEM._groups[key].name);
        //将旧活跃隐藏
        if (JSON.stringify(CHATSYSTEM._curFocus) != '{}') {
            $("#window-body-" + (CHATSYSTEM._curFocus.type == "friendid" ? "friend" : "group") +
                CHATSYSTEM._curFocus.id).addClass(
                "hide");
            $((CHATSYSTEM._curFocus.type == "friendid" ? "#friendslist-" : "#grouplist-") +
                    CHATSYSTEM._curFocus.id)
                .removeClass("active");
        }
        //将新活跃显示
        $("#window-body-group" + key).removeClass("hide");
        CHATSYSTEM._curFocus.type = "groupid";
        CHATSYSTEM._curFocus.id = key;
        $("#grouplist-" + key).addClass("active");
        if ($(`#grouplist-${key} span`).length > 0)
            $(`#grouplist-${key} span`).hide();
    },
    initFriendList: function() { //初始化好友列表
        let ans = false;
        $.ajax({
            url: "/friend/findall",
            async: false, //同步请求
            success: function(result) {
                if (result.status == 10200) {

                    for (var friend of result.data) {
                        friend.img = "/static/img/avatar.jpg";
                        $("#friendslist >p:eq(3)").after(`<a href="#" id="F-${friend.userid}" class="list-group-item list-group-item-action" onclick="CHATSYSTEM.friendListOnClickEvent('${friend.userid}')">
        <div class="chat_head border border-blue"><img src="${friend.img}" /></div>
        <div class="friend_text">
            <p class="friend_name">${friend.nickname}</p>
        </div></a>`);
                        $(".window_body").append(
                            `<div id="window-body-friend${friend.userid}" class="hide"></div>`
                        );
                        CHATSYSTEM._friends[friend.userid] = friend; //缓存朋友信息
                    }
                    ans = true;
                } else {
                    alert("加载好友列表失败");
                }
            },
            error: function() {
                alert("加载好友列表失败");
            }
        });
        return ans;
    },
    initGroupList: function() { //初始化_groups数组
        let ans = false;
        $.ajaxSettings.async = false;
        $.get("/groups", function(data, status) {
            if (status != "success") {
                alert("加载群组列表失败");
                return ans;
            }
            if (data.status == 10400) {
                alert(data.message);
                return ans;
            }
            for (var group of data.data) {
                CHATSYSTEM._groups[group.id] = group; // 缓存群组信息
                var members = group.members;
                CHATSYSTEM._groups[group.id].members = {};
                for (var member of members) {
                    CHATSYSTEM._groups[group.id].members[member.member.userid] = member.member;
                }
                $("#friendslist >p:eq(4)").after(`<a href="#" id="G-list-${group.id}" class="list-group-item list-group-item-action" onclick="CHATSYSTEM.groupListOnClickEvent('${group.id}')">
<div class="chat_head border border-blue"><img src="${"/static/img/avatar.jpg"}" /></div>
<div class="friend_text">
    <p class="friend_name">${group.name}</p>
</div></a>`);
                $(".window_body").append(
                    `<div id="window-body-group${group.id}" class="hide"></div>`
                );
            }
            ans = true;
        });
        $.ajaxSettings.async = true;
        return ans;
    },
    showUserInfo: function(e) {
        var data = JSON.parse($(e).children("span").text());
        var index = layer.open({
            title: data.username,
            content: `
            <div class="own-head-content">
                <div class="own_head_top">
                    <div class="own_head_text">
                        <p class="own_name">昵称：` + data.nickname + `</p>
                        <p class="own_numb">用户名：` + data.username + `</p>
                        
                    </div>
                    <img class="sculpture  border border-white" src="https://p.qqan.com/up/2020-6/2020061409134850890.jpg" alt="" />
                </div>
                <div class="own_head_bottom">
                    <p><span>邮箱: </span>` + data.email + `</p>        
                </div>
            </div>`,
            btn: [],
            shade: 0
        });
    },

    showGroupInfo: function(e) {
        var data = JSON.parse($(e).children("span").text());
        var index = layer.open({
            title: data.name,
            content: `
            <div class="own-head-content">
                <div class="own_head_top">
                    <div class="own_head_text">
                        <p class="own_name">群组名：` + data.name + `</p>
                        <p class="own_numb">群主id：` + data.masterid + `</p>
                        
                    </div>
                    <img class="sculpture  border border-white" src="https://p.qqan.com/up/2020-6/2020061409134850890.jpg" alt="" />
                </div>
                <div class="own_head_bottom">
                    <p><span>创建时间: </span>` + data.createtime + `</p>        
                </div>
            </div>`,
            btn: [],
            shade: 0
        });
    },
    searchButtonOnClickEvent: function() {
        $("#searchlist").html("");
        var name = $("#search-text").val();
        var flag = 0;
        $.ajax({
            url: "/user/find/" + name,
            type: "GET",
            success: function(result) {
                if (result.status == 10200) {
                    console.log(result.data);
                    if (result.data != null) {
                        $("#searchlist").html(`
                    <a href="#" class="list-group-item list-group-item-action">
                        <div class="chat_head border border-blue"><img src="/static/img/avatar.jpg" /></div>
                        <div class="friend_text"  onclick="CHATSYSTEM.showUserInfo(this)">
                            <p class="friend_name">${result.data.username}</p>
                            <span hidden>${JSON.stringify(result.data)}</span>
                        </div>
                        <div class="btn-group make-sure" role="group">
                            <button type="button" class="btn btn-outline-success btn-sm" onclick="CHATSYSTEM.insertFriend('${result.data.userid}')">添加</button>
                        </div>
                    </a>`);
                    }
                } else {
                    // alert(result.msg);
                }
            },
            error: function() {
                alert("搜索失败");
            }
        });
        if (/^\d+$/.test(name)) //判断是否是id
            $.ajax({
            url: "/group/" + name,
            type: "GET",
            success: function(result) {
                if (result.status == 10200) {
                    if (result.data != null) {
                        $("#searchlist").append(`
                    <a href="#" class="list-group-item list-group-item-action">
                        <div class="chat_head border border-blue"><img src="/static/img/avatar.jpg" /></div>
                        <div class="friend_text"  onclick="CHATSYSTEM.showGroupInfo(this)">
                            <p class="friend_name">${result.data.name}</p>
                            <span hidden>${JSON.stringify(result.data)}</span>
                        </div>
                        <div class="btn-group make-sure" role="group">
                            <button type="button" class="btn btn-outline-success btn-sm" onclick="CHATSYSTEM.insertGroup('${result.data.id}')">添加</button>
                        </div>
                    </a>`);
                    }
                } else {
                    // alert(result.msg);
                }
            },
            error: function() {
                alert("搜索失败");
            }
        });
    },
    adviseHandle: function(operation, num, id, userid = null) { //处理通知点击按钮事件
        //operation 接受好友 101  拒绝好友 102 同意入群 201 拒绝入群202
        switch (operation) {
            case 101:
                $.ajax({
                    url: "/friend/accept/" + id,
                    type: "GET",
                    success: function(result) {
                        if (result.status == 10200) {
                            $("#advise-F-" + num).remove();
                            $.ajax({
                                url: "/user/findbyid/" + result.data.fromid,
                                type: "GET",
                                success: function(data) {
                                    if (data.status == 10200) {
                                        let friend = data.data;
                                        friend.img = "/static/img/avatar.jpg";
                                        $("#friendslist >p:eq(3)").after(`<a href="#" id="F-${friend.userid}"  class="list-group-item list-group-item-action" onclick="CHATSYSTEM.friendListOnClickEvent('${friend.userid}')">
                    <div class="chat_head border border-blue"><img src="${friend.img}" /></div>
                    <div class="friend_text">
                        <p class="friend_name">${friend.nickname}</p>
                    </div></a>`);
                                        $(".window_body").append(
                                            `<div id="window-body-friend${friend.userid}" class="hide"></div>`
                                        );
                                        CHATSYSTEM._friends[friend.userid] = friend;
                                    } else {
                                        alert(`查找用户${result.data.fromid}失败`);
                                    }
                                },
                                error: function() {
                                    alert(`查找用户${id}失败`);
                                }
                            });
                        } else {
                            alert(result.msg);
                        }
                    },
                    error: function() {
                        alert("接受添加好友操作失败");
                    }
                });
                break;
            case 102:
                $.ajax({
                    url: "/friend/ignore/" + id,
                    type: "DELETE",
                    success: function(result) {
                        if (result.status == 10200) {
                            $("#advise-F-" + num).remove();
                        } else {
                            alert(result.msg);
                        }
                    },
                    error: function() {
                        alert("拒绝添加好友操作失败");
                    }
                });
                break;
            case 201:
                $.ajax({
                    url: "/groupmember",
                    type: "PATCH",
                    data: { "groupid": id, "member.userid": userid },
                    success: function(result) {
                        if (result.status == 10200) {
                            $("#advise-G-" + num).remove();
                            //_key
                        } else {
                            alert(result.msg);
                        }
                    },
                    error: function() {
                        alert("同意入群请求操作失败");
                    }
                });
                break;
            case 202:
                $.ajax({
                    url: "/groupmember",
                    type: "DELETE",
                    data: { "groupid": id, "member.userid": userid },
                    success: function(result) {
                        if (result.status == 10200) {
                            $("#advise-G-" + num).remove();
                        } else {
                            alert(result.msg);
                        }
                    },
                    error: function() {
                        alert("拒绝入群请求操作失败");
                    }
                });
                break;
        }
    },
    getOnClickIdInfo: function(id) {
        $.ajax({
            url: "/user/findbyid/" + id,
            type: "GET",
            success: function(result) {
                if (result.status == 10200) {
                    var index = layer.open({
                        title: id,
                        content: `
                        <div class="own-head-content">
                            <div class="own_head_top">
                                <div class="own_head_text">
                                    <p class="own_name">昵称：` + result.data.nickname + `</p>
                                    <p class="own_numb">用户名：` + result.data.username + `</p>
                                    
                                </div>
                                <img class="sculpture  border border-white" src="https://p.qqan.com/up/2020-6/2020061409134850890.jpg" alt="" />
                            </div>
                            <div class="own_head_bottom">
                                <p><span>邮箱: </span>` + result.data.email + `</p>        
                            </div>
                        </div>`,
                        btn: [],
                        shade: 0
                    });
                } else {
                    alert(result.msg);
                }
            },
            error: function() {
                alert("加载信息失败");
            }
        });

    },
    messageAccepteHandle: function(data) {
        if (data.type < 1000) { // 处理聊天消息
            switch (data.type) {
                case 1: //好友消息

                    if (data.message.userid == CHATSYSTEM.USER_INFO.userid) {
                        if ($("#friendslist-" + data.message.friendid).length <= 0) { //如果不存在聊天框
                            $(".list-group.chatlist").append(` <a href="#" id="friendslist-${data.message.friendid}" class="list-group-item list-group-item-action" onclick="CHATSYSTEM.friendListOnClickEvent('${data.message.friendid}')">
                            <div class="chat_head border border-blue"><img src="` + "/static/img/avatar.jpg" + `"/></div>
                            <div class="chat_text">
                                <p class="chat_name">` + CHATSYSTEM._friends[data.message.friendid].nickname + `<span class="layui-badge-dot" style="display: none;"></p>
                                <p class="chat_message">` + data.message.message + `</p>
                            </div>
                            <div class="chat_time">${data.createtime}</div></a>`);
                        } else {
                            $(".list-group.chatlist").prepend($("#friendslist-" + data.message
                                .friendid)); //移动到最前面
                            $("#friendslist-" + data.message.friendid + " .chat_message")
                                .text(data.message.message);
                            $("#friendslist-" + data.message.friendid + " .chat_time").text(
                                data.createtime);
                        }

                    } else {
                        //检查是否存在当前通信人信息 无就导入  此人还不是列表中的好友关系
                        if (CHATSYSTEM._friends[data.message.userid] == undefined) {
                            $.ajax({
                                url: "/user/findbyid/" + data.message.userid,
                                type: "GET",
                                async: false,
                                success: function(result) {
                                    if (result.status == 10200) {
                                        let friend = result.data;
                                        friend.img = "/static/img/avatar.jpg";
                                        $("#friendslist >p:eq(3)").after(`<a href="#" id="F-${friend.userid}" class="list-group-item list-group-item-action" onclick="CHATSYSTEM.friendListOnClickEvent('${friend.userid}')">
                    <div class="chat_head border border-blue"><img src="${friend.img}" /></div>
                    <div class="friend_text">
                        <p class="friend_name">${friend.nickname}</p>
                    </div></a>`);
                                        $(".window_body").append(
                                            `<div id="window-body-friend${friend.userid}" class="hide"></div>`
                                        );
                                        CHATSYSTEM._friends[data.message.userid] = friend;
                                    } else {
                                        alert(`查找用户${data.message.userid}失败`);
                                    }
                                },
                                error: function() {
                                    alert(`查找用户${data.message.userid}失败`);
                                }
                            });
                        }

                        if ($("#friendslist-" + data.message.userid).length <= 0) {
                            $(".list-group.chatlist").append(` <a href="#" id="friendslist-${data.message.userid}" class="list-group-item list-group-item-action" onclick="CHATSYSTEM.friendListOnClickEvent('${data.message.userid}')">
                    <div class="chat_head border border-blue"><img src="` + "/static/img/avatar.jpg" + `"/></div>
                    <div class="chat_text">
                        <p class="chat_name">` + CHATSYSTEM._friends[data.message.userid].nickname + `<span class="layui-badge-dot" style="display: none;"></p>
                        <p class="chat_message">` + data.message.message + `</p>
                    </div>
                    <div class="chat_time">${data.createtime}</div></a>`);
                        } else {
                            $(".list-group.chatlist").prepend($("#friendslist-" + data.message
                                .userid)); //移动到最前面
                            $("#friendslist-" + data.message.userid + " .chat_message")
                                .text(data.message.message);
                            $("#friendslist-" + data.message.userid + " .chat_time").text(
                                data.createtime);
                        }
                    }
                    if (data.message.userid == CHATSYSTEM.USER_INFO.userid) { //处理自己发的消息
                        $("#window-body-friend" + data.message.friendid).append(`<div class="chat-item item-right clearfix">
                        <div class="img fr"><img src="/static/img/avatar.jpg" alt=""></div>
                        <span class="fr message">${data.message.message}</span>
                    </div>`);
                        //在收到自己发送的消息之前 不能再发送  此处 接触只读状态
                        $('#input_box').val("");
                        $('#input_box').removeAttr("readonly");
                    } else {
                        $("#window-body-friend" + data.message.userid).append(`<div class="chat-item item-left clearfix rela">
                        <span class="abs uname">${CHATSYSTEM._friends[data.message.userid].nickname}</span>
                        <div class="img fl"><img src="/static/img/avatar.jpg" alt=""></div>
                        <span class="fl message">${data.message.message}</span>
                    </div>`);
                    }
                    //添加 红点 标记未读
                    if (JSON.stringify(CHATSYSTEM._curFocus) == '{}' || CHATSYSTEM._curFocus.type != "friendid" || CHATSYSTEM._curFocus.id != data.message.userid) {
                        $(`#friendslist-${data.message.userid} span`).show();
                    }

                    break;
                case 2: //群组消息
                    //检查群组成员列表是否有该消息的成员信息 无就同步导入
                    if (CHATSYSTEM._groups[data.message.groupid].members[data.message.userid] == undefined) {
                        //_key
                        $.ajax({
                            url: "/user/findbyid/" + data.message.userid,
                            type: "GET",
                            async: false,
                            success: function(result) {
                                if (result.status == 10200) {
                                    CHATSYSTEM._groups[data.message.groupid].members[data.message.userid] = result.data;
                                } else {
                                    alert(`查找用户${data.message.userid}失败`);
                                }
                            },
                            error: function() {
                                alert(`查找用户${data.message.userid}失败`);
                            }
                        });
                    }
                    if ($("#grouplist-" + data.message.groupid).length <= 0) {
                        $(".list-group.chatlist").append(` <a href="#" id="grouplist-${data.message.groupid}" class="list-group-item list-group-item-action" onclick="CHATSYSTEM.groupListOnClickEvent('${data.message.groupid}')">
                <div class="chat_head border border-blue"><img src="` + "/static/img/avatar.jpg" + `"/></div>
                <div class="chat_text">
                    <p class="chat_name">` + CHATSYSTEM._groups[data.message.groupid].members[data.message.userid].nickname + `<span class="layui-badge-dot" style="display: none;"></p>
                    <p class="chat_message">` + data.message.message + `</p>
                </div>
                <div class="chat_time">${data.createtime}</div></a>`);
                    } else {
                        $(".list-group.chatlist").prepend($("#grouplist-" + data.message
                            .groupid)); //移动到最前面
                        $("#grouplist-" + data.message.groupid + " .chat_message")
                            .text(data.message.message);
                        $("#grouplist-" + data.message.groupid + " .chat_time").text(
                            data.createtime);
                    }

                    if (data.message.userid == CHATSYSTEM.USER_INFO.userid) { //处理自己发的消息
                        $("#window-body-group" + data.message.groupid).append(`<div class="chat-item item-right clearfix">
                <div class="img fr"><img src="/static/img/avatar.jpg" alt=""></div>
                <span class="fr message">${data.message.message}</span>
            </div>`);
                        //在收到自己发送的消息之前 不能再发送  此处 接触只读状态
                        $('#input_box').val("");
                        $('#input_box').removeAttr("readonly");
                    } else {
                        $("#window-body-group" + data.message.groupid).append(`<div class="chat-item item-left clearfix rela">
                <span class="abs uname">${CHATSYSTEM._groups[data.message.groupid].members[data.message.userid].nickname}</span>
                <div class="img fl"><img src="/static/img/avatar.jpg" alt=""></div>
                <span class="fl message">${data.message.message}</span>
            </div>`);

                        if (JSON.stringify(CHATSYSTEM._curFocus) == '{}' || CHATSYSTEM._curFocus.type != "groupid" || CHATSYSTEM._curFocu.id != data.message.groupid) {
                            $(`"#grouplist-${data.message.groupid} span`).show();
                        }
                    }
                    break;
            }
        } else { //处理通知消息
            var msg = "";
            let num;
            switch (data.type) {
                //好友通知
                case 1100:
                    num = $("a[id^=advise-F-]").length;
                    $("#friendslist >p:eq(1)").after(`<a href="#" id="advise-F-${num + 1}" class="list-group-item list-group-item-action">
                    <div class="chat_head border border-blue"><img src="${"/static/img/avatar.jpg"}" /></div>
                    <div class="friend_text">
                        <p class="friend_name" onclick="getOnClickIdInfo('${data.ext.fromid}')">${data.ext.fromid}</p>
                    </div><div class="btn-group make-sure" role="group">
                    <button type="button" class="btn btn-outline-success btn-sm" onclick="CHATSYSTEM.adviseHandle(101,${num + 1},'${data.ext.fromid}')">接受</button>
                    <button type="button" class="btn btn-outline-danger btn-sm" onclick="CHATSYSTEM.adviseHandle(102,${num + 1},'${data.ext.fromid}')">拒绝</button>
                </div></a>`);
                    break;
                case 1101:
                    msg = "已同意添加好友";
                case 1102:
                    if (msg == "")
                        msg = "拒绝添加好友";
                case 1103:
                    if (msg == "")
                        msg = "删除与你的好友关系";
                    num = $("a[id^=advise-friend-]").length;
                    $("#friendslist >p:eq(0)").after(`<a href="#" id="advise-friend-${num + 1}" class="list-group-item list-group-item-action">
                    <div class="chat_head border border-blue"><img src="${"/static/img/avatar.jpg"}" /></div>
                    <div class="chat_text" style="width: 170px;">
                        <p class="chat_name" onclick="CHATSYSTEM.getOnClickIdInfo('${data.ext.fromid}')">${data.ext.fromid}</p>
                        <p class="chat_message">${msg}</p>
                    </div>
                    <div class="btn-group make-sure" role="group">
                        <button type="button" class="btn btn-outline-danger btn-sm" onclick="javascript:$('#advise-friend-${num + 1}').remove()">delete</button>
                    </div></a>`);
                    break;
                    //群组通知
                case 1200:
                    num = $("a[id^=advise-G-]").length;
                    $("#friendslist >p:eq(2)").after(`<a href="#"  id="advise-G-${num + 1}" class="list-group-item list-group-item-action">
                    <div class="chat_head border border-blue"><img src="${"/static/img/avatar.jpg"}" /></div>
                    <div class="chat_text" style="width: 140px;">
                        <p class="chat_name" onclick="CHATSYSTEM.getOnClickIdInfo('${data.ext.member.userid}')">${data.ext.member.userid}</p>
                        <p class="chat_message">申请加入:${CHATSYSTEM._groups[data.ext.groupid].name}</p>
                    </div>
                    <div class="btn-group make-sure" role="group">
                    <button type="button" class="btn btn-outline-success btn-sm" onclick="CHATSYSTEM.adviseHandle(201,${num + 1},'${data.ext.groupid}','${data.ext.member.userid}')">接受</button>
                    <button type="button" class="btn btn-outline-danger btn-sm" onclick="CHATSYSTEM.adviseHandle(201,${num + 1},'${data.ext.groupid}','${data.ext.member.userid}')">拒绝</button>
                    </div></a>`);
                    break;
                case 1201:
                    if (msg == "")
                        msg = "同意你加入群组";
                    if (/^\d+$/.test(data.ext.groupid)) //判断是否是id
                        $.ajax({
                        url: "/group/" + data.ext.groupid,
                        type: "GET",
                        async: false,
                        success: function(result) {
                            if (result.status == 10200) {
                                if (result.data != null) {
                                    let group = result.data;
                                    CHATSYSTEM._groups[group.id] = group; // 缓存群组信息
                                    var members = group.members;
                                    CHATSYSTEM._groups[group.id].members = {};
                                    for (var member of members) {
                                        CHATSYSTEM._groups[group.id].members[member.member.userid] = member.member;
                                    }
                                    $("#friendslist >p:eq(4)").after(`<a href="#" id="G-list-${group.id}" class="list-group-item list-group-item-action" onclick="CHATSYSTEM.groupListOnClickEvent('${group.id}')">
                    <div class="chat_head border border-blue"><img src="${"/static/img/avatar.jpg"}" /></div>
                    <div class="friend_text">
                        <p class="friend_name">${group.name}</p>
                    </div></a>`);
                                    $(".window_body").append(
                                        `<div id="window-body-group${group.id}" class="hide"></div>`
                                    );
                                }
                            } else {
                                alert("同步群组信息失败");
                            }
                        },
                        error: function() {
                            alert("同步群组信息失败");
                        }
                    });
                case 1202:
                    if (msg == "")
                        msg = "拒绝你加入群组";
                case 1203:
                    if (msg == "")
                        msg = "你已被移出群组";
                    num = $("a[id^=advise-group-]").length;
                    $("#friendslist >p:eq(0)").after(`<a href="#" id="advise-group-${num + 1}" class="list-group-item list-group-item-action">
                    <div class="chat_head border border-blue"><img src="${"/static/img/avatar.jpg"}" /></div>
                    <div class="chat_text" style="width: 170px;">
                        <p class="chat_name">${CHATSYSTEM._groups[data.ext.groupid].name}</p>
                        <p class="chat_message">${msg}</p>
                    </div>
                    <div class="btn-group make-sure" role="group">
                        <button type="button" class="btn btn-outline-danger btn-sm" onclick="javascript:$('#advise-group-${num + 1}').remove()">delete</button>
                    </div></a>`);
                    break;
                case 1204:
                    num = $("a[id^=advise-group-]").length;
                    $("#friendslist >p:eq(0)").after(`<a href="#" id="advise-group-${num + 1}" class="list-group-item list-group-item-action">
                    <div class="chat_head border border-blue"><img src="${"/static/img/avatar.jpg"}" /></div>
                    <div class="chat_text" style="width: 170px;">
                        <p class="chat_name">${data.ext.member.userid}</p>
                        <p class="chat_message">退出：${CHATSYSTEM._groups[data.ext.groupid].name}</p>
                    </div>
                    <div class="btn-group make-sure" role="group">
                        <button type="button" class="btn btn-outline-danger btn-sm" onclick="javascript:$('#advise-group-${num + 1}').remove()>delete</button>
                    </div></a>`);
                    break;
            }
        }
    },
    initWebSocket: function() {
        // return;
        //判断当前浏览器是否支持 webSocket
        if (window.WebSocket) {
            this._webSocket = this._webSocket ? this._webSocket : new WebSocket("ws://127.0.0.1:8888/ws");
            this._webSocket.onopen = function(ev) {
                console.log("建立连接");
                CHATSYSTEM.sendMessage(CHATSYSTEM.createMessage("hello"));
            }
            this._webSocket.onclose = function(ev) {
                console.log("断开连接");
            }
            window.onbeforeunload = function() {
                websocket.close();
            };
            this._webSocket.onmessage = function(ev) { //接受websocket返回的消息
                var data = JSON.parse(ev.data);
                console.log("接收到服务器的消息::  " + ev.data);
                CHATSYSTEM.messageAccepteHandle(data);
            }
            return true;
        } else {
            alert("当前浏览器不支持 webSocket\n 聊天室起点失败");
            return false;
        }
    },
    init: function() {
            $("#friendslist").append(`<p class="lable">通知</p>`);
            $("#friendslist").append(`<p class="lable">好友请求</p>`);
            $("#friendslist").append(`<p class="lable">入群请求</p>`);
            $("#friendslist").append(`<p class="lable">现有好友</p>`);
            $("#friendslist").append(`<p class="lable">现有群组</p>`);
            CHATSYSTEM.initUserInfo() &&
                CHATSYSTEM.initFriendList() &&
                CHATSYSTEM.initGroupList() && //初始化群组列表
                CHATSYSTEM.initWebSocket() && alert("开启成功"); //连接websocket

        } //init
}
CHATSYSTEM.init();