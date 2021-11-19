package com.zjt.asm.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.FileOutputStream

class MyTransform : Transform() {
    override fun getName(): String {
        return "zjt_transform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        val inputs = transformInvocation?.inputs
        val outputProvider = transformInvocation?.outputProvider

        if (!isIncremental) {
            outputProvider?.deleteAll()
        }

        inputs?.forEach { it ->
            it.directoryInputs.forEach {
                if (it.file.isDirectory) {
                    FileUtils.getAllFiles(it.file).forEach {
                        val file = it
                        val name = file.name
                        if (name.endsWith(".class") && name != ("R.class")
                            && !name.startsWith("R\$") && name != ("BuildConfig.class")
                        ) {

                            val classPath = file.absolutePath
                            println(">>>>>> asm ------ classPath :$classPath")

                            val cr = ClassReader(file.readBytes())
                            val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                            val visitor = MyClassVisitor(cw)
                            cr.accept(visitor, ClassReader.EXPAND_FRAMES)

                            val bytes = cw.toByteArray()

                            val fos = FileOutputStream(classPath)
                            fos.write(bytes)
                            fos.close()
                        }
                    }
                }

                val dest = outputProvider?.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.DIRECTORY
                )
                FileUtils.copyDirectoryToDirectory(it.file, dest)
            }

            //  !!!!!!!!!! !!!!!!!!!! !!!!!!!!!! !!!!!!!!!! !!!!!!!!!!
            //使用androidx的项目一定也注意jar也需要处理，否则所有的jar都不会最终编译到apk中，千万注意
            //导致出现ClassNotFoundException的崩溃信息，当然主要是因为找不到父类，因为父类AppCompatActivity在jar中
            it.jarInputs.forEach {
                val dest = outputProvider?.getContentLocation(
                    it.name,
                    it.contentTypes,
                    it.scopes,
                    Format.JAR
                )
                FileUtils.copyFile(it.file, dest)
            }
        }

    }
}