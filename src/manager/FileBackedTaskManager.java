package manager;

import exceptions.FileWriterSaveException;
import task.*;
import java.io.*;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                Task task = taskFromString(line);
                if (task.getType().equals("EPIC")) {
                    manager.createEpic((Epic) task);
                } else if (task.getType().equals("SUBTASK")) {
                    manager.createSubtask((Subtask) task);
                } else {
                    manager.createTask(task);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return manager;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            List<Task> allTask = getAllTask();

            writer.write("id,type,name,status,description,epic\n");
            for (Task task : allTask) {
                writer.write(taskToString(task));
            }

            for (Epic epic : getAllEpics()) {
                writer.write(taskToString(epic));
                for (Subtask subtask : epic.getSubtasks()) {
                    writer.write(taskToString(subtask));
                }
            }
        } catch (IOException e) {
            throw new FileWriterSaveException("Ошибка записи в файл", e);
        }
    }

    private static Task taskFromString(String line) {
        String[] fields = line.split(",");

        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        String description = fields[4];

        Task task = null;

        switch (type) {
            case "TASK":
                task = new Task(name, description);
                break;
            case "EPIC":
                task = new Epic(name, description);
                break;
            case "SUBTASK":
                int epicId = Integer.parseInt(fields[5]);
                task = new Subtask(name, description, epicId);
                break;
        }

        if (task != null) {
            task.setTaskId(id);
        }

        return task;
    }

    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getTaskId()).append(",");
        sb.append(getTaskType(task)).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if (task.getType().equals(TypeTask.SUBTASK)) {
            sb.append(((Subtask) task).getEpicId()).append(",");
        }
        sb.append("\n");
        return sb.toString();
    }

    private String getTaskType(Task task) {
        return task.getType().name();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void clearAllTask() {
        super.clearAllTask();
        save();
    }

    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void clearAllEpic() {
        super.clearAllEpic();
        save();
    }

    @Override
    public void updateEpic(int id, String name, String description) {
        super.updateEpic(id, name, description);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void clearAllSubtask() {
        super.clearAllSubtask();
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void updateEpicStatus(int id) {
        super.updateEpicStatus(id);
        save();
    }
}
