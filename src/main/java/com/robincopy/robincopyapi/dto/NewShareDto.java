package com.robincopy.robincopyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewShareDto {

    @NotNull
    private String userId;

    @NotNull
    private String symbol;

    @NotNull
    private Integer quantity;
}
