package com.apiDataProcessor.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalResponse<T> {
    private Boolean success;
    @Setter(AccessLevel.NONE)
    private Long size;
    private List<T> data;
    private String error;
    private Boolean hasMore;

    private ExternalResponse(Boolean success, Long size, List<T> data, String error, Boolean hasMore) {
        this.success = success;
        this.size = size;
        this.data = data;
        this.error = error;
        this.hasMore = hasMore;
    }

    public static <T> ExternalResponseBuilder<T> builder() {
        return new ExternalResponseBuilder<>();
    }

    public static class ExternalResponseBuilder<T> {
        private Boolean success;
        private Long size;
        private List<T> data;
        private String error;
        private Boolean hasMore;

        public ExternalResponseBuilder<T> success(Boolean success) {
            this.success = success;
            return this;
        }

        public ExternalResponseBuilder<T> data(List<T> data) {
            this.data = data;
            this.size = (long) data.size();
            return this;
        }

        public ExternalResponseBuilder<T> error(String error) {
            this.error = error;
            return this;
        }

        public ExternalResponseBuilder<T> hasMore(Boolean hasMore) {
            this.hasMore = hasMore;
            return this;
        }

        public ExternalResponse<T> build() {
            return new ExternalResponse<>(success, size, data, error, hasMore);
        }
    }
}
