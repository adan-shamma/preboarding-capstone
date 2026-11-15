package com.example.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@Import(TaskService.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTestReturns200() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Learn Spring Boot"));
    }

    @Test
    void getTaskByIdWith200() throws Exception {
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"));
    }

    @Test
    void getTaskByIdWith404() throws Exception {
        mockMvc.perform(get("/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postTaskReturn201() throws Exception {
        mockMvc.perform(post("/tasks").contentType("application/json").content("""
                    {
                        "title": "Learn Testing",
                        "done": false
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Learn Testing"))
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void postTaskReturns400WhenTitleBlank() throws Exception {
        mockMvc.perform(post("/tasks").contentType("application/json").content("""
                    {
                        "title": "   ",
                        "done": false
                    }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTaskReturns204() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTaskReturns404WhenNotFound() throws Exception {
        mockMvc.perform(delete("/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTasksWithoutDoneParamReturnsAllTasksWithMixedDoneStatuses() throws Exception {
        mockMvc.perform(post("/tasks").contentType("application/json").content("""
                    {
                        "title": "Completed Task",
                        "done": true
                    }
                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].done").value(false))
                .andExpect(jsonPath("$[1].done").value(true));
    }

    @Test
    void getTasksFilterByDoneFalse() throws Exception {
        mockMvc.perform(post("/tasks").contentType("application/json").content("""
                    {
                        "title": "Completed Task",
                        "done": true
                    }
                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tasks").param("done", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Learn Spring Boot"))
                .andExpect(jsonPath("$[0].done").value(false));
    }

    @Test
    void getTasksFilterByDoneTrueReturnsEmptyWhenNoneDone() throws Exception {
        mockMvc.perform(get("/tasks").param("done", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getTasksFilterByDoneTrueReturnsMatchingTask() throws Exception {
        mockMvc.perform(post("/tasks").contentType("application/json").content("""
                    {
                        "title": "Completed Task",
                        "done": true
                    }
                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tasks").param("done", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Completed Task"))
                .andExpect(jsonPath("$[0].done").value(true));
    }

    @Test
    void putTaskReturns200() throws Exception {
        mockMvc.perform(put("/tasks/1").contentType("application/json").content("""
                    {
                        "title": "Updated Title",
                        "done": true
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void putTaskUpdatesDoneFromTrueToFalse() throws Exception {
        mockMvc.perform(put("/tasks/1").contentType("application/json").content("""
                    {
                        "title": "In Progress",
                        "done": true
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true));

        mockMvc.perform(put("/tasks/1").contentType("application/json").content("""
                    {
                        "title": "Reopened Task",
                        "done": false
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Reopened Task"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void putTaskReturns404WhenNotFound() throws Exception {
        mockMvc.perform(put("/tasks/999").contentType("application/json").content("""
                    {
                        "title": "Missing Task",
                        "done": true
                    }
                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void putTaskReturns400WhenTitleBlank() throws Exception {
        mockMvc.perform(put("/tasks/1").contentType("application/json").content("""
                    {
                        "title": "   ",
                        "done": true
                    }
                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTaskCountReturnsInitialCounts() throws Exception {
        mockMvc.perform(get("/tasks/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.done").value(0));
    }

    @Test
    void getTaskCountReflectsAddedAndCompletedTasks() throws Exception {
        mockMvc.perform(post("/tasks").contentType("application/json").content("""
                    {
                        "title": "Completed Task",
                        "done": true
                    }
                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/tasks/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2))
                .andExpect(jsonPath("$.done").value(1));
    }

    @Test
    void getTaskCountWhenAllTasksDeleted() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.done").value(0));
    }

    @Test
    void getTaskCountReflectsUpdatedTaskStatus() throws Exception {
        mockMvc.perform(put("/tasks/1").contentType("application/json").content("""
                    {
                        "title": "Learn Spring Boot",
                        "done": true
                    }
                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.done").value(1));
    }
}
