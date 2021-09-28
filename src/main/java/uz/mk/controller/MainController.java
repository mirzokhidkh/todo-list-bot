package uz.mk.controller;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mk.dto.CodeMessage;
import uz.mk.service.FileInfoService;

public class MainController extends TelegramLongPollingBot {
    private final GeneralController generalController;
    private final FileInfoService fileInfoService;
    private final TodoController todoController;


    public MainController() {
        this.generalController = new GeneralController();
        this.fileInfoService = new FileInfoService();
        this.todoController = new TodoController();
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

        Message message = update.getMessage();

        try {
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();

                String data = callbackQuery.getData();
                message = callbackQuery.getMessage();

                if (data.equals("menu")) {
                    this.sendMsg(generalController.handle(data, message.getChatId(), message.getMessageId()));
                } else if (data.startsWith("/todo")) {
                    this.sendMsg(todoController.handle(data, message.getChatId(), message.getMessageId()));
                }

            } else if (message != null) {
                String text = message.getText();

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                Integer messageId = message.getMessageId();


                if (text != null) {
                    if (text.equals("/start") || text.equals("/settings") || text.equals("/help")) {
                        this.sendMsg(generalController.handle(text, message.getChatId(), messageId));
                    } else if (todoController.getTodoItemStep().containsKey(message.getChatId()) || text.startsWith("/todo_")) {
                        this.sendMsg(todoController.handle(text, message.getChatId(), messageId));
                    } else {
                        sendMessage.setText("This command does not exist");
                        this.sendMsg(sendMessage);
                    }
                } else {
                    this.sendMsg(this.fileInfoService.getFileInfo(message));
                }

                //            User user = message.getFrom();

            }

        } catch (Exception e) {
            e.printStackTrace();
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
                case MESSAGE_VIDEO:
                    execute(codeMessage.getSendMessage());
                    execute(codeMessage.getSendVideo());
                    break;
                default:
                    break;
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
