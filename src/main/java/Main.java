import java.nio.ByteBuffer;

/**
 * @author wangcong
 * @version 1.0
 * @date 2021/8/4 上午11:52
 */
public class Main {
    public static void main(String[] args) {
        JNILoader.loadLibrary();
        JNIByteBuffer jniByteBuffer = new JNIByteBuffer();
        jniByteBuffer.init();
        ByteBuffer bb = jniByteBuffer.getBB();
        System.out.println("1: " + bb.getInt(0));
        System.out.println("2: " + bb.getInt(4));
        System.out.println("3: " + bb.getInt(8));
        System.out.println("4: " + bb.getInt(12));
        System.out.println("5: " + bb.getInt(16));
        Sdk.getInstance().nativeDownload();
    }
}
