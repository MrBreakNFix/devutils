package com.mrbreaknfix.scripting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mrbreaknfix.Dev;
import com.mrbreaknfix.scripting.apis.*;
import com.mrbreaknfix.scripting.apis.info.PlayerInfo;
import com.mrbreaknfix.scripting.apis.info.ScreenInfo;
import com.mrbreaknfix.scripting.apis.info.WSPort;
import com.mrbreaknfix.scripting.apis.playeractions.Chat;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.MethodNotSupportedException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApiHandler implements HttpHandler {

    private final List<ApiBase> apis = new ArrayList<>();

    public ApiHandler() {
        apis.add(new ChatLog());
        apis.add(new WSPort());

        // Info
        apis.add(new ScreenInfo());
        apis.add(new PlayerInfo());

        // Player actions
        apis.add(new Chat());
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] parts = exchange.getRequestURI().getRawPath().split("/");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = URLDecoder.decode(parts[i], Charset.defaultCharset());
        }

        Optional<ApiBase> api;
        synchronized (apis) {
            api = apis.stream().filter(a -> a.getRoute().equals(parts[2])).findFirst();
        }

        if (api.isEmpty()) {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
            return;
        }

        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    processGet(parts, api.get(), exchange);
                    break;
                case "POST":
                    processPost(api.get(), exchange);
                    break;
                case "PUT":
                    processPut(parts, api.get(), exchange);
                    break;
                case "DELETE":
                    processDelete(parts, api.get(), exchange);
                    break;
            }
        }
        catch (MethodNotSupportedException e) {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
        }
        catch (NotFoundHttpException e) {
            byte[] data = e.getMessage().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, 0);
            OutputStream stream = exchange.getResponseBody();
            stream.write(data);
            stream.close();
            exchange.close();
        }
        catch (HttpException e) {
            sendMessage(exchange, 503, e.getMessage());
        }
        catch (Throwable e) {
            sendMessage(exchange, 500, e.getMessage());
        }
    }

    private void processGet(String[] parts, ApiBase api, HttpExchange exchange) throws HttpException, IOException {
        String response;
        if (parts.length == 3) {
            response = api.get();
        } else {
            response = api.get(parts[3]);
        }
        Proc(exchange, response);
    }

    private void Proc(HttpExchange exchange, String response) throws IOException {
        byte[] data = response.getBytes(StandardCharsets.UTF_8);
        HttpHelper.setJsonContentType(exchange);
        exchange.sendResponseHeaders(200, data.length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(data);
        stream.close();
        exchange.close();
    }

    private void processPost(ApiBase api, HttpExchange exchange) throws HttpException, IOException {
        String body = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
        JsonObject jsonObject;

        try {
            jsonObject = JsonParser.parseString(body).getAsJsonObject();
        } catch (Exception e) {
            String response = api.post(new HashMap<>());

            try {
                Proc(exchange, response);
                Dev.LOGGER.info("Response: " + response);
            } catch (Exception f) {
                Dev.LOGGER.error("Error processing POST request: ", f);
            }
            return;
        }


        // Iterate over the key-value pairs in the JSON object
        Map<String, String> fields = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            fields.put(entry.getKey(), entry.getValue().getAsString());
        }

        // Call the APIs post method with the extracted fields
        String response = api.post(fields);

        if (response == null) {
            response = "{}";
        }

        Proc(exchange, response);
    }


    private void processPut(String[] parts, ApiBase api, HttpExchange exchange) throws HttpException, IOException {

        if (parts.length < 4) {
            throw new MethodNotSupportedException("PUT requires id");
        }

        String body = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
        api.put(parts[3], body);

        byte[] data = "{}".getBytes(StandardCharsets.UTF_8);
        HttpHelper.setJsonContentType(exchange);
        exchange.sendResponseHeaders(200, data.length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(data);
        stream.close();
        exchange.close();

    }

    private void processDelete(String[] parts, ApiBase api, HttpExchange exchange) throws HttpException, IOException {

        if (parts.length < 4) {
            throw new MethodNotSupportedException("DELETE requires id");
        }

        String response = api.delete(parts[3]);
        Proc(exchange, response);

    }

    private void sendMessage(HttpExchange exchange, int code, String message) throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(bytes);
        stream.close();
        exchange.close();
    }
}
