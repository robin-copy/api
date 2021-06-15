package com.robincopy.robincopyapi.dto.share;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareDto {

    private String userId;

    private String symbol;

    private Integer quantity;

    private Double price;


}
