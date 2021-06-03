package org.myan.proxy;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ProxyBenchmark {

    @Benchmark
    public String jdkProxy() {
        OrderService service = new OrderServiceImpl();
        InvocationHandler handler = new OrderServiceProxy(service);
        OrderService proxied = (OrderService) Proxy.newProxyInstance(service.getClass().getClassLoader(),
                service.getClass().getInterfaces(), handler);
        return proxied.getOrder() + proxied.getId();
    }


    @Benchmark
    public String cglibProxy() {
        OrderService service = new OrderServiceImpl();
        CglibProxy cglibProxy = new CglibProxy();
        OrderService proxied = (OrderService) cglibProxy.getProxy(service);
        return proxied.getOrder() + proxied.getId();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(ProxyBenchmark.class.getSimpleName())
                .forks(2)
                .warmupIterations(3)
                .measurementIterations(6)
                .measurementTime(TimeValue.seconds(5))
                .threads(10)
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(options).run();
    }
}
