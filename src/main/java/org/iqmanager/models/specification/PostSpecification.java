package org.iqmanager.models.specification;

import lombok.AllArgsConstructor;
import org.iqmanager.models.Category;
import org.iqmanager.models.Post;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

@AllArgsConstructor
public class PostSpecification implements Specification<Post> {

    private Category category;
    private long priceMin;
    private long priceMax;
    private Instant date;

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(criteriaBuilder.equal(root.get("categories"), category));
        }
        if (priceMin != 0 && priceMax != 0) {
            predicates.add(criteriaBuilder.between(root.get("price"), priceMin, priceMax));
        }
        if (priceMin != 0 && priceMax == 0) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceMin));
        }
        if (priceMin == 0 && priceMax != 0) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceMax));
        }
        if (date != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), date));
        }

        return criteriaBuilder.and(predicates.toArray(new javax.persistence.criteria.Predicate[0]));
    }
}
