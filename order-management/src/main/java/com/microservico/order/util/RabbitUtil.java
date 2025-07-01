package com.microservico.order.util;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RabbitUtil {

    public int getTentativas(Message message) {
        Map<String, Object> headers = message.getMessageProperties().getHeaders();

        if (headers.containsKey("x-death")) {
            Object xDeath = headers.get("x-death");

            if (xDeath instanceof List<?> list) {
                Object first = list.get(0);
                if (first instanceof Map<?, ?> map) {
                    Object count = map.get("count");
                    if (count instanceof Long) {
                        return ((Long) count).intValue();
                    }
                }
            }
        }
        return 0;
    }

}
