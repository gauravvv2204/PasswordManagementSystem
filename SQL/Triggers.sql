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
end;