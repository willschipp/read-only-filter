package com.example.cloud.filter;

import com.example.cloud.model.SimpleModel;
import com.example.cloud.model.SimpleModelRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ReadOnlyFilterTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    Filter readOnlyFilter;

    @Autowired
    SimpleModelRepository repository;

    @Autowired
    ObjectMapper mapper;

    MockMvc mvc;

    @Before
    public void before() throws Exception {
        mvc = webAppContextSetup(context).addFilters(readOnlyFilter).build();
        repository.deleteAllInBatch();
        //reset the filter
        ((ReadOnlyFilter) readOnlyFilter).setReadOnly(false);
    }



    @Test
    public void testFilterNotActivePOST() throws Exception {
        //make a POST to the service
        SimpleModel model = new SimpleModel();
        model.setName("model 1");
        String json = mapper.writeValueAsString(model);
        MvcResult result = mvc.perform(post("/model").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        String location = result.getResponse().getHeader("Location");
        //retrieve the POST successfully
        result = mvc.perform(get("/model").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        Map<String,Object> resultMap = mapper.readValue(result.getResponse().getContentAsByteArray(),new TypeReference<Map<String,Object>>() {});
        assertNotNull(((Map<String,Object>) resultMap.get("_embedded")).get("simpleModels"));
    }

    @Test
    public void testFilterNotActiveDELETE() throws Exception {
        //make a POST to the service
        SimpleModel model = new SimpleModel();
        model.setName("model 1");
        String json = mapper.writeValueAsString(model);
        MvcResult result = mvc.perform(post("/model").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        String location = result.getResponse().getHeader("Location");
        //retrieve the POST successfully
        result = mvc.perform(get("/model").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        Map<String,Object> resultMap = mapper.readValue(result.getResponse().getContentAsByteArray(),new TypeReference<Map<String,Object>>() {});
        assertNotNull(((Map<String,Object>) resultMap.get("_embedded")).get("simpleModels"));
        int count = ((Map<String,Integer>) resultMap.get("page")).get("totalElements");
        //get the model id
        String id = location.substring(location.lastIndexOf("/"));
        //retrieve the reduced record count
        //make a DELETE to the service
        mvc.perform(delete("/model/{id}",id).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
        //retrieve the reduced record count
        result = mvc.perform(get("/model").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        resultMap = mapper.readValue(result.getResponse().getContentAsByteArray(),new TypeReference<Map<String,Object>>() {});
        assertNotNull(((Map<String,Object>) resultMap.get("_embedded")).get("simpleModels"));
        int newCount = ((Map<String,Integer>) resultMap.get("page")).get("totalElements");
        assertTrue(newCount < count);
        //see the change
    }

    @Test
    public void testFilterNotActivePUT() throws Exception {
        //make a POST to the service
        SimpleModel model = new SimpleModel();
        model.setName("model 1");
        String json = mapper.writeValueAsString(model);
        MvcResult result = mvc.perform(post("/model").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        String location = result.getResponse().getHeader("Location");
        //retrieve the POST successfully
        result = mvc.perform(get("/model").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        Map<String,Object> resultMap = mapper.readValue(result.getResponse().getContentAsByteArray(),new TypeReference<Map<String,Object>>() {});
        assertNotNull(((Map<String,Object>) resultMap.get("_embedded")).get("simpleModels"));
        int count = ((Map<String,Integer>) resultMap.get("page")).get("totalElements");
        //get the model id
        String id = location.substring(location.lastIndexOf("/"));
        //make a PUT to the service
        model.setName("blah blah");
        json = mapper.writeValueAsString(model);
        //do the put
        mvc.perform(put("/model/{id}",id).content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        //retrieve the reduced record count
        result = mvc.perform(get("/model").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        resultMap = mapper.readValue(result.getResponse().getContentAsByteArray(),new TypeReference<Map<String,Object>>() {});
        assertNotNull(((Map<String,Object>) resultMap.get("_embedded")).get("simpleModels"));
        Collection<Map<String,Object>> models = (Collection<Map<String,Object>>) ((Map<String,Object>) resultMap.get("_embedded")).get("simpleModels");
        Map<String,Object> modelMap = models.iterator().next();
        assertEquals(modelMap.get("name").toString(),"blah blah");
        //see the change
    }

    @Test
    public void testActiveFilterNoPost() throws Exception {
        //activate the setting for the filter
        ((ReadOnlyFilter) readOnlyFilter).setReadOnly(true);
        //make a POST to the service
        SimpleModel model = new SimpleModel();
        model.setName("model 1");
        String json = mapper.writeValueAsString(model);
        mvc.perform(post("/model").content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isMethodNotAllowed());
    }
}
