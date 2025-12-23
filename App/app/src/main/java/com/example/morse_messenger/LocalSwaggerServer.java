package com.example.morse_messenger;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class LocalSwaggerServer extends NanoHTTPD {

    private final AssetManager assets;
    private final Gson gson = new Gson();

    public LocalSwaggerServer(AssetManager assets, int port) {
        super(port);
        this.assets = assets;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String method = session.getMethod().name();
        String uri = session.getUri();

        try {
            if (uri.equals("/") || uri.equals("/swagger") || uri.equals("/swagger-ui") || uri.equals("/swagger-ui/")) {
                return redirect("/swagger-ui/index.html");
            }

            if (uri.equals("/openapi.yaml")) {
                return assetText("openapi.yaml", "application/yaml; charset=utf-8");
            }

            if (uri.startsWith("/swagger-ui/")) {
                String assetPath = uri.substring(1); // remove leading "/"
                return assetBinary(assetPath, guessMime(assetPath));
            }

            if (uri.equals("/health") && method.equals("GET")) {
                JsonObject obj = new JsonObject();
                obj.addProperty("status", "ok");
                return json(obj);
            }

            if (uri.equals("/translate") && method.equals("POST")) {
                Map<String, String> body = new HashMap<>();
                session.parseBody(body);
                String raw = body.get("postData");
                if (raw == null) raw = "";

                JsonObject req = gson.fromJson(raw, JsonObject.class);
                String text = (req != null && req.has("text")) ? req.get("text").getAsString() : "";

                // ВОТ ТУТ ПОДКЛЮЧАЕШЬ СВОЮ ЛОГИКУ
                // Например: String result = MorseTranslator.translate(text);
                String result = translateText(text);

                JsonObject resp = new JsonObject();
                resp.addProperty("result", result);
                return json(resp);
            }

            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain; charset=utf-8", "Not found");
        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain; charset=utf-8",
                    "Server error: " + e.getMessage());
        }
    }

    private String translateText(String text) {
        // Заглушка. Заменишь на реальный вызов.
        return "RESULT: " + text;
    }

    private Response redirect(String location) {
        Response r = newFixedLengthResponse(Response.Status.REDIRECT, "text/plain", "");
        r.addHeader("Location", location);
        return r;
    }

    private Response json(JsonObject obj) {
        byte[] bytes = gson.toJson(obj).getBytes(StandardCharsets.UTF_8);
        Response r = newFixedLengthResponse(
                Response.Status.OK,
                "application/json; charset=utf-8",
                new java.io.ByteArrayInputStream(bytes),
                bytes.length
        );
        r.addHeader("Access-Control-Allow-Origin", "*");
        r.addHeader("Cache-Control", "no-cache");
        return r;
    }

    private Response assetText(String assetPath, String mime) throws IOException {
        byte[] data = readAsset(assetPath);
        return newFixedLengthResponse(Response.Status.OK, mime, new java.io.ByteArrayInputStream(data), data.length);
    }

    private Response assetBinary(String assetPath, String mime) throws IOException {
        byte[] data = readAsset(assetPath);
        Response r = newFixedLengthResponse(Response.Status.OK, mime, new java.io.ByteArrayInputStream(data), data.length);
        r.addHeader("Cache-Control", "no-cache");
        return r;
    }

    private byte[] readAsset(String assetPath) throws IOException {
        InputStream is = assets.open(assetPath);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) != -1) {
                bos.write(buf, 0, n);
            }
            return bos.toByteArray();
        } finally {
            is.close();
        }
    }

    private String guessMime(String path) {
        String lower = path.toLowerCase();
        if (lower.endsWith(".html")) return "text/html; charset=utf-8";
        if (lower.endsWith(".css")) return "text/css; charset=utf-8";
        if (lower.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".json")) return "application/json; charset=utf-8";
        if (lower.endsWith(".yaml") || lower.endsWith(".yml")) return "application/yaml; charset=utf-8";
        return "application/octet-stream";
    }
}
