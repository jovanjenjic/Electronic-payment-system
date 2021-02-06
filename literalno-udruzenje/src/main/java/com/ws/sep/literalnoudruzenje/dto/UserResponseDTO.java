package com.ws.sep.literalnoudruzenje.dto;

import com.ws.sep.literalnoudruzenje.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String name;

    private String email;

    private Long pib;

    private String access_token;

    private Set<Roles> roles;

    private List<SubscriptionListItemDTO> subscriptionList;
}
