package com.mrbreaknfix.scripting;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RootHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String filename;
        if (exchange.getRequestURI().getPath().equals("/")) {
            filename = "/index.html";
        } else {
            filename = exchange.getRequestURI().getPath();
        }

        byte[] bytes;
        InputStream stream = ResourceHelper.get("web" + filename);
        try (stream) {
            if (stream == null) {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
                return;
            }

            bytes = IOUtils.toByteArray(stream);
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(503, 0);
            exchange.close();
            return;
        }

        HttpHelper.setContentType(exchange, filename);
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
        exchange.close();
    }
}
