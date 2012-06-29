package org.openapplicant.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class ErrorViewHandlerExceptionResolver implements HandlerExceptionResolver {

	private static final Log log = LogFactory.getLog(ErrorViewHandlerExceptionResolver.class);
	
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		log.error(ex);
		return new ModelAndView("error/index");
	}
}
