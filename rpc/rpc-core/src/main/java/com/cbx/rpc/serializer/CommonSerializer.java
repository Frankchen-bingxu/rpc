package com.cbx.rpc.serializer;

/**
 * 通用的序列化反序列化接口
 * @author ziyang
 */
public interface CommonSerializer {

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 3:
                return new ProtostuffSerializer();
            default:
                return null;
        }
    }

}