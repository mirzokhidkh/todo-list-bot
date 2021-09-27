package uz.mk.util;

import com.vdurmont.emoji.EmojiParser;
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

    public static InlineKeyboardButton keyboardButton(String text, String callbackData, String emoji) {
        String emojiText = EmojiParser.parseToUnicode(emoji + " " + text);
        InlineKeyboardButton keyboardButton = new InlineKeyboardButton(emojiText);
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
