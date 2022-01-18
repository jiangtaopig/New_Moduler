package com.example.lib.multithread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture 和 Future 的最大区别在于获取异步结果的方式：
 * Future 只能通过 get 方法，如果此时任务还未执行完，那么会阻塞调用 get() 方法所在的线程。
 * CompletableFuture 是类似于Rxjava 的链式操作，但不会调用 CompletableFuture 执行任务的线程
 */
public class TestFutureTask {
    private static ExecutorService executorService = new ThreadPoolExecutor(1, 2, 60_000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(4));

    public static void main(String[] args) {
        System.out.println("-----------------");

//        testCompletableFuture();
//        completableFutureWithoutReturnValue();
//        completableFutureWithMoreTask();
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService , new LinkedBlockingDeque<>(2));
        completionService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("---- start-----");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("---- end ----");
            }
        }, "哈哈哈");

        try {
           String result = completionService.take().get();
            System.out.println(" result " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }


    private static void test(Object obj) {
        System.out.println("obj = " + obj);
    }


    /**
     * 带返回值的用法 whenComplete
     */
    private static void testCompletableFuture() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("小米正在吃饭，请稍等");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "小米已经吃好饭了";
        }, executorService)
                .whenComplete((res, exception) -> { // 带返回值 ：res 即返回值
                    System.out.println("xxx _ " + res);
                });

        System.out.println("小米在吃饭，可以自己玩会");
        executorService.shutdown();
    }

    /**
     * 不需要返回值可以用 thenAccept
     */
    private static void completableFutureWithoutReturnValue() {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("小米正在吃饭，请稍等");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "小米已经吃好饭了";
        }, executorService)
                .thenAccept(v -> { // 不需要返回值
                    System.out.println("萌虎，一起去玩吧-----");
                }).exceptionally(throwable -> {
                    // 处理异常
                    throwable.printStackTrace();
                    return null;
                });
        executorService.shutdown();
    }

    /**
     * 2 个串行的任务
     */
    private static void completableFutureWithMoreTask() {
        // 小米吃完饭后要去换衣服， 这是2个串行的任务
        // 吃饭的任务
        CompletableFuture<String> eatFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("小米正在吃饭，请稍等");
            try {

                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "小米已经吃好饭了";
        }, executorService);


        CompletableFuture<String> dressFuture = eatFuture.thenApply(eat -> {
            System.out.println(eat+" , 开始穿衣服了");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return eat + " 衣服也穿好了！";
        });

        dressFuture.thenAccept(res -> {
            System.out.println(" res >>> " + res);
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
        executorService.shutdown();
    }

    private static void testFutureTask() {

        // FutureTask 的2中使用方法
//        FutureTask<String> future = new FutureTask<String>(()->{
//            return "";
//        });
//
//        new Thread(future).start();
//        future.get();

        ExecutorService executorService = new ThreadPoolExecutor(1, 3, 60_000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());

        Future<String> future = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " >>> start");
            Thread.sleep(1_000);
            System.out.println(Thread.currentThread().getName() + " >>> end");
            return "xxxx";
        });

        try {
            String res = future.get();
            System.out.println("res = " + res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

        System.out.println(Thread.currentThread().getName() + ".... end .....");

        String m;
        test(m = "233");
    }
}
