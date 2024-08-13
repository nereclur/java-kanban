
package manager;

import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class TaskManager {
    private static Map<Integer, Task> tasks = new HashMap<>();
    private static Map<Integer, Subtask> subtasks = new HashMap<>();
    private static Map<Integer, Epic> epics = new HashMap<>();
    private static int id = 1;

    public void createTask(Task task) {
        task.setTaskId(id++);
        tasks.put(task.getTaskId(), task);
    }

    public List<Task> getAllTask() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
        return new ArrayList<>(tasks.values());
    }

    public void clearAllTask() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void updateTask(Task task, int id) {
        Task updatedTask = tasks.get(id);
        if (updatedTask != null) {
            task.setTaskId(updatedTask.getTaskId());
            tasks.put(id, task);
        }
    }

    public void removeTaskById(int id) {
        Task deletedTask = tasks.get(id);
        if (deletedTask != null) {
            tasks.remove(deletedTask.getTaskId(), deletedTask);
        }
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
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

    public void updateEpic(int id, String name, String description) {
        Epic updatedEpic = epics.get(id);
        if (updatedEpic != null) {
            updatedEpic.setName(name);
            updatedEpic.setDescription(description);
        }
    }

    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getTaskId());
            }
        }
    }

    public void createSubtask(Subtask subtask) {
        subtask.setTaskId(id++);
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
            updateEpicStatus(epic.getTaskId());
        }
    }

    public List<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    public void clearAllSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            updateEpicStatus(epic.getTaskId());
        }
    }

    public List<Subtask> getSubtasksById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            return epic.getSubtasks();
        }
        return null;
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic.getTaskId());
        }
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtasks(subtask);
                updateEpicStatus(epic.getTaskId());
            }
        }
    }

    public void updateEpicStatus(int id) {
        Epic epic = epics.get(id);

        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            boolean isAllSubtaskDone = true;
            boolean isAllSubtaskNew = true;

            for (Subtask subtask : epic.getSubtasks()) {
                if (subtask.getStatus() != TaskStatus.DONE) {
                    isAllSubtaskDone = false;
                }
                if (subtask.getStatus() != TaskStatus.NEW) {
                    isAllSubtaskNew = false;
                }
            }

            if (isAllSubtaskDone) {
                epic.setStatus(TaskStatus.DONE);
            } else if (isAllSubtaskNew) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }
}