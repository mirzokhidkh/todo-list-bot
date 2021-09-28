package uz.mk.controller;

import lombok.Data;
import org.aopalliance.reflect.Code;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import uz.mk.dto.CodeMessage;
import uz.mk.dto.TodoItem;
import uz.mk.enums.MessageType;
import uz.mk.enums.TodoItemType;
import uz.mk.repository.TodoRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uz.mk.enums.MessageType.MESSAGE;
import static uz.mk.enums.TodoItemType.*;
import static uz.mk.util.InlineButton.*;
import static uz.mk.util.InlineButton.keyboardButton;

@Data
public class TodoController {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private Map<Long, TodoItem> todoItemStep = new HashMap<>();
    private final TodoRepository todoRepository = new TodoRepository();

    public CodeMessage handle(String text, Long chatId, Integer messageId) {
        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        if (text.startsWith("/todo/")) {
            String[] commandList = text.split("/");
            String command = commandList[2];

            if (command.equals("list")) {
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setMessageId(messageId);
                editMessageText.setChatId(String.valueOf(chatId));
                editMessageText.setParseMode("HTML");

                List<TodoItem> todoItemList = todoRepository.getTodoItem(chatId);

                if (todoItemList == null) {
                    editMessageText.setText("The list is empty");
                    editMessageText.setReplyMarkup(
                            keyboardMarkup(
                                    rowCollection(
                                            row(keyboardButton("Go to Menu", "menu"))
                                    )));
                }
                else {

                    StringBuilder stringBuilder = new StringBuilder();
                    int count = 1;
                    for (TodoItem todoItem : todoItemList) {
                        stringBuilder.append("<b>").append(count).append("</b>");
                        stringBuilder.append("\n");
                        stringBuilder.append(todoItem.getTitle());
                        stringBuilder.append("\n");
                        stringBuilder.append(todoItem.getContent());
                        stringBuilder.append("\n");
                        stringBuilder.append(simpleDateFormat.format(todoItem.getCreatedDate()));
                        stringBuilder.append(" /todo_edit_").append(todoItem.getId());
                        stringBuilder.append("\n\n");
                        count++;
                    }

                    editMessageText.setReplyMarkup(
                            keyboardMarkup(
                                    rowCollection(
                                            row(keyboardButton("Go to Menu", "menu"))
                                    )));

                    editMessageText.setText(stringBuilder.toString());
                }

                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setType(MessageType.EDIT);
            } else if (command.equals("create")) {
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setChatId(String.valueOf(chatId));
                editMessageText.setText("Send *Title*");
                editMessageText.setParseMode("MarkdownV2");
                editMessageText.setMessageId(messageId);

                TodoItem todoItem = new TodoItem();
                todoItem.setId(messageId.toString());
                todoItem.setUserId(chatId);
                todoItem.setType(TITLE);
                this.todoItemStep.put(chatId, todoItem);

                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setType(MessageType.EDIT);

            }

            return codeMessage;
        }


        if (text.startsWith("/todo_")) {
            String todoId = text.split("/todo_edit_")[1];
            TodoItem todoItem = todoRepository.getItem(chatId, todoId);
            if (todoItem == null) {
                sendMessage.setText("todo Id does not exists");
            } else {
                sendMessage.setText(todoItem.getTitle() + "\n" + todoItem.getContent() + "\n" +
                        "_" + simpleDateFormat.format(todoItem.getCreatedDate()) + "_");

                sendMessage.setReplyMarkup(
                        keyboardMarkup(
                                rowCollection(
                                        row(
                                                keyboardButton("Update Title", "/todo/update/title" + todoId),
                                                keyboardButton("Update Content", "/todo/update/content" + todoId),
                                                keyboardButton("Delete", "/todo/delete/content" + todoId, ":x:")
                                        ),
                                        row(
                                                keyboardButton("ToDo List", "/todo/list", ":clipboard:")
                                        )
                                )));

                sendMessage.setParseMode("Markdown");
            }
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setType(MESSAGE);
        }

        if (this.todoItemStep.containsKey(chatId)) {
            TodoItem todoItem = this.todoItemStep.get(chatId);

            sendMessage.setParseMode("MarkdownV2");
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setType(MESSAGE);

            if (todoItem.getType().equals(TITLE)) {
                todoItem.setTitle(text);
                sendMessage.setText("*Title* : " + todoItem.getTitle() + "\n" + "Send *Content*:");
                todoItem.setType(CONTENT);
            } else if (todoItem.getType().equals(CONTENT)) {
                todoItem.setContent(text);
                todoItem.setCreatedDate(new Date());
                todoItem.setType(FINISHED);
                int n = todoRepository.add(chatId, todoItem);
                todoItemStep.remove(chatId);

                sendMessage.setText("ItemCount : " + n + "\n*Title* : " + todoItem.getTitle() + "\n" + "*Content*: " + todoItem.getContent() + "\n" +
                        "Creating Todo finished");

                sendMessage.setReplyMarkup(
                        keyboardMarkup(
                                rowCollection(
                                        row(keyboardButton("ToDo List", "/todo/list", ":clipboard:")),
                                        row(keyboardButton("Go to Menu", "menu"))
                                )));
            }


        }


        return codeMessage;
    }
}
