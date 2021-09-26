package uz.mk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.mk.enums.TodoItemType;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodoItem {
    private String id;
    private String title;
    private String content;
    private Date createdDate;
    private Long userId;
    private TodoItemType type;
}
