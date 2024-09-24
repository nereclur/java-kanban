package task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, int epicId, TypeTask typeTask) {
        super(name, description, typeTask);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "infrastructure.Subtask{" +
                "epicId=" + epicId +
                ", " + super.toString() +
                '}';
    }
}
