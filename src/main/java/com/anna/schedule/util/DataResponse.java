package com.anna.schedule.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataResponse<T> {

    private T data;

    private String message;
}
