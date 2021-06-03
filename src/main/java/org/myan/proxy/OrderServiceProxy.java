package org.myan.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

public class OrderServiceProxy implements InvocationHandler {
    private final Object target;

    public OrderServiceProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Object result;

        if (Objects.equals("getOrder", method.getName())) {
            System.out.println("Proxy before");
            result = method.invoke(target, args);
            System.out.println("Proxy after");
        } else {
            result = method.invoke(target, args);
        }
        return result;
    }
}
