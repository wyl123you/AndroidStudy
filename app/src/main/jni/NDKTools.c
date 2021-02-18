//
// Created by WYL on 2020/8/18.
//
#include <jni.h>
#include <string.h>

//返回 a+b的结果
JNIEXPORT jint JNICALL Java_com_example_study_demo_jni_NDKTools_add
        (JNIEnv *env, jclass jobj, jint a, jint b) {
    return a + b;
}

JNIEXPORT jstring JNICALL Java_com_example_study_demo_jni_NDKTools_getStringFromNDK
        (JNIEnv *env, jclass clazz, jstring j_string) {
    // TODO: implement getStringFromNDK()
    const char *c_str = (*env)->GetStringUTFChars(env, j_string, JNI_FALSE);
    const char *a = "你好,JNI_C！";
    return (*env)->NewStringUTF(env, a);
}

JNIEXPORT jstring JNICALL Java_com_example_study_demo_jni_NDKTools_getSign
        (JNIEnv *env, jclass clazz, jstring j_url, jobject j_map, jstring j_key) {
    // TODO: implement getSign()
    const char *c_url = (*env)->GetStringUTFChars(env, j_url, JNI_FALSE);
    const char *a;

    return (*env)->NewStringUTF(env, c_url);
}