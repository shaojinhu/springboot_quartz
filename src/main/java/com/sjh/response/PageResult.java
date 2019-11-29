package com.sjh.response;

import lombok.*;

import java.util.List;

/**
 * 分页Result结果集
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PageResult<T> {
    private long total;
    private List<T> rows;
}