package ru.practicum.ewm.stats.server.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "apps")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class App {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 128)
    private String name;
}
