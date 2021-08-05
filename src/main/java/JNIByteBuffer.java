import java.nio.ByteOrder;

/**
 * @author wangcong
 * @version 1.0
 * @date 2021/8/5 ??5:11
 */
public class JNIByteBuffer {
    private java.nio.ByteBuffer bb;

    native static void initByteBuffer(java.nio.ByteBuffer bb);

    public void init() {
        bb = java.nio.ByteBuffer.allocateDirect(5 * 4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        initByteBuffer(bb);
    }

    public java.nio.ByteBuffer getBB() {
        return bb;
    }
}