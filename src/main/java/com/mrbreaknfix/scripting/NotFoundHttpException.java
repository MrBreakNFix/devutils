package com.mrbreaknfix.scripting;

import org.apache.http.HttpException;

public class NotFoundHttpException extends HttpException {

    public NotFoundHttpException() {
        super();
    }

    public NotFoundHttpException(String message) {
        super(message);
    }
}