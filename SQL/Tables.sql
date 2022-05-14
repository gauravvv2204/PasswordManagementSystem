
create table Master_table(
U_ID Number(10)  constraint mt_uid_pk primary key ,
Email varchar2(30) constraint mt_email_unq unique constraint mt_email_nn not null,
M_Password varchar2(40) constraint mt_pwd_nn not null);

create table user_details(u_id number(10) constraint ud_uid_pk primary key constraint ud_uid_fk references master_table(u_id),
first_name varchar2(25),last_name varchar2(25),
DOB date,
gender char(1),
house_no varchar2(10),
Area varchar2(100),
city varchar2(20),
state_of_residence varchar2(20),
country varchar2(20));

create table user_mobile(
u_id number(10) constraint um_uid_fk references master_table(u_id),
mobile_no number(12),
constraint um_pk primary key(u_id,mobile_no));

create table RSA_keys(
u_id number(10) constraint rsa_uid_pk primary key constraint rsa_uid_fk references master_table(u_id),
public_key varchar2(2000),
private_key varchar2(2000),
modulo varchar2(2000));

create table Instagram(u_id number(10) constraint insta_uid_fk references master_table(u_id),username varchar2(50),password varchar2(2000), constraint insta_pk primary key(u_id,username));

create table Gmail(u_id number(10) constraint gmail_uid_fk references master_table(u_id),username varchar2(50),password varchar2(2000), constraint gmail_pk primary key(u_id,username));

create table Github(u_id number(10) constraint github_uid_fk references master_table(u_id),username varchar2(50),password varchar2(2000), constraint github_pk primary key(u_id,username));