package ru.practicum.ewm.stats.server.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 256)
    private String app;

    @Column(nullable = false, length = 1024)
    private String uri;

    @Column(nullable = false, length = 16)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
