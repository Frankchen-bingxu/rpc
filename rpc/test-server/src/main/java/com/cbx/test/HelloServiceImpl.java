package com.cbx.test;


import com.cbx.api.HelloObject;
import com.cbx.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author ziyang
 */
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到：{}", object.getMessage());
        return "这是掉用的返回值，id=" + object.getId();
    }

}