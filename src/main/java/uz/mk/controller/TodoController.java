package uz.mk.controller;

import lombok.Data;
import org.aopalliance.reflect.Code;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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

import static uz.mk.enums.MessageType.EDIT;
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

            switch (command) {
                case "list":
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setMessageId(messageId);
                    editMessageText.setChatId(String.valueOf(chatId));
                    editMessageText.setParseMode("HTML");

                    List<TodoItem> todoItemList = todoRepository.getTodoItem(chatId);

                    if (todoItemList == null || todoItemList.isEmpty()) {
                        editMessageText.setText("You do not have any todo items");
                        editMessageText.setReplyMarkup(
                                keyboardMarkup(
                                        rowCollection(
                                                row(keyboardButton("Go to Menu", "menu"))
                                        )));
                    } else {

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
                    break;
                case "create":
                    editMessageText = new EditMessageText();
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
                    break;
                case "update":
                    command = commandList[3];
                    String id = commandList[4];

                    editMessageText = new EditMessageText();

                    todoItem = this.todoRepository.getItem(chatId, id);
                    if (todoItem == null) {
                        editMessageText.setText("todo Id does not exists");
                    } else {
                        editMessageText.setChatId(String.valueOf(chatId));
                        editMessageText.setMessageId(messageId);
                        editMessageText.setText("Send *Title*");
                        editMessageText.setParseMode("MarkdownV2");

                        if (command.equals("title")) {
                            editMessageText.setText("'Current Title' : " + todoItem.getTitle() + "\nPlease send new Title");

                            editMessageText.setReplyMarkup(
                                    keyboardMarkup(
                                            rowCollection(
                                                    row(keyboardButton("Cancel", "/todo/cancel"))
                                            )));

                            codeMessage.setEditMessageText(editMessageText);
                            codeMessage.setType(EDIT);
                            todoItem.setType(UPDATE_TITLE);
                            this.todoItemStep.put(chatId, todoItem);

                        } else if (command.equals("content")) {
                            editMessageText.setText("'Current Content' : " + todoItem.getContent() + "\nPlease send new Content");

                            codeMessage.setEditMessageText(editMessageText);
                            codeMessage.setType(EDIT);
                            todoItem.setType(UPDATE_CONTENT);
                            this.todoItemStep.put(chatId, todoItem);
                        }
                    }

                    break;
                case "cancel":
                    this.todoItemStep.remove(chatId);
                    editMessageText = new EditMessageText();
                    editMessageText.setMessageId(messageId);
                    editMessageText.setChatId(String.valueOf(chatId));
                    editMessageText.setText("Update was canceled");

                    editMessageText.setReplyMarkup(
                            keyboardMarkup(
                                    rowCollection(
                                            row(keyboardButton("ToDo List", "/todo/list", ":clipboard:")),
                                            row(keyboardButton("Go to Menu", "menu"))
                                    )));

                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setType(EDIT);
                    break;
                case "delete":
                    id = commandList[3];

                    editMessageText = new EditMessageText();
                    editMessageText.setMessageId(messageId);
                    editMessageText.setChatId(String.valueOf(chatId));

                    boolean result = this.todoRepository.delete(chatId, id);
                    if (result) {
                        editMessageText.setText("Todo was canceled");
                    } else {
                        editMessageText.setText("Error");
                    }


                    editMessageText.setReplyMarkup(
                            keyboardMarkup(
                                    rowCollection(
                                            row(keyboardButton("ToDo List", "/todo/list", ":clipboard:")),
                                            row(keyboardButton("Go to Menu", "menu"))
                                    )));
                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setType(EDIT);
                    break;
            }

//            if (command.equals("list")) {
//
//            } else if (command.equals("create")) {
//
//
//            } else if (command.equals("update")) {
//
//
//            } else if (command.equals("cancel")) {
//
//            } else if (command.equals("delete")) {
//
//            }

            return codeMessage;
        }


        if (text.startsWith("/todo_")) {
            String todoId = text.split("/todo_edit_")[1];
            TodoItem todoItem = this.todoRepository.getItem(chatId, todoId);
            if (todoItem == null) {
                sendMessage.setText("todo Id does not exists");
            } else {
                sendMessage.setText(todoItem.getTitle() + "\n" + todoItem.getContent() + "\n" +
                        "_" + simpleDateFormat.format(todoItem.getCreatedDate()) + "_");
                sendMessage.setReplyMarkup(getTodoItemKeyboard(todoId));
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
                this.todoItemStep.remove(chatId);

                sendMessage.setText("ItemCount : " + n + "\n*Title* : " + todoItem.getTitle() + "\n" + "*Content*: " + todoItem.getContent() + "\n" +
                        "Creating Todo finished");

                sendMessage.setReplyMarkup(
                        keyboardMarkup(
                                rowCollection(
                                        row(keyboardButton("ToDo List", "/todo/list", ":clipboard:")),
                                        row(keyboardButton("Go to Menu", "menu"))
                                )));
            } else if (todoItem.getType().equals(UPDATE_TITLE)) {
                todoItem.setTitle(text);
                this.todoItemStep.remove(chatId);
                sendMessage.setText("'Title' :  " + todoItem.getTitle() + "\n" + "'Content : '" + todoItem.getContent());
                sendMessage.setReplyMarkup(getTodoItemKeyboard(todoItem.getId()));
            } else if (todoItem.getType().equals(UPDATE_CONTENT)) {
                todoItem.setContent(text);
                this.todoItemStep.remove(chatId);
                sendMessage.setText("'Title' :  " + todoItem.getTitle() + "\n" + "'Content : '" + todoItem.getContent());
                sendMessage.setReplyMarkup(getTodoItemKeyboard(todoItem.getId()));
            }


        }


        return codeMessage;
    }

    private InlineKeyboardMarkup getTodoItemKeyboard(String todoId) {
        return keyboardMarkup(
                rowCollection(
                        row(
                                keyboardButton("Update Title", "/todo/update/title/" + todoId),
                                keyboardButton("Update Content", "/todo/update/content/" + todoId),
                                keyboardButton("Delete", "/todo/delete/" + todoId, ":x:")
                        ),
                        row(
                                keyboardButton("ToDo List", "/todo/list", ":clipboard:")
                        )
                ));
    }
}
