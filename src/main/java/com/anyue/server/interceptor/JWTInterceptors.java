package com.anyue.server.interceptor;

import com.anyue.common.utils.JWTUtil;
import com.anyue.server.utils.ThreadLocalUtil;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class JWTInterceptors implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, Object> map = new HashMap<>();
        // 获取请求头中令牌
        String token = request.getHeader("Authorization");
        try {
            // 验证令牌
            JWTUtil.verify(token);
            String userId = JWTUtil.parseId(token);
            ThreadLocalUtil.setCurrentUser(userId);
            return true;  // 放行请求

        } catch (SignatureVerificationException e) {
            e.printStackTrace();
//            map.put("message","无效签名！");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
//            map.put("message","token过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
//            map.put("message","算法不一致");
        } catch (Exception e) {
            e.printStackTrace();
//            map.put("message","token无效！");
        }
        map.put("code", "401");  // 设置状态
        map.put("message", "登录已过期！");
        // 将map以json的形式响应到前台  map --> json  (jackson)
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
