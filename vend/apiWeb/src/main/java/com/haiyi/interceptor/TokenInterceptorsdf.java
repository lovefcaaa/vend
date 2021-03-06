package com.haiyi.interceptor;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.haiyi.token.TokenAnno;
 
/**
 * @author Administrator
 *
 */
public class TokenInterceptorsdf extends HandlerInterceptorAdapter {
	
	//验证请求Token
	private final String TOKEN="token";
	 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(true){
            return true;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            
            TokenAnno annotation = method.getAnnotation(TokenAnno.class);
            if (annotation != null ) {
                boolean needVerify = annotation.verifyToken();
                if (needVerify) {
                	//判断token是否合法
                	String token = request.getParameter("token");
                	//判断设备的token
                    return true;
                }
            }
            return true ;
        }else{
            return super.preHandle(request, response, handler);
        }
    }
    
    private void redirect(HttpServletResponse response,String URI) throws ServletException, IOException{
		response.sendRedirect(URI);
	}
}