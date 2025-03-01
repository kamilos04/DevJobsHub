package com.kamiljach.devjobshub.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PresignedUrlResponse {
    private String url;
    private String key;
}
