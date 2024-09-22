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

}
