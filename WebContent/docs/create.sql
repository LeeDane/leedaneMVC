
-- ----------------------------
-- Table structure for t_financial
-- ----------------------------
DROP TABLE IF EXISTS `t_financial`;
CREATE TABLE `t_financial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `local_id` int(11) COMMENT '客户端本地存储的ID',
  `imei` varchar(20) COMMENT '客户端唯一标记imei码',
  `status` int(11) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  `addition_time` datetime NOT NULL   '很重要，添加时间，用于今后的统计时间，必须',
  `create_user_id` int(11) DEFAULT NULL,
  `modify_user_id` int(11) DEFAULT NULL,
  `model` int(11) DEFAULT '0' COMMENT '模块，1:收入；2：支出',
  `money` float NOT NULL DEFAULT '0.00' COMMENT '相关金额',
  `one_level` varchar(20) DEFAULT NULL COMMENT '一级分类',
  `two_level` varchar(20) DEFAULT NULL COMMENT '二级分类',
  `has_img` bit(1) DEFAULT b'0' COMMENT '是否有图片',
  `path` longtext DEFAULT NULL COMMENT '',
  `latitude` double COMMENT '纬度',
  `location` varchar(255) DEFAULT NULL  COMMENT '位置的展示信息',
  `longitude` double COMMENT '经度',
  `financial_desc` longtext COMMENT '备注信息',
  `add_day` varchar(10) NOT NULL COMMENT '取addition_time前面的10为，格式如2016-09-01',
  PRIMARY KEY (`id`),
  KEY `financial_create_user_id` (`create_user_id`),
  KEY `financial_modify_user_id` (`modify_user_id`),
  CONSTRAINT `FK_financial_create_user_id` FOREIGN KEY (`create_user_id`) REFERENCES `t_user` (`id`),
  CONSTRAINT `FK_financial_modify_user_id` FOREIGN KEY (`modify_user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

/*添加imei码和localId、add_day的唯一性约束，避免用户多次提交*/
alter table t_financial add constraint imei_local_id_unique UNIQUE(imei, local_id, add_day);

/* 在插入数据之前设置add_day列的值*/
create trigger financial_add_day_trigger before INSERT on t_financial
for EACH ROW
BEGIN
 SET NEW.add_day = date_format(NEW.addition_time,'%Y-%m-%d');
end;
