package com.example.zjt_plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager

/**
 * /**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
*/
 */
class ThreadPoolTransform : Transform() {
    override fun getName(): String {
        return "Zjt_Thread_Pool"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return mutableSetOf<QualifiedContent.ContentType>().apply {
            addAll(TransformManager.CONTENT_JARS)
        }
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return mutableSetOf<QualifiedContent.ScopeType>().apply {
            addAll(TransformManager.SCOPE_FULL_PROJECT)
        }
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        val helper = ThreadAsmHelper()
        val baseTransform = BaseTransform(transformInvocation, object : TransformCallBack {
            override fun process(className: String, classBytes: ByteArray?): ByteArray? {
//                println("zjt_asm_thread_pool >>>> className = $className")
                return if (ClassUtils.checkClassName(className)) {
                    classBytes?.let { helper.modifyClass(it, className) }
                } else {
                    null
                }
            }
        })
        baseTransform.startTransform()
    }
}