package com.sales.din.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaleDTO {

    @NotBlank(message = "O item da venda é obrigatório")
    private long productId;
    @NotBlank(message = "O campo quantidade é obrigatório")
    private int quantity;

}
