package com.library.management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReturnRequest {

    @NotNull(message = "Mã phiếu mượn không được để trống")
    private Long borrowingId;

    @Valid
    private List<ReturnItemRequest> items = new ArrayList<>();

    public ReturnRequest() {
    }

    public ReturnRequest(Long borrowingId, List<ReturnItemRequest> items) {
        this.borrowingId = borrowingId;
        if (items != null) this.items = items;
    }

    public static ReturnRequestBuilder builder() {
        return new ReturnRequestBuilder();
    }

    public Long getBorrowingId() { return borrowingId; }
    public void setBorrowingId(Long borrowingId) { this.borrowingId = borrowingId; }
    public List<ReturnItemRequest> getItems() { return items; }
    public void setItems(List<ReturnItemRequest> items) { this.items = items; }

    public static class ReturnRequestBuilder {
        private Long borrowingId;
        private List<ReturnItemRequest> items = new ArrayList<>();

        public ReturnRequestBuilder borrowingId(Long borrowingId) { this.borrowingId = borrowingId; return this; }
        public ReturnRequestBuilder items(List<ReturnItemRequest> items) { this.items = items; return this; }

        public ReturnRequest build() {
            return new ReturnRequest(borrowingId, items);
        }
    }

    public static class ReturnItemRequest {
        @NotNull(message = "Mã dòng chi tiết không được để trống")
        private Long detailId;

        @NotNull(message = "Số lượng trả không được để trống")
        @Min(value = 0, message = "Số lượng trả phải >= 0")
        private Integer returnQuantity;

        public ReturnItemRequest() {
        }

        public ReturnItemRequest(Long detailId, Integer returnQuantity) {
            this.detailId = detailId;
            this.returnQuantity = returnQuantity;
        }

        public static ReturnItemRequestBuilder builder() {
            return new ReturnItemRequestBuilder();
        }

        public Long getDetailId() { return detailId; }
        public void setDetailId(Long detailId) { this.detailId = detailId; }
        public Integer getReturnQuantity() { return returnQuantity; }
        public void setReturnQuantity(Integer returnQuantity) { this.returnQuantity = returnQuantity; }

        public static class ReturnItemRequestBuilder {
            private Long detailId;
            private Integer returnQuantity;

            public ReturnItemRequestBuilder detailId(Long detailId) { this.detailId = detailId; return this; }
            public ReturnItemRequestBuilder returnQuantity(Integer returnQuantity) { this.returnQuantity = returnQuantity; return this; }

            public ReturnItemRequest build() {
                return new ReturnItemRequest(detailId, returnQuantity);
            }
        }
    }
}
