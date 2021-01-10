package com.ws.sep.literalnoudruzenje.controllers;

import com.ws.sep.literalnoudruzenje.dto.CreateItemDTO;
import com.ws.sep.literalnoudruzenje.exceptions.SimpleException;
import com.ws.sep.literalnoudruzenje.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @PostMapping(value="/add")
    public ResponseEntity<?> addItem(@RequestBody @Valid CreateItemDTO createItemDTO, @RequestHeader("Authorization") String token) throws SimpleException {
        return itemService.addItem(createItemDTO, token);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllItems() {
        return itemService.getAllItems();
    }
}
