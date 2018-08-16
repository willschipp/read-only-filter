package com.example.cloud.service;

import com.example.cloud.model.SimpleModel;
import com.example.cloud.service.jwt.JWTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class FilterManagementEndpointTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    JWTokenUtil tokenUtil;

    @Autowired
    ObjectMapper mapper;

    MockMvc mvc;

    @Before
    public void before() throws Exception {
        mvc = webAppContextSetup(context).addFilters(context.getBean(FilterRegistrationBean.class).getFilter()).build();
    }

    @Test
    public void testChangingFilterSettingsAuthenticated() throws Exception {
        //setup a request header with a token
        String token = tokenUtil.getToken();
        //set the filter to "on" (true)
        mvc.perform(post("/admin/filter").header(HttpHeaders.AUTHORIZATION,"Bearer " + token).content("on").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        //try a post and it should receive a 405
        SimpleModel model = new SimpleModel();
        model.setName("model 1");
        String json = mapper.writeValueAsString(model);
        mvc.perform(post("/model").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
        //set the filter to "off" (false)
        mvc.perform(post("/admin/filter").header("Authorization","Bearer " + token).content("off").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        //try a post and it should receive a 201
        mvc.perform(post("/model").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

    @Test
    public void testChangingFilterUnauthenticated() throws Exception {
        String token = "not a real token";
        //set the filter to "on" (true)
        mvc.perform(post("/admin/filter").header(HttpHeaders.AUTHORIZATION,"Bearer " + token).content("on").accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
        //try a post and it should receive a 201
        SimpleModel model = new SimpleModel();
        model.setName("model 1");
        String json = mapper.writeValueAsString(model);
        mvc.perform(post("/model").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }
}
