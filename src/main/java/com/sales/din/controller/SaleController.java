package com.sales.din.controller;

import com.sales.din.dto.SaleDTO;
import com.sales.din.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping
    public ResponseEntity post(@RequestBody SaleDTO saleDTO) {
        try {
            long id = saleService.save(saleDTO);
            return new ResponseEntity<>("Venda realizada com sucesso: " + id, HttpStatus.CREATED);
        } catch (Exception error) {
            return new ResponseEntity(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
