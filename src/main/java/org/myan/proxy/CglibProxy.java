package org.myan.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Objects;

public class CglibProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result;
        if (Objects.equals("getOrder", method.getName())) {
            System.out.println("Proxy before");
            result = methodProxy.invokeSuper(o, args);
            System.out.println("Proxy after");
        } else {
            result = methodProxy.invokeSuper(o, args);
        }

        return result;
    }

    public Object getProxy(Object target) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        enhancer.setClassLoader(target.getClass().getClassLoader());
        return enhancer.create();
    }
}
