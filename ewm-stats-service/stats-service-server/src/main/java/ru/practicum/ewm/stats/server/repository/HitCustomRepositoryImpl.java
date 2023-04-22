package ru.practicum.ewm.stats.server.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.dto.HitResponseDto;
import ru.practicum.ewm.stats.server.model.QHit;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.Projections.bean;

@Repository
@RequiredArgsConstructor
public class HitCustomRepositoryImpl implements HitCustomRepository {

    private final EntityManager em;

    @Override
    public List<HitResponseDto> findHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        QHit hit = QHit.hit;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        StringPath app = hit.app;
        StringPath uri = hit.uri;
        StringPath ip = hit.ip;

        NumberExpression<Long> count = unique ? ip.countDistinct().as("hits") : ip.count().as("hits");
        BooleanExpression predicate = hit.timestamp.between(start, end);
        if (!uris.isEmpty()) {
            predicate = predicate.and(uri.in(uris));
        }

        return queryFactory.select(bean(HitResponseDto.class, app, uri, count))
                .from(hit).where(predicate)
                .groupBy(app, uri)
                .fetch();
    }
}
