package uz.mk.repository;

import uz.mk.dto.TodoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoRepository {
    private Map<Long, List> todoMap = new HashMap<>();

    public int add(Long userId, TodoItem todoItem) {
        if (todoMap.containsKey(userId)) {
            todoMap.get(userId).add(todoItem);
            return todoMap.get(userId).size();
        } else {
            List<TodoItem> list = new ArrayList<>();
            list.add(todoItem);
            todoMap.put(userId, list);
            return 1;
        }
    }

    public List<TodoItem> getTodoItem(Long userId) {
        if (todoMap.containsKey(userId)) {
            return todoMap.get(userId);
        }
        return null;
    }
}
