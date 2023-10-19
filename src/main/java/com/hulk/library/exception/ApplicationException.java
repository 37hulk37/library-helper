package com.hulk.library.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private Object[] params = null;

    public ApplicationException(Integer code) {
        super(code.toString());
    }

    public ApplicationException(Integer code, Object... params) {
        super(code.toString());
        this.params = params;
    }

}

