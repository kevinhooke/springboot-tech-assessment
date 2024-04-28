package kh.springbootassessment.fileparser.interceptor;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.springbootassessment.fileparser.data.RequestContext;
import kh.springbootassessment.fileparser.data.RequestData;
import kh.springbootassessment.fileparser.service.RequestLoggerService;

/**
 * Spring Interceptor to log request entries to the relational database.
 * 
 * The request logging is implemented as HandlerInterceptor in order to capture and log data about the request, some values such
 * as the HTTP status code are not known for sure until after the request has completed.
 * 
 */
@Component
public class RequestLoggerInterceptor implements HandlerInterceptor {

	@Autowired
	private RequestLoggerService loggerService;
	
	@Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception
    {
		//store the start time millis for the current request
		RequestData requestData = new RequestData();
		requestData.setRequestStartMillis(Instant.now().toEpochMilli());
		RequestContext.setCurrentRequestContext(requestData);
		
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception
    {
    	try {
        System.out.println("RequestLoggerInterceptor afterCompletion()");
        RequestData data = RequestContext.getCurrentRequestContext();
        long elapsedMillis = Instant.now().toEpochMilli() - data.getRequestStartMillis();
        
        this.loggerService.logIncomingRequest(response.getStatus(), request.getRemoteAddr(), request.getRequestURI(), elapsedMillis);
        System.out.println("...afterCompletion() complete");
    	}
    	finally {
    		//clear the thread local context
    		RequestContext.clear();
    	}
    }
	
}
