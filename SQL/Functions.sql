create or replace function password_check
(password in varchar2) 
return boolean
as
m number(3);
digitarray varchar2(10):= '0123456789';
chararray varchar2(52):= 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'; 
punctarray varchar2(40):='@!"#$%&()''*+,-/:;<=>?_';
begin
IF length(password) < 8 THEN 
   return false;
END IF;
m := length(password);
FOR i IN 1..10 LOOP  
  FOR j IN 1..m LOOP  
    IF substr(password,j,1) = substr(digitarray,i,1) THEN 
        GOTO findchar; 
    END IF; 
   END LOOP; 
END LOOP;
return false;


<<findchar>> 
FOR i IN 1..length(chararray) LOOP  
  FOR j IN 1..m LOOP  
    IF substr(password,j,1) = substr(chararray,i,1) THEN  
         GOTO findpunct; 
       END IF; 
    END LOOP; 
END LOOP;
return false;

<<findpunct>>
FOR i IN 1..length(punctarray) LOOP  
  FOR j IN 1..m LOOP  
    IF substr(password,j,1) = substr(punctarray,i,1) THEN  
         return true; 
       END IF; 
   END LOOP; 
END LOOP;
return false;
end;


create or replace function login
(entered_Email in varchar2,entered_Password in varchar2)
return master_table.u_id%type
as
id master_table.u_id%type;
begin
select u_id into id from master_table where email  = lower(entered_Email) and m_password = sha256.encrypt(entered_Password);
return id;
exception
when no_data_found then
return -1;
end;

create or replace function insert_user(email in varchar2,password in varchar2) 
return number
as
begin
insert into master_table values(master_table_seq.NEXTVAL,email,password);
return 1;
exception
when dup_val_on_index then
return -1;
end;

create or replace function email_check
(email in varchar2)
return boolean is
b_isvalid   BOOLEAN;
begin
b_isvalid :=
      REGEXP_LIKE (email,
                   '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$');
      return b_isvalid;
end;

create or replace function create_user(email in varchar2,password in varchar2) 
return number 
as
invalid_email exception;
invalid_pass exception;
status number(1);
begin
if not email_check(lower(email)) then
raise invalid_email;
end if;
if not password_check(password) then
raise invalid_pass;
end if;
status := insert_user(lower(email),sha256.encrypt(password));
if status = 1 then
return 1;
else
return -1;
end if;
exception
when invalid_email then
return -2;
when invalid_pass then
return -3;
end;

create or replace function delete_user(uid number,pass varchar2)
return number
as
password master_table.m_password%type;
begin
select m_password into password from master_table where u_id = uid;
if sha256.encrypt(pass) = password then
delete from master_table where u_id = uid;
return 1;
else
return -1;
end if;
end;