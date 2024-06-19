package com.mrbreaknfix.scripting;

import org.apache.http.HttpException;
import org.apache.http.MethodNotSupportedException;

import java.util.Map;

public abstract class ApiBase {
    public abstract String getRoute();

    public String get() throws HttpException {
        throw new MethodNotSupportedException("Method not supported");
    }

    public String get(String id) throws HttpException {
        throw new MethodNotSupportedException("Method not supported");
    }

    public String put(String id, String body) throws HttpException {
        throw new MethodNotSupportedException("Method not supported");
    }

    public String post(Map<String, String> fields) throws HttpException {
        throw new MethodNotSupportedException("Method not supported");
    }

    public String get(Map<String, String> fields) throws HttpException {
        throw new MethodNotSupportedException("Method not supported");
    }

//    public String post(String body) throws HttpException {
//        throw new MethodNotSupportedException("Method not supported");
//    }

    public String delete(String id) throws HttpException {
        throw new MethodNotSupportedException("Method not supported");
    }
}
