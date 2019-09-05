package com.alextim.socket;

import com.alextim.cache.MyCache;
import com.alextim.client.SocketMessageClient;
import com.alextim.domain.User;
import com.alextim.messages.*;
import com.alextim.repository.UserRepositoryImpl;
import com.alextim.service.ServiceDB;
import com.alextim.service.ServiceDbImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class DBServiceSocketMessageClient extends SocketMessageClient{

    private final ServiceDB serviceDB;

    public DBServiceSocketMessageClient(ServiceDB serviceDB, String[] args) throws IOException {
        super(args);
        this.serviceDB = serviceDB;
    }

    public static void main(String[] args ) throws IOException {
        DBServiceSocketMessageClient client
                = new DBServiceSocketMessageClient(
                        new ServiceDbImpl(new UserRepositoryImpl(),  new MyCache<>(10,
                TimeUnit.SECONDS.toMillis(1),
                TimeUnit.SECONDS.toMillis(1))), args);
        client.start();

        client.sendMessage(new RegistrationMessage(client.getID()));
    }

    @Override
    public Message handlerMessage(Message message) {
        if(message instanceof SaveUserMassage) {
            SaveUserMassage saveUserMassage = (SaveUserMassage)message;
            User user = UserDto.toUser(saveUserMassage.getUser());
            try {
                serviceDB.save(user);
            }
            catch (RuntimeException e) {
                return new ErrorMessage(e.getMessage());
            }
            UserDto userDto = UserDto.toUserDto(user);
            return new SavedUserMassage(userDto);
        }
        else if(message instanceof GetAllUsersMessage) {
            List<User> users;
            try {
                users = serviceDB.loadAll(0, Integer.MAX_VALUE);
                users.forEach((user) -> user.setPhones(serviceDB.getPhone(user.getId())));
            }
            catch (RuntimeException e) {
                return new ErrorMessage(e.getMessage());
            }
            List<UserDto> usersDto =  users.stream().map(UserDto::toUserDto).collect(Collectors.toList());
            return new UsersMessage(usersDto);
        }
        else if(message instanceof OkMessage) {
            log.info("{}: Message Ok!", ID);
            return null;
        }
        else
            return new UnknownMessage();
    }
}
