package tasks.services;

import tasks.model.Task;
import tasks.model.TaskList;
import tasks.repository.TaskFileRepository;
import tasks.repository.TaskFileRepositoryException;

public class TaskServiceV2 {
    private TaskFileRepository taskFileRepository;

    public TaskServiceV2(TaskFileRepository taskFileRepository) {
        this.taskFileRepository = taskFileRepository;
    }

    public void addTask(Task task) {
        try {
            this.taskFileRepository.addTask(task);
        } catch (TaskFileRepositoryException e) {
            e.printStackTrace();
        }
    }

    public TaskList getAll() {
        return taskFileRepository.getAll();
    }
}
