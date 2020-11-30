package com.cbx.rpc.codec;
import com.cbx.rpc.entity.RpcRequest;
import com.cbx.rpc.entity.RpcResponse;
import com.cbx.rpc.enumeration.PackageType;
import com.cbx.rpc.enumeration.RpcError;
import com.cbx.rpc.exception.RpcException;
import com.cbx.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
/**
 * +---------------+---------------+-----------------+-------------+
 * |  Magic Number |  Package Type | Serializer Type | Data Length |
 * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
 * +---------------+---------------+-----------------+-------------+
 * |                          Data Bytes                           |
 * |                   Length: ${Data Length}                      |
 * +---------------------------------------------------------------+
 * Magic Number 是 4 字节魔数，表识一个协议包。
 * Package Type，标明这是一个调用请求还是调用响应，
 * Serializer Type 标明了实际数据使用的序列化器，这个服务端和客户端应当使用统一标准；
 * Data Length 就是实际数据的长度，设置这个字段主要防止粘包，
 * Data Bytes 是经过序列化后的实际数据，可能是 RpcRequest 也可能是 RpcResponse 经过序列化后的字节，取决于 Package Type。
 */
/**
 * 通用的解码拦截器
 * @author ziyang
 */
public class CommonDecoder extends ReplayingDecoder {

    //CommonDecoder 继承自 ReplayingDecoder ，与 MessageToByteEncoder 相反，它用于将收到的字节序列还原为实际对象。
    // 主要就是一些字段的校验，比较重要的就是取出序列化器的编号，以获得正确的反序列化方式，并且读入 length 字段来确定数据包的长度（防止粘包），
    // 最后读入正确大小的字节数组，反序列化成对应的对象。
    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magic = in.readInt();
        if(magic != MAGIC_NUMBER) {
            logger.error("不识别的协议包: {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }
        int packageCode = in.readInt();
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if(packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包: {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }
        int serializerCode = in.readInt();
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if(serializer == null) {
            logger.error("不识别的反序列化器: {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }

}