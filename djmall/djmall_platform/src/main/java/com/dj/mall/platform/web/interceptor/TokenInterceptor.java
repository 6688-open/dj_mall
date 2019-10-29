package com.dj.mall.platform.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dj.mall.api.order.cmpt.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final String TOKEN_KEY = "token";

    @Autowired
    private RedisService redisService;

    /**
     * token验证
     *
     * @param token
     * @return
     */
    private boolean checkToken(String token) {
        if (StringUtils.hasText(token)) {
            // token有效性校验
            if (redisService.checkKeyIsExist(token)) {
                // 验证通过
                redisService.expireKey(token, 3600);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断请求类型
        // 是ajax请求 聪Harder中获取Token信息
        if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            // 从请求的头中获取TOKEN
            String token = request.getHeader(TOKEN_KEY);
            if (checkToken(token)) {
                return true;
            }
            // 设置未登录状态码
            response.setStatus(401);
            JSONObject result = new JSONObject();
            result.put("code", 401);
            result.put("msg", "未登录");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().append(result.toJSONString());
            return false;
        } else {
            String token = request.getParameter(TOKEN_KEY);
            if (checkToken(token)) {
                return true;
            }
            response.sendRedirect(request.getContextPath() + "/user/toIndex");//转发到首页
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
