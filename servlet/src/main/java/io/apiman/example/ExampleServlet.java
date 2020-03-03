package io.apiman.example;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExampleServlet extends HttpServlet {
    private MeterRegistry registry;

    @Override
    public void init() throws ServletException {
        registry = PrometheusUtil.prometheus();
        new JvmMemoryMetrics().bindTo(registry);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Counter counter = registry.counter("requests");
        counter.increment();
        final String counterMsg = "Counter: " + counter.count();
        System.out.println(counterMsg);
        resp.getOutputStream().println(counterMsg);
        resp.getOutputStream().flush();
    }

    @Override
    public void destroy() {
        PrometheusUtil.shutdown();
    }
}
