package com.example.morse_messenger;
import android.content.Context;

public final class SwaggerServerHolder {
    private static LocalSwaggerServer server;

    private SwaggerServerHolder() {}

    public static synchronized void start(Context context) {
        if (server != null) return;
        server = new LocalSwaggerServer(context.getAssets(), 8080);
        try {
            server.start(5000, false);
        } catch (Exception e) {
            server = null;
            throw new RuntimeException("Failed to start server: " + e.getMessage(), e);
        }
    }

    public static synchronized void stop() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }
}
