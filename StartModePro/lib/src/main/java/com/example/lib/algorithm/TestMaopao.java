package com.example.lib.algorithm;

public class TestMaopao {

    public static void main(String[] args) {

        int[] arr = {3, 9 ,7, 12, 5, 27, 8};
        arr = maopaoSort2(arr);
        traverseArray(arr);
    }

    /**
     * 倒序
     * @param arr
     * @return
     */
    private static int [] maopaoSort1(int [] arr) {
        int len = arr.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = i+1; j < len; j++) {
                if (arr[i] < arr[j]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        return arr;
    }

    /**
     * 正序
     * @param arr
     * @return
     */
    private static int [] maopaoSort2(int [] arr) {
        int len = arr.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = i+1; j < len; j++) {
                if (arr[i] > arr[j]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        return arr;
    }

    private static void traverseArray(int [] arr) {
        for (int i = 0, len = arr.length; i< len; i++) {
            System.out.print(arr[i] + "   ");
        }
    }
}
