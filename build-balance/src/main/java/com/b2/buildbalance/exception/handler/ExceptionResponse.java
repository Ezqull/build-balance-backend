package com.b2.buildbalance.exception.handler;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionResponse {

    Integer status;
    String message;
}
