package com.ws.sep.paypalservice.tasks;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.ws.sep.paypalservice.enums.OrderState;
import com.ws.sep.paypalservice.model.SellerOrders;
import com.ws.sep.paypalservice.repository.SellerOrderRepository;
import com.ws.sep.paypalservice.services.SellerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PendingTasksSchedule {

    @Autowired
    private SellerOrderRepository orderRepository;

    @Autowired
    private SellerInfoService sellerInfoService;

    Logger logger = LoggerFactory.getLogger(PendingTasksSchedule.class);

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void resolvePendingTasks() {
        logger.info("Start scheduling");
        List<SellerOrders> orders = orderRepository.findPendingOrders(LocalDateTime.now().minusDays(1L));

        // case when orders are found
        if(orders.size() > 0) {
            orders.forEach(order -> {
                // if we have payment id for the orders
                // check them and update
                if(Optional.ofNullable(order.getPaymentId()).isPresent()) {
                    try {
                        APIContext apiContext = sellerInfoService.getApiContext(order.getSellerId());

                        Payment payment = Payment.get(apiContext, order.getPaymentId());

                        if(payment.getState().equals("approved")) {
                            order.setOrderState(OrderState.SUCCESS);
                        }
                        if(payment.getState().equals("failed")) {
                            order.setOrderState(OrderState.FAILED);
                        }
                        if(payment.getState().equals("created")) {
                            order.setOrderState(OrderState.CANCELED);
                        }
                    } catch (PayPalRESTException e) {
                        // TODO: log error message
                        logger.error("Failed to get payment information");
                    }
                }
            });
            orderRepository.saveAll(orders);

        }
        logger.info("End scheduling");
    }

}
