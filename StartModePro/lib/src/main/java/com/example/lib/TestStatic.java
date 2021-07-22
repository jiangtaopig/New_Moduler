package com.example.lib;

public class TestStatic {
    public static void main(String[] args) {
        ThreadLocalHashCode hashCode = new ThreadLocalHashCode();
        int code1 = hashCode.getHashCode();

        ThreadLocalHashCode hashCode2 = new ThreadLocalHashCode();
        int code2 = hashCode2.getHashCode();

        System.out.printf("code1 = "+code1+", code2 = "+code2);
    }
}
