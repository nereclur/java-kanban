package managers;

import exceptions.ManagerLoadException;
import exceptions.ManagerSaveException;
import tasks.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Файл не может быть null");
        }
        this.file = file;
    }

    public FileBackedTaskManager(File file, Map<Integer, Task> tasks,
                                 Map<Integer, Epic> epics,
                                 Map<Integer, Subtask> subtasks,
                                 int maxId) {
        if (file == null) {
            throw new IllegalArgumentException("Файл не может быть null");
        }
        this.file = file;
        this.idTask = tasks != null ? tasks : new HashMap<>();
        this.idEpic = epics != null ? epics : new HashMap<>();
        this.idSubtask = subtasks != null ? subtasks : new HashMap<>();
        this.taskId = maxId;
    }

    private void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file)) {
            if (!getAllTasks().isEmpty() || !getAllSubtasks().isEmpty() || !getAllEpic().isEmpty()) {
                fileWriter.write("ID,TYPE,NAME,STATUS,DESCRIPTION,EPIC,DURATION,START_TIME,END_TIME\n");

                getAllTasks().stream()
                        .map(Task::toString)
                        .forEach(task -> {
                            try {
                                fileWriter.write(task + "\n");
                            } catch (IOException ex) {
                                throw new ManagerSaveException(ex.getMessage());
                            }
                        });
                getAllEpic().stream()
                        .map(Task::toString)
                        .forEach(epic -> {
                            try {
                                fileWriter.write(epic + "\n");
                            } catch (IOException ex) {
                                throw new ManagerSaveException(ex.getMessage());
                            }
                        });
                getAllSubtasks().stream()
                        .map(Task::toString)
                        .forEach(subtask -> {
                            try {
                                fileWriter.write(subtask + "\n");
                            } catch (IOException ex) {
                                throw new ManagerSaveException(ex.getMessage());
                            }
                        });
            }
        } catch (IOException ex) {
            throw new ManagerSaveException(ex.getMessage());
        }
    }

    private static Task fromString(String value) {
        String[] fields = value.split(",");
        if (fields.length < 5) {
            throw new IllegalArgumentException("Некорректный формат подзадачи, ожидалось 5 полей.");
        }
        int id = Integer.parseInt(fields[0]);
        TaskType type = TaskType.valueOf(fields[1]);
        String title = fields[2];
        TaskStatus status = TaskStatus.valueOf(fields[3]);  // Здесь статус задачи обрабатывается как TaskStatus
        String description = fields[4];

        switch (type) {
            case TASK:
                return new Task(id, title, description, status);  // Здесь передается статус, а не Integer
            case EPIC:
                return new Epic(id, title, description, status);  // Тоже передается статус
            case SUBTASK:
                if (fields.length < 6) {
                    throw new IllegalArgumentException("Некорректный формат подзадачи, ожидалось 6 полей.");
                }
                int epicId = Integer.parseInt(fields[5]);
                return new Subtask(id, title, description, epicId, status);  // Здесь epicId — это Integer
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadException {
        if (file == null) {
            throw new IllegalArgumentException("Файл не может возвращать null");
        }
        Map<Integer, Task> tasks = new HashMap<>();
        Map<Integer, Subtask> subtasks = new HashMap<>();
        Map<Integer, Epic> epics = new HashMap<>();
        int maxId = 0;
        int[] maxIdContainer = {maxId};
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.lines()
                    .skip(1)
                    .map(FileBackedTaskManager::fromString)
                    .forEach(task -> {
                        if (task != null) {
                            maxIdContainer[0] = Math.max(maxIdContainer[0], task.getId());
                            switch (task.getType()) {
                                case TASK:
                                    tasks.put(task.getId(), task);
                                    break;
                                case SUBTASK:
                                    subtasks.put(task.getId(), (Subtask) task);
                                    break;
                                case EPIC:
                                    epics.put(task.getId(), (Epic) task);
                                    break;
                            }
                        }
                    });
            subtasks.values().stream()
                    .forEach(sub -> {
                        Epic epic = epics.get(sub.getEpicId());
                        if (epic != null) {
                            epic.addSubtask(sub);
                        }
                    });
        } catch (IOException ex) {
            throw new ManagerLoadException(ex.getMessage());
        }
        maxId = maxIdContainer[0];

        return new FileBackedTaskManager(file, tasks, epics, subtasks, maxId);
    }

    @Override
    public Task addNewTask(Task newTask) {
        if (newTask == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }
        Task task = super.addNewTask(newTask);
        saveSilently();
        return task;
    }

    @Override
    public Epic addNewTask(Epic newEpic) {
        if (newEpic == null) {
            throw new IllegalArgumentException("Epic не может быть null");
        }
        Epic epic = super.addNewTask(newEpic);
        saveSilently();
        return epic;
    }

    @Override
    public Subtask addNewTask(Subtask newSubtask) {
        if (newSubtask == null) {
            throw new IllegalArgumentException("Subtask не может быть null");
        }
        Subtask subtask = super.addNewTask(newSubtask);
        saveSilently();
        return subtask;
    }

    @Override
    public Task updateTask(Task updatedTask) {
        if (updatedTask == null) {
            throw new IllegalArgumentException("Task не может быть null");
        }
        Task task = super.updateTask(updatedTask);
        saveSilently();
        return task;
    }

    @Override
    public Subtask updateTask(Subtask subtaskUpdate) {
        if (subtaskUpdate == null) {
            throw new IllegalArgumentException("Subtask не может быть null");
        }
        Subtask subtask = super.updateTask(subtaskUpdate);
        saveSilently();
        return subtask;
    }

    @Override
    public Epic updateTask(Epic epicUpdate) {
        if (epicUpdate == null) {
            throw new IllegalArgumentException("Epic не может быть null");
        }
        Epic epic = super.updateTask(epicUpdate);
        saveSilently();
        return epic;
    }

    @Override
    public Task deleteTask(Integer taskId) {
        Task task = super.deleteTask(taskId);
        saveSilently();
        return task;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        saveSilently();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        saveSilently();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        saveSilently();
    }

    @Override
    public void deleteEpicSubtasks(Integer epicId) {
        super.deleteEpicSubtasks(epicId);
        saveSilently();
    }

    private void saveSilently() {
        try {
            save();
        } catch (ManagerSaveException ex) {
            ex.printStackTrace();
        }
    }
}
