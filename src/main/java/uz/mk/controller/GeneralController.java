package uz.mk.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import uz.mk.dto.CodeMessage;

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
                                        row(keyboardButton("Go to Menu", "menu",""))
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

                SendVideo sendVideo = new SendVideo();
//                sendVideo.setVideo(new InputFile("BAACAgIAAxkBAAN4YU9Ymaxu3NuufBW8qN25tbxZawMAAiQTAALm6oBK9BgXiKLWoL0hBA"));
                sendVideo.setVideo(new InputFile("BAACAgIAAxkBAAOgYU9jV8BddEhTmavWU5P9KWPgyTAAAoQIAAJ7amFKXoV1y2pd574hBA"));
                sendVideo.setChatId(String.valueOf(chatId));
                sendVideo.setCaption("Watch video");
                sendVideo.setParseMode("HTML");

                codeMessage.setSendVideo(sendVideo);
                codeMessage.setType(MESSAGE_VIDEO);
                break;
            case "menu":
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setChatId(String.valueOf(chatId));
                editMessageText.setText("Main menu");
                editMessageText.setMessageId(messageId);

                editMessageText.setReplyMarkup(
                        keyboardMarkup(
                                rowCollection(
                                        row(keyboardButton("ToDo List", "/todo/list",":clipboard:")),
                                        row(keyboardButton("Create New", "/todo/create",":heavy_plus_sign:"))
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
