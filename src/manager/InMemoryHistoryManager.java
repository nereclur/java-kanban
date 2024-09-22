package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private Node tail;
    private Map<Integer, Node> nodeMap;

    public InMemoryHistoryManager() {
        this.nodeMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getTaskId())) {
            return; // Задача уже в истории, не добавляем
        }

        Node newNode = new Node(task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        nodeMap.put(task.getTaskId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    public void remove(int id) {
        Node nodeToRemove = nodeMap.get(id);
        if (nodeToRemove != null) {
            if (nodeToRemove.prev != null) {
                nodeToRemove.prev.next = nodeToRemove.next;
            } else {
                head = nodeToRemove.next; // Удаляем голову
            }
            if (nodeToRemove.next != null) {
                nodeToRemove.next.prev = nodeToRemove.prev;
            } else {
                tail = nodeToRemove.prev; // Удаляем хвост
            }
            nodeMap.remove(id);
        }
    }


    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        nodeMap.put(task.getTaskId(), newNode);
    }

    private void removeNode(Node node) {
        if (node == null) return;

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next; // Если это голова
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev; // Если это хвост
        }

        nodeMap.remove(node.task.getTaskId());
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    private static class Node {
        Task task;
        Node next;
        Node prev;

        Node(Task task) {
            this.task = task;
        }
    }
}
