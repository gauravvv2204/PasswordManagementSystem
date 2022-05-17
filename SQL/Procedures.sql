
create or replace procedure add_mno(uid number,mno number) as
begin
insert into user_mobile values(uid,mno);
exception 
when dup_val_on_index then 
dbms_output.put_line('Number already exists for the same user');
end;

create or replace procedure del_mno(uid number,mno number) as
begin
delete from user_mobile where u_id=uid and mobile_no=mno;
end;

create or replace PROCEDURE isPresent
(cEmail in varchar2,
 out_result OUT VARCHAR2)
AS
checkUid number(10);
BEGIN

  select u_id into checkUid from master_table where email=cEmail;
  out_result := 'TRUE';

EXCEPTION
  WHEN no_data_found THEN 
  out_result := 'FALSE';
END;

create or replace procedure update_address(uid number,Hno varchar2, Locality varchar2, City_of_residence varchar2, State varchar2, country_of_residence varchar2) as
begin
update user_details set HOUSE_NO=Hno,AREA=locality,city=city_of_residence,state_of_residence=State,country=country_of_residence  where u_id=uid;
end;

create or replace procedure update_dob(uid number,recieved_dob varchar2) as
begin
update user_details set dob=to_date(recieved_dob) where u_id=uid;
end;

create or replace procedure update_gender(uid number,preffered_gender char) as
begin
update user_details set gender=preffered_gender where u_id=uid;
end;

create or replace procedure update_mpassword(given_email varchar,new_password varchar,out_result out varchar) as
invalid_password exception;
begin
if not password_check(new_password) then
raise invalid_password;
end if;
update master_table set m_password = sha256.encrypt(new_password) where email=given_email;
out_result:='TRUE';
exception
when invalid_password then
out_result:= 'FALSE';
end;

create or replace procedure update_name(uid number,fname varchar2,lname varchar2) as
begin
update user_details set first_name=fname,last_name=lname where u_id = uid;
end;

create or replace procedure getMobileNo(uid in number,out_cursor_user_mobile out sys_refcursor)
as
begin
open out_cursor_user_mobile 
for select mobile_no from user_mobile where u_id = uid;
end;

create or replace procedure insert_rsa(ema varchar2,e varchar2, d varchar2, n varchar2) as
uid number(10);
begin
select u_id into uid from master_table where email=ema;
insert into rsa_keys values(uid,e,d,n);
end;

create or replace procedure insert_insta(uid number,uname varchar,epass varchar) as
begin
insert into instagram values(uid,uname,epass);
exception
when dup_val_on_index then
update instagram set password=epass where u_id=uid and username=uname;
end;

create or replace procedure insert_github(uid number,uname varchar,epass varchar) as
begin
insert into github values(uid,uname,epass);
exception
when dup_val_on_index then
update github set password=epass where u_id=uid and username=uname;
end;

create or replace procedure insert_gmail(uid number,uname varchar,epass varchar) as
begin
insert into gmail values(uid,uname,epass);
exception
when dup_val_on_index then
update gmail set password=epass where u_id=uid and username=uname;
end;

create or replace procedure get_rsa_keys(uid number,pubk out varchar,prik out varchar,modu out varchar) as
begin
select public_key,private_key,modulo into pubk,prik,modu from rsa_keys where u_id = uid;
end;

create or replace procedure getGithubAccounts(uid in number,out_cursor_github out sys_refcursor)
as
begin
open out_cursor_github 
for select username,password from github where u_id = uid;
end;

create or replace procedure getGmailAccounts(uid in number,out_cursor_gmail out sys_refcursor)
as
begin
open out_cursor_gmail 
for select username,password from gmail where u_id = uid;
end;

create or replace procedure getInstaAccounts(uid in number,out_cursor_instagram out sys_refcursor)
as
begin
open out_cursor_instagram 
for select username,password from instagram where u_id = uid;
end;

create or replace procedure logout_user(uid number)
as
begin
insert into logs values(LOG_TABLE_SEQ.NEXTVAL,uid,'User LogOut',CURRENT_TIMESTAMP);
end;

create or replace procedure out_logs(n in number,out_cursor_out_logs out sys_refcursor)
as
begin
open out_cursor_out_logs 
for select * from logs order by log_id desc fetch first n rows  only;
end;

create or replace procedure search_by_uid(uid in number,out_cursor_out_logs out sys_refcursor)
as
begin
open out_cursor_out_logs 
for select * from logs where u_id = uid order by log_id desc fetch first 10 rows  only;
end;

create or replace procedure delete_github(uid in number, user_n in varchar2)
as 
begin
delete from github where u_id=uid and username=user_n;
end;

create or replace procedure delete_gmail(uid in number, user_n in varchar2)
as 
begin
delete from gmail where u_id=uid and username=user_n;
end;

create or replace procedure delete_insta(uid in number, user_n in varchar2)
as 
begin
delete from instagram where u_id=uid and username=user_n;
end;