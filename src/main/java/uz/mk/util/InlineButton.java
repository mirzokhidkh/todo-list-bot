package uz.mk.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineButton {
    public static InlineKeyboardButton keyboardButton(String text, String callbackData) {
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton(text);
        keyboardButton.setCallbackData(callbackData);
        return keyboardButton;
    }

    public static List<InlineKeyboardButton> row(InlineKeyboardButton... keyboardButtons) {
        return Arrays.asList(keyboardButtons);
    }

    public static List<List<InlineKeyboardButton>> rowCollection(List<InlineKeyboardButton>... rows) {
        return Arrays.asList(rows);
    }

    public static InlineKeyboardMarkup keyboardMarkup(List<List<InlineKeyboardButton>> rowCollection) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(rowCollection);
        return keyboardMarkup;
    }
}
