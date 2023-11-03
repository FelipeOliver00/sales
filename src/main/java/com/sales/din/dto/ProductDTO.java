package com.sales.din.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    @NotBlank(message = "O campo descrição é obrigatório")
    private String description;
    @NotNull(message = "O campo price é obrigatório")
    private BigDecimal price;
    @NotNull(message = "O campo quantity é obrigatório")
    @Min(1)
    private int quantity;
}
