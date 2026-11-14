package com.example.tasks;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.test.annotation.DirtiesContext;


@WebMvcTest(TaskController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    void getTestReturns200() throws Exception{
        mockMvc.perform(get("/tasks")).andExpect(status().isOk()).andExpect(jsonPath("$[0].title").value("Learn Spring Boot"));
    }

    @Test
    void getTaskByIdWith200() throws Exception{
        mockMvc.perform(get("/tasks/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.title").value("Learn Spring Boot"));
    }


    @Test
    void getTaskByIdWith404() throws  Exception
    {
        mockMvc.perform(get("/tasks/100")).andExpect(status().isNotFound());
    }

    @Test
    void postTaskReturn201() throws Exception{
        mockMvc.perform(post("/tasks").contentType("application/json").content(
                """
                         {
                        "title": "Learn Testing",
                        "done": false
                    }
                    """))
                .andExpect(status().isCreated()) .andExpect(jsonPath("$.title").value("Learn Testing"))
                .andExpect(jsonPath("$.done").value(false)).andExpect(jsonPath("$.id").exists());

    }


    @Test
    void postTaskReturns400WhenTitleBlank() throws Exception {
        mockMvc.perform(post("/tasks").contentType("application/json")
                        .content("""
                    {
                        "title": "   ",
                        "done": false
                    }
                    """)).andExpect(status().isBadRequest());
    }


    @Test
    void deleteTaskReturns204() throws Exception {
        mockMvc.perform(delete("/tasks/1")).andExpect(status().isNoContent());
    }


    @Test
    void deleteTaskReturns404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/tasks/999")).andExpect(status().isNotFound());
    }
}
