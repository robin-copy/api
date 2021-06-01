package com.robincopy.robincopyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareAddedDto {

    private String userId;

    private String symbol;

    private Integer quantity;
}
