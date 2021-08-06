#include "JNIAsyncConsumer.h"

#include <iostream>
#include <string>
#include <thread>

static JavaVM *Vm;
static jobject Obj;
static bool NeedDetach = false;

/*
 * Class:     JNIAsyncConsumer
 * Method:    sendQuery
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_JNIAsyncConsumer_sendQuery(JNIEnv *env, jobject thiz)
{
    env->GetJavaVM(&Vm);
        Obj = env->NewGlobalRef(thiz);
        std::thread([=] {
            JNIEnv *env;
            // check native thread attached to jvm
            int getEnvStat = Vm->GetEnv((void**) &env, JNI_VERSION_1_8);
            if (getEnvStat == JNI_EDETACHED) {
                // if not, actively attach to jvm and get env
                if (Vm->AttachCurrentThread((void**)&env, nullptr) != 0) {
                    std::cout << "Failed to attach current thread" << std::endl;
                    return;
                }
                NeedDetach = JNI_TRUE;
            }

            jclass jConsumerClass = env->GetObjectClass(Obj);
            if (jConsumerClass == nullptr) {
                std::cout << "Unable to find class: JNIConsumer";
                Vm->DetachCurrentThread();
                return;
            }

            jmethodID javaCallBack = env->GetMethodID(jConsumerClass, "onReceivedMessage", "(IZ)Z");
            if (javaCallBack == nullptr) {
                std::cout << "Unable to find method: onReceivedMessage" << std::endl;
                return;
            }
            for (size_t i = 0; i < 9; ++i) {
                env->CallBooleanMethod(Obj, javaCallBack, i, false);
            }
            env->CallBooleanMethod(Obj, javaCallBack, 10, true);

            if (NeedDetach) {
                Vm->DetachCurrentThread();
            }
            env = nullptr;

        }).detach();
}
