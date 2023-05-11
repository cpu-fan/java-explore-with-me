package ru.practicum.ewm.model.event;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false, length = 2000)
    private String annotation;

    @Column(nullable = false, length = 7000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "location_lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))
    })
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "is_paid")
    private boolean paid;

    @Column(name = "created_date")
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private long participantLimit;

    @Column(name = "is_request_moderation")
    private boolean requestModeration;

    @Column(name = "published_date")
    private LocalDateTime publishedOn;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventState state = EventState.PENDING;
}
