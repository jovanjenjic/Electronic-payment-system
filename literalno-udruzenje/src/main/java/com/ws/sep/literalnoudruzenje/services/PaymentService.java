package com.ws.sep.literalnoudruzenje.services;

import com.ws.sep.literalnoudruzenje.dto.BankInfoDTO;
import com.ws.sep.literalnoudruzenje.dto.BtcInfoDTO;
import com.ws.sep.literalnoudruzenje.dto.KpLoginResponse;
import com.ws.sep.literalnoudruzenje.dto.PaypalInfoDTO;
import com.ws.sep.literalnoudruzenje.exceptions.SimpleException;
import com.ws.sep.literalnoudruzenje.model.PaymentType;
import com.ws.sep.literalnoudruzenje.model.PaymentTypes;
import com.ws.sep.literalnoudruzenje.model.RoleName;
import com.ws.sep.literalnoudruzenje.model.User;
import com.ws.sep.literalnoudruzenje.repository.PaymentTypesRepository;
import com.ws.sep.literalnoudruzenje.repository.RolesRepository;
import com.ws.sep.literalnoudruzenje.repository.UserRepository;
import com.ws.sep.literalnoudruzenje.utils.EncryptionDecryption;
import com.ws.sep.literalnoudruzenje.utils.JwtUtil;
import com.ws.sep.literalnoudruzenje.utils.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    PaymentTypesRepository paymentTypesRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RestTemplate restTemplate;

    public void checkIfSeller(User user) throws SimpleException {
        user.getRoles().stream()
                .filter(v -> v.getName().equals(RoleName.ROLE_SELLER))
                .findFirst()
                .orElseThrow(() -> new SimpleException(403, "User is not seller"));
    }

    public void checkIfPaymentTypeAlreadyExists(User user, Object paymentInfo) throws SimpleException {
        PaymentType paymentType = null;

        if(paymentInfo.getClass() == PaypalInfoDTO.class) paymentType = PaymentType.PAYPAL;
        else if(paymentInfo.getClass() == BtcInfoDTO.class) paymentType = PaymentType.BTC;
        else if(paymentInfo.getClass() == BankInfoDTO.class) paymentType = PaymentType.BANK;
        else throw new SimpleException(409, "Weird payment object");


        PaymentType finalPaymentType = paymentType;
        Optional<PaymentTypes> paymentTypeOptional =
                user.getTypes().stream()
                .filter(v -> v.getType().equals(finalPaymentType))
                .findFirst();

        if(paymentTypeOptional.isPresent()) throw new SimpleException(409, "Payment method already exists");
    }

    public void updateUserPaymentTypes(User user, Object paymentInfo) throws SimpleException {
        PaymentType paymentType = null;

        if(paymentInfo.getClass() == PaypalInfoDTO.class) paymentType = PaymentType.PAYPAL;
        else if(paymentInfo.getClass() == BtcInfoDTO.class) paymentType = PaymentType.BTC;
        else if(paymentInfo.getClass() == BankInfoDTO.class) paymentType = PaymentType.BANK;
        else throw new SimpleException(409, "Weird payment object");

        PaymentTypes type = paymentTypesRepository.findByType(paymentType)
                .orElseThrow(() -> new SimpleException(404, "Payment type not found"));

        user.getTypes().add(type);
        userRepository.save(user);
    }

    public String loginToKp(User user) throws SimpleException {
        try {
            HashMap<String, String> reqBody = new HashMap<>();
            reqBody.put("email", user.getEmail());
            reqBody.put("password", EncryptionDecryption.decryptString(user.getPassword()));

            KpLoginResponse kpLoginResponse = restTemplate
                    .postForObject(Urls.KP_SELLER_URL + "/auth/signin", reqBody, KpLoginResponse.class);

            return "Bearer " + kpLoginResponse.getAccessToken();
        } catch (Exception e) {
            throw new SimpleException(403, "Failed to log user to KP");
        }
    }

    public ResponseEntity<?> registerPayment(Object paymentInfo, String token) {
        Long userId = jwtUtil.extractUserId(token.substring(7));

        User user = userRepository.findById(userId).orElseThrow(() -> new SimpleException(404, "User not found"));
        // check if user is seller
        checkIfSeller(user);
        // check if payment is already defined
        checkIfPaymentTypeAlreadyExists(user, paymentInfo);

        String kpToken = loginToKp(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kpToken);

        String url = "";

        if(paymentInfo.getClass() == PaypalInfoDTO.class) url = Urls.PAYPAL_URL + "/addPayment";
        if(paymentInfo.getClass() == BtcInfoDTO.class) url = Urls.BTC_URL + "/addPayment";
        if(paymentInfo.getClass() == BankInfoDTO.class) url = Urls.BANK_URL + "/merchant/";


        try {
            HttpEntity<Object> httpEntity = new HttpEntity<>(paymentInfo, headers);
            restTemplate.postForObject(url, httpEntity, String.class);

            HashMap<String, String> response = new HashMap<>();
            updateUserPaymentTypes(user, paymentInfo);
            response.put("message", "payment added successfully!");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RestClientResponseException e) {
            throw new SimpleException(409, "Error occured while adding payment type");
        }
    }

}
