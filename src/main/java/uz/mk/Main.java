package uz.mk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.mk.controller.MainController;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    public static void main(String[] args) {
        LOGGER.info(" *START* ");
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new MainController());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
