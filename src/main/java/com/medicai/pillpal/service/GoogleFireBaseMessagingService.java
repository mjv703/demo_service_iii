package com.medicai.pillpal.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.medicai.pillpal.service.dto.NotificationDTO;
import org.springframework.stereotype.Service;

@Service
public class GoogleFireBaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public GoogleFireBaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public String sendNotification(NotificationDTO note, String token) throws FirebaseMessagingException {
        Notification notification = Notification.builder().setTitle(note.getSubject()).setBody(note.getContent()).build();

        Message message = Message.builder().setToken(token).setNotification(notification).putAllData(note.getData()).build();

        return firebaseMessaging.send(message);
    }
}
