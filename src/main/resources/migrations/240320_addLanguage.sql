alter table users add column language tinyint(1) default null;
update users set language = 0 where 1=1;