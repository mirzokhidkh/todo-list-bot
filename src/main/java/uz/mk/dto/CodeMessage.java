package uz.mk.dto;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import uz.mk.enums.MessageType;

@Data
public class CodeMessage {
    private SendMessage sendMessage;
    private EditMessageText editMessageText;
    private MessageType type;
}
