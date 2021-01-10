package com.ws.sep.literalnoudruzenje.services;

import com.google.gson.Gson;
import com.ws.sep.literalnoudruzenje.dto.*;
import com.ws.sep.literalnoudruzenje.exceptions.SimpleException;
import com.ws.sep.literalnoudruzenje.model.*;
import com.ws.sep.literalnoudruzenje.repository.*;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    PaymentTypesRepository paymentTypesRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RestTemplate restTemplate;

    private void checkIfSeller(User user) throws SimpleException {
        user.getRoles().stream()
                .filter(v -> v.getName().equals(RoleName.ROLE_SELLER))
                .findFirst()
                .orElseThrow(() -> new SimpleException(403, "User is not seller"));
    }

    private void checkIfPaymentTypeAlreadyExists(User user, Object paymentInfo) throws SimpleException {
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

    private void checkIfSellerHasPaymentType(User user, PaymentType paymentType) throws SimpleException {
        user.getTypes().stream()
                .filter(v -> v.getType().equals(paymentType))
                .findFirst()
                .orElseThrow(() -> new SimpleException(404, "Seller does not have provided type"));
    }

    private void updateUserPaymentTypes(User user, Object paymentInfo) throws SimpleException {
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

    private String loginToKp(User user) throws SimpleException {
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

    // TODO: Check bank how to extract this
    private Long extractKpId(String paymentJSONResponse, PaymentType paymentType) {
        if(paymentType.equals(PaymentType.PAYPAL)) {
            return new Gson().fromJson(paymentJSONResponse, PaymentResponseDTO.class).getKp_id();
        }
        if(paymentType.equals(PaymentType.BTC)) {
            return new Gson().fromJson(paymentJSONResponse, PaymentResponseDTO.class).getKp_id();
        }
        if(paymentType.equals(PaymentType.BANK)) {
            // TODO: And check here
        }
        return 0L;
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


    public ResponseEntity<?> createOrder(CreateOrderDTO createOrderDTO, String token) throws SimpleException {
        // id used to set user which has order
        Long userId = jwtUtil.extractUserId(token.substring(7));

        User user = userRepository.findById(userId).orElseThrow(() -> new SimpleException(404, "User not found"));

        Item item = itemRepository.findById(createOrderDTO.getItemId()).orElseThrow(() -> new SimpleException(404, "Item not found"));

        // get seller for the item
        User seller = item.getUser();

        // Check if seller has payment type provided for the order
        checkIfSellerHasPaymentType(seller, createOrderDTO.getPaymentType());

        String kpToken = loginToKp(seller);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kpToken);

        // start flow of the order creation
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setCurrency(createOrderDTO.getCurrency());
        order.setItem(item);
        order.setItemsCount(createOrderDTO.getItemsCount());
        order.setPaymentType(createOrderDTO.getPaymentType());
        order.setPrice(createOrderDTO.getPrice());
        order.setOrderState(OrderState.CREATED);
        order.setDescription(item.getName() + " / " +item.getDescription());
        order.setUser(user);
        // save order to the DB
        order = orderRepository.save(order);

        String url = "";
        Object orderRequest = null;
        if(createOrderDTO.getPaymentType().equals(PaymentType.PAYPAL)){
            url = Urls.PAYPAL_URL + "/pay";
            orderRequest = new PaypalCreateOrderDTO(createOrderDTO, order);
        }
        else if(createOrderDTO.getPaymentType().equals(PaymentType.BTC)) {
            url = Urls.BTC_URL + "/createOrder";
            orderRequest = new BtcCreateOrderDTO(createOrderDTO, order);
        }
        else if(createOrderDTO.getPaymentType().equals(PaymentType.BANK)) {
            url = Urls.BANK_URL + "/merchant/create";
            orderRequest = new BankCreateOrderDTO(createOrderDTO, order);
        } else throw new SimpleException(409, "Weird payment type");

        try {
            HttpEntity<Object> httpEntity = new HttpEntity<>(orderRequest, headers);
            String response = restTemplate.postForObject(url, httpEntity, String.class);
            order.setKp_id(extractKpId(response, createOrderDTO.getPaymentType()));
            orderRepository.save(order);
            return ResponseEntity.ok(response);

        } catch (RestClientResponseException e) {
            System.out.println(e.getMessage());
            order.setOrderState(OrderState.FAILED);
            orderRepository.save(order);
            throw new SimpleException(409, "Error occurred while creating order type");
        }
    }

    public ResponseEntity<?> executePaypalPayment(PaypalExecuteDTO paypalExecuteDTO, Long orderId) throws SimpleException {

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new SimpleException(404, "Order not found"));

        // seller for the specified item
        User seller = order.getItem().getUser();

        String kpToken = loginToKp(seller);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kpToken);

        String url = Urls.PAYPAL_URL + "/pay/" + order.getKp_id() + "/success";

        try {
            HttpEntity<PaypalExecuteDTO> httpEntity = new HttpEntity<>(paypalExecuteDTO, headers);
            String response = restTemplate.postForObject(url, httpEntity, String.class);
            order.setOrderState(OrderState.SUCCESS);
            orderRepository.save(order);
            return ResponseEntity.ok(response);

        } catch (RestClientResponseException e) {
            System.out.println(e.getMessage());
            order.setOrderState(OrderState.FAILED);
            orderRepository.save(order);
            throw new SimpleException(409, "Error occurred while executing payment");
        }
    }

    public ResponseEntity<?> cancelPaypalPayment(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new SimpleException(404, "Order not found"));

        // seller for the specified item
        User seller = order.getItem().getUser();

        String cancelUrl = Urls.PAYPAL_URL + "/pay/" + order.getKp_id() + "/cancel";

        String kpToken = loginToKp(seller);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kpToken);

        try {
            HttpEntity<Object> httpEntity = new HttpEntity<>(new HashMap<String, String>(), headers);
            String response = restTemplate.postForObject(cancelUrl, httpEntity, String.class);
            order.setOrderState(OrderState.CANCELED);
            orderRepository.save(order);
            return ResponseEntity.ok(response);

        } catch (RestClientResponseException e) {
            System.out.println(e.getMessage());
            order.setOrderState(OrderState.FAILED);
            orderRepository.save(order);
            throw new SimpleException(409, "Error occurred while executing payment");
        }
    }

    public ResponseEntity<?> updateBtcTransaction(BtcExecuteDTO btcExecuteDTO) {
        Order order = orderRepository.findById(btcExecuteDTO.getTransactionId())
                .orElseThrow(() -> new SimpleException(404, "Order not found"));

        User seller = order.getItem().getUser();

        String url = Urls.BTC_URL + "/setStateOfTransaction";

        String kpToken = loginToKp(seller);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", kpToken);

        btcExecuteDTO.setTransactionId(order.getKp_id());

        try {
            HttpEntity<BtcExecuteDTO> httpEntity = new HttpEntity<>(btcExecuteDTO, headers);
            String response = restTemplate.postForObject(url, httpEntity, String.class);
            order.setOrderState(btcExecuteDTO.getIsSuccess() ? OrderState.SUCCESS :  OrderState.CANCELED);
            orderRepository.save(order);
            return ResponseEntity.ok(response);

        } catch (RestClientResponseException e) {
            System.out.println(e.getMessage());
            order.setOrderState(OrderState.FAILED);
            orderRepository.save(order);
            throw new SimpleException(409, "Error occurred while executing payment");
        }
    }

    public ResponseEntity<?> getSellerPaymentTypes(String token) {
        // id used to set user which has order
        Long userId = jwtUtil.extractUserId(token.substring(7));

        User user = userRepository.findById(userId).orElseThrow(() -> new SimpleException(404, "User not found"));

        return ResponseEntity.ok(user.getTypes());
    }
}
