package com.sales.din.controller;

import com.sales.din.dto.ResponseDTO;
import com.sales.din.entity.User;
import com.sales.din.exceptions.NoItemException;
import com.sales.din.service.UserService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable long id) { return new ResponseEntity<>(userService.findById(id), HttpStatus.OK); }

    @PostMapping
    public ResponseEntity post(@RequestBody User user) {
        try {
            return new ResponseEntity(userService.save(user), HttpStatus.CREATED);
        } catch (Exception error) {
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity put(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
        } catch (NoItemException error) {
            return new ResponseEntity<>(new ResponseDTO<>(error.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception error) {
            return new ResponseEntity<>(new ResponseDTO<>(error.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {

        try {
            userService.deleteById(id);
            return new ResponseEntity<>(new ResponseDTO<>("Usuário removido com sucesso!"), HttpStatus.OK);
        } catch (EmptyResultDataAccessException error) {
            return new ResponseEntity<>(new ResponseDTO<>("Não foi possível localiar o usuário!"), HttpStatus.BAD_REQUEST);
        }
        catch (Exception error) {
            return new ResponseEntity(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
