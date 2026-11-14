package com.example.tasks;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
public class TaskController {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId=2;

    public  TaskController(){
        tasks.add(new Task(1,"Learn Spring Boot",false));
    }


    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return tasks;
    }


    @GetMapping("/tasks/{id}")
    public  ResponseEntity<Task> getTaskById(@PathVariable long id)
    {
        for(Task task:tasks)
        {
            if(task.id()==id)
            {
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest task)
    {
        Task newTask=new Task(nextId,task.title(),task.done());
        nextId++;
        tasks.add(newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }


    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable long id)
    {
        for(int i=0;i<tasks.size();i++)
        {
            Task task=tasks.get(i);
            if(task.id()==id)
            {
                tasks.remove(i);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }


}