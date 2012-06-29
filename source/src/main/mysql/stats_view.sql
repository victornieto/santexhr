
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
    