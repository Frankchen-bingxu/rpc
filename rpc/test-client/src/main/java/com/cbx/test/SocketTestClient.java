package com.cbx.test;


import com.cbx.api.HelloObject;
import com.cbx.api.HelloService;
import com.cbx.rpc.RpcClientProxy;
import com.cbx.rpc.socket.client.SocketClient;

/**
 * 测试用消费者（客户端）
 * @author ziyang
 */
public class SocketTestClient {

    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient("127.0.0.1",9000);
        RpcClientProxy proxy = new RpcClientProxy(socketClient);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }

}