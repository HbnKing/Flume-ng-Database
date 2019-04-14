# Flume-SqlSource
Flume-SqlSource

Flume  是一个强大的日志采集工具  
在大数据 领域使用 非常广泛 本身的架构又非常简单，而且灵活 。
但是本身不支持 一些 数据的源 。这里 做一些 拓展。欢迎提出不足和建议 

## 常见思路 
本身自带 各种工具 入 canal  ogg  等等  
使用数据库的触发器   
使用数据库操作日志  如 binlog    
使用 sql  jdbc 查询 后 修改 目标表  


## 支持数据源 
RDB  常见关系型数据库（mysql  oracle  pg）

| 数据源  | source | sink |版本 |
| ------ | ------ | ------ | ------ |
| RDB| S |  X| 0.0.1 |
| Mongodb| x |  S| 0.0.1 |



## 使用方式 
高级Api（未完）
基于数据库日志级别的扩展 
 
低级Api 同步原理   
1时间戳的增量  
2主键或者某一字段的增量  
3字段修改后的标记同步  

##  如何使用 

```
cd  RDB 
$ mvn package
$ cp RDB-O.O.1.jar $FLUME_HOME/lib

cd MongoDB
....同上

```
其他 Mysql  日志级别 同步还未完成  
需要 alibaba fastjson-1.2.49 及 数据库驱动





## 配置文件样例
RDB 配置文件 [详见](./RDB/src/conf)
Mongodb 配置文件[详见](./MongoDB/src/conf/Sink_conf_demo1)


## 必须配置 

## 常见问题 

## 鸣谢
感谢 大佬们的付出 思想源于[github](https://github.com/keedio/flume-ng-sql-source) 

## 需要帮助  
flume 批量发送 单条发送事务性  及 配置  
解析类型为 Date 和 timestamp  可能存在的风险  
json 插入数据库 

自增字段  的通用性   时间 整型  转为object?

