package com.alextim.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity @Table(name = "user")
@Data @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(exclude = {"id", "address", "phones", "creationDate"}) @ToString()
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @CreationTimestamp
    private Date creationDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Singular
    private List<Phone> phones;


    public static enum Gender {
        MALE, FEMALE
    }
}
