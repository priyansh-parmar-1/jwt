package com.ecommerce.ecomApp.response;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder(value = {"timestamp", "status", "data", "error"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> {

    @JsonProperty(value = "status")
    private String status;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd 'T' HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd 'T' HH:mm:ss")
    @JsonProperty(value = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

    @JsonProperty(value = "data")
    private T data;

    @JsonProperty(value = "error")
    private String error;

}
