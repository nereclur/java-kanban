package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

class EpicTest {

    private InMemoryTaskManager manager;
    private Epic epic1;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        epic1 = new Epic("Эпик 1", "Описание эпика 1");
        manager.createEpic(epic1);
    }

    @Test
    void shouldReturnCountOfAddedSubtask() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getTaskId());
        manager.createSubtask(subtask1);
        List<Subtask> subtasks = epic1.getSubtasks();
        Assertions.assertEquals(1, subtasks.size());
    }

    @Test
    void removeSubtasks() {
        Subtask subtaskToRemove = new Subtask("Subtask 2", "Описание subtask 2", epic1.getTaskId());
        manager.createSubtask(subtaskToRemove);
        epic1.removeSubtasks(subtaskToRemove);
        assertFalse(epic1.getSubtasks().contains(subtaskToRemove), "Сабтаска должны быть удалена");

        Subtask subtask1 = new Subtask("Subtask 1", "Описание subtask 1", epic1.getTaskId());
        manager.createSubtask(subtask1);
        Subtask subtask3 = new Subtask("Subtask 3", "Описание subtask 3", epic1.getTaskId());
        manager.createSubtask(subtask3);

        assertTrue(epic1.getSubtasks().contains(subtask1), "Subtask 1 должен оставаться в списке");
        assertTrue(epic1.getSubtasks().contains(subtask3), "Subtask 3 должен оставаться в списке");
    }
}
