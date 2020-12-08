package com.ws.sep.paypalservice.tasks;

import com.ws.sep.paypalservice.enums.OrderState;
import com.ws.sep.paypalservice.model.SellerOrders;
import com.ws.sep.paypalservice.repository.SellerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PendingTasksSchedule {

    @Autowired
    private SellerOrderRepository orderRepository;

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void resolvePendingTasks() {
        List<SellerOrders> orders = orderRepository.findPendingOrders(LocalDateTime.now().minusDays(1L));

        if(orders.size() > 0) {
            orders.stream().forEach(t -> t.setOrderState(OrderState.CANCELED));

            orderRepository.saveAll(orders);
        }
    }

}
