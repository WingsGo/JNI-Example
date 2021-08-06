import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangcong
 * @version 1.0
 * @date 2021/8/6 下午1:11
 */
public class JNIAsyncConsumer {
    private LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1024);
    private AtomicBoolean completed = new AtomicBoolean(false);

    private Integer currentValue;

    public native void sendQuery();

    public boolean next() {
        if (completed.get() && queue.isEmpty()) {
            return false;
        }
        try {
            currentValue = queue.take();
            return true;
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public int getCurrentValue() {
        return currentValue;
    }

    private boolean onReceivedMessage(int value, boolean finish) {
        try {
            queue.put(value);
            if (finish) {
                completed.set(true);
            }
            return true;
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}

