package com.apiDataProcessor.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalResponse<T> {
    private Boolean success;
    private T data;

    public static <T> InternalResponseBuilder<T> builder() {
        return new InternalResponseBuilder<>();
    }

    public static class InternalResponseBuilder<T> {
        private Boolean success;
        private T data;

        public InternalResponseBuilder<T> success(Boolean success) {
            this.success = success;
            return this;
        }

        public InternalResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public InternalResponse<T> build() {
            return new InternalResponse<>(success, data);
        }
    }
}
