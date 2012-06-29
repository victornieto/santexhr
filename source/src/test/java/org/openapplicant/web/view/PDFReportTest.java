package org.openapplicant.web.view;

import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.createNiceMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openapplicant.dao.ICandidateDAO;
import org.openapplicant.dao.ICandidateWorkFlowEventDAO;
import org.openapplicant.dao.ISessionFacade;
import org.openapplicant.dao.ISittingDAO;
import org.openapplicant.domain.Candidate;
import org.openapplicant.domain.CandidateBuilder;
import org.openapplicant.domain.Name;
import org.openapplicant.domain.Profile;
import org.openapplicant.domain.ProfileBuilder;
import org.openapplicant.domain.Sitting;
import org.openapplicant.domain.User;
import org.openapplicant.domain.UserBuilder;
import org.openapplicant.domain.email.AutoInviteEmailTemplateBuilder;
import org.openapplicant.domain.email.EmailTemplate;
import org.openapplicant.domain.event.CandidateWorkFlowEvent;
import org.openapplicant.security.ICurrentUserService;
import org.openapplicant.service.AdminService;
import org.openapplicant.util.TestUtils;
import org.openapplicant.web.view.PDFReport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.validation.BeanPropertyBindingResult;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

@ContextConfiguration(locations="/applicationContext-test.xml")
public class PDFReportTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	private static PDFReport report;
	private Document packet;
	private Candidate candidate;
	private List<CandidateWorkFlowEvent> events;
	private PdfWriter writer;
	private Sitting sitting;
	
	@Resource
	private ICandidateDAO candidateDao;
	
	@Resource
	private ICandidateWorkFlowEventDAO eventDao;
	
	@Resource
	private ISittingDAO sittingDao;
	
	@BeforeClass
	public static void classSetUp() {
		report = new PDFReport();
	}
		
	//@Before
	public void setUp() {
		candidate = candidateDao.find(new Long(199));
		events = eventDao.findAllByCandidateId(candidate.getId());
		sitting = sittingDao.find(new Long(206));
	}
	
	@Test
	public void testHello() {
		logger.debug("Hello, world!");
		// do nothing!
	}
	
	@Ignore
	public void testAllHistoryEvents() throws DocumentException, FileNotFoundException {
		packet = new Document(PageSize.LETTER);
		writer = PdfWriter.getInstance(packet, new FileOutputStream("/tmp/testAllHistoryEvents.pdf"));
		packet.open();
		report.addCandidateProfile(packet, candidate, events);
		packet.close();

	}
	
	@Ignore
	public void testExamResults() throws FileNotFoundException, DocumentException {
		packet = new Document(PageSize.LETTER);
		writer = PdfWriter.getInstance(packet, new FileOutputStream("/tmp/testExamResults.pdf"));
		packet.open();
		report.addExamResults(packet, sitting);
		packet.close();
	}
	
}
