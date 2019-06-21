package guru.sfg.brewery.inventory_service.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 2019-05-31.
 */
@Slf4j
@Component
public class NewInventoryListener {

    @JmsListener(destination = "inventoryCreate")
    public void listen(Message<String> message){
        log.debug(message.getPayload());
    }
}
