package kh.springbootassessment.fileparser.data;

/**
 * Stores context for the current request on a ThreadLocal.
 * 
 * This is used by the RequestLoggerInterceptor to store data about the current request within the preHandle. postHandle and 
 * afterCompletion Interceptor lifecycle methods.
 * 
 * This is to support the request logging requirement.
 */
public class RequestContext {

	private static final ThreadLocal<RequestData> currentRequest = new ThreadLocal<>();

	
    public static RequestData getCurrentRequestContext() {
        return currentRequest.get();
    }

    public static void setCurrentRequestContext(RequestData request) {
    	currentRequest.set(request);
    }

    public static void clear() {
    	currentRequest.remove();
    }
}
