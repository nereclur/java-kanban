package tasks;

import java.util.Objects;

public class Subtask extends Task {

    private Integer epicId;

    protected final TaskType type = TaskType.SUBTASK;

    public Subtask(int id, String name, String description, Integer epicId, TaskStatus status) {
        super(id, name, description, status);
        this.epicId = epicId;

    }

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, Integer epicId) {
        super(id, name, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description) {
        super(id, name, description);
    }

    public Subtask(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public Subtask(int id) {
        super(id);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return type;
    }

    public void setEpicId(Epic epic) {
        if (Objects.nonNull(epic)) {
            this.epicId = epic.getId();
        } else {
            System.out.println("Epic can't be null");
        }
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s", id, type, name, status, description, epicId);
    }
}