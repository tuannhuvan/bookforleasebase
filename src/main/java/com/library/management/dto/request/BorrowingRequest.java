package com.library.management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BorrowingRequest {

    @NotNull(message = "Vui lòng chọn thành viên mượn sách")
    private Long memberId;

    @NotEmpty(message = "Vui lòng chọn ít nhất 1 cuốn sách để mượn")
    @Valid
    private List<BorrowingItemRequest> items = new ArrayList<>();

    public BorrowingRequest() {
    }

    public BorrowingRequest(Long memberId, List<BorrowingItemRequest> items) {
        this.memberId = memberId;
        if (items != null) this.items = items;
    }

    public static BorrowingRequestBuilder builder() {
        return new BorrowingRequestBuilder();
    }

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }
    public List<BorrowingItemRequest> getItems() { return items; }
    public void setItems(List<BorrowingItemRequest> items) { this.items = items; }

    public static class BorrowingRequestBuilder {
        private Long memberId;
        private List<BorrowingItemRequest> items = new ArrayList<>();

        public BorrowingRequestBuilder memberId(Long memberId) { this.memberId = memberId; return this; }
        public BorrowingRequestBuilder items(List<BorrowingItemRequest> items) { this.items = items; return this; }

        public BorrowingRequest build() {
            return new BorrowingRequest(memberId, items);
        }
    }

    public static class BorrowingItemRequest {
        @NotNull(message = "Sách không được để trống")
        private Long bookId;

        @NotNull(message = "Số lượng không được để trống")
        @Min(value = 1, message = "Số lượng mượn phải >= 1")
        private Integer quantity;

        public BorrowingItemRequest() {
        }

        public BorrowingItemRequest(Long bookId, Integer quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }

        public static BorrowingItemRequestBuilder builder() {
            return new BorrowingItemRequestBuilder();
        }

        public Long getBookId() { return bookId; }
        public void setBookId(Long bookId) { this.bookId = bookId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public static class BorrowingItemRequestBuilder {
            private Long bookId;
            private Integer quantity;

            public BorrowingItemRequestBuilder bookId(Long bookId) { this.bookId = bookId; return this; }
            public BorrowingItemRequestBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }

            public BorrowingItemRequest build() {
                return new BorrowingItemRequest(bookId, quantity);
            }
        }
    }
}
