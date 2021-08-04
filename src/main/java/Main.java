/**
 * @author wangcong
 * @version 1.0
 * @date 2021/8/4 上午11:52
 */
public class Main {
    public static void main(String[] args) {
        HelloJNILoader.loadLibrary();
        HelloJNI.sayHello("jni");
    }
}
