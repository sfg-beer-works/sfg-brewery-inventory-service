package guru.sfg.brewery.inventory_service.listeners;

import guru.sfg.brewery.beer_service.events.NewInventoryEvent;
import guru.sfg.brewery.inventory_service.domain.BeerInventory;
import guru.sfg.brewery.inventory_service.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 2019-05-31.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class NewInventoryListener {

    private final BeerInventoryRepository beerInventoryRepository;

    @JmsListener(destination = "new-inventory")
    public void listen(NewInventoryEvent event){
        log.debug(event.toString());

        beerInventoryRepository.save(BeerInventory.builder()
                .beerId(event.getBeerDto().getId())
                .quantityOnHand(event.getBeerDto().getQuantityOnHand())
                .upc(event.getBeerDto().getUpc())
                .build());
    }
}
