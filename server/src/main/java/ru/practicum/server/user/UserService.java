package ru.practicum.server.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.exception.UserAlreadyExistException;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;


import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto create(UserDto dto) {
        User user = UserMapper.toUser(dto);
        User newUser = userRepository.save(user);
        return UserMapper.toUserDto(newUser);
    }

    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return UserMapper.toUserDto(user);
    }

    @Transactional
    public UserDto update(Integer id, UserDto dto) {
        User user = UserMapper.toUser(dto);
        User updateUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        if (userRepository.existsByEmailAndIdNot(user.getEmail(), id)) {
            throw new UserAlreadyExistException("Пользователь с такой почтой уже существует");
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            updateUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updateUser.setEmail(user.getEmail());
        }

        User savedUser = userRepository.save(updateUser);
        return UserMapper.toUserDto(savedUser);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
