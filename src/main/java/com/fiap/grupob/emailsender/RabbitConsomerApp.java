package com.fiap.grupob.emailsender;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class RabbitConsomerApp {
    @Value ("${trace_logger}")
    private String trace_logger;

    @RabbitListener(queues = {"sendermail"})
    public void receive(@Payload String fileBody) {
        Logger.getLogger(trace_logger).log(Level.INFO, String.format("Recebido payload para enviar mensagem. \n\r%s",fileBody));
        try {
            EmailGenerator eg = new EmailGenerator(fileBody);
        } catch (Exception e){
            Logger.getLogger(trace_logger).log(Level.SEVERE, String.format("Faha no envio de e-mail. \n\r%s", e.getMessage()));
        }
    }

}
