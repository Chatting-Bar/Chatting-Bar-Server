package com.chatbar.global.error;

import com.chatbar.global.payload.ErrorCode;
import lombok.Getter;

import javax.naming.AuthenticationException;


@Getter
public class DefaultAuthenticationException extends AuthenticationException {

    private ErrorCode errorCode;

    public DefaultAuthenticationException(String msg, Throwable t) {
        super(msg);
        this.errorCode = ErrorCode.INVALID_REPRESENTATION;
    }

    public DefaultAuthenticationException(String msg) {
        super(msg);
    }

    public DefaultAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
