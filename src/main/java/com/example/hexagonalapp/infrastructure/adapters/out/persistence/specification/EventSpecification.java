package com.example.hexagonalapp.infrastructure.adapters.out.persistence.specification;

import com.example.hexagonalapp.infrastructure.entity.EventEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<EventEntity> withFilters(
            String name,
            String status,
            Long venueId,
            LocalDateTime startDateFrom,
            LocalDateTime startDateTo) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (venueId != null) {
                predicates.add(criteriaBuilder.equal(root.get("venue").get("id"), venueId));
            }

            if (startDateFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("startDate"), startDateFrom));
            }

            if (startDateTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("startDate"), startDateTo));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}