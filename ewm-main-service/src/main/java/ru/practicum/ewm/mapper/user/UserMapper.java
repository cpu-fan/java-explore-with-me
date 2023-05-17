package ru.practicum.ewm.mapper.user;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.user.UserRequestDto;
import ru.practicum.ewm.dto.user.UserResponseDto;
import ru.practicum.ewm.model.user.User;

@Component
public class UserMapper {

    public User toUser(UserRequestDto userRequestDto) {
        return new User(
                0,
                userRequestDto.getName(),
                userRequestDto.getEmail()
        );
    }

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
