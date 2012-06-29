package org.openapplicant.util;


import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.openapplicant.dao.hibernate.CandidateDAO;
import org.openapplicant.dao.hibernate.CompanyDAO;
import org.openapplicant.dao.hibernate.ExamDAO;
import org.openapplicant.dao.hibernate.FileAttachmentDAO;
import org.openapplicant.dao.hibernate.UserDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.Company;
import org.openapplicant.domain.CoverLetter;
import org.openapplicant.domain.Exam;
import org.openapplicant.domain.ExamBuilder;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.QuestionAndResponse;
import org.openapplicant.domain.Response;
import org.openapplicant.domain.Resume;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.User;
import org.openapplicant.domain.User.Role;
import org.openapplicant.service.ReportService;
import org.openapplicant.util.Pagination;
import org.quartz.utils.Pair;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;


public class CopyCandidates {

	private static Log logger = LogFactory.getLog(CopyCandidates.class);
	
	private static CompanyDAO companyDao;
	private static CandidateDAO candidateDao;
	private static FileAttachmentDAO fileAttachmentDao;
	private static ExamDAO examDao;
	private static UserDAO userDao;
	private static Pagination foo;
	
	public static void main(String args[]) throws IOException {
		foo = Pagination.zeroBased().forPage(0).perPage(1000);

		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("applicationContext-test.xml"));
		
		logger.info("Created the factory.");
		
		companyDao = (CompanyDAO)factory.getBean("companyDao");
		candidateDao = (CandidateDAO)factory.getBean("candidateDao");
		fileAttachmentDao = (FileAttachmentDAO)factory.getBean("fileAttachmentDao");
		examDao = (ExamDAO)factory.getBean("examDao");
		userDao = (UserDAO)factory.getBean("userDao");
		
		
		PlatformTransactionManager txMgr = (PlatformTransactionManager) factory.getBean("transactionManager");
		TransactionTemplate template = new TransactionTemplate(txMgr);
		logger.debug("Main:  about to execute.");
		
		final Long fromCompanyId = Long.parseLong(args[0]);
		final Long toCompanyId = Long.parseLong(args[1]);
		
		template.execute(new TransactionCallback() {
			public Object doInTransaction(TransactionStatus status) {
				try {
					doMain(fromCompanyId,toCompanyId);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		
		}
		public static void doMain(Long fromCompanyId, Long toCompanyId) throws Exception {
		
		Company fromCompany = companyDao.find(fromCompanyId);
		
		Company toCompany = companyDao.find(toCompanyId);
		
		// get the ID map
		Map<Long,Long> idMap = new HashMap<Long, Long>();
		List<Exam> fromExams = null; // examDao.findLatestActiveVersionsByCompanyId(fromCompanyId);
		List<Exam> toExams   = null; // examDao.findLatestActiveVersionsByCompanyId(toCompanyId);
		for(Exam fromExam : fromExams) {
			Exam toExam = null;
			for(int i = 0; i < toExams.size(); i++) {
				if (toExams.get(i).getName().equals(fromExam.getName())) {
					toExam = toExams.get(i);
					break;
				}
			}
			if (toExam != null) {
				logger.info("Mapping exam "+fromExam.getId()+" to "+toExam.getId());
				idMap.put(fromExam.getId(),toExam.getId());
				for(int i = 0; i < fromExam.getQuestions().size(); i++) {
					Long id1 = fromExam.getQuestions().get(i).getId();
					Long id2 = toExam.getQuestions().get(i).getId();
					idMap.put(id1, id2);
					logger.info("Mapping question "+id1+" to question "+id2);
				}
			}
		}
		
		List<User> fromUser = userDao.findAllActiveUsersByCompanyId(fromCompanyId,foo);
		List<User> toUser   = userDao.findAllActiveUsersByCompanyId(toCompanyId, foo);
		for(User user : fromUser) {
			idMap.put(user.getId(),toUser.get(0).getId());
		}
		
		
		
		List<Candidate> candidates = candidateDao.findAllByCompanyId(fromCompanyId, foo);
		
		
		for(Candidate oldCandidate : candidates) {
			Candidate newCandidate = new Candidate();
			newCandidate.setName(oldCandidate.getName());

			
			if (oldCandidate.getResume() != null) {
				Resume resume = new Resume(oldCandidate.getResume().getContent(), oldCandidate.getResume().getFileType());
				newCandidate.setResume(resume);
				fileAttachmentDao.save(resume);
				idMap.put(oldCandidate.getResume().getId(), resume.getId());
				logger.info("Mapped resume "+oldCandidate.getResume().getId()+" to "+resume.getId());
			}
			
			if (oldCandidate.getCoverLetter() != null) {
				CoverLetter coverLetter = new CoverLetter(oldCandidate.getCoverLetter().getContent(), oldCandidate.getCoverLetter().getFileType());
				newCandidate.setCoverLetter(coverLetter);
				fileAttachmentDao.save(coverLetter);
				idMap.put(oldCandidate.getCoverLetter().getId(), coverLetter.getId());
			}
			
			
			newCandidate.setCompany(toCompany);
			newCandidate.setAddress(oldCandidate.getAddress());
			newCandidate.setEmail(oldCandidate.getEmail());
			newCandidate.setCellPhoneNumber(oldCandidate.getCellPhoneNumber());
			newCandidate.setHomePhoneNumber(oldCandidate.getHomePhoneNumber());
			newCandidate.setWorkPhoneNumber(oldCandidate.getWorkPhoneNumber());
			newCandidate.setMatchScore(oldCandidate.getMatchScore());
			newCandidate.setStatus(oldCandidate.getStatus());
			candidateDao.save(newCandidate);
			logger.info("Have saved "+newCandidate.getId());
			idMap.put(oldCandidate.getId(),newCandidate.getId());
		}

		int current_sitting = 0;
		int current_question_and_response = 0;
		int current_response = 0;
		int current_grade = 0;
		int current_grade_score = 0;
		
		int new_sitting = 0;
		int new_question_and_response = 0;
		int new_response = 0;
		int new_grade = 0;
		int new_grade_score = 0;
		
		
		int sitting = 0;
		int question_and_response = 0;
		int response = 0;
		int grade = 0;
		int grade_score = 0;
		
		
		String sql = "select "+
        "s.id as s_id, "+
        "s.next_question_index as s_next_question_index, "+
        "s.away_time as s_away_time, "+
        "s.browser_change as s_browser_change, "+
        "s.browser_type as s_browser_type, "+
        "s.browser_version as s_browser_version, "+
        "s.erase_chars as s_erase_chars, "+
        "s.erase_presses as s_erase_presses, "+
        "s.focus_changes as s_focus_changes, "+
        "s.focus_time as s_focus_time, "+
        "s.hesitation_time as s_hesitation_time, "+
        "s.key_chars as s_key_chars, "+
        "s.key_presses as s_key_presses, "+
        "s.line_count as s_line_count, "+
        "s.lines_per_hour as s_lines_per_hour, "+
        "s.load_timestamp as s_load_timestamp, "+
        "s.paste_chars as s_paste_chars, "+
        "s.paste_presses as s_paste_presses, "+
        "s.reviewing_time as s_reviewing_time, "+
        "s.total_time as s_total_time, "+
        "s.typing_time as s_typing_time, "+
        "s.word_count as s_word_count, "+
        "s.words_per_minute as s_words_per_minute, "+
        "s.score as s_score, "+
        "s.exam as s_exam, "+
        "qar.id as qar_id, "+
        "qar.question as qar_question, "+
        "qar.response as qar_response, "+
        "qar.sitting as qar_sitting, "+
        "qar.ordinal as qar_ordinal, "+
        "r.id as r_id,"+
        "r.away_time as r_away_time,"+
        "r.browser_type as r_browser_type,"+
        "r.browser_version as r_browser_version,"+
        "r.content as r_content,"+
        "r.cut_copy as r_cut_copy,"+
        "r.erase_chars as r_erase_chars,"+
        "r.erase_presses as r_erase_presses,"+
        "r.focus_changes as r_focus_changes,"+
        "r.focus_events as r_focus_events,"+
        "r.focus_time as r_focus_time,"+
        "r.hesitation_time as r_hesitation_time,"+
        "r.key_chars as r_key_chars,"+
        "r.key_presses as r_key_presses,"+
        "r.keypress_events as r_keypress_events,"+
        "r.line_count as r_line_count,"+
        "r.lines_per_hour as r_lines_per_hour,"+
        "r.load_timestamp as r_load_timestamp,"+
        "r.paste_chars as r_paste_chars,"+
        "r.paste_events as r_paste_events,"+
        "r.paste_presses as r_paste_presses,"+
        "r.reviewing_time as r_reviewing_time,"+
        "r.total_time as r_total_time,"+
        "r.typing_time as r_typing_time,"+
        "r.word_count as r_word_count,"+
        "r.words_per_minute as r_words_per_minute,"+
        "r.grade as r_grade,"+
        "g.id as g_id, "+
        "g.score as g_score, "+
        "gs.grade as gs_grade, "+
        "gs.score as gs_score, "+
        "gs.mapkey as gs_mapkey "+
  "from candidate c  "+
  "       left join sitting s on c.id=s.candidate "+
  "       left join question_and_response qar on s.id=qar.sitting "+
  "       left join response r on qar.response=r.id "+
  "       left join grade g on r.grade=g.id "+
  "       left join grade_scores gs on g.id=gs.grade "+
  "where c.id=? "+
  "order by c.id,s.id,r.id,qar.ordinal,g.id";

		String insert_sitting_sql = "insert into sitting ("+
		"id,"+
		"candidate,"+
		"exam,"+
		"next_question_index,"+
		"away_time,"+
		"browser_change,"+
		"browser_type,"+
		"browser_version,"+
		"erase_chars,"+
		"erase_presses,"+
		"focus_changes,"+
		"focus_time,"+
		"hesitation_time,"+
		"key_chars,"+
		"key_presses,"+
		"line_count,"+
		"lines_per_hour,"+
		"load_timestamp,"+
		"paste_chars,"+
		"paste_presses,"+
		"reviewing_time,"+
		"total_time,"+
		"typing_time,"+
		"word_count,"+
		"words_per_minute,"+
		"score"+
			") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		String insert_question_and_response_sql =
			"insert into question_and_response ("+
				"id,"+
				"question,"+
				"response,"+
				"sitting,"+
				"ordinal"
				+") values (?,?,?,?,?)";
		
		String insert_response_sql =
			"insert into response ("+
			"id,"+
			"away_time,"+
			"browser_type,"+
			"browser_version,"+
			"content,"+
			"cut_copy,"+
			"erase_chars,"+
			"erase_presses,"+
			"focus_changes,"+
			"focus_events,"+
			"focus_time,"+
			"grade,"+
			"hesitation_time,"+
			"key_chars,"+
			"key_presses,"+
			"keypress_events,"+
			"line_count,"+
			"lines_per_hour,"+
			"load_timestamp,"+
			"paste_chars,"+
			"paste_events,"+
			"paste_presses,"+
			"reviewing_time,"+
			"total_time,"+
			"typing_time,"+
			"word_count,"+
			"words_per_minute"+
			") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		String insert_grade_sql = 
			"insert into grade ("+
				"id,"+
				"score"+
				") values (?,?)";
		
		String insert_grade_scores_sql =
			"insert into grade_scores ("+
				"grade,"+
				"score,"+
				"mapkey"+
				") values (?,?,?)";
				

		
		int ordinal = 0;
		
		for(Candidate oldCandidate : candidates) {
			try {
				Connection connection = candidateDao.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();
				
				PreparedStatement insert_sitting_stmt = connection.prepareStatement(insert_sitting_sql);
				PreparedStatement insert_question_and_response_stmt = connection.prepareStatement(insert_question_and_response_sql);
				PreparedStatement insert_response_stmt = connection.prepareStatement(insert_response_sql);
				PreparedStatement insert_grade_stmt = connection.prepareStatement(insert_grade_sql);
				PreparedStatement insert_grade_scores_stmt = connection.prepareStatement(insert_grade_scores_sql);
				
				
				LidFetcher fetcher = new LidFetcher(connection);
				
				PreparedStatement query = connection.prepareStatement(sql);
				query.setLong(1, oldCandidate.getId());
				query.execute();
				ResultSet rs = query.getResultSet();
				while(rs.next()) {					
					sitting = rs.getInt("s_id");
					if (sitting == 0) {
						logger.info("Null sitting!  Continuing!");
						continue;
					}
						
					if (sitting != current_sitting) {
						new_sitting = fetcher.fetchLid();
						logger.info("Inserting sitting "+new_sitting);
						logger.info("Exam is "+rs.getLong("s_exam"));
						insert_sitting_stmt.setLong(1, new_sitting);
						insert_sitting_stmt.setLong(2, idMap.get(oldCandidate.getId()));
						insert_sitting_stmt.setLong(3, idMap.get(rs.getLong("s_exam")));
						insert_sitting_stmt.setLong(4, rs.getLong("s_next_question_index"));
						insert_sitting_stmt.setLong(5, rs.getLong("s_away_time"));
						insert_sitting_stmt.setLong(6, rs.getLong("s_browser_change"));
						insert_sitting_stmt.setString(7, rs.getString("s_browser_type"));
						insert_sitting_stmt.setString(8, rs.getString("s_browser_version"));
						insert_sitting_stmt.setLong(9, rs.getLong("s_erase_chars"));
						insert_sitting_stmt.setLong(10, rs.getLong("s_erase_presses"));
						insert_sitting_stmt.setLong(11, rs.getLong("s_focus_changes"));
						insert_sitting_stmt.setLong(12, rs.getLong("s_focus_time"));
						insert_sitting_stmt.setLong(13, rs.getLong("s_hesitation_time"));
						insert_sitting_stmt.setLong(14, rs.getLong("s_key_chars"));
						insert_sitting_stmt.setLong(15, rs.getLong("s_key_presses"));
						insert_sitting_stmt.setLong(16, rs.getLong("s_line_count"));
						insert_sitting_stmt.setFloat(17, rs.getFloat("s_lines_per_hour"));
						insert_sitting_stmt.setLong(18, rs.getLong("s_load_timestamp"));
						insert_sitting_stmt.setLong(19, rs.getLong("s_paste_chars"));
						insert_sitting_stmt.setLong(20, rs.getLong("s_paste_presses"));
						insert_sitting_stmt.setLong(21, rs.getLong("s_reviewing_time"));
						insert_sitting_stmt.setLong(22, rs.getLong("s_total_time"));
						insert_sitting_stmt.setLong(23, rs.getLong("s_typing_time"));
						insert_sitting_stmt.setLong(24, rs.getLong("s_word_count"));
						insert_sitting_stmt.setFloat(25, rs.getFloat("s_words_per_minute"));
						insert_sitting_stmt.setFloat(26, rs.getFloat("s_score"));
						insert_sitting_stmt.execute();
						idMap.put(new Long(sitting),new Long(new_sitting));
						current_sitting = sitting;
						ordinal = 0;
					}
					
					// grade
					grade = rs.getInt("g_id");
					if (grade == 0) 
						continue;
					if ((grade != 0 ) && (grade != current_grade)) {
						new_grade = fetcher.fetchLid();
						logger.info("Inserting new grade "+new_grade);
						insert_grade_stmt.setLong(1,new_grade);
						insert_grade_stmt.setLong(2, rs.getLong("g_score"));
						insert_grade_stmt.execute();
					}
					
					/// response
					response = rs.getInt("r_id");
					if (response == 0)
						continue;
					if (response != current_response) {
						new_response = fetcher.fetchLid();
						logger.info("Inserting response "+new_response);
						insert_response_stmt.setLong(1,new_response);insert_sitting_stmt.setLong(4, rs.getLong("s_next_question_index"));
						insert_response_stmt.setLong(2, rs.getLong("r_away_time"));
						insert_response_stmt.setString(3, rs.getString("r_browser_type"));
						insert_response_stmt.setString(4, rs.getString("r_browser_version"));
						insert_response_stmt.setClob(5,rs.getClob("r_content"));
						insert_response_stmt.setLong(6, rs.getLong("r_cut_copy"));
						insert_response_stmt.setLong(7, rs.getLong("r_erase_chars"));
						insert_response_stmt.setLong(8, rs.getLong("r_erase_presses"));
						insert_response_stmt.setLong(9, rs.getLong("r_focus_changes"));
						insert_response_stmt.setClob(10,rs.getClob("r_focus_events"));
						insert_response_stmt.setLong(11, rs.getLong("r_focus_time"));
						insert_response_stmt.setLong(12, new_grade);
						insert_response_stmt.setLong(13, rs.getLong("r_hesitation_time"));
						insert_response_stmt.setLong(14, rs.getLong("r_key_chars"));
						insert_response_stmt.setLong(15, rs.getLong("r_key_presses"));
						insert_response_stmt.setClob(16,rs.getClob("r_keypress_events"));
						insert_response_stmt.setLong(17, rs.getLong("r_line_count"));
						insert_response_stmt.setFloat(18, rs.getFloat("r_lines_per_hour"));
						insert_response_stmt.setLong(19, rs.getLong("r_load_timestamp"));
						insert_response_stmt.setLong(20, rs.getLong("r_paste_chars"));
						insert_response_stmt.setClob(21,rs.getClob("r_paste_events"));
						insert_response_stmt.setLong(22, rs.getLong("r_paste_presses"));
						insert_response_stmt.setLong(23, rs.getLong("r_reviewing_time"));
						insert_response_stmt.setLong(24, rs.getLong("r_total_time"));
						insert_response_stmt.setLong(25, rs.getLong("r_typing_time"));
						insert_response_stmt.setLong(26, rs.getLong("r_word_count"));
						insert_response_stmt.setFloat(27, rs.getFloat("r_words_per_minute"));
						insert_response_stmt.execute();
						idMap.put(new Long(response), new Long(new_response));
						current_response = response;
					}

					// question_and_response
					question_and_response = rs.getInt("qar_id");
					if (question_and_response != current_question_and_response) {
						new_question_and_response = fetcher.fetchLid();
						logger.info("Inserting new question and response "+new_question_and_response);
						insert_question_and_response_stmt.setLong(1,new_question_and_response);
						insert_question_and_response_stmt.setLong(2,idMap.get(rs.getLong("qar_question")));
						insert_question_and_response_stmt.setLong(3,new_response);
						insert_question_and_response_stmt.setLong(4,new_sitting);
						insert_question_and_response_stmt.setLong(5,ordinal);
						insert_question_and_response_stmt.execute();
						ordinal++;
						
						idMap.put(new Long(question_and_response), new Long(new_question_and_response));
					}
					
					
					
					insert_grade_scores_stmt.setLong(1, new_grade);
					insert_grade_scores_stmt.setFloat(2,  rs.getFloat("gs_score"));
					insert_grade_scores_stmt.setString(3, rs.getString("gs_mapkey"));
					
				}
			

			} catch (Exception e) {
				logger.error("Exception!",e);
			}
			
			
		} // candidate loop
		

		// now history events
		String history_sql = "select e.* from candidate c "+
			"join candidate_work_flow_event e on c.id=e.candidate "+
			"where c.company=?";
		String history_insert_sql = "insert into candidate_work_flow_event ("+
			"type,"+
			"id,"+
			"status,"+
			"sitting_score,"+
			"exam_link,"+
			"user,"+
			"resume,"+
			"cover_letter,"+
			"note,"+
			"sitting,"+
			"candidate"+
			") values (?,?,?,?,?,?,?,?,?,?,?)";
	
		
		try {
		Connection connection = candidateDao.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();

		PreparedStatement history_query = connection.prepareStatement(history_sql);
		PreparedStatement history_insert_stmt = connection.prepareStatement(history_insert_sql);
		LidFetcher fetcher = new LidFetcher(connection);

		history_query.setLong(1, fromCompanyId);
		history_query.execute();
		ResultSet rs2 = history_query.getResultSet();
		while(rs2.next()) {
			logger.info("Processing a "+rs2.getString("type")+" for "+rs2.getLong("candidate"));
			int new_id = fetcher.fetchLid();
			history_insert_stmt.setString(1, rs2.getString("type"));
			history_insert_stmt.setLong(2,new_id);
			history_insert_stmt.setString(3,rs2.getString("status"));
			if (rs2.getFloat("sitting_score") != 0) 
				history_insert_stmt.setFloat(4,rs2.getFloat("sitting_score"));
			else
				history_insert_stmt.setNull(4,java.sql.Types.DECIMAL);

			if (rs2.getLong("exam_link") != 0)
				history_insert_stmt.setLong(5, rs2.getLong("exam_link"));
			else
				history_insert_stmt.setNull(5,java.sql.Types.BIGINT);
			
			if (rs2.getLong("user") != 0)
				history_insert_stmt.setLong(6, idMap.get(rs2.getLong("user")));
			else
				history_insert_stmt.setNull(6,java.sql.Types.BIGINT);
			
			logger.info("Checking on resume "+rs2.getLong("resume"));
			if (rs2.getLong("resume") != 0)
				if (idMap.containsKey(rs2.getLong("resume"))) 
					history_insert_stmt.setLong(7, idMap.get(rs2.getLong("resume")));
				else
					continue;
			else
				history_insert_stmt.setNull(7,java.sql.Types.BIGINT);
			
			logger.info("Checking on cover letter "+rs2.getLong("cover_letter"));
			if (rs2.getLong("cover_letter") != 0)
				if (idMap.containsKey(rs2.getLong("cover_letter")))
					history_insert_stmt.setLong(8, idMap.get(rs2.getLong("cover_letter")));
				else
					continue;
			else
				history_insert_stmt.setNull(8,java.sql.Types.BIGINT);
			
			// note.
			history_insert_stmt.setNull(9,java.sql.Types.BIGINT);
			
			if (rs2.getLong("sitting") != 0)
				if (idMap.containsKey(rs2.getLong("sitting")))
					history_insert_stmt.setLong(10, idMap.get(rs2.getLong("sitting")));
				else
					continue;
			else
				history_insert_stmt.setNull(10,java.sql.Types.BIGINT);
			
			if (rs2.getLong("candidate") != 0)
				history_insert_stmt.setLong(11, idMap.get(rs2.getLong("candidate")));
			else
				history_insert_stmt.setNull(11,java.sql.Types.BIGINT);
			
			history_insert_stmt.execute();
		
		}
		} catch (Exception e) {
			logger.error("exception processing events!",e);
		}
		
	
				
	}
	
		
	static class LidFetcher {
		
		PreparedStatement insert_uuid;
		PreparedStatement fetch_lid;
		
		public LidFetcher(Connection c) throws SQLException {
			insert_uuid = c.prepareStatement("insert into entity_info (guid) values (uuid())");
			fetch_lid   = c.prepareStatement("select last_insert_id()");
		}
		
		public int fetchLid() throws SQLException {
			insert_uuid.execute();
			fetch_lid.execute();
			ResultSet rs = fetch_lid.getResultSet();
			rs.next();
			return rs.getInt(1);
		}
		
	}

	
	
}
