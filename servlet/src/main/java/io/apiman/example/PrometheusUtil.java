package io.apiman.example;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.lang.Nullable;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;

import static java.util.Optional.ofNullable;

public class PrometheusUtil {

    private static HttpServer server;

    /**
     * To use pushgateway instead:
     * new PushGateway("localhost:9091").pushAdd(registry.getPrometheusRegistry(), "samples");
     *
     * @return A prometheus registry.
     */
    public static PrometheusMeterRegistry prometheus() {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(new PrometheusConfig() {
            @Override
            public Duration step() {
                return Duration.ofSeconds(10);
            }

            @Override
            @Nullable
            public String get(String k) {
                return null;
            }
        });

        try {
            server = HttpServer.create(new InetSocketAddress(9999), 0);
            server.createContext("/prometheus", httpExchange -> {
                String response = prometheusRegistry.scrape();
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            });

            new Thread(server::start).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return prometheusRegistry;
    }

    public static void shutdown() {
        ofNullable(server).ifPresent(s -> {
            server.stop(0);
            server = null;
        });
    }
}
