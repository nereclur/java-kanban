package test;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;

import java.util.List;

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
        Subtask subtaskToRemove = new Subtask("Subtask 2", "Description of subtask 2", 2);
        epic.removeSubtasks(subtaskToRemove);
        assertFalse(epic.getSubtasks().contains(subtaskToRemove), "Subtask should be removed");
        assertTrue(epic.getSubtasks().contains(new Subtask("Subtask 1", "Description of subtask 1", 1)),
                "Subtask 1 должен оставаться в списке");
        assertTrue(epic.getSubtasks().contains(new Subtask("Subtask 3", "Description of subtask 3", 3)),
                "Subtask 3 олжен оставаться в списке");
    }
}