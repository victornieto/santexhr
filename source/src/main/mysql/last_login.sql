create table login_events (
  user bigint,
  login_time timestamp
);

delimiter /

create trigger last_login_log
after update on user
for each row
begin
  if (new.last_login_time != old.last_login_time) then
    insert into login_events (user,login_time) values (new.id,new.last_login_time);
  end if;
end;
/

delimiter ;
