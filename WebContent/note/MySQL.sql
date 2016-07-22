/*新建表语句*/
create table t_day(id int PRIMARY key auto_increment, days datetime not null);

/*去除前后的空格*/
update t_crawl set url = TRIM(url);
/*创建索引*/
create index t_blog_content on t_blog(content);
/*可是上面创建索引报错：[Err] 1170 - BLOB/TEXT column 'content' used in key specification without a key length*/

/*查看event是否开启*/
show variables like '%sche%'; 

/*将event计划开启*/
set global event_scheduler =1;
/*或*/
SET GLOBAL event_scheduler = ON;

/*建立定时任务(每天定时更新时间表)*/
CREATE EVENT e_day
ON SCHEDULE EVERY 1 DAY 
DO INSERT INTO t_day(days) VALUES (NOW());