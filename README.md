# miaosha
慕课  秒杀项目学习记录

# 存在的疑问
- redis  key 为什么要用 接口-> 抽象类 -> 实现方法的结构构造prefix
- 为什么登录密码需要2次 MD5加密
- 怎么实现一个自定义校验器 自定义校验器的几个注解分别是什么用途
- 全局异常处理器的作用
- 分布式 session 实现与作用
- argumentreslove 的实现与作用 webmvcargumentresloves过时  实现webmvcconfig接口
- 遇到的坑 数据库连接驱动8.0版本要指定  DataSource.url里面的时区  连接地址添加 serverTimezone=Asia/Shanghai

# 用户表结构
```sql
CREATE TABLE `miaosha_user` (
`id`  bigint NOT NULL COMMENT '手机号码' ,
`nickname`  varchar(255) NOT NULL COMMENT '登录名' ,
`password`  varchar(32) NOT NULL COMMENT 'md5(md5(pass+固定salt)+salt)' ,
`salt`  varchar(10) NOT NULL COMMENT '盐值' ,
`head`  varchar(128) NOT NULL COMMENT '头像' ,
`register_date`  datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '注册时间' ,
`last_login_date`  datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '上次登录时间' ,
`login_count`  int(11) NOT NULL DEFAULT 0 COMMENT '登录次数' ,
PRIMARY KEY (`id`)
)
;

```

# 商品表结构
```sql
CREATE TABLE `goods`(
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
	`goods_name` VARCHAR(16) DEFAULT NULL COMMENT '商品名称',
	`goods_title` VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
	`goods_img` VARCHAR(64) DEFAULT NULL COMMENT '商品图片',
	`goods_detail` LONGTEXT COMMENT '商品的详情介绍',
	`goods_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品单价',
	`goods_stock` INT(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
	PRIMARY KEY (`id`)
);
```
秒杀商品表
```sql
CREATE TABLE `miaosha_goods`(
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀商品ID',
	`goods_id` BIGINT(16) DEFAULT NULL COMMENT '商品id',
	`miaosha_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '秒杀价',
	`stock_count` INT(11) DEFAULT '0' COMMENT '库存数量',
	`start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
	`end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
	PRIMARY KEY (`id`)
);
```
订单信息表
```sql
CREATE TABLE `order_info`(
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'order ID',
	`user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
	`goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品id',
	`delivery_addr_id` BIGINT(20) DEFAULT NULL COMMENT '收货地址',
	`goods_name` VARCHAR(16) DEFAULT NULL COMMENT '商品名称',
	`goods_count` INT(11) DEFAULT '0' COMMENT '商品数量',
	`goods_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品单价',
	`order_channel` TINYINT(4) DEFAULT '0' COMMENT '1pc,2android,3ios',
	`status` TINYINT(4) DEFAULT '0' COMMENT '0新建未支付，2已支付，3已发货4，已收货，5已完成',
	`create_date` datetime DEFAULT NULL COMMENT '订单创建时间',
	`pay_date` datetime DEFAULT NULL COMMENT '支付时间',
	PRIMARY KEY (`id`)
);
```
秒杀订单表
```sql
CREATE TABLE `miaosha_order`(
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀 order ID',
	`user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
	`order_id` BIGINT(20) DEFAULT NULL COMMENT '订单id',
	`goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品id',
	PRIMARY KEY (`id`)
);
```
