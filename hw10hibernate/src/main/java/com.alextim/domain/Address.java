package com.alextim.domain;

import lombok.*;

import javax.persistence.*;

@Entity @Table(name = "address")
@Data @NoArgsConstructor @RequiredArgsConstructor @EqualsAndHashCode(exclude = {"id", "user"}) @ToString(exclude = {"user"})
public class Address {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String street;

    @OneToOne(mappedBy = "address")
    private User user;
}
