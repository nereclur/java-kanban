package manager;

import task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {
    private static class Node {
        Task task;
        Node prev;
        Node next;

        public Node(Task task) {
            this.task = task;
        }
    }

    private Node head;
    private Node tail;
    private Map<Integer, Node> tasksById = new HashMap<>();

    @Override
    public void add(Task task) {

        Node existingNode = tasksById.get(task.getTaskId());
        if (existingNode != null) {
            remove(existingNode.task.getTaskId());
        }

        Node newNode = new Node(task);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        tasksById.put(task.getTaskId(), newNode);
    }


    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public void remove(int taskId) {
        Node nodeToRemove = tasksById.get(taskId);
        if (nodeToRemove != null) {
            // Узел найден, продолжаем удаление

            if (nodeToRemove == head) {
                // Узел является головой списка
                head = head.next;
                if (head != null) {
                    head.prev = null;
                }
            } else if (nodeToRemove == tail) {
                // Узел является хвостом списка
                tail = tail.prev;
                if (tail != null) {
                    tail.next = null;
                }
            } else {
                // Узел находится в середине списка
                nodeToRemove.prev.next = nodeToRemove.next;
                nodeToRemove.next.prev = nodeToRemove.prev;
            }

            // Удаляем узел из мапы по id
            tasksById.remove(taskId);
        }
    }


}
