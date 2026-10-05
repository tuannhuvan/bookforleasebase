package com.library.management.specification;

import com.library.management.entity.Book;
import com.library.management.enums.BookStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BookSpecification {

    public static Specification<Book> filter(String keyword, Long categoryId, BookStatus status) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            } else {
                // By default hide soft-deleted books from list
                predicate = cb.and(predicate, cb.notEqual(root.get("status"), BookStatus.DELETED));
            }

            if (categoryId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
            }

            if (StringUtils.hasText(keyword)) {
                String likePattern = "%" + keyword.trim().toLowerCase() + "%";
                var titleLike = cb.like(cb.lower(root.get("title")), likePattern);
                var authorLike = cb.like(cb.lower(root.get("author")), likePattern);
                var isbnLike = cb.like(cb.lower(root.get("isbn")), likePattern);
                predicate = cb.and(predicate, cb.or(titleLike, authorLike, isbnLike));
            }

            return predicate;
        };
    }
}
