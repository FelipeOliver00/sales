package com.sales.din.service;

import com.sales.din.dto.UserDTO;
import com.sales.din.dto.UserResponseDTO;
import com.sales.din.entity.User;
import com.sales.din.exceptions.NoItemException;
import com.sales.din.repository.UserRepository;
import com.sales.din.security.SecurityConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private ModelMapper mapper = new ModelMapper();

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(user ->
                new UserResponseDTO(user.getId(), user.getName(), user.getUsername(), user.isEnabled())).collect(Collectors.toList());
    }

    public UserDTO save(UserDTO user) {
        user.setPassword(SecurityConfiguration.passwordEncoder().encode(user.getPassword()));
        User userToSave = mapper.map(user, User.class);
        userRepository.save(userToSave);
        return new UserDTO(userToSave.getId(), userToSave.getName(), userToSave.getUsername(), userToSave.getPassword(), userToSave.isEnabled());
    }

    public UserDTO findById(long id) {
        Optional<User> optional = userRepository.findById(id);

        if (!optional.isPresent()){
            throw new NoItemException("Usuário não encontrado!");
        }
        User user = optional.get();

        return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getPassword(), user.isEnabled());
    }

    public UserDTO update(UserDTO user) {
        user.setPassword(SecurityConfiguration.passwordEncoder().encode(user.getPassword()));
        User userToSave = mapper.map(user, User.class);

        Optional<User> userToEdit = userRepository.findById(userToSave.getId());

        if (!userToEdit.isPresent()){
            throw new NoItemException("Usuário não encontrado!");
        }

        userRepository.save(userToSave);
        return new UserDTO(userToSave.getId(), userToSave.getName(), userToSave.getUsername(), userToSave.getPassword(), userToSave.isEnabled());
    }

    public void deleteById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoItemException("Usuário não encontrado!"));
        userRepository.deleteById(user.getId());
    }

    public User getByUserName(String username) {
        return userRepository.findUserByUserName(username);
    }
}
