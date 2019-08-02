package com.hoolai.bi.etlengine.web.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class PanelExceptionHandler implements HandlerExceptionResolver {
	
	private static final Logger logger=Logger.getLogger("exception");

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView modelAndView=new ModelAndView("error/50x.jsp");
		logger.error("req "+request.getServletPath()+" meet error.", ex);
		return modelAndView;
	}

}
