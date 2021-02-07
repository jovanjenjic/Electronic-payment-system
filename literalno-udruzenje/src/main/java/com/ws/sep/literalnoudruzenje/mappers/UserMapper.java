package com.ws.sep.literalnoudruzenje.mappers;

import com.ws.sep.literalnoudruzenje.dto.RegisterDTO;
import com.ws.sep.literalnoudruzenje.dto.UserResponseDTO;
import com.ws.sep.literalnoudruzenje.model.User;
import com.ws.sep.literalnoudruzenje.utils.EncryptionDecryption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "password", target = "password", qualifiedByName = "passwordPrepare")
    User mapRequestToUser(RegisterDTO registerDTO);

    @Named("passwordPrepare")
    static String preparePassword(String password) {
        return EncryptionDecryption.encryptString(password);
    }

    UserResponseDTO mapUserToResponse(User user);

}
