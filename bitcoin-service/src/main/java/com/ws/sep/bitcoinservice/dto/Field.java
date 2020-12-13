package com.ws.sep.bitcoinservice.dto;

import com.ws.sep.bitcoinservice.utility.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Field {

        private FieldType type;

        private String fieldName;
    }
