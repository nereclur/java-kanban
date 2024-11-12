package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseHttpHandler implements HttpHandler {
    protected static final Logger logger = Logger.getAnonymousLogger();

    protected void sendText(HttpExchange h, String text, Integer code) {
        try {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(code, resp.length);
            h.getResponseBody().write(resp);
            h.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while sending text", e);
        }
    }

    protected void sendNotFound(HttpExchange h) {
        try {
            String response = "{\"error\":\"Not Found\"}";
            byte[] resp = response.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(404, resp.length);
            h.getResponseBody().write(resp);
            h.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while sending not found", e);
        }
    }

    protected void sendHasInteractions(HttpExchange h) {
        try {
            String response = "{\"error\":\"Task has interactions and cannot be created or updated\"}";
            byte[] resp = response.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            h.sendResponseHeaders(409, resp.length); // Using 409 Conflict status code
            h.getResponseBody().write(resp);
            h.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while sending has interactions", e);
        }
    }

    protected void sendInternalError(HttpExchange h) {
        try {
            h.sendResponseHeaders(500, 0);
            h.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while sending internal error", e);
        }
    }
}
