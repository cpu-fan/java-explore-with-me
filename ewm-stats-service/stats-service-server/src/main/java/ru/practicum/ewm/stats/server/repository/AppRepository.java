package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.stats.server.model.App;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {

    Optional<App> findByName(String name);
}
