package com.alextim.service;

import com.alextim.controller.MessageDto;
import com.alextim.controller.UserDto;
import com.alextim.domain.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.alextim.controller.UserDto.toUserDto;

@Service
@Data @RequiredArgsConstructor @Slf4j
public class FrontendServiceImpl implements FrontendService {

    private final SimpMessagingTemplate webSocket;

    @Override
    public void allUsers(List<User> users) {
        log.info("All users: {}", users);
        List<UserDto> usersDto = users.stream().map(UserDto::toUserDto).collect(Collectors.toList());
        webSocket.convertAndSend("/topic/response2", usersDto);
    }

    @Override
    public void addedUser(User user) {
        log.info("Added user: {}", user);
        UserDto userDto = toUserDto(user);
        webSocket.convertAndSend("/topic/response", userDto);
    }

    @Override
    public void errorHandler(Exception exception) {
        log.info("errorHandler: {}", exception);
        webSocket.convertAndSend("/topic/response3", new MessageDto(exception.getMessage()));
    }
}