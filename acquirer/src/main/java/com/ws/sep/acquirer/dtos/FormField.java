package com.ws.sep.acquirer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormField
{

    private FieldType type;

    private String fieldId;


}
