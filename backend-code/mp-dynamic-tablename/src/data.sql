create database if not exists mp_dynamic_tablename_test;

use mp_dynamic_tablename_test;

-- 学科表
drop table if exists subject;
create table subject(id int primary key , name varchar(64));
insert into subject(id, name) values (1, 'Math'), (2, 'English'), (3, 'Chinese');

-- 学生表
drop table if exists student;
create table student (id int primary key , name varchar(64));
insert into student(id, name) values(1, 'aaa'), (2, 'bbb');

-- 成绩表(学生-学科 多对多)
drop table if exists score;
create table score(id int primary key , student_id int, subject_id int, result int);
insert into score(id, student_id, subject_id, result) values (1, 1, 1, 74), (2, 1, 2, 83), (3, 1, 3, 69),
                                                             (4, 2, 1, 91), (5, 2, 3, 87);

-- 指定前缀 ustc 的表
-- 学生表
drop table if exists ustc_student;
create table ustc_student (id int primary key , name varchar(64));
insert into ustc_student(id, name) values(1, 'u_aaa'), (2, 'u_bbb');

-- 成绩表(学生-学科 多对多)
drop table if exists ustc_score;
create table ustc_score(id int primary key , student_id int, subject_id int, result int);
insert into ustc_score(id, student_id, subject_id, result) values (1, 1, 1, 89), (2, 1, 2, 81), (3, 1, 3, 32),
                                                                  (4, 2, 1, 71), (5, 2, 2, 77);

-- 指定前缀 zju 的表
-- 学生表
drop table if exists zju_student;
create table zju_student (id int primary key , name varchar(64));
insert into zju_student(id, name) values(5, 'z_aaa'), (6, 'z_bbb');

-- 成绩表(学生-学科 多对多)
drop table if exists zju_score;
create table zju_score(id int primary key , student_id int, subject_id int, result int);
insert into zju_score(id, student_id, subject_id, result) values (1, 5, 1, 91), (2, 5, 2, 66), (3, 5, 3, 85),
                                                                 (4, 6, 1, 48), (5, 6, 2, 59);


