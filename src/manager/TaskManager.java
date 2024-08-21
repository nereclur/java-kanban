package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    List<Task> getAllTask();

    void clearAllTask();

    Task getTaskById(int id);

    void updateTask(Task task, int id);

    void removeTaskById(int id);

    List<Epic> getAllEpics();

    void createEpic(Epic epic);

    void clearAllEpic();

    Epic getEpicById(int id);

    void updateEpic(int id, String name, String description);

    void removeEpicById(int id);

    void createSubtask(Subtask subtask);

    List<Subtask> getAllSubtask();

    void clearAllSubtask();

    List<Subtask> getSubtasksByEpicId(int id);

    Subtask getSubtaskById(int id);

    void updateSubtask(Subtask subtask);

    void removeSubtaskById(int id);

    void updateEpicStatus(int id);

    List<Task> getHistory();
}
