package com.alextim.messages;

import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

@Data @AllArgsConstructor
public class UserDto implements Serializable {

    @Setter(AccessLevel.NONE)
    private long id;

    private String name;

    private String cryptPassword;

    private String gender;

    private String address;

    private String phones;

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                "***",
                user.getGender().toString(),
                user.getAddress().getStreet(),
                user.getPhones().stream().map(Phone::getNumber).reduce("", (s1, s2) -> s1.isEmpty() ? s2 : (s1 + ", " + s2)));
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .cryptPassword(userDto.getCryptPassword())
                .role("GUEST")
                .role("USER")
                .gender(User.Gender.valueOf(userDto.getGender()))
                .address(new Address(userDto.getAddress()))
                .phones(Arrays.stream(userDto.getPhones().split(", ")).map(Phone::new).collect(Collectors.toList()))
                .build();
    }
}
