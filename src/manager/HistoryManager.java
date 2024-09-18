package manager;

import org.w3c.dom.Node;
import task.Task;

import java.util.List;
import java.util.Objects;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

}
