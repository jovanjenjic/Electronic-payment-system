package com.ws.sep.literalnoudruzenje.services;

import com.ws.sep.literalnoudruzenje.dto.LoginDTO;
import com.ws.sep.literalnoudruzenje.dto.RegisterDTO;
import com.ws.sep.literalnoudruzenje.dto.UserResponseDTO;
import com.ws.sep.literalnoudruzenje.exceptions.SimpleException;
import com.ws.sep.literalnoudruzenje.mappers.UserMapper;
import com.ws.sep.literalnoudruzenje.model.RoleName;
import com.ws.sep.literalnoudruzenje.model.Roles;
import com.ws.sep.literalnoudruzenje.model.User;
import com.ws.sep.literalnoudruzenje.repository.RolesRepository;
import com.ws.sep.literalnoudruzenje.repository.UserRepository;
import com.ws.sep.literalnoudruzenje.utils.EncryptionDecryption;
import com.ws.sep.literalnoudruzenje.utils.JwtUtil;
import com.ws.sep.literalnoudruzenje.utils.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository rolesRepository;

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
}
