public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание задач
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        manager.createTask(task1);
        manager.createTask(task2);

        // Создание эпиков и подзадач
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        manager.createEpic(epic1);
        manager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getTaskId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getTaskId());
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", epic2.getTaskId());

        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        // Печать задач, эпиков и подзадач
        manager.getAllTask();
        manager.getAllEpics();
        manager.getAllSubtask();

        // Изменение статусов
        task1.setStatus(TaskStatus.DONE);
        manager.updateTask(1, "Баба яга", "Убить", "DONE");

        subtask1.setStatus(TaskStatus.DONE);
        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        // Печать обновленных задач и эпиков
       manager.getAllTask();
        manager.getAllEpics();

        // Удаление задачи и эпика
        manager.removeTaskById(2);
        manager.removeEpicById(1);

        // Печать задач, эпиков и подзадач после удаления
        manager.getAllTask();
        manager.getAllEpics();
        manager.getAllSubtask();
    }
}