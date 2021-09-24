package uz.mk.controller;

import org.aopalliance.reflect.Code;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mk.dto.CodeMessage;
import uz.mk.enums.MessageType;

import java.util.ArrayList;
import java.util.List;

import static uz.mk.enums.MessageType.*;
import static uz.mk.util.InlineButton.*;

public class GeneralController {


    public CodeMessage handle(String text, Long chatId, Integer messageId) {
        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));

        switch (text) {
            case "/start":
                sendMessage.setText("Hello, You are in *TodoList* bot. Welcome to our bot.");
                sendMessage.setParseMode("Markdown");

                sendMessage.setReplyMarkup(
                        keyboardMarkup(
                                rowCollection(
                                        row(keyboardButton("Go to Menu", "menu"))
                                )));
                codeMessage.setSendMessage(sendMessage);
                codeMessage.setType(MESSAGE);
                break;
            case "/settings":
                sendMessage.setText("Setting is not yet ready");
                codeMessage.setSendMessage(sendMessage);
                codeMessage.setType(MESSAGE);
                break;
            case "/help":
                sendMessage.setText("Not sure how to use this bot? Then watch this [video](https://www.youtube.com/channel/UCFoy0KOV9sihL61PJSh7x1g)\n");
                sendMessage.setParseMode("Markdown");
                codeMessage.setSendMessage(sendMessage);
                codeMessage.setType(MESSAGE);
                break;
            case "menu":
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setChatId(String.valueOf(chatId));
                editMessageText.setText("Main menu");
                editMessageText.setMessageId(messageId);

                editMessageText.setReplyMarkup(
                        keyboardMarkup(
                                rowCollection(
                                        row(keyboardButton("ToDo List", "/todo/list")),
                                        row(keyboardButton("Create New", "/todo/create"))
                                )));
                codeMessage.setEditMessageText(editMessageText);
                codeMessage.setType(EDIT);
                break;
            default:
                sendMessage.setText("This command does not exist");
        }

        return codeMessage;
    }
}
