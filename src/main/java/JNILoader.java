import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author wangcong
 * @version 1.0
 * @date 2021/8/4 下午1:25
 */
public class JNILoader {
    public static final boolean isLinux = System.getProperty("os.name").toLowerCase()
            .startsWith("linux");

    private static volatile boolean so_loaded = false;

    public static void loadLibrary()
            throws RuntimeException {
        if (!isLinux) {
            return;
        }
        if (so_loaded) {
            return;
        }

        synchronized (JNILoader.class) {
            if (so_loaded) {
                return;
            }
            AccessController.doPrivileged(
                    (PrivilegedAction) () -> {
                        try {
                            NativeUtils.loadLibraryFromJar("/libJNIByteBuffer.so");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
            so_loaded = true;
        }
    }

}

