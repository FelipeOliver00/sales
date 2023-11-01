package com.sales.din.service;

import com.sales.din.dto.UserDTO;
import com.sales.din.entity.User;
import com.sales.din.exceptions.NoItemException;
import com.sales.din.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user ->
                new UserDTO(user.getId(), user.getName(), user.isEnabled())).collect(Collectors.toList());
    }

    public UserDTO save(User user) {
        userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.isEnabled());
    }

    public UserDTO findById(long id) {
        Optional<User> optional = userRepository.findById(id);

        if (!optional.isPresent()){
            throw new NoItemException("Usuário não encontrado!");
        }
        User user = optional.get();

        return new UserDTO(user.getId(), user.getName(), user.isEnabled());
    }

    public UserDTO update(User user) {
        Optional<User> userToEdit = userRepository.findById(user.getId());
        if (!userToEdit.isPresent()){
            throw new NoItemException("Usuário não encontrado!");
        }

        userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.isEnabled());
    }

    public void deleteById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoItemException("Usuário não encontrado!"));
        userRepository.deleteById(user.getId());
    }
}
