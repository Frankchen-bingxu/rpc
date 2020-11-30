package com.cbx.test;

import com.cbx.api.HelloObject;
import com.cbx.api.HelloService;
import com.cbx.rpc.RpcClient;
import com.cbx.rpc.RpcClientProxy;
import com.cbx.rpc.netty.client.NettyClient;

public class NettyTestClient {

    public static void main(String[] args) {
        RpcClient rpcClient = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy clientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(12, "This is a message");
        String res = helloService.hello(helloObject);
        System.out.println(res);
    }
}
