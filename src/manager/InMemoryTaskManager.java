package manager;

import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private static int id = 1;

    @Override
    public void createTask(Task task) {
        task.setTaskId(id++);
        task.setType(TypeTask.TASK);
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void clearAllTask() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }


    @Override
    public void updateTask(Task task, int id) {
        Task updatedTask = tasks.get(id);
        if (updatedTask != null) {
            task.setTaskId(updatedTask.getTaskId());
            tasks.put(id, task);
        }
    }

    @Override
    public void removeTaskById(int id) {
        // Удаляем задачу из основного хранилища
        tasks.remove(id);
        // Также удаляем задачу из истории, если она там есть
        historyManager.remove(id);
    }


    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setTaskId(id++);
        epic.setType(TypeTask.EPIC);
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void clearAllEpic() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void updateEpic(int id, String name, String description) {
        Epic updatedEpic = epics.get(id);
        if (updatedEpic != null) {
            updatedEpic.setName(name);
            updatedEpic.setDescription(description);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getTaskId());
                historyManager.remove(subtask.getTaskId());
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setTaskId(id++);
        subtask.setType(TypeTask.SUBTASK);
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
        }
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearAllSubtask() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int id) {
        Epic epic = epics.get(id);
        return epic != null ? epic.getSubtasks() : null;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic.getTaskId());
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtasks(subtask);
            }
            historyManager.remove(id);
        }
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
