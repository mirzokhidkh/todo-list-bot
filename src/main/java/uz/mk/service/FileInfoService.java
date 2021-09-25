package uz.mk.service;

import org.aopalliance.reflect.Code;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Video;
import uz.mk.dto.CodeMessage;
import uz.mk.enums.MessageType;

import java.util.List;

import static uz.mk.enums.MessageType.MESSAGE;

public class FileInfoService {

    public CodeMessage getFileInfo(Message message) {
        Long cId = message.getChatId();

        CodeMessage codeMessage = new CodeMessage();
        codeMessage.setType(MESSAGE);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(cId));

        if (message.getPhoto() != null) {
            String s = this.show_photo_detail(message.getPhoto());
            sendMessage.setText(s);
        }else if(message.getVideo() != null){
            String s = this.show_video_detail(message.getVideo());
            sendMessage.setText(s);
        }else {
            sendMessage.setText("NOT FOUND");
        }

        codeMessage.setSendMessage(sendMessage);
        return codeMessage;
    }

    private String show_photo_detail(List<PhotoSize> photoSizeList) {
        StringBuilder s = new StringBuilder("=======================PHOTO INFO=========================\n");
        for (PhotoSize photoSize : photoSizeList) {
            s.append(" Size = ").append(photoSize.getFileSize()).append(" , ID = ").append(photoSize.getFileId()).append(" \n");
        }
        return s.toString();
    }

    private String show_video_detail(Video video) {
        StringBuilder s = new StringBuilder("=======================VIDEO INFO=========================\n");
        s.append(video.toString());
        return s.toString();

    }



}
