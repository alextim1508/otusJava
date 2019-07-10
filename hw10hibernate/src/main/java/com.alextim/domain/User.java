package com.alextim.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.alextim.domain.User.COLUMN_NAME;
import static com.alextim.domain.User.TABLE;

@Entity @Table(name = TABLE, uniqueConstraints= @UniqueConstraint(columnNames={COLUMN_NAME}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(exclude = {"id", "address", "phones", "creationDate"}) @ToString()
public class User {

    public static final String TABLE = "User";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_CREATION_DATE = "creationDate";
    public static final String COLUMN_ADDRESS_ID = "address_id";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = COLUMN_NAME)
    private String name;

    @Column(name = COLUMN_GENDER) @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = COLUMN_CREATION_DATE) @CreationTimestamp
    private Date creationDate;

    @OneToOne(cascade = CascadeType.ALL) @JoinColumn(name = COLUMN_ADDRESS_ID)
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @Singular
    private List<Phone> phones;

    public enum Gender {
        MALE, FEMALE
    }
}
