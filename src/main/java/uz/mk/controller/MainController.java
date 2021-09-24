package uz.mk.controller;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mk.dto.CodeMessage;

public class MainController extends TelegramLongPollingBot {
    private final GeneralController generalController;

    public MainController() {
        this.generalController = new GeneralController();
    }


    @Override
    public String getBotUsername() {
        return "todo_list";
    }

    @Override
    public String getBotToken() {
        return "2001999194:AAF28p9yJe07P2kPQknzJuPbjVZuCsjogGs";
    }

    @Override
    public void onUpdateReceived(Update update) {


        SendMessage sendMessage = new SendMessage();
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            String data = callbackQuery.getData();
            Message message = callbackQuery.getMessage();

            if (data.equals("menu")) {
                this.sendMsg(generalController.handle(data, message.getChatId(), message.getMessageId()));
            }

        } else {
            Message message = update.getMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));

            Integer messageId = message.getMessageId();

            String text = message.getText();
            User user = message.getFrom();


            if (text.equals("/start") || text.equals("/settings") || text.equals("/help")) {
                this.sendMsg(generalController.handle(text, message.getChatId(), messageId));
            } else {
                this.sendMsg(sendMessage);
            }

        }


    }


    public void sendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(CodeMessage codeMessage) {
        try {
            switch (codeMessage.getType()) {
                case MESSAGE:
                    execute(codeMessage.getSendMessage());
                    break;
                case EDIT:
                    execute(codeMessage.getEditMessageText());
                    break;
                default:
                    break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
