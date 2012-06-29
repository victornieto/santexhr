package org.openapplicant.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelpController {
	
	@RequestMapping(method=GET)
	public String index() {
		return "help/index";
	}

}
