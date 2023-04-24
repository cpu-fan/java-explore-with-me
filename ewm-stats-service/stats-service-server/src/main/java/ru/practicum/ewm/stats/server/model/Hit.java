package ru.practicum.ewm.stats.server.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private App app;

    @Column(nullable = false, length = 1024)
    private String uri;

    @Column(nullable = false, length = 16)
    private String ip;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
