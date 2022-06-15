package com.kramphub.recruitment.mediasearch.controller;

import com.kramphub.recruitment.mediasearch.utils.JsonUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Operation Log Aspect Processing Class
 */
@Component
@Aspect
public class AopController {
    private Logger logger = Logger.getLogger(getClass());
    long startTime =System.currentTimeMillis();


    @Pointcut("execution(public * com.kramphub.recruitment.mediasearch.controller..*.*(..))")
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // Receive the request, record the content of the request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // Record the content of the request
        logger.info("---------------request----------------");
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = (String) enu.nextElement();
            logger.info("name :" + name + "-value :" + request.getParameter(name));
        }
    }
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        logger.info("---------------response----------------");
        // The request is processed and the content is returned
        logger.info("RESPONSE : " + JsonUtil.obj2String(ret));
        logger.info("START TIME  : " + startTime);
        logger.info("CURRENT TIME  : " + System.currentTimeMillis());
        logger.info("TIME SPENT OF SERVICE  : " + (System.currentTimeMillis() - startTime) + "millisecond");
    }
}
