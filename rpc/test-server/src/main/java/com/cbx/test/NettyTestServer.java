package com.cbx.test;

import com.cbx.api.HelloService;
import com.cbx.rpc.netty.server.NettyServer;
import com.cbx.rpc.registry.DefaultServiceRegistry;
import com.cbx.rpc.registry.ServiceRegistry;

public class NettyTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        NettyServer nettyServer = new NettyServer();
        nettyServer.start(9999);
    }
}
