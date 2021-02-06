package com.ws.sep.literalnoudruzenje.services;

import com.ws.sep.literalnoudruzenje.dto.LoginDTO;
import com.ws.sep.literalnoudruzenje.dto.RegisterDTO;
import com.ws.sep.literalnoudruzenje.dto.SubscriptionListItemDTO;
import com.ws.sep.literalnoudruzenje.dto.UserResponseDTO;
import com.ws.sep.literalnoudruzenje.exceptions.SimpleException;
import com.ws.sep.literalnoudruzenje.mappers.UserMapper;
import com.ws.sep.literalnoudruzenje.model.*;
import com.ws.sep.literalnoudruzenje.repository.ItemRepository;
import com.ws.sep.literalnoudruzenje.repository.RolesRepository;
import com.ws.sep.literalnoudruzenje.repository.SubscriptionRepository;
import com.ws.sep.literalnoudruzenje.repository.UserRepository;
import com.ws.sep.literalnoudruzenje.utils.EncryptionDecryption;
import com.ws.sep.literalnoudruzenje.utils.JwtUtil;
import com.ws.sep.literalnoudruzenje.utils.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RestTemplate restTemplate;

    public UserResponseDTO registerUser(RegisterDTO registerDTO) throws SimpleException {
        User user = UserMapper.INSTANCE.mapRequestToUser(registerDTO);
        Roles userRole = rolesRepository.findByName(registerDTO.getRole()).orElseThrow(() -> new SimpleException(404, "User Role not set." ));

        user.setRoles(Collections.singleton(userRole));

        user = userRepository.save(user);

        // case if user is seller
        if(registerDTO.getRole().equals(RoleName.ROLE_SELLER)) {
            restTemplate.postForObject(Urls.KP_SELLER_URL + "/auth/signup", registerDTO, String.class);
        }

        UserResponseDTO userResponseDTO = UserMapper.INSTANCE.mapUserToResponse(user);

        userResponseDTO.setAccess_token(jwtUtil.generateToken(user));

        return userResponseDTO;
    }

    public ResponseEntity<?> loginUser(LoginDTO loginDTO) throws SimpleException {

        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());
        if(
                userOptional.isPresent()
                && loginDTO.getPassword().equals(EncryptionDecryption.decryptString(userOptional.get().getPassword()))
        ) {
            User user = userOptional.get();
            UserResponseDTO userResponseDTO = UserMapper.INSTANCE.mapUserToResponse(user);
            userResponseDTO.setAccess_token(jwtUtil.generateToken(user));
            return ResponseEntity.ok(userResponseDTO);
        }
        throw new SimpleException(404, "Invalid credentials");
    }

    public ResponseEntity<?> getCurrentUser(String token) throws SimpleException {
        Long userId = jwtUtil.extractUserId(token.substring(7));
        User user = userRepository.findById(userId).orElseThrow(() -> new SimpleException(404, "User not found"));

        List<Item> memberships = itemRepository.findAllByItemType(ItemType.MEMBERSHIP);

        List<SubscriptionListItemDTO> subscriptionListItemDTOList = new ArrayList<>();

        for(Item membership : memberships) {
            Optional<Subscription> subscription = subscriptionRepository.findByByItemAndAndUser(membership, user).stream().findFirst();
            if (subscription.isPresent()) {
                subscriptionListItemDTOList.add(new SubscriptionListItemDTO(membership.getDiscount(), membership.getName(), membership.getDescription()));
            }
        }

        UserResponseDTO userResponseDTO = UserMapper.INSTANCE.mapUserToResponse(user);
        userResponseDTO.setAccess_token(jwtUtil.generateToken(user));
        userResponseDTO.setSubscriptionList(subscriptionListItemDTOList);

        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }


}
