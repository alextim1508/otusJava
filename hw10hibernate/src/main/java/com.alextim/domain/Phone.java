package com.alextim.domain;

import lombok.*;

import javax.persistence.*;

import static com.alextim.domain.Phone.COLUMN_NUMBER;
import static com.alextim.domain.Phone.TABLE;

@Entity @Table(name = TABLE, uniqueConstraints= @UniqueConstraint(columnNames={COLUMN_NUMBER}))
@Data @NoArgsConstructor @RequiredArgsConstructor @EqualsAndHashCode(exclude = {"id", "user"}) @ToString(exclude = {"user"})
public class Phone implements DataSet {

    public static final String TABLE = "Phone";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_USER_ID = "user_id";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = COLUMN_NUMBER)
    private String number;

    @ManyToOne @JoinColumn(name = COLUMN_USER_ID)
    private User user;
}
