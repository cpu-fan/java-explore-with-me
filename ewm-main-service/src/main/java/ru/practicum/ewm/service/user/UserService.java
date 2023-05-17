package ru.practicum.ewm.service.user;

import ru.practicum.ewm.dto.user.UserRequestDto;
import ru.practicum.ewm.dto.user.UserResponseDto;
import ru.practicum.ewm.model.user.User;

import java.util.Collection;

public interface UserService {

    Collection<UserResponseDto> getUsers(Collection<Long> ids, int from, int size);

    UserResponseDto createUser(UserRequestDto user);

    void deleteUser(long id);

    User getUserById(long id);
}
