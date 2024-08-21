package task;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {


    @Test
    void shouldReturnCountOfAddedSubtask() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getTaskId());
        manager.createSubtask(subtask1);
        List<Subtask> subtasks = epic1.getSubtasks();
        Assertions.assertEquals(1, subtasks.size());
    }

    @Test
    void removeSubtasks() {
    }
}