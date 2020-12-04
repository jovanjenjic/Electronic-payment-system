package com.ws.sep.paypalservice.dto;

import com.ws.sep.paypalservice.enums.FieldType;
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
