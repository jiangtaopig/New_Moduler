package com.example.lib;

public class TestException {
    public static void main(String[] args) {
        testCatchException();
    }

    private static void testCatchException() {
        try {
            for (int i = 0; i < 5; i++){
                System.out.println(" i = " + i);
                try {
                    Throwable thrown = null;
                    try {
                        if (i == 2) {
                            int a = 2 / 0;
                        }
                    } catch (RuntimeException x) {
                        System.out.println("抛出异常了");
                        thrown = x;
                        throw x;
                    } catch (Error x) {
                        thrown = x;
                        throw x;
                    } catch (Throwable x) {
                        thrown = x;
                        throw new Error(x);
                    } finally {
                        System.out.println("finally ----- 1");
                    }
                } finally {
                    System.out.println("finally ----- 2");
                }
            }
        } finally {
            System.out.println("finally ----- 3");
        }
    }
}
