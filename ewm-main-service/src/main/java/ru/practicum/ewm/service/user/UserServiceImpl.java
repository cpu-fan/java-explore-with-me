package ru.practicum.ewm.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.UserRequestDto;
import ru.practicum.ewm.dto.user.UserResponseDto;
import ru.practicum.ewm.errorhandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.user.UserMapper;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper mapper;

    @Override
    public Collection<UserResponseDto> getUsers(Collection<Long> ids, int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<User> userList = ids != null
                ? userRepository.findAllByIdIn(ids, page)
                : userRepository.findAll(page).getContent();

        log.info("Запрошен список пользователей");
        return userList.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userDto) {
        User user = mapper.toUser(userDto);
        user = userRepository.save(user);
        log.info("Создан пользователь {}", user);
        return mapper.toDto(user);
    }

    @Override
    public void deleteUser(long id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        log.info("Удален пользователь: {}", user);
    }

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            String message = "Пользователь id = " + id + " не найден";
            log.error(message);
            throw new NotFoundException(message);
        });
    }
}
