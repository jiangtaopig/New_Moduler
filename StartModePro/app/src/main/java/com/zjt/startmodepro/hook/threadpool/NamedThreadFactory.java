package com.zjt.startmodepro.hook.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

//import static com.didiglobal.booster.instrument.ShadowThread.setThreadName;

public class NamedThreadFactory implements ThreadFactory{
     final String MARK = "\u200B";
    /**
     * Used by {@code ThreadTransformer} for thread renaming
     *
     * @param prefix the prefix of name
     * @return an instance of ThreadFactory
     */
    public static ThreadFactory newInstance(final String prefix) {
        return new NamedThreadFactory(prefix);
    }

    /**
     * Used by {@code ThreadTransformer} for thread renaming
     *
     * @param factory the factory delegate
     * @param prefix  the prefix of name
     * @return an instance of ThreadFactory
     */
    public static ThreadFactory newInstance(final ThreadFactory factory, final String prefix) {
        return new NamedThreadFactory(factory, prefix);
    }

    private final String name;
    private final AtomicInteger counter = new AtomicInteger(1);
    private final ThreadGroup group;
    private final ThreadFactory factory;

    public NamedThreadFactory(final String name) {
        this(null, name);
    }

    public NamedThreadFactory(final ThreadFactory factory, final String name) {
        this.factory = factory;
        this.name = name;
        this.group = Thread.currentThread().getThreadGroup();

    }

    @Override
    public Thread newThread(final Runnable r) {
        if (null == this.factory) {
            final Thread t = new Thread(this.group, r, this.name + "#" + this.counter.getAndIncrement(), 0);

            if (t.isDaemon()) {
                t.setDaemon(false);
            }

            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }

            return t;
        }

        return setThreadName(this.factory.newThread(r), this.name);
    }

    public Thread setThreadName(final Thread t, final String prefix) {
        t.setName(makeThreadName(t.getName(), prefix));
        return t;
    }

    public String makeThreadName(final String name, final String prefix) {
        return name == null ? prefix : (name.startsWith(MARK) ? name : (prefix + "#" + name));
    }
}
