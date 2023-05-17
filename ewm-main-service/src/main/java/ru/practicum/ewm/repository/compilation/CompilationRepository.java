package ru.practicum.ewm.repository.compilation;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.practicum.ewm.model.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long>, QuerydslPredicateExecutor<Compilation> {

    boolean existsById(long id);

    @NonNull
    Page<Compilation> findAll(@Nullable Predicate predicate, @Nullable Pageable pageable);
}
