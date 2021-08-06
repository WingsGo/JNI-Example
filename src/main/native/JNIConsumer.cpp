#include "JNIConsumer.h"

#include <iostream>
#include <string>
/*
 * Class:     JNIConsumer
 * Method:    sendQuery
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_JNIConsumer_sendQuery(JNIEnv *env, jobject thiz)
{
    jclass jConsumerClass = env->GetObjectClass(thiz);
    if (jConsumerClass == nullptr) {
        std::cout << "Failed to find class: Consumer" << std::endl;
        return;
    }

    jmethodID javaCallBack = env->GetMethodID(jConsumerClass, "onReceivedMessage", "(IZ)Z");
    if (javaCallBack == nullptr) {
        std::cout << "Failed to find method: onReceivedMessage";
        return;
    }
    jboolean ret = env->CallBooleanMethod(thiz, javaCallBack, 1, true);
}