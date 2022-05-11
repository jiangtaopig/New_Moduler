package com.example.zjt_plugin

import com.example.zjt_plugin.PoolEntity.Companion.Owner
import com.example.zjt_plugin.ThreadPoolCreator.EXECUTORS_OWNER
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.io.IOException

/**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
 */
class ThreadAsmHelper : AsmHelper {

    @Throws(IOException::class)
    override fun modifyClass(srcClass: ByteArray, className: String): ByteArray {
        val classNode = ClassNode(Opcodes.ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)
        //2 对classNode的处理逻辑
        val iterator: Iterator<MethodNode> = classNode.methods.iterator()
        while (iterator.hasNext()) {
            val method = iterator.next()
            method.instructions?.iterator()?.forEach {
                if (it.opcode == Opcodes.INVOKESTATIC) { // INVOKESTATIC 针对的是 类方法 即静态方法
                    if (it is MethodInsnNode) {
                        it.hookExecutors2(className, method)
                    }
                } else if (it.opcode == Opcodes.INVOKEVIRTUAL) { // INVOKEVIRTUAL 针对的是 对象方法
                    if (it is MethodInsnNode) {
                        it.transformInvokeVirtual(className, method)
                    }
                } else if (it.opcode == Opcodes.INVOKESPECIAL) {
                    if (it is MethodInsnNode) {
                        it.transformInvokeSpecial(className, method)
                    }
                }
            }
        }
        val classWriter = ClassWriter(0)
        //3  将classNode转为字节数组
        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }

    private fun MethodInsnNode.hookExecutors(className: String) {
        when (this.owner) {
            EXECUTORS_OWNER -> {
                println("zjt_asm_thread_pool >>>> className = $className, name = $name")
                ThreadPoolCreator.poolList.forEach {
                    if (it.name == this.name && this.name == it.name && this.owner == it.owner) {
                        this.owner = Owner
                        this.name = it.methodName
                        this.desc = it.replaceDesc()
                    }
                }
            }
        }
    }

    private fun MethodInsnNode.hookExecutors2(className: String, methodNode: MethodNode) {
        // 过滤掉自己定义的替换线程池的类，否则会循环调用，栈溢出
        if (className.startsWith(BOOSTER_INSTRUMENT.replace("/", "."))) return
        when (this.owner) {
            EXECUTORS_OWNER -> {
                when (this.name) {
                    "newCachedThreadPool",
                    "newFixedThreadPool",
                    "newSingleThreadExecutor",
                    "newSingleThreadScheduledExecutor",
                    "newScheduledThreadPool",
                    "newWorkStealingPool" -> {
                        val r = this.desc.lastIndexOf(')')
                        val name = this.name.replace("new", "newOptimized")
                        val desc = "${
                            this.desc.substring(
                                0,
                                r
                            )
                        }Ljava/lang/String;${this.desc.substring(r)}"
//                        println(" * ${this.owner}.${this.name}${this.desc} => $SHADOW_EXECUTORS.$name$desc: ${className}.${methodNode.name}${methodNode.desc}")
                        println("zjt_asm jdk 自带线程池 >>>> className = $className, name = ${this.name}, desc = $desc, name = $name")
                        if (this.name.equals("newFixedThreadPool")) { // 测试，如果是 newFixedThreadPool的方式构建线程池，那么会通过ASM调用自己写的线程池
                            this.owner = SHADOW_EXECUTORS
                            this.name = "newMyExecutor"
                            this.desc = "(Ljava/lang/String;)${this.desc.substring(r + 1)}"
                        } else {
                            this.owner = SHADOW_EXECUTORS
                            this.name = name
                            this.desc = desc
                        }
                        methodNode.instructions.insertBefore(
                            this,
                            LdcInsnNode(makeThreadName(className))
                        )
                    }
                }
            }
        }
    }

    private fun MethodInsnNode.transformInvokeVirtual(className: String, methodNode: MethodNode) {
        // if (context.klassPool.get(THREAD).isAssignableFrom(this.owner))
        // 过滤掉自己定义的替换线程池的类，否则会循环调用，栈溢出
        if (className.startsWith(BOOSTER_INSTRUMENT.replace("/", "."))) return
        when (this.owner) {
            THREAD -> {
                println("zjt_asm_thread >>>> className = $className, name = ${this.name}, desc = $desc")
                when ("${this.name}${this.desc}") {
                    "start()V" -> { // 为start方法的线程加上名字
                        methodNode.instructions.insertBefore(
                            this,
                            LdcInsnNode(makeThreadName(className))
                        )
                        methodNode.instructions.insertBefore(
                            this,
                            MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                SHADOW_THREAD,
                                "setThreadName",
                                "(Ljava/lang/Thread;Ljava/lang/String;)Ljava/lang/Thread;",
                                false
                            )
                        )
//                    logger.println(" + $SHADOW_THREAD.makeThreadName(Ljava/lang/String;Ljava/lang/String;) => ${this.owner}.${this.name}${this.desc}: ${klass.name}.${method.name}${method.desc}")
                        this.owner = THREAD
                    }
                    "setName(Ljava/lang/String;)V" -> {
                        methodNode.instructions.insertBefore(
                            this,
                            LdcInsnNode(makeThreadName(className))
                        )
                        methodNode.instructions.insertBefore(
                            this,
                            MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                SHADOW_THREAD,
                                "makeThreadName",
                                "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
                                false
                            )
                        )
//                    logger.println(" + $SHADOW_THREAD.makeThreadName(Ljava/lang/String;Ljava/lang/String;) => ${this.owner}.${this.name}${this.desc}: ${klass.name}.${method.name}${method.desc}")
                        this.owner = THREAD
                    }
                }
            }
        }
    }

    private fun MethodInsnNode.transformInvokeSpecial(className: String, methodNode: MethodNode) {
        if (this.name != "<init>") {
            return
        }
        when (this.owner) {
//            THREAD -> transformThreadInvokeSpecial(klass, method)
//            HANDLER_THREAD -> transformHandlerThreadInvokeSpecial(klass, method)
//            TIMER -> transformTimerInvokeSpecial(klass, method)
            THREAD_POOL_EXECUTOR -> transformThreadPoolExecutorInvokeSpecial(className, methodNode)
        }
    }

    private fun MethodInsnNode.transformThreadPoolExecutorInvokeSpecial(
        className: String,
        method: MethodNode,
        init: MethodInsnNode = this
    ) {
        // 过滤掉自己定义的替换线程池的类，否则会循环调用，栈溢出
        if (className.startsWith(BOOSTER_INSTRUMENT.replace("/", "."))) return

        when (this.desc) {
            // ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue)
            "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;)V" -> {
                println("zjt_asm 自定义线程池 >>>> className = $className, name = ${this.name}, desc = $desc")
                method.instructions.apply {
                    // ..., queue => ..., queue, prefix
                    insertBefore(init, LdcInsnNode(makeThreadName(className)))
                    // ..., queue, prefix => ..., queue, factory
                    insertBefore(
                        init,
                        MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            NAMED_THREAD_FACTORY,
                            "newInstance",
                            "(Ljava/lang/String;)Ljava/util/concurrent/ThreadFactory;",
                            false
                        )
                    )
                }
                this.desc = "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;)V"
            }
            // ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue, ThreadFactory)
            "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;)V" -> {
                println("zjt_asm 自定义线程池 >>>> className = $className, name = ${this.name}, desc = $desc")
                method.instructions.apply {
                    // ..., factory => ..., factory, prefix
                    insertBefore(init, LdcInsnNode(makeThreadName(className)))
                    // ..., factory, prefix => ..., factory
                    insertBefore(
                        init,
                        MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            NAMED_THREAD_FACTORY,
                            "newInstance",
                            "(Ljava/util/concurrent/ThreadFactory;Ljava/lang/String;)Ljava/util/concurrent/ThreadFactory;",
                            false
                        )
                    )
                }
            }
            // ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue, RejectedExecutionHandler)
            "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/RejectedExecutionHandler;)V" -> {
                println("zjt_asm 自定义线程池 >>>> className = $className, name = ${this.name}, desc = $desc")
                method.instructions.apply {
                    // ..., handler => ..., handler, prefix
                    insertBefore(init, LdcInsnNode(makeThreadName(className)))
                    // ..., handler, prefix => ..., handler, factory
                    insertBefore(
                        init,
                        MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            NAMED_THREAD_FACTORY,
                            "newInstance",
                            "(Ljava/lang/String;)Ljava/util/concurrent/ThreadFactory;",
                            false
                        )
                    )
                    // 因为 ThreadFactory作为参数是在 RejectedExecutionHandler的前面，所以要交换，否则生产的class 文件为
                    /**
                     *   ExecutorService service1 = new ThreadPoolExecutor(1, 3, 10000L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(1), new RejectedExecutionHandler() {
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                            Log.e("hook_thread_pool", Thread.currentThread().getName() + " >>> test define thread_pool2 rejected:");
                          }
                        }, NamedThreadFactory.newInstance("\u200bcom.zjt.startmodepro.concurrent.TestHookThreadPool"));
                     */
                    // ..., RejectedExecutionHandler, ThreadFactory => ..., ThreadFactory, RejectedExecutionHandler
                    insertBefore(init, InsnNode(Opcodes.SWAP))
                }
                this.desc =
                    "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;)V"
            }
            // ThreadPoolExecutor(int, int, long, TimeUnit, BlockingQueue, ThreadFactory, RejectedExecutionHandler)
            "(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;Ljava/util/concurrent/RejectedExecutionHandler;)V" -> {
                println("zjt_asm 自定义线程池 >>>> className = $className, name = ${this.name}, desc = $desc")
                method.instructions.apply {
                    /**
                     * 因为 RejectedExecutionHandler 在操作数栈中的位置是在 ThreadFactory 的上面，如果不交换操作，那么操作的就是 RejectedExecutionHandler，结果如下：
                     *  ExecutorService service1 = new ThreadPoolExecutor(1, 3, 10000L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(1), new ZjtThreadFactory(), NamedThreadFactory.newInstance(new RejectedExecutionHandler() {
                        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                            Log.e("hook_thread_pool", Thread.currentThread().getName() + " >>> test define thread_pool2 rejected:");
                         }
                       }, "\u200bcom.zjt.startmodepro.concurrent.TestHookThreadPool"));
                      * ------- 可以看到之后的2步操作都是操作在RejectedExecutionHandler中了
                     */
                    // ..., factory, handler => ..., handler, factory
                    insertBefore(init, InsnNode(Opcodes.SWAP))
                    // ..., handler, factory => ..., handler, factory, prefix
                    insertBefore(init, LdcInsnNode(makeThreadName(className)))
                    // ..., handler, factory, prefix => ..., handler, factory
                    insertBefore(
                        init,
                        MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            NAMED_THREAD_FACTORY,
                            "newInstance",
                            "(Ljava/util/concurrent/ThreadFactory;Ljava/lang/String;)Ljava/util/concurrent/ThreadFactory;", false
                        )
                    )
                    // ..., handler, factory => ..., factory, handler
                    insertBefore(init, InsnNode(Opcodes.SWAP))
                }
            }
        }
    }


    // com.zjt.startmodepro.hook.threadpool
    private val BOOSTER_INSTRUMENT = "com/zjt/startmodepro/hook/threadpool/"
    private val SHADOW = "${BOOSTER_INSTRUMENT}Shadow"
    private val SHADOW_EXECUTORS = "${SHADOW}Executors"
    private val MARK = "\u200B" // 这个是不可见字符

    private val JAVA_UTIL = "java/util/"
    private val THREAD = "java/lang/Thread"
    private val JAVA_UTIL_CONCURRENT = "${JAVA_UTIL}concurrent/"
    private val SHADOW_THREAD = "${SHADOW}Thread"
    private val NAMED_THREAD_FACTORY = "${BOOSTER_INSTRUMENT}NamedThreadFactory"
    private val THREAD_POOL_EXECUTOR = "${JAVA_UTIL_CONCURRENT}ThreadPoolExecutor"

    private fun makeThreadName(name: String) = MARK + name
}