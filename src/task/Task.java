package task;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int taskId;
    private TaskStatus status;
    private TypeTask type;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.type = TypeTask.TASK;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTaskId() {
        return taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TypeTask getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    @Override
    public String toString() {
        return "infrastructure.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
