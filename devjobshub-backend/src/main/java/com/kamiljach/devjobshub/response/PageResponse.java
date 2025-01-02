package com.kamiljach.devjobshub.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;

    private Integer number;

    private Integer elements;

    private Integer totalPages;

    private Long totalElements;

    public PageResponse(List<T> content, Page page) {
        this.content = content;
        this.number = page.getNumber();
        this.elements = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();

    }
}
