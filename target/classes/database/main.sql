use scau;
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
    `pid` varchar(20) NOT NULL,
    `pname` varchar(32) NOT NULL UNIQUE,
    PRIMARY KEY (`pid`)
    ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
    `roleid` varchar(20) NOT NULL,
    `rolename` varchar(32) NOT NULL UNIQUE COMMENT '角色名称',
    PRIMARY KEY (`roleid`)
    ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `userid` varchar(20) NOT NULL,
    `username` varchar(32) NOT NULL UNIQUE COMMENT '用户昵称',
    `password` varchar(32) DEFAULT NULL COMMENT '密码',
    `nickname` varchar(32) DEFAULT NULL COMMENT '用户昵称',
    `email` varchar(128) DEFAULT NULL COMMENT '邮箱|登录帐号',
    `photo` varchar(128) DEFAULT NULL,
    `roleid` varchar(20) DEFAULT 'hello',
    `createtime` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`userid`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
    `roleid` varchar(20) DEFAULT NULL COMMENT '角色ID',
    `pid` varchar(20) DEFAULT NULL COMMENT '权限ID',
    KEY `roleid` (`roleid`),
    KEY `pid` (`pid`),
    CONSTRAINT `role_permission_roleid` FOREIGN KEY (`roleid`) REFERENCES `role` (`roleid`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `role_permission_pid` FOREIGN KEY (`pid`) REFERENCES `permission` (`pid`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
    `id` varchar(20) NOT NULL,
    `masterid` varchar(20) NOT NULL,
    `name` varchar(32) NOT NULL,
    `createtime` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `groupship`;
CREATE TABLE `groupship` (
    `id` varchar(20) NOT NULL,
    `userid` varchar(20) NOT NULL,
    `groupid` varchar(20) NOT NULL,
    `createtime` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `friendship`;
CREATE TABLE `friendship` (
    `id` varchar(20) NOT NULL,
    `fromid` varchar(20) NOT NULL,
    `toid` varchar(20) NOT NULL,
    `createtime` datetime DEFAULT NULL COMMENT '创建时间',
    `state` TINYINT(1) NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `groupship`;
CREATE TABLE `groupship` (
    `id` varchar(20) NOT NULL,
    `userid` varchar(20) NOT NULL,
    `groupid` varchar(20) NOT NULL,
    `createtime` datetime DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `user` VALUES ('1', '1','1','1', '1','1','1',null);
INSERT INTO `user` VALUES ('2', '2','2','2', '2','2','2',null);

INSERT INTO `permission` VALUES ('1', 'select');
INSERT INTO `permission` VALUES ('2', 'add');
INSERT INTO `permission` VALUES ('3', 'delete');
INSERT INTO `permission` VALUES ('4', 'update');

INSERT INTO `role` VALUES ('1', 'admin');
INSERT INTO `role` VALUES ('2', 'user');
INSERT INTO `role` VALUES ('3', 'visitor');

INSERT INTO `role_permission` VALUES ('1', '3');
INSERT INTO `role_permission` VALUES ('1', '2');
INSERT INTO `role_permission` VALUES ('2', '1');

