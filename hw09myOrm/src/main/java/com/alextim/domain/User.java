package com.alextim.domain;

import com.alextim.repository.Id;
import lombok.*;

@Data @NoArgsConstructor @RequiredArgsConstructor @ToString @EqualsAndHashCode(exclude = "id")
public class User {

    @Id
    @Setter(AccessLevel.NONE)
    private long id = -1;

    @NonNull
    private String name;

    @NonNull
    private int age;
}

