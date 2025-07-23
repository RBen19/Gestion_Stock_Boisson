package org.beni.gestionboisson.shared.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String message;
    private int status;
    private long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .message("Success")
                .status(200)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .data(null)
                .message(message)
                .status(status)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    public static <T> ApiResponse<T> errorConflict(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .message(message)
                .status(409)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .message(message)
                .status(404)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
