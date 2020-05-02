package tasks.model;

import java.util.Objects;

public class TaskValidator {
    public void validate(Task task) throws ValidationException {
        String message = "";
        if (Objects.isNull(task)) {
            message += "Task is null.\n";
        }
        if (Objects.nonNull(task) && task.getTitle().contains(";")) {
            message += "Task title contains forbidden characters.\n";
        }
        if (Objects.nonNull(task) && task.getEndTime().before(task.getStartTime())) {
            message += "Wrong interval.\n";
        }
        if (!message.isEmpty()) {
            throw new ValidationException(message);
        }
    }
}
