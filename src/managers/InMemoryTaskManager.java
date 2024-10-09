package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    protected Map<Integer, Task> idTask = new HashMap<>();
    protected Map<Integer, Subtask> idSubtask = new HashMap<>();
    protected Map<Integer, Epic> idEpic = new HashMap<>();
    protected final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    protected Integer taskId = 0;

    protected int generateNewId() {
        return taskId++;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public Task addNewTask(Task newTask) {
        int newId;
        if (Objects.nonNull(newTask)) {
            newId = generateNewId();
            newTask.setId(newId);
            if (newTask.getClass() == Task.class) {
                idTask.put(newTask.getId(), newTask);
                System.out.println("Added task: " + newTask);
                return newTask;
            } else {
                System.out.println("Received class is not Task");
                return null;
            }
        } else {
            System.out.println("Task is null");
            return null;
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return Stream.concat(Stream.concat(idTask.values().stream(), idSubtask.values().stream()), idEpic.values().stream())
                .filter(task -> task.getStartTime() != null)
                .sorted(Comparator.comparing(Task::getStartTime))
                .toList();
    }


    @Override
    public boolean tasksOverlap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();
        return start1.isBefore(end2) && start2.isBefore(end1);  // Условие пересечения
    }


    @Override
    public Epic addNewTask(Epic newEpic) {
        int newId;
        if (Objects.nonNull(newEpic)) {
            newId = generateNewId();
            newEpic.setId(newId);
            idEpic.put(newEpic.getId(), newEpic);
            System.out.println("Added epic: " + newEpic);
            return newEpic;
        } else {
            System.out.println("Epic is null");
            return null;
        }
    }

    @Override
    public Subtask addNewTask(Subtask newSubtask) {
        int newId;
        if (Objects.nonNull(newSubtask)) {
            Integer subtaskEpicId = newSubtask.getEpicId();
            newId = generateNewId();
            newSubtask.setId(newId);
            if (subtaskEpicId != null && subtaskEpicId >= 0) {
                if (idEpic.containsKey(newSubtask.getEpicId())) {
                    idEpic.get(newSubtask.getEpicId()).addSubtask(newSubtask);
                } else {
                    System.out.println("Map not contains epic");
                    return null;
                }
                idSubtask.put(newSubtask.getId(), newSubtask);
                refreshEpicStatus(newSubtask.getEpicId());
                System.out.println("Added subtask: " + newSubtask);
                return newSubtask;
            } else {
                System.out.println("Subtask must contain Epic ");
                return null;
            }
        } else {
            System.out.println("Subtask is null");
            return null;
        }
    }

    @Override
    public Task updateTask(Task updatedTask) {
        int taskId;
        if (Objects.nonNull(updatedTask)) {
            taskId = updatedTask.getId();
            if (idTask.containsKey(taskId) && updatedTask.getClass() == Task.class) {
                if (updatedTask.getStatus() == null) {
                    updatedTask.setStatus(idTask.get(taskId).getStatus());
                }
                idTask.put(taskId, updatedTask);
                System.out.println("Updated task: " + updatedTask);
                return updatedTask;
            } else {
                System.out.println("Task with id " + taskId + " not exist");
                return null;
            }
        } else {
            System.out.println("Task is null");
            return null;
        }
    }

    @Override
    public Subtask updateTask(Subtask subtaskUpdate) {
        int subtaskId;
        if (Objects.nonNull(subtaskUpdate)) {
            subtaskId = subtaskUpdate.getId();
            if (idSubtask.containsKey(subtaskId)) {
                Subtask subtaskMap = idSubtask.get(subtaskId);
                if (subtaskUpdate.getStatus() == null) {
                    subtaskUpdate.setStatus(idSubtask.get(subtaskId).getStatus());
                }
                if (subtaskUpdate.getEpicId() == null) {
                    subtaskUpdate.setEpicId(idEpic.get(subtaskMap.getEpicId()));
                    idSubtask.put(subtaskId, subtaskUpdate);
                    refreshEpicStatus(subtaskUpdate.getEpicId());
                    System.out.print("Updated subtask: " + subtaskUpdate);
                } else if (!subtaskUpdate.getEpicId().equals(idSubtask.get(subtaskId).getEpicId())) {
                    idEpic.get(idSubtask.get(subtaskId).getEpicId()).removeSubtask(subtaskId);
                    refreshEpicStatus(idSubtask.get(subtaskId).getEpicId());
                    idSubtask.put(subtaskId, subtaskUpdate);
                    idEpic.get(subtaskUpdate.getEpicId()).addSubtask(subtaskUpdate);
                    refreshEpicStatus(subtaskUpdate.getEpicId());
                    System.out.println("Updated subtask: " + subtaskUpdate);

                }
            } else {
                System.out.println("Task with id " + subtaskId + " not exist");
                return null;
            }
        } else {
            System.out.println("Subtask is null");
            return null;
        }
        return subtaskUpdate;
    }

    @Override
    public Epic updateTask(Epic epicUpdate) {
        int epicId;
        if (Objects.nonNull(epicUpdate)) {
            epicId = epicUpdate.getId();
            if (idEpic.containsKey(epicId)) {
                epicUpdate.replaceSubtasks(idEpic.get(epicId).getEpicSubtasksId());
                idEpic.put(epicId, epicUpdate);
                System.out.println("Updated epic: " + epicUpdate);
                return epicUpdate;
            } else {
                System.out.println("Task with id " + epicId + " not exist");
                return null;
            }
        } else {
            System.out.println("Subtask is null");
            return null;
        }
    }

    @Override
    public Task deleteTask(Integer taskId) {
        Task removedTask;
        if (Objects.nonNull(taskId) && taskId >= 0) {
            if (idTask.containsKey(taskId)) {
                removedTask = idTask.remove(taskId);
                historyManager.remove(taskId);
            } else if (idSubtask.containsKey(taskId)) {
                Subtask subtask = idSubtask.get(taskId);
                idEpic.get(subtask.getEpicId()).removeSubtask(taskId);
                refreshEpicStatus(subtask.getEpicId());
                removedTask = idSubtask.remove(taskId);
                historyManager.remove(taskId);
            } else if (idEpic.containsKey(taskId)) {
                removedTask = idEpic.remove(taskId);
                historyManager.remove(taskId);
            } else {
                System.out.println("Task with id " + taskId + " not exist");
                return null;
            }
        } else {
            System.out.println("Task is null or id less than 0");
            return null;
        }

        System.out.println("Removed task: " + removedTask);
        return removedTask;
    }

    @Override
    public void deleteAllTasks() {
        if (!idTask.isEmpty()) {
            int tasksSum = idTask.size();
            idTask.keySet().forEach(historyManager::remove);
            idTask.clear();
            System.out.println("Removed " + tasksSum + " tasks");
        }
    }


    @Override
    public void deleteAllSubtasks() {
        if (!idSubtask.isEmpty()) {
            idSubtask.values().forEach(subtask -> {
                Epic epic = idEpic.get(subtask.getEpicId());
                if (epic != null) {
                    epic.clearSubtasks();
                    refreshEpicStatus(epic.getId());
                }
            });
            int tasksSum = idSubtask.size();
            idSubtask.keySet().forEach(historyManager::remove);
            idSubtask.clear();

            System.out.println("Removed " + tasksSum + " subtasks");
        }
    }


    @Override
    public void deleteAllEpic() {
        int tasksSum = 0;

        if (!idEpic.isEmpty()) {
            idEpic.values().stream()
                    .flatMap(epic -> epic.getEpicSubtasksId().stream())
                    .forEach(subtaskId -> {
                        idSubtask.remove(subtaskId);
                        historyManager.remove(subtaskId);
                    });
            tasksSum = idEpic.size();
            idEpic.keySet().forEach(id -> historyManager.remove(id));
            idEpic.clear();
        }

        System.out.println("Removed " + tasksSum + " epics");
    }


    @Override
    public List<Task> getAllTasks() {
        if (!idTask.isEmpty()) {
            return new ArrayList<>(idTask.values());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Task> getAllSubtasks() {
        if (!idSubtask.isEmpty()) {
            return new ArrayList<>(idSubtask.values());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Task> getAllEpic() {
        if (!idEpic.isEmpty()) {
            return new ArrayList<>(idEpic.values());
        }
        return new ArrayList<>();
    }

    @Override
    public Task getTask(Integer taskId) {
        if (Objects.nonNull(idTask.get(taskId))) {
            historyManager.add(idTask.get(taskId));
            return idTask.get(taskId);
        } else {
            System.out.println("There is not task-id " + taskId);
            return null;
        }
    }

    @Override
    public Epic getEpic(Integer epicId) {
        if (Objects.nonNull(idEpic.get(epicId))) {
            historyManager.add(idEpic.get(epicId));
            return idEpic.get(epicId);
        } else {
            System.out.println("There is not epic-id " + epicId);
            return null;
        }
    }

    @Override
    public Subtask getSubtask(Integer subtaskId) {
        if (Objects.nonNull(idSubtask.get(subtaskId))) {
            historyManager.add(idSubtask.get(subtaskId));
            return idSubtask.get(subtaskId);
        } else {
            System.out.println("There is not subtask-id " + subtaskId);
            return null;
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(Integer epicId) {
        if (!idEpic.containsKey(epicId)) {
            System.out.println("Not Epic with this id: " + epicId);
            return Collections.emptyList();
        }

        return idEpic.get(epicId).getEpicSubtasksId().stream()
                .map(idSubtask::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void deleteEpicSubtasks(Integer epicId) {
        Epic epicInMap = idEpic.get(epicId);

        if (epicInMap == null) {
            System.out.println("Map not contains epic ");
            return;
        }
        List<Subtask> subtasks = getEpicSubtasks(epicId);
        if (!subtasks.isEmpty()) {
            subtasks.stream()
                    .peek(subtask -> {
                        historyManager.remove(subtask.getId());
                        idSubtask.remove(subtask.getId());
                    })
                    .count();
            epicInMap.clearSubtasks();
            refreshEpicStatus(epicId);
            System.out.println("Removed all subtasks from " + epicInMap.getName());
        } else {
            System.out.println("Epic not contains subtasks");
        }
    }

    public void refreshEpicStatus(int epicId) {
        Epic epic = getEpic(epicId);
        if (epic != null) {
            List<Subtask> subtasks = getEpicSubtasks(epicId);
            boolean hasNew = subtasks.stream().anyMatch(subtask -> subtask.getStatus() == TaskStatus.NEW);
            boolean allDone = subtasks.stream().allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);

            if (hasNew) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (allDone) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.NEW);
            }
        }
    }

    public boolean hasTimeConflicts(Task newTask) {
        Task[] tasks = new Task[0];
        for (Task existingTask : tasks) {
            if (existingTask.getStartTime() != null && newTask.getStartTime() != null) {
                boolean startBeforeEnd = newTask.getStartTime().isBefore(existingTask.getEndTime());
                boolean endAfterStart = newTask.getEndTime().isAfter(existingTask.getStartTime());
                if (startBeforeEnd && endAfterStart) {
                    return true;
                }
            }
        }
        return false;
    }
}
