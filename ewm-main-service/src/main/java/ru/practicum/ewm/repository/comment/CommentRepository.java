package ru.practicum.ewm.repository.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.comment.CountEventComments;
import ru.practicum.ewm.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByEventId(long eventId, Pageable page);

    long countByEventId(long eventId);

    @Query("select new ru.practicum.ewm.dto.comment.CountEventComments(c.event.id, count(c.id)) " +
            "from Comment c " +
            "where c.event.id in ?1 " +
            "group by c.event.id")
    List<CountEventComments> countEventComments(List<Long> ids);
}
