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

        // sync callback
        JNIConsumer consumer = new JNIConsumer();
        consumer.sendQuery();
        while (consumer.next()) {
            System.out.println("Consumer get value: " + consumer.getCurrentValue());
        }

        // async callback
        JNIAsyncConsumer asyncConsumer = new JNIAsyncConsumer();
        asyncConsumer.sendQuery();
        while (asyncConsumer.next()) {
            System.out.println("Async Consumer get value: " + asyncConsumer.getCurrentValue());
        }

        // test direct memory allocate and set
        int size = 1073741824;
        long l = System.currentTimeMillis();
        ByteBuffer byteBufferFromCpp = JNIMemory.allocateAndSetDirectMemoryFromCpp(size);
        long cost = System.currentTimeMillis() - l;
        System.out.println("allocate and set DirectMemory from cpp with " + size + " bytes cost " + cost + " ms");
        l = System.currentTimeMillis();
        ByteBuffer byteBufferFromJava = JNIMemory.allocateAndSetDirectMemoryFromJava(size);
        cost = System.currentTimeMillis() - l;
        System.out.println("allocate and set DirectMemory from java with " + size + " bytes cost " + cost + " ms");

        // test memory allocate and set
        l = System.currentTimeMillis();
        ByteBuffer cppByteBuffer = ByteBuffer.wrap(JNIMemory.allocateAndSetMemoryFromCpp(size));
        cost = System.currentTimeMillis() - l;
        System.out.println("allocate and set HeapMemory from cpp with " + size + " bytes cost " + cost + " ms");
        l = System.currentTimeMillis();
        ByteBuffer javaByteBuffer = JNIMemory.allocateAndSetMemoryFromJava(size);
        cost = System.currentTimeMillis() - l;
        System.out.println("allocate and set HeapMemory from java with " + size + " bytes cost " + cost + " ms");
    }
}
