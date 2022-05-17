create or replace trigger trgAfterCreateUser 
after insert 
on master_table
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into user_details(u_id) values (user_id);
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Created User',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterUpdateMPass 
after update
on master_table
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Master Password Changed',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterUpdateProfile
after update
on user_details
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Profile Updated',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterInsertMobile
after insert
on user_mobile
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Mobile No. Inserted',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterDeleteMobile
after delete
on user_mobile
referencing old as old
for each row
declare
user_id number(10);
begin
    user_id := :old.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Mobile No. Deleted',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterInsertInsta
after insert
on Instagram
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'New Instagram Inserted',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterUpdateInsta
after update
on Instagram
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Instagram Updated',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterInsertGit
after insert
on Github
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'New Github Inserted',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterUpdateGit
after update
on Github
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Github Updated',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterInsertGmail
after insert
on Gmail
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'New Gmail Inserted',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterUpdateGmail
after update
on Gmail
referencing new as new
for each row
declare
user_id number(10);
begin
    user_id := :new.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Gmail Updated',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterDeleteGmail
after delete
on Gmail
referencing old as old
for each row
declare
user_id number(10);
begin
    user_id := :old.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Gmail Deleted',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterDeleteGit
after delete
on Github
referencing old as old
for each row
declare
user_id number(10);
begin
    user_id := :old.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Github Deleted',CURRENT_TIMESTAMP);
end;

create or replace trigger trgAfterDeleteInsta
after delete
on Instagram
referencing old as old
for each row
declare
user_id number(10);
begin
    user_id := :old.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'Instagram Deleted',CURRENT_TIMESTAMP);
end;


create or replace trigger trgAfterDeleteUser
after delete
on Master_table
referencing old as old
for each row
declare
user_id number(10);
begin
    user_id := :old.u_id;
   insert into logs values(LOG_TABLE_SEQ.NEXTVAL,user_id,'User Account Deleted',CURRENT_TIMESTAMP);
end;
