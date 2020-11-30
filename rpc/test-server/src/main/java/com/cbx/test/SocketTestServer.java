package com.cbx.test;


import com.cbx.api.HelloService;
import com.cbx.rpc.registry.DefaultServiceRegistry;
import com.cbx.rpc.registry.ServiceRegistry;
import com.cbx.rpc.socket.server.SocketServer;

public class SocketTestServer {

    public static void main(String[] args) {
        HelloService helloService =  new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);


        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.start(9000);

    }

}