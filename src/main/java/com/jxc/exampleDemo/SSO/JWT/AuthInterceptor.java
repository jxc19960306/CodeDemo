package com.jxc.exampleDemo.SSO.JWT;

import com.jxc.exampleDemo.HTTPUtil.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断当前访问的方法是否需要认证拦截
        HandlerMethod method = (HandlerMethod)handler;
        LoginRequire methodAnnotation = method.getMethodAnnotation(LoginRequire.class);

        if(methodAnnotation==null){
            return true;
        }

        String oldToken = CookieUtil.getCookieValue(request, "userToken", true);
        String newToken = request.getParameter("newToken");

        String token = "";


//        oldToken不空，新token空，用户登陆过
//        oldToken空，新token不空，用户第一次登陆
//        oldToken空，新token空，用户从没登陆
//        oldToken不空，新token不空，用户登录过期

        if(StringUtils.isNotBlank(oldToken)&&StringUtils.isBlank(newToken)){
            //登陆过
            token = oldToken;
        }
        if(StringUtils.isBlank(oldToken)&&StringUtils.isNotBlank(newToken)){
            //第一次登陆
            token = newToken;
        }

        if(StringUtils.isNotBlank(oldToken)&&StringUtils.isNotBlank(newToken)){
            //登陆过期
            token = newToken;
        }


        if(methodAnnotation.ifNeedSuccess()&& StringUtils.isBlank(token))
        {
            StringBuffer requestURL = request.getRequestURL();
            response.sendRedirect("http://passport.gmall.com:8085/index?returnURL="+requestURL);
            return false;
        }

        String success = "";
        if(StringUtils.isNotBlank(token)){
            // 远程访问passport，验证token
            success = HttpclientUtil.doGet("http://passport.gmall.com:8085/verify");

        }

        if(!success.equals("success")&&methodAnnotation.ifNeedSuccess()){
            response.sendRedirect("http://passport.gmall.com:8085/index");
            return false;
        }

        if(success.equals("success")){
            // cookie验证通过，重新刷新cookie的过期时间
            CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);
        }

        if(!success.equals("success")&&!methodAnnotation.ifNeedSuccess())
        {

            // 购物车方法

            return true;
        }



        return true;
    }

}
