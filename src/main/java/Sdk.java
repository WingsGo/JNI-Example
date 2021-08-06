/**
 * Created by jiong103 on 2017/3/23.
 */

public class Sdk {
    private Sdk() {
    }

    private static class SdkHodler {
        static Sdk instance = new Sdk();
    }

    public static Sdk getInstance() {
        return SdkHodler.instance;
    }

    public native void nativeDownload();

    private int onProgressCallBack(Integer total, Integer already) {
        System.out.println("total:"+total);
        System.out.println("already:"+already);
        return 1;
    }
}