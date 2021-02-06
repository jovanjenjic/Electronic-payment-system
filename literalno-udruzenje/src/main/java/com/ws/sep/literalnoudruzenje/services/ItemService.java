package com.ws.sep.literalnoudruzenje.services;

import com.ws.sep.literalnoudruzenje.dto.CreateItemDTO;
import com.ws.sep.literalnoudruzenje.dto.ItemResponseDTO;
import com.ws.sep.literalnoudruzenje.exceptions.SimpleException;
import com.ws.sep.literalnoudruzenje.mappers.ItemMapper;
import com.ws.sep.literalnoudruzenje.model.Item;
import com.ws.sep.literalnoudruzenje.model.ItemType;
import com.ws.sep.literalnoudruzenje.model.RoleName;
import com.ws.sep.literalnoudruzenje.model.User;
import com.ws.sep.literalnoudruzenje.repository.ItemRepository;
import com.ws.sep.literalnoudruzenje.repository.UserRepository;
import com.ws.sep.literalnoudruzenje.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;

    public void checkIfSeller(User user) throws SimpleException {
        user.getRoles().stream()
                .filter(v -> v.getName().equals(RoleName.ROLE_SELLER))
                .findFirst()
                .orElseThrow(() -> new SimpleException(403, "User is not seller"));
    }

    public ResponseEntity<?> addItem(CreateItemDTO createItemDTO, String token) throws SimpleException {
        Long userId = jwtUtil.extractUserId(token.substring(7));

        User user = userRepository.findById(userId).orElseThrow(() -> new SimpleException(404, "User not found"));
        // check if user is seller
        checkIfSeller(user);

        Item item = ItemMapper.INSTANCE.mapRequestToItem(createItemDTO, user);
        item = itemRepository.save(item);

        return ResponseEntity.ok(ItemMapper.INSTANCE.mapItemToResponse(item));
    }

    public ResponseEntity<?> getAllItems() {
        ItemMapper itemMapper = ItemMapper.INSTANCE;
        List<ItemResponseDTO> itemResponseDTOList = itemRepository.findAllByItemType(ItemType.MAGAZINE).stream().map(i ->  itemMapper.mapItemToResponse(i)).collect(Collectors.toList());
        return new ResponseEntity<>(itemResponseDTOList, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllMemberships() {
        ItemMapper itemMapper = ItemMapper.INSTANCE;
        List<ItemResponseDTO> itemResponseDTOList = itemRepository.findAllByItemType(ItemType.MEMBERSHIP).stream().map(i ->  itemMapper.mapItemToResponse(i)).collect(Collectors.toList());
        return new ResponseEntity<>(itemResponseDTOList, HttpStatus.OK);
    }


}
