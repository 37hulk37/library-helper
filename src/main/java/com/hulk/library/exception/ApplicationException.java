package com.hulk.library.exception;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class ApplicationException extends RuntimeException {
    public ApplicationException(Integer code) {
        super(code.toString());
    }

    public ApplicationException(Integer code, Object... params) {
        super(code.toString() + "/" + Arrays.toString(params));
    }

}

