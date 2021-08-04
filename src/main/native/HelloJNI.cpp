#include "HelloJNI.h"

#include <iostream>
#include <string>

std::string jstring2string(JNIEnv *env, jstring jstr)
{
    const char *buffer = env->GetStringUTFChars(jstr, 0);
    if (!buffer)
    {
        return "";
    }
    std::string str(buffer);
    env->ReleaseStringUTFChars(jstr, buffer);

    return str;
}

JNIEXPORT void JNICALL Java_HelloJNI_sayHello(JNIEnv *env, jobject, jstring str)
{
    std::string value = jstring2string(env, str);
    std::cout << "Hello " << value << std::endl;
}