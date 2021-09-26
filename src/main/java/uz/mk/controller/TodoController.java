package uz.mk.controller;

import lombok.Data;
import org.aopalliance.reflect.Code;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import uz.mk.dto.CodeMessage;
import uz.mk.dto.TodoItem;
import uz.mk.enums.MessageType;
import uz.mk.enums.TodoItemType;

import java.util.HashMap;
import java.util.Map;

import static uz.mk.enums.TodoItemType.CONTENT;
import static uz.mk.enums.TodoItemType.TITLE;

@Data
public class TodoController {
    private Map<Long, TodoItem> todoItemStep = new HashMap<>();

    public CodeMessage handle(String text, Long chatId, Integer messageId) {
        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        if (text.startsWith("/todo/")) {
            String[] commandList = text.split("/");
            String command = commandList[2];

            if (command.equals("list")) {
                sendMessage.setText("List ");
                codeMessage.setSendMessage(sendMessage);
                codeMessage.setType(MessageType.MESSAGE);
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


        if (this.todoItemStep.containsKey(chatId)) {
            TodoItem todoItem = this.todoItemStep.get(chatId);

            sendMessage.setParseMode("MarkdownV2");
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setType(MessageType.MESSAGE);

            if (todoItem.getType().equals(TITLE)) {
                todoItem.setTitle(text);
                sendMessage.setText("*Title* : " + todoItem.getTitle() + "\n" + "Send *Content*:");
                todoItem.setType(CONTENT);
            } else if (todoItem.getType().equals(CONTENT)) {
                todoItem.setContent(text);
                sendMessage.setText("*Title* : " + todoItem.getTitle() + "\n" + "*Content*: " + todoItem.getContent()+"\n" +
                        "Creating Todo finished");
            }

        }


        return codeMessage;
    }
}
