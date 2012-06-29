create table attachment_indexable (
	attachment_id bigint primary key,
  	string_content longtext
) engine=MyISAM;

create fulltext index idx_attachment_fulltext on attachment_indexable(string_content);


delimiter /


create trigger search_hack
after insert on file_attachment
for each row
  insert into attachment_indexable (attachment_id,string_content) values (NEW.id, NEW.string_content)
    on duplicate key update string_content=NEW.string_content;
/
    
create trigger search_hack_update
after update on file_attachment
for each row
  insert into attachment_indexable (attachment_id,string_content) values (NEW.id, NEW.string_content)
    on duplicate key update string_content=NEW.string_content;
/

delimiter ;
