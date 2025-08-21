//package com.inventory.controller;
//
//import com.inventory.dto.notification.NotificationResponse;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//@Controller
//public class WebSocketController {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    public void sendNotification(NotificationResponse notification) {
//        messagingTemplate.convertAndSend("/topic/notifications", notification);
//    }
//}
