package com.example.cloud.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Order(1)
public class ReadOnlyFilter implements Filter {

    private static final Log logger = LogFactory.getLog(ReadOnlyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }

    private AtomicBoolean readOnly = new AtomicBoolean(false);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //check the value
        if (readOnly.get()) {
            //read-only
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            if (!request.getMethod().equals(RequestMethod.GET.name())) {
                ((HttpServletResponse) servletResponse).setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
                logger.info("Request made that was denied: " + request.getRequestURI());
                return; //ended here
            }//end if
        }//end if
        //continue
        filterChain.doFilter(servletRequest,servletResponse);//pass it on
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }
}
