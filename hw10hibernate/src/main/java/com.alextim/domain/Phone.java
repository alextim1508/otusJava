package com.alextim.domain;

import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "phone")
@Data @NoArgsConstructor @RequiredArgsConstructor @EqualsAndHashCode(exclude = {"id", "user"}) @ToString(exclude = {"user"})
public class Phone {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String number;

    @ManyToOne
    private User user;
}
