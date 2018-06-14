/*
Navicat MySQL Data Transfer

Source Server         : 湖南高速手机ETC
Source Server Version : 50547
Source Host           : 10.180.29.19:3306
Source Database       : gongyanyong

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-06-14 17:36:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(100) NOT NULL COMMENT '权限名称',
  `permission_value` varchar(100) NOT NULL COMMENT '权限值',
  `is_available` tinyint(3) unsigned NOT NULL COMMENT '是否可用：0-不可用，1-可用',
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_name` (`permission_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='权限表';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_description` varchar(100) NOT NULL COMMENT '角色描述',
  `is_available` tinyint(3) unsigned NOT NULL COMMENT '是否可用：0-不可用，1-可用',
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_name` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='角色表';

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色id',
  `permission_id` bigint(20) unsigned NOT NULL COMMENT '权限id',
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='角色权限关系表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `salt` varchar(50) NOT NULL COMMENT '盐',
  `is_locked` tinyint(3) unsigned NOT NULL COMMENT '是否锁定：0-锁定，1-正常',
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1 COMMENT='用户表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色id',
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6006 DEFAULT CHARSET=latin1 COMMENT='用户角色关系表';
