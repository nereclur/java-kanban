import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }


    public void setSubtasks(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtasks(Subtask subtask) {
        subtasks.remove(subtask);
    }



    @Override
    public String toString() {
        return "Epic{" +
                ", subtasks=" + subtasks +
                ", " + super.toString() +
                '}';
    }
}
