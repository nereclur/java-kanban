import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;


public class TaskManager {
    Scanner scanner = new Scanner(System.in);
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private int id = 1;

    public void createTask(Task task) {
        task.setTaskId(id++);
        tasks.put(task.getTaskId(), task);
    }

    public void getAllTask() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void clearAllTask() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void updateTask(int id, String name, String description, String stat) {
        Task updatedTask = tasks.get(id);
        updatedTask.setName(name);
        updatedTask.setDescription(description);
        TaskStatus status = TaskStatus.valueOf(stat);
        updatedTask.setStatus(status);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    // методы эпиков
    public void getAllEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    public void createEpic(Epic epic) {
        epic.setTaskId(id++);
        epics.put(epic.getTaskId(), epic);
    }

    public void clearAllEpic() {
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateEpic(int id, String name, String description, String stat) {
        Epic updatedEpic = epics.get(id);
        updatedEpic.setName(name);
        updatedEpic.setDescription(description);
        TaskStatus status = TaskStatus.valueOf(stat);
        updatedEpic.setStatus(status);
    }

    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getEpicId());
            }
        }
    }

    // методы подзадач
    public void createSubtask(Subtask subtask) {
        subtask.setTaskId(id++);
        subtasks.put(subtask.getEpicId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.setSubtasks(subtask);
        }
    }

    public void getAllSubtask() {
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    public void clearAllSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateStatus();
        }
    }

    public List<Subtask> getSubtasksById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return epic.getSubtasks();
        }
        return null;
    }

    public void clearAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateStatus();
        }
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }


    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getEpicId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateStatus();
        }
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtasks(subtask);
            }
        }
    }

    public void updateStatus() {
        for (Epic epic : epics.values()) {
            if (epic.getSubtasks().isEmpty()) {
                epic.setStatus(TaskStatus.NEW);
                continue;
            }
            boolean isallSubtaskDone = true;
            boolean isallSubtaskNew = true;

            for (Subtask subtask : epic.getSubtasks()) {
                if (subtask.getStatus() != TaskStatus.DONE) {
                    isallSubtaskDone = false;
                }
                if (subtask.getStatus() != TaskStatus.NEW) {
                    isallSubtaskNew = false;
                }
            }

            if (isallSubtaskDone) {
                epic.setStatus(TaskStatus.DONE);
            } else if (isallSubtaskNew) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }
}






