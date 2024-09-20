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
    public void remove(int id) {
        Node nodeToRemove = nodeMap.get(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        Node existingNode = nodeMap.get(task.getTaskId());
        if (existingNode != null) {
            // Обновляем существующую задачу и перемещаем её в конец списка
            existingNode.task = task;
            moveToTail(existingNode);
        } else {
            // Добавляем новую задачу в историю
            linkLast(task);
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            tail = newNode;
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        nodeMap.put(task.getTaskId(), newNode);
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

    private void removeNode(Node node) {
        if (node == head) {
            head = node.next;
            if (head != null) {
                head.prev = null;
            }
        } else if (node == tail) {
            tail = node.prev;
            if (tail != null) {
                tail.next = null;
            }
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        nodeMap.remove(node.task.getTaskId());
    }

    private void moveToTail(Node node) {
        if (node != tail) {
            if (node == head) {
                head = node.next;
                if (head != null) {
                    head.prev = null;
                }
            } else {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            tail.next = node;
            node.prev = tail;
            node.next = null;
            tail = node;
        }
    }

    class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Task task) {
            this.task = task;
        }
    }
}
