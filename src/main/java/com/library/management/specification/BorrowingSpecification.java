package com.library.management.specification;

import com.library.management.entity.Borrowing;
import com.library.management.enums.BorrowingStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BorrowingSpecification {

    public static Specification<Borrowing> filter(String keyword, BorrowingStatus status) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            if (StringUtils.hasText(keyword)) {
                String likePattern = "%" + keyword.trim().toLowerCase() + "%";
                var memberNameLike = cb.like(cb.lower(root.get("member").get("fullName")), likePattern);
                var memberEmailLike = cb.like(cb.lower(root.get("member").get("email")), likePattern);
                predicate = cb.and(predicate, cb.or(memberNameLike, memberEmailLike));
            }

            return predicate;
        };
    }
}
