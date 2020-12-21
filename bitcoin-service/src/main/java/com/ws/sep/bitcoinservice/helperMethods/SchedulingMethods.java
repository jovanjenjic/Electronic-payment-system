package com.ws.sep.bitcoinservice.helperMethods;

import com.ws.sep.bitcoinservice.controllers.CryptocurrencyController;
import com.ws.sep.bitcoinservice.dto.GetOrderResponseDTO;
import com.ws.sep.bitcoinservice.enums.TransactionStatus;
import com.ws.sep.bitcoinservice.model.PaymentInformation;
import com.ws.sep.bitcoinservice.repository.PaymentInformarmationRepository;
import com.ws.sep.bitcoinservice.utility.GlobalDataString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

@Component
public class SchedulingMethods {

    @Autowired
    PaymentInformarmationRepository paymentInformarmationRepository;

    Logger logger = LoggerFactory.getLogger(CryptocurrencyController.class);

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void setState() {
        logger.info("Start scheduling");
        Collection<PaymentInformation> listOfPayment = paymentInformarmationRepository.findAllByStatus(TransactionStatus.CANCELED.name(), TransactionStatus.PAID.name(), TransactionStatus.EXPIRED.name());

        for (PaymentInformation pi : listOfPayment) {
            if(pi.getCreatedAt() != null) {
                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Token " + pi.getCryptocurrencyPayment().getApiKey());

                HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
                ResponseEntity<GetOrderResponseDTO> response = restTemplate.exchange(GlobalDataString.SENDBOX_ORDERS + "/" + pi.getPaymentId(), HttpMethod.GET, entity, GetOrderResponseDTO.class);

                if(!pi.getStatus().equals(response.getBody().getStatus().toUpperCase())) {
                    pi.setStatus(response.getBody().getStatus().toUpperCase());
                    logger.info("Set status from: " + pi.getStatus() + " to " + response.getBody().getStatus());
                    paymentInformarmationRepository.save(pi);
                }
            }
        }
        logger.info("End scheduling");
    }

    public void setStateOfPayment(Long paymentId, String status) {
        PaymentInformation paymentInformation = paymentInformarmationRepository.findOneById(paymentId);
        if(paymentInformation.getStatus() != status) {
            paymentInformation.setStatus(status);
            paymentInformarmationRepository.save(paymentInformation);
        }
    }

}
