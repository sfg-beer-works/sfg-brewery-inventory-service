package guru.sfg.brewery.inventory_service.services;

import guru.sfg.brewery.inventory_service.domain.BeerInventory;
import guru.sfg.brewery.inventory_service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jt on 2019-09-09.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrderDto) {
                       log.debug("Allocating Order" + beerOrderDto.getCustomerRef());

                AtomicInteger totalOrdered = new AtomicInteger();
                AtomicInteger totalAllocated = new AtomicInteger();

                beerOrderDto.getBeerOrderLines().forEach(beerOrderLine -> {
                    if ((beerOrderLine.getOrderQuantity() - beerOrderLine.getQuantityAllocated()) > 0) {
                        allocateBeerOrderLine(beerOrderLine);
                    }
                    totalOrdered.set(totalOrdered.get() + beerOrderLine.getOrderQuantity());
                    totalAllocated.set(totalAllocated.get() + beerOrderLine.getQuantityAllocated());
                });

        return null;
    }

        private void allocateBeerOrderLine(BeerOrderLineDto beerOrderLine) {
        List<BeerInventory> beerInventoryList = beerInventoryRepository.findAllByUpc(beerOrderLine.getUpc());

        beerInventoryList.forEach(beerInventory -> {
            int inventory = (beerInventory.getQuantityOnHand() == null) ? 0 : beerInventory.getQuantityOnHand();
            int orderQty = (beerOrderLine.getOrderQuantity() == null) ? 0 : beerOrderLine.getOrderQuantity() ;
            int allocatedQty = (beerOrderLine.getQuantityAllocated() == null) ? 0 : beerOrderLine.getQuantityAllocated();
            int qtyToAllocate = orderQty - allocatedQty;

            if(inventory >= qtyToAllocate){ // full allocation
                inventory = inventory - qtyToAllocate;
                beerOrderLine.setQuantityAllocated(orderQty);
                beerInventory.setQuantityOnHand(inventory);
            } else if (inventory > 0) { //partial allocation
                beerOrderLine.setQuantityAllocated(allocatedQty + inventory);
                beerInventory.setQuantityOnHand(0);
            }
        });

        beerInventoryRepository.saveAll(beerInventoryList);

        //remove zero records
        List<BeerInventory> zeroRecs = new ArrayList<>();

        beerInventoryList.stream()
                .filter(beerInventory -> beerInventory.getQuantityOnHand() == 0)
                .forEach(zeroRecs::add);

        beerInventoryRepository.deleteAll(zeroRecs);
    }
}
