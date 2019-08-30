package com.alextim.controller;

import com.alextim.domain.Address;
import com.alextim.domain.Phone;
import com.alextim.domain.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

@NoArgsConstructor @AllArgsConstructor
@Data
public class UserDto implements Serializable{

    @Setter(AccessLevel.NONE)
    private long id;

    @NotNull @NotEmpty @NotBlank
    private String name;

    @NotNull @NotEmpty @NotBlank
    private String cryptPassword;

    @NotNull @NotEmpty @NotBlank
    private String gender;

    @NotNull @NotEmpty @NotBlank
    private String address;

    @NotNull @NotEmpty @NotBlank
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
