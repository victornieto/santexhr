

alter table company drop column exam_path;
alter table company drop column admin_path;

alter table response add column lines_per_hour double;
alter table response add column words_per_minute double;

update response set lines_per_hour=((line_count) / ( ( (total_time / 1000) / 60) / 60)) where total_time > 0;
update response set words_per_minute = ((word_count) / ( ( total_time / 1000) / 60)) where total_time > 0;

update response set lines_per_hour = 0 where lines_per_hour is null;
update response set words_per_minute = 0 where words_per_minute is null;

alter table response modify column lines_per_hour double not null;
alter table response modify column words_per_minute double not null;

alter table response add browser_type varchar(255) NOT NULL default 'unknown';
alter table response add browser_version varchar(255) not null default 'unknown';




create view v_question_candidate_stats as
select qar.question,qar.response,
        s1.id as sitting_id,
        c1.id as candidate_id,
        case c1.status
          when 'HIRED' then 'HIRED'
          when 'REJECTED' then 'REJECTED'
          when 'CANDIDATE_REJECTED' then 'REJECTED'
          when 'RESUME_REJECTED' then 'REJECTED'
          when 'FUTURE_CANDIDATE' then 'FUTURE'
          when 'BENCHMARKED' then 'BENCHMARKED'
          when 'INCOMPLETE' then 'INCOMPLETE'
          else 'ACTIVE'
        end   as status,
        r1.away_time,r1.cut_copy,r1.erase_chars,r1.erase_presses,
          r1.focus_changes,r1.focus_time,r1.hesitation_time,
          r1.key_chars,r1.key_presses,r1.line_count,r1.load_timestamp,
          r1.paste_chars,r1.paste_presses,
          r1.reviewing_time,r1.total_time,r1.typing_time,
          r1.word_count,          
        g1.score as score,
        gs1.score as style,
        gs2.score as correctness
from question_and_response as qar 
  join sitting as s1 
    on qar.sitting=s1.id
  join candidate as c1
    on s1.candidate=c1.id
  left join response as r1
    on qar.response=r1.id
  left join grade as g1
    on r1.grade=g1.id
  left join grade_scores as gs1
    on (r1.grade=gs1.grade and gs1.mapkey='form')
  left join grade_scores as gs2
    on (r1.grade=gs2.grade and gs2.mapkey='function');
    


create table temp_sitting as
select s.next_question_index,s.score,s.exam,s.candidate,v.*
from sitting as s right join 
 ( 
 select sitting_id as id,
 ifnull(sum(away_time),0) as away_time, 
 ifnull(sum(erase_chars),0)  as erase_chars,
 ifnull(sum(erase_presses),0)  as erase_presses,
 ifnull(sum(focus_changes),0)  as focus_changes,
 ifnull(sum(focus_time),0)  as focus_time,
 ifnull(sum(hesitation_time),0)  as hesitation_time,
 ifnull(sum(key_chars),0)  as key_chars,
 ifnull(sum(key_presses),0)  as key_presses,
 ifnull(sum(line_count),0)  as line_count,
 ifnull(((sum(line_count)) / ( ( (sum(total_time) / 1000) / 60) / 60)),0)  as lines_per_hour,
 ifnull(min(load_timestamp),0)  as load_timestamp,
 ifnull(sum(paste_chars),0)  as paste_chars,
 ifnull(sum(paste_presses),0)  as paste_presses,
 ifnull(sum(reviewing_time),0)  as reviewing_time,
 ifnull(sum(total_time),0)  as total_time,
 ifnull(sum(typing_time),0)  as typing_time,
 ifnull(sum(word_count),0)  as word_count,
      ifnull(((sum(word_count)) / ( ( (sum(total_time) / 1000) / 60) ) ),0)  as words_per_minute
from v_question_candidate_stats group by sitting_id
 ) as v on s.id=v.id;

drop table sitting;

create table sitting (
        id bigint not null,
        next_question_index integer not null,
        away_time bigint default 0 not null,
        erase_chars bigint default 0 not null,
        erase_presses bigint default 0 not null,
        focus_changes bigint default 0 not null,
        focus_time bigint default 0 not null,
        hesitation_time bigint default 0 not null,
        key_chars bigint default 0 not null,
        key_presses bigint default 0 not null,
        line_count bigint default 0 not null,
        lines_per_hour double default 0 not null,
        load_timestamp bigint not null,
        paste_chars bigint default 0 not null,
        paste_presses bigint default 0 not null,
        reviewing_time bigint default 0 not null,
        total_time bigint default 0 not null,
        typing_time bigint default 0 not null,
        word_count bigint default 0 not null,
        words_per_minute double default 0 not null,
        score numeric(19,2) not null,
        candidate bigint not null,
        exam bigint not null,
        primary key (id)
    ) ENGINE=InnoDB;
    
insert into sitting (id,next_question_index,away_time,erase_chars,erase_presses,
         focus_changes,focus_time,hesitation_time,key_chars,
         key_presses,line_count,lines_per_hour,load_timestamp,paste_chars,
         paste_presses,reviewing_time,total_time,typing_time,word_count,
         words_per_minute,score,candidate,exam)
    select id,next_question_index,away_time,erase_chars,erase_presses,
         focus_changes,focus_time,hesitation_time,key_chars,
         key_presses,line_count,lines_per_hour,load_timestamp,paste_chars,
         paste_presses,reviewing_time,total_time,typing_time,word_count,
         words_per_minute,score,candidate,exam from temp_sitting;



alter table sitting add browser_change bit(1) NOT NULL default 0;
alter table sitting add browser_type varchar(255) default NULL;
alter table sitting add browser_version varchar(255) default NULL;

drop table temp_sitting;


