drop table if exists `poem`.`poem`;

CREATE TABLE `poem`.`poem` (
   `id` varchar(36) NOT NULL COMMENT 'UUID',
   `poem_name` varchar(50) DEFAULT NULL COMMENT '诗名',
   `author` varchar(20) DEFAULT NULL COMMENT '作者',
   `poem_type` varchar(20) DEFAULT NULL COMMENT '类型',
   `poem_source` varchar(20) DEFAULT '网络' COMMENT '来源',
   `poem_content` varchar(1000) DEFAULT NULL COMMENT '内容',
   `author_des` text COMMENT '作者简介',
   `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   `create_time` datetime DEFAULT NULL COMMENT '修改时间',
   PRIMARY KEY (`id`)) 
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
