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
        if (tasksById.containsKey(task.getTaskId())) {
            remove(tasksById.get(task.getTaskId()));
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


    public void remove(Node node) {
        if(node == head && node == tail) {
            head = tail = null;
        } else if (node == head) {
            head = head.next;
            head.prev = null;
        } else if (node == tail) {
            tail = tail.prev;
            tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        tasksById.remove(node.task.getTaskId());
    }
}
