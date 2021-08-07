import java.nio.ByteBuffer;

/**
 * @author wangcong
 * @version 1.0
 * @date 2021/8/6 下午2:57
 */
public class JNIMemory {
    public static native ByteBuffer allocateAndSetDirectMemoryFromCpp(int size);

    public static native byte[] allocateAndSetMemoryFromCpp(int size);

    public static ByteBuffer allocateAndSetDirectMemoryFromJava(int size) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size);
        setDirectByteBufferMemory(byteBuffer);
        return byteBuffer;
    }

    public static ByteBuffer allocateAndSetMemoryFromJava(int size) {
        byte[] buffer = new byte[size];
        setByteBufferMemory(buffer);
        return ByteBuffer.wrap(buffer);
    }

    private static native void setDirectByteBufferMemory(ByteBuffer bb);

    private static native void setByteBufferMemory(byte[] bb);
}
