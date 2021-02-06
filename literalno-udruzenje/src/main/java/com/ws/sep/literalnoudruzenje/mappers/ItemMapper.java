package com.ws.sep.literalnoudruzenje.mappers;

import com.ws.sep.literalnoudruzenje.dto.CreateItemDTO;
import com.ws.sep.literalnoudruzenje.dto.ItemResponseDTO;
import com.ws.sep.literalnoudruzenje.model.Item;
import com.ws.sep.literalnoudruzenje.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "createItemDTO.price", target = "price")
    @Mapping(source = "createItemDTO.name", target = "name")
    @Mapping(source = "createItemDTO.description", target = "description")
    @Mapping(source = "createItemDTO.itemType", target = "itemType")
    @Mapping(source = "createItemDTO.discount", target = "discount")
    @Mapping(source="user", target = "user")
    Item mapRequestToItem(CreateItemDTO createItemDTO, User user);

    @Mapping(source="user", target = "user_id", qualifiedByName = "mapUserToId")
    ItemResponseDTO mapItemToResponse(Item item);

    @Named("mapUserToId")
    static Long mapUserToId(User user) {
        return user.getId();
    }
}
