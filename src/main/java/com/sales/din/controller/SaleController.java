package com.sales.din.controller;

import com.sales.din.dto.ResponseDTO;
import com.sales.din.dto.SaleDTO;
import com.sales.din.dto.SaleInfoDTO;
import com.sales.din.exceptions.InvalidOperationException;
import com.sales.din.exceptions.NoItemException;
import com.sales.din.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity(new ResponseDTO("", saleService.findAll()) , HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable long id) {
        try {
            return new ResponseEntity<>( new ResponseDTO<>("", saleService.getById(id)), HttpStatus.OK);
        } catch (NoItemException | InvalidOperationException error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity post(@RequestBody SaleDTO saleDTO) {
        try {
            long id = saleService.save(saleDTO);
            return new ResponseEntity<>(new ResponseDTO<>("Venda realizada com sucesso: " , id), HttpStatus.CREATED);
        } catch (NoItemException | InvalidOperationException error) {
            return new ResponseEntity<>(new ResponseDTO<>(error.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (Exception error) {
            return new ResponseEntity<>(new ResponseDTO<>(error.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
