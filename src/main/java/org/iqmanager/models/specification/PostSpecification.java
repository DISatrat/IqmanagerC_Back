package org.iqmanager.models.specification;

import lombok.AllArgsConstructor;
import org.iqmanager.models.Extra;
import org.iqmanager.models.Post;
import org.iqmanager.models.RatesAndServices;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PostSpecification implements Specification<Post> {

    private String category;
    private Long priceMin;
    private Long priceMax;

    @Override
    public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        query.distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        Subquery<Long> minPriceSubquery = query.subquery(Long.class);
        Root<Extra> extraRoot = minPriceSubquery.from(Extra.class);
        Join<Extra, RatesAndServices> ratesJoin = extraRoot.join("ratesAndServices");

        minPriceSubquery.select(criteriaBuilder.min(ratesJoin.get("price")))
                .where(
                        criteriaBuilder.equal(extraRoot.get("post"), root), // Ссылка на текущий пост
                        criteriaBuilder.equal(extraRoot.get("type"), "SELECT_SINGLE")
                );

        Predicate pricePredicate = criteriaBuilder.between(
                criteriaBuilder.coalesce(minPriceSubquery, root.get("price")), priceMin, priceMax
        );
        predicates.add(pricePredicate);

        if (category != null && !category.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.join("categories", JoinType.INNER).join("categoryNames", JoinType.INNER).get("name"), "%" + category + "%"));
            predicates.add(criteriaBuilder.equal(root.join("categories", JoinType.INNER).join("categoryNames", JoinType.INNER).get("language"), "ru"));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
