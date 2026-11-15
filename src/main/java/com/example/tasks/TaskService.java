package com.example.tasks;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private long nextId = 2;

    public TaskService() {
        tasks.add(new Task(1, "Learn Spring Boot", false));
    }

    public List<Task> getAllTasks(Boolean done) {
        if (done == null) {
            return new ArrayList<>(tasks);
        }
        return tasks.stream()
                .filter(task -> task.done() == done)
                .toList();
    }

    public Optional<Task> getTaskById(long id) {
        return tasks.stream()
                .filter(task -> task.id() == id)
                .findFirst();
    }

    public Task createTask(TaskRequest request) {
        Task newTask = new Task(nextId, request.title(), request.done());
        nextId++;
        tasks.add(newTask);
        return newTask;
    }

    public Optional<Task> updateTask(long id, TaskRequest request) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).id() == id) {
                Task updatedTask = new Task(id, request.title(), request.done());
                tasks.set(i, updatedTask);
                return Optional.of(updatedTask);
            }
        }
        return Optional.empty();
    }

    public boolean deleteTask(long id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).id() == id) {
                tasks.remove(i);
                return true;
            }
        }
        return false;
    }
}
