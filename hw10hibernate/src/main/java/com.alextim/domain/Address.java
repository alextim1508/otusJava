package com.alextim.domain;

import lombok.*;

import javax.persistence.*;

import static com.alextim.domain.Address.COLUMN_STREET;
import static com.alextim.domain.Address.TABLE;

@Entity @Table(name = TABLE, uniqueConstraints= @UniqueConstraint(columnNames={COLUMN_STREET}))
@Data @NoArgsConstructor @RequiredArgsConstructor @EqualsAndHashCode(exclude = {"id", "user"}) @ToString(exclude = {"user"})
public class Address implements DataSet {

    public static final String TABLE = "Address";
    public static final String COLUMN_STREET = "street";

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = COLUMN_STREET)
    private String street;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private User user;
}
