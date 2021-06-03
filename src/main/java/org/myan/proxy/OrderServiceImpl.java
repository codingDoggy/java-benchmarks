package org.myan.proxy;

public class OrderServiceImpl implements OrderService{
    @Override
    public String getOrder() {
        System.out.println("----get the order");
        return "the order";
    }

    @Override
    public long getId() {
        return 12;
    }
}
