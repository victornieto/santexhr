package org.openapplicant.web.filter;

import gr.abiss.mvn.plugins.jstools.web.JavascriptDependencyFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//============================================================================
// RESOURCE FITLER
//============================================================================
/**
 * JavascriptDependencyFilter throws a NullPointerException when loading 
 * files with non-standard extensions (.ejs for example).  Declaring these
 * extensions as supported doesn't seem to help.  This class is a work 
 * around.
 */
public class ResourceFilter extends JavascriptDependencyFilter {

	//========================================================================
	// MEMBERS
	//========================================================================
	private static final Log log = LogFactory.getLog(JavascriptDependencyFilter.class);
	
	//========================================================================
	// METHODS
	//========================================================================
	
	//------------------------------------------------------------------------
	// (OVERRIDE) DO FILTER
	//------------------------------------------------------------------------
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		try {
			super.doFilter(req, res, chain);
		} catch(NullPointerException e) {
			HttpServletRequest request = (HttpServletRequest)req;
			String path = StringUtils.substringAfterLast(request.getRequestURI(), basePath);
			path = path.startsWith("/") ? path : "/" + path;
			String ext = FilenameUtils.getExtension(path);
			if(!allowedExtentions.contains(ext)) {
				throw e;
			} else {
				String resource = IOUtils.toString(getClass().getResourceAsStream(path));
				res.setContentType(getMimeTypeFromFileExtention(ext));
				res.getWriter().print(resource);
			}
		}
	}
	
	//------------------------------------------------------------------------
	// (OVERRIDE) GET MIME TYPE FROM FILE EXTENSION
	//------------------------------------------------------------------------
	@Override
	protected String getMimeTypeFromFileExtention(String extention) {
		try {
			return super.getMimeTypeFromFileExtention(extention);
		} catch(NullPointerException e) {
			log.error("getMimeTypeFromFileExtension: ", e);
			return "text/plain";
		}
	}
}
