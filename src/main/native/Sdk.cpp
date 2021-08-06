#include "Sdk.h"
#include <iostream>
JNIEXPORT void JNICALL Java_Sdk_nativeDownload(JNIEnv *env, jobject thiz)
{
    jclass jSdkClass =env->GetObjectClass(thiz);
    if (jSdkClass == 0) {
        std::cout << "Unable to find class" << std::endl;
        return;
    }
    jmethodID javaCallback = env->GetMethodID(jSdkClass,
                                                 "onProgressCallBack", "(Ljava/lang/Integer;Ljava/lang/Integer;)I");
    if (javaCallback == nullptr) {
        std::cout << "failed to find onProgressCallBack" << std::endl;
        return;
    }

    jclass intClass = env->FindClass("java/lang/Integer");
    jmethodID initInt = env->GetMethodID(intClass, "<init>", "(I)V");
    if (initInt == nullptr) {
        std::cout << "failed to find Integer init method" << std::endl;
        return;
    }
    jobject newIntObj1 = env->NewObject(intClass, initInt, 1);
    jobject newIntObj2 = env->NewObject(intClass, initInt, 2);

    jint ret = env->CallIntMethod(thiz, javaCallback, newIntObj1, newIntObj2);

    env->DeleteGlobalRef(newIntObj1);
    env->DeleteGlobalRef(newIntObj2);

    return ;
}