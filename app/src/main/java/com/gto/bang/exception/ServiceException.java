package com.gto.bang.exception;

/**
 * Created by shenjialong on 17/11/25 14:58.
 */
public class ServiceException extends Exception {

    String code;

    public ServiceException(String code) {
        super(code);
        this.code=code;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return getCode();
    }
}
