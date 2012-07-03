package org.openapplicant.web.view;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.codec.PngImage;
import com.lowagie.tools.concat_pdf;
import org.apache.commons.lang.time.DateFormatUtils;
import org.openapplicant.domain.*;
import org.openapplicant.domain.event.*;
import org.openapplicant.domain.question.Question;
import org.openapplicant.util.CalendarUtils;
import org.openapplicant.util.Strings;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class PDFReport extends AbstractPdfView {
	


	private static final Font courierFont = new Font(Font.NORMAL);
	private static final Font arialFont = new Font(Font.BOLD);
	private static final Font headerFont = new Font(Font.BOLD,24);

	private String formatDate(Calendar cal) {
		//TimeZone timezone = (TimeZone)Config.get(pageContext.getSession(), Config.FMT_TIME_ZONE);
		TimeZone timezone = TimeZone.getDefault();
		return DateFormatUtils.format(cal.getTime(),"MM/dd/yyyy h:mm a", timezone);
	}
	
	private class EventVisitor implements ICandidateWorkFlowEventVisitor {

		private final Document doc;

		public Image getImage(String filename) {
			String fullname = "";
			Image returnValue;
			try {
				fullname = "/img/history_icons/"+filename+".png";
				String realPath = getServletContext().getRealPath(fullname);
				returnValue = null;
				File file = new File(realPath);				
				InputStream inputStream = new FileInputStream(file);
				returnValue = PngImage.getImage(inputStream);
			} catch (Exception e) {
				logger.error("Caught exception opening png image " + filename
						+ "(" + fullname + ")", e);
				return null;
			}
			return returnValue;
		}
		
	
			
		public EventVisitor(Document doc) {
			this.doc = doc;
		}
		
		
		
		private void formatter(String image, String text, CandidateWorkFlowEvent event) {
			Paragraph paragraph1 = new Paragraph();
			paragraph1.setSpacingBefore(10);
			Image imageObject = getImage(image);
			if (imageObject != null)
				paragraph1.add(new Chunk(imageObject, 0, -8)  );
			paragraph1.add(new Chunk("   "+formatDate(event.getEntityInfo().getCreatedDate()) + " "));
			paragraph1.add(new Chunk(text));
			paragraph1.setSpacingAfter(10);

			try {
				doc.add(paragraph1);
			} catch (Exception e) {
				logger.error("Exception adding paragraph in formatter", e);
			}			
		}

		public void visit(AddNoteToCandidateEvent event) {
			String bodyText = event.getNote().getAuthor().getName().getFullName() + ": added note";
			formatter("note",bodyText,event);
			Paragraph paragraph2 = new Paragraph();
			paragraph2.setIndentationLeft(26);
			paragraph2.add(new Chunk(event.getNote().getBody()));
			try {
				doc.add(paragraph2);
			} catch (Exception e) {
				logger.error("Exception adding note body",e);
			}
		}
		

		public void visit(UserAttachedResumeEvent event) {
			String bodyText = event.getUser().getName().getFullName() + ": Uploaded resume";
			formatter("resume",bodyText,event);
		}

		public void visit(UserAttachedCoverLetterEvent event) {
			String bodyText = event.getUser().getName().getFullName() + ": Uploaded cover letter";
			formatter("coverletter",bodyText,event);
		}

		public void visit(CandidateCreatedEvent event) {
			String bodyText = "Santex HR: Created candidate from received email";
			formatter("candidate_added",bodyText,event);

		}

		public void visit(CandidateCreatedByUserEvent event) {
			String bodyText = event.getUser().getName().getFullName() + ": Created candidate";
			formatter("candidate_added",bodyText,event);
		}

		public void visit(CandidateStatusChangedEvent event) {
			String verb;
			String image;
			if (event.getStatus().isArchived()) {
				verb = "Archived Candidate";
				image = "archived";
			} else {
				verb = "Activated Candidate";
				image = "activated";
			}	
			String bodyText = event.getUser().getName().getFullName() + ": "+ verb + ": " + Strings.humanize(event.getStatus().toString());
			formatter(image,bodyText,event);
		}

		public void visit(CreateExamLinkForCandidateEvent event) {
			// TODO we're moving away from these 
		}

		public void visit(SittingGradedEvent event) {
			String bodyText =event.getUser().getName().getFullName()
				+ ": "
				+ "Graded Exam: "+event.getSitting().getExam()
				+ ". "
				+ "Score: "+event.getSitting().getScore().getValue().intValue();
			formatter("exam",bodyText,event);
		}

		public void visit(SittingCreatedEvent event) {
			formatter(
					"exam",
					"Candidate started exam: "+event.getSitting().getExam().getName() + " at " + formatDate(event.getEntityInfo().getCreatedDate()),
					event
			);
		}
		
		public void visit(SittingCompletedEvent event) {
			formatter(
					"exam",
					"Candidate completed exam: "+event.getSitting().getExam().getName() + " at " + formatDate(event.getEntityInfo().getCreatedDate()),
					event
			);
		}

		public void visit(FacilitatorReceivedEmailEvent event) {
			String bodyText = "Santex HR: Received e-mail from "+event.getCandidate().getEmail();
			formatter("fac_rec_email",bodyText,event);
		}

		public void visit(FacilitatorRejectedResumeEvent event) {
			String bodyText = "Santex HR: Sent rejection due to low screening score.";
			formatter("fac_reject_resume",bodyText,event);
		}

		public void visit(FacilitatorRequestedResumeEvent event) {
			String bodyText = "Santex HR: Sent request for resume to "+event.getCandidate().getEmail();
			formatter("fac_rec_resume",bodyText,event);
		}

		public void visit(FacilitatorSentExamLinkEvent event) {
			String bodyText = "Santex HR: Emailed invitation for "+event.getExamLink().getName()+" to: "+event.getCandidate().getEmail();
			formatter("fac_sent_exam_link",bodyText,event);
		}

		public void visit(UserSentExamLinkEvent event) {
			String bodyText = event.getUser().getName().getFullName() 
				+": Emailed invitation for "+event.getExamLink().getName();
			formatter("email",bodyText,event);
		}
	
	};

	private class HeaderFooterEvents extends PdfPageEventHelper {
		private final String logoFilename;
		
		public HeaderFooterEvents(String logoFilename) {
			this.logoFilename = logoFilename;
		}
		
		public void onEndPage(PdfWriter writer, Document document) {
			try {
				PdfContentByte cb = writer.getDirectContent();
				
				FileInputStream logoStream = new FileInputStream(logoFilename);
				Image logo = PngImage.getImage(logoStream);
//				logo.setAlignment(Image.ALIGN_CENTER);
//				logo.scalePercent(20);
				cb.addImage(logo,51,0,0,19.2f, document.right()-70 , document.top() - 20);

				cb.beginText();
				cb.setTextMatrix(280, document.bottom());
				cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI, BaseFont.NOT_EMBEDDED), 10);
				cb.showText("Page "+writer.getCurrentPageNumber());
				cb.endText();
			} catch (Exception e) {
				logger.error("Error writing footer",e);
			}
		}
	}
	
	void addCoverLetter(Document doc, Candidate candidate)
			throws DocumentException {
		logger.debug("adding cover letter.");
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Chunk("Cover Letter",headerFont));
		paragraph.setSpacingAfter(10);
		doc.add(paragraph);
		paragraph = new Paragraph();
		CoverLetter letter = candidate.getCoverLetter();
		paragraph.add(new Chunk(letter.getStringContent()));
		doc.add(paragraph);
	}

	protected void addCandidateProfile(Document doc, Candidate candidate,
			List<CandidateWorkFlowEvent> events) throws DocumentException {
		logger.debug("Adding Candidate Profile for "
				+ candidate.getName().getFullName());
		Paragraph paragraph = new Paragraph();
		paragraph.add(new Chunk("Candidate Profile",headerFont));
		doc.add(paragraph);
		paragraph = new Paragraph();
		paragraph.add(new Chunk("Name: "+candidate.getName().getFullName(), arialFont));
		doc.add(paragraph);
		paragraph = new Paragraph();
		paragraph.add(new Chunk("Email address: " + candidate.getEmail()));
		doc.add(paragraph);
		Map<String, PhoneNumber> phones = new HashMap<String, PhoneNumber>(3);
		if (!candidate.getHomePhoneNumber().isBlank())
			phones.put("home", candidate.getHomePhoneNumber());
		if (!candidate.getCellPhoneNumber().isBlank())
			phones.put("cell", candidate.getCellPhoneNumber());
		if (!candidate.getWorkPhoneNumber().isBlank())
			phones.put("work", candidate.getWorkPhoneNumber());
		if (phones.size() > 0) {
			paragraph = new Paragraph();
			paragraph.add(new Chunk("Phone Numbers: "));
			doc.add(paragraph);
			for (String kind : phones.keySet()) {
				doc.add(new Paragraph(new Chunk(" - " + kind + ":"
						+ phones.get(kind).getNumber())));
			}
		}
		if (events.size() > 0) {
			doc.add(new Paragraph("History"));
			EventVisitor visitor = new EventVisitor(doc);
			for (CandidateWorkFlowEvent event : events) {
				logger.debug("adding workflow event " + toString());
				event.accept(visitor);
			}
		}
	}

	void addResume(Document doc, Candidate candidate)
			throws DocumentException {
		Resume resume = candidate.getResume();

		Paragraph paragraph = new Paragraph();
		paragraph.add(new Chunk("Resume",headerFont));
		paragraph.setSpacingAfter(10);
		doc.add(paragraph);

		paragraph = new Paragraph();
		paragraph.add(new Chunk(resume.getStringContent()));
		doc.add(paragraph);

		if (resume.getFileType().equals("txt")) {
			logger.debug("Text resume.");
		} else if (resume.getFileType().equals("doc")) {
			logger.debug("Doc resume.");
		} else if (resume.getFileType().equals("pdf")) {
			logger.debug("PDF resume.");
		}

	}

	protected void addExamResults(Document doc, Sitting sitting)
			throws DocumentException {

		logger.debug("Adding Exam Results to document.");
		Paragraph paragraph;

		logger.debug("Adding sitting " + sitting.getId());

		Exam exam = sitting.getExam();

		paragraph = new Paragraph();
		paragraph.add(new Chunk("Exam Results: " + exam.getName(), headerFont));
		paragraph.setSpacingAfter(10);
		doc.add(paragraph);
		paragraph = new Paragraph();
		paragraph.add(new Chunk("Taken: "+ formatDate(exam.getEntityInfo().getCreatedDate())));
		paragraph.setSpacingAfter(20);
		doc.add(paragraph);
		int lcv = 0;
		Paragraph hr = new Paragraph("\n---------------------------------------------------\n",courierFont);
		hr.setSpacingAfter(10);
		for (Question question : exam.getQuestions()) {
			
			doc.add(hr);
			paragraph = new Paragraph();
			paragraph.add(new Chunk((lcv + 1) + ". " + question.getPrompt()
					+ "\n", arialFont));
			doc.add(paragraph);
			try {
				paragraph = new Paragraph();
				Response userResponse = sitting.getQuestionsAndResponses().get(
						lcv).getResponse();
				if(userResponse != null) {
					paragraph.add(new Chunk(userResponse.getContent() + "\n\n",
							courierFont));
					doc.add(paragraph);
					PdfPTable table = new PdfPTable(4);
					table.setWidthPercentage(100);
					
					int totalTime = (int)userResponse.getTotalTime()/1000;
					int totalChars = userResponse.getContent().length();
					Grade grade = userResponse.getGrade();
					
					PdfPCell cell;
					
					// header rows 
					cell = new PdfPCell(new Paragraph("Time"));
					table.addCell(cell);
					cell = new PdfPCell(new Paragraph("Characters"));
					table.addCell(cell);
					cell = new PdfPCell(new Paragraph("Rate"));
					table.addCell(cell);
					cell = new PdfPCell(new Paragraph("Grade"));
					table.addCell(cell);

					// row 1
					cell = new PdfPCell(new Paragraph("Total: "+totalTime+" s"));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph("Response: "+totalChars+" chars"));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph("CPS: "+ (totalChars / totalTime)));
					table.addCell(cell);
					
					if (grade != null) {
						cell = new PdfPCell(new Paragraph("Correctness: " + grade.getScores().get("function").getValue().intValue()));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Paragraph(" "));
						table.addCell(cell);
					}
			
					
					// row 2
					cell = new PdfPCell(new Paragraph("Hesitation: "+userResponse.getHesitationTime()/1000+" s"));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph("Typed: "+userResponse.getKeyChars()+" chars"));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph("WPM: "+ (userResponse.getWordsPerMinute())));
					table.addCell(cell);
					
					if (grade != null) {
						cell = new PdfPCell(new Paragraph("Style: " + grade.getScores().get("form").getValue().intValue()));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Paragraph(" "));
						table.addCell(cell);	
					}
					
					
					
					// row 3
					cell = new PdfPCell(new Paragraph("Typing: "+userResponse.getTypingTime()/1000+" s"));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph("Erased: "+userResponse.getEraseChars()+" chars"));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph("LPH: "+ (userResponse.getLinesPerHour())));
					table.addCell(cell);
					
					if (grade != null) {
						cell = new PdfPCell(new Paragraph("Total: " + grade.getScore().getValue().intValue()));
						table.addCell(cell);
					} else {
						cell = new PdfPCell(new Paragraph(" "));
						table.addCell(cell);	
					}
					
					
					
					// row 4
					cell = new PdfPCell(new Paragraph("Away: "+userResponse.getAwayTime()/1000+" s"));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph(" "));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph(" "));
					table.addCell(cell);
					
					cell = new PdfPCell(new Paragraph(" "));
					table.addCell(cell);
					
					
					
					
					
					
					doc.add(table);
					
				} else {
					paragraph.add(new Chunk("[No response]",arialFont));
					doc.add(paragraph);
				}
			} catch (Exception e) {
				// FIXME obviously we should do the right thing here
			}
			lcv++;

		}
		logger.debug("Added exam results.");
	}

	@Override
	protected void buildPdfDocument(Map xmap, Document doc, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		Candidate candidate = (Candidate) xmap.get("candidate");
		Collection<Sitting> sittings = (Collection<Sitting>) xmap.get("sittings");
		List<CandidateWorkFlowEvent> events = (List<CandidateWorkFlowEvent>) xmap.get("events");
		
		
		File tocFile = File.createTempFile("toc", ".pdf");
		File packetFile = File.createTempFile("packet", ".pdf");
		FileOutputStream tocFileOutputStream = new FileOutputStream(tocFile);
		FileOutputStream packetFileOutputStream = new FileOutputStream(packetFile);

		
		logger.debug("Fetching logo instance.");
		String logoFilename = request.getSession().getServletContext().getRealPath("img/logo.png");
		logger.debug("Fetching logo from "+logoFilename);
		FileInputStream logoStream = new FileInputStream(logoFilename);
		Image logo = PngImage.getImage(logoStream);
		logo.setAlignment(Image.ALIGN_CENTER);
		
		Document packet = new Document();
		PdfWriter packetWriter = PdfWriter.getInstance(packet, packetFileOutputStream);
		packet.open();
		packetWriter.setPageEvent(new HeaderFooterEvents(logoFilename));
		
		Document toc = new Document();
		PdfWriter tocWriter = PdfWriter.getInstance(toc, tocFileOutputStream);
		toc.open();
		
		toc.add(logo);
		
		Paragraph header = new Paragraph(new Chunk("Santex HR Candidate Info Packet",headerFont));
		header.setAlignment(Paragraph.ALIGN_CENTER);
		toc.add(header);
		
		Paragraph name = new Paragraph(new Chunk(candidate.getName().getFullName(),headerFont));
		name.setAlignment(Paragraph.ALIGN_CENTER);
		toc.add(name);
		
		
		PdfPTable tocTable = new PdfPTable(new float[]{90,20});
		tocTable.setSpacingBefore(10);
		tocTable.setSpacingAfter(10);
		
		logger.debug("Adding candidate profile to packet.");
		tocTable.addCell(new Phrase("Profile"));
		tocTable.addCell(new Phrase("1"));
		addCandidateProfile(packet, candidate, events);
		
		if (candidate.getResume() != null) {
			logger.debug("Adding resume to packet.");
			packet.newPage();
			tocTable.addCell(new Phrase("Resume"));
			tocTable.addCell(new Paragraph(""+packetWriter.getCurrentPageNumber()));
			addResume(packet, candidate);
		}
			
		if (candidate.getCoverLetter() != null) {
			logger.debug("Adding cover letter to packet.");
			packet.newPage();
			tocTable.addCell(new Phrase("Cover Letter"));
			tocTable.addCell(new Phrase(""+packetWriter.getCurrentPageNumber()));
			addCoverLetter(packet, candidate);
		}
		
		logger.debug("Adding sittings to packet.");
		for(Sitting sitting : sittings) {
			logger.debug("...adding sitting "+sitting.getId());
			packet.newPage();
			tocTable.addCell(new Paragraph(new Chunk("Exam Results - "+
					sitting.getExam().getName() + " - " +
					CalendarUtils.toString(sitting.getEntityInfo().getCreatedDate()))));
			tocTable.addCell(new Phrase(""+packetWriter.getCurrentPageNumber()));
			addExamResults(packet, sitting);
		}
		
		toc.add(tocTable);

		
		logger.debug("closing files.");
		packet.close();
		toc.close();
		packetWriter.close();
		tocWriter.close();

		
		// THE FOLLOWING CHUNK OF CODE CONTAINS NO USER-SERVICABLE PARTS
		logger.debug("About to call the kludgy concat_pdf routine.");
		File concatenated = File.createTempFile("packet_toc", ".pdf");
		logger.debug("Concatenating "+tocFile.getAbsolutePath()+" and "+packetFile.getAbsolutePath());
		concat_pdf.main(new String[] {tocFile.getAbsolutePath(),packetFile.getAbsolutePath(),concatenated.getAbsolutePath()});
		logger.debug("About to output the file.");
		PdfContentByte cb = writer.getDirectContent();
		PdfReader reader = new PdfReader(concatenated.getAbsolutePath());
		for(int i = 1; i <= reader.getNumberOfPages(); i++) {
			logger.debug("Reading page "+i);
			PdfImportedPage page = writer.getImportedPage(reader, i);
			cb.addTemplate(page, 0, 0);
			doc.newPage();
		}
		reader.close();
		doc.close();
		concatenated.delete();
		tocFile.delete();
		packetFile.delete();
	}

}
