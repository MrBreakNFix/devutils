package com.mrbreaknfix.scripting.apis.info;

import com.mrbreaknfix.Dev;
import com.mrbreaknfix.scripting.ApiBase;

import java.util.Map;

public class WSPort extends ApiBase {

    @Override
    public String getRoute() {
        return "wsport";
    }

    @Override
    public String post(Map<String, String> fields) {
        Dev.LOGGER.info("Websocket port requested");
        return String.valueOf(Dev.webSocketPort);
    }
}