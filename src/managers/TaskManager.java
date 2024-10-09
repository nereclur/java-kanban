package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {

    List<Task> getPrioritizedTasks();

    boolean hasTimeConflicts(Task newTask);

    boolean tasksOverlap(Task task1, Task task2);

    HistoryManager getHistoryManager();

    Task addNewTask(Task newTask);

    Epic addNewTask(Epic newEpic);

    Subtask addNewTask(Subtask newSubtask);

    Task updateTask(Task updatedTask);

    Subtask updateTask(Subtask subtaskUpdate);

    Epic updateTask(Epic epicUpdate);

    Task deleteTask(Integer taskId);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpic();

    List<Task> getAllTasks();

    List<Task> getAllSubtasks();

    List<Task> getAllEpic();

    Task getTask(Integer taskId);

    Epic getEpic(Integer epicId);

    Subtask getSubtask(Integer subtaskId);

    List<Subtask> getEpicSubtasks(Integer epicId);

    void deleteEpicSubtasks(Integer epicId);

}