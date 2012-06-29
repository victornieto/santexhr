package org.openapplicant.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.openapplicant.domain.FileAttachment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AttachmentController extends AdminController {
	
	@RequestMapping
	public void find(@RequestParam("guid") String guid, HttpServletResponse response) 
		throws IOException {
		FileAttachment file = getAdminService().findFileAttachmentByGuid(guid);
		
		response.setCharacterEncoding("utf-8");
		//FIXME: we should add a filename so it's easier to differentiate between the resume & cover letter
		response.setHeader("Content-disposition", "attachment; filename=\"file."+file.getFileType().toLowerCase()+"\"");		
		response.setContentType(file.getContentType());
		
		response.getOutputStream().write(file.getContent());
	}
}
