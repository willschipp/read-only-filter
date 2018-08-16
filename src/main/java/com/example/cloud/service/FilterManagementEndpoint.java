package com.example.cloud.service;

import com.example.cloud.filter.ReadOnlyFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Administration controller to allow enabling/disabling the filter
 */
@RestController
@RequestMapping("${filter.adminUrl}")
public class FilterManagementEndpoint {

    private static final Log logger = LogFactory.getLog(FilterManagementEndpoint.class);

    private static final String ON = "on";

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private FilterRegistrationBean filterRegistrationBean;

    @RequestMapping(method= RequestMethod.POST)
    public void setSwitch(@RequestBody String value, @RequestHeader(name="Authorization",required = true) String bearerToken, HttpServletResponse response) throws Exception {
        String token = bearerToken.split(" ")[1];
        //convert the token and check
        if (!authorizationService.isAuthorized(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }//end if
        //switch
        if (value.equalsIgnoreCase(ON)) {
            ((ReadOnlyFilter) filterRegistrationBean.getFilter()).setReadOnly(true);
            logger.info("Switching Read-Only Filter On");
        } else {
            ((ReadOnlyFilter) filterRegistrationBean.getFilter()).setReadOnly(false);
            logger.info("Switching Read-Only Filter Off");
        }//end if
        //set the response status
        response.setStatus(HttpStatus.OK.value());
        //return blank
        return;
    }

}
