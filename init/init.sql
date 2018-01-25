CREATE TABLE `user` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `token` varchar(64) DEFAULT NULL COMMENT 'token',
  `device_token` varchar(64) DEFAULT NULL COMMENT '推送设备号',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号',
  `email` varchar(128) NOT NULL COMMENT '邮箱',
  `nick_name` varchar(10) DEFAULT NULL COMMENT '昵称',
  `password` varchar(16) NOT NULL COMMENT '密码',
  `salt` varchar(64) NOT NULL COMMENT '加密盐',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uk_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

