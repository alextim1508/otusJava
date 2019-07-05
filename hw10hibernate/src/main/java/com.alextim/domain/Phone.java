package com.alextim.domain;

import lombok.*;

import javax.persistence.*;

import java.util.List;

import static com.alextim.domain.Phone.COLUMN_NUMBER;
import static com.alextim.domain.Phone.TABLE;

@Entity @Table(name = TABLE, uniqueConstraints= @UniqueConstraint(columnNames={COLUMN_NUMBER}))
@Data @NoArgsConstructor @RequiredArgsConstructor @EqualsAndHashCode(exclude = {"id", "user"}) @ToString(exclude = {"user"})
public class Phone {

    public static final String TABLE = "Phone";
    public static final String COLUMN_NUMBER = "number";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = COLUMN_NUMBER)
    private String number;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "phones")
    private List<User> user;
}
