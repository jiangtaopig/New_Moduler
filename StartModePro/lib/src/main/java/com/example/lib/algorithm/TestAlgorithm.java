package com.example.lib.algorithm;

import java.util.HashMap;

public class TestAlgorithm {
    public static void main(String[] args) {

        int[] nums = {4, 1, 2, 3, 5, 6, 8, 9, 10};
        exchange2(nums);
//        for (int i = 0; i < nums.length; i++) {
//            System.out.println(nums[i]);
//        }

        int[] nums2 = {1, 3, 4 ,8, 2, 6};
        findNum(nums2, 6);
    }

    /**
     * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有奇数位于数组的前半部分，所有偶数位于数组的后半部分。
     * 输入：nums = [1,2,3,4]
     * 输出：[1,3,2,4]
     * 注：[3,1,2,4] 也是正确的答案之一。
     *
     * @param nums
     */
    private static void exchange(int[] nums) {
        // 从前往后遍历，如果发现是偶数那么从数组的尾部开始比较，如果是尾部的数据是奇数则交换
        // 注意，j > i
        for (int i = 0, len = nums.length, j = len - 1; i < len; i++) {
            if ((nums[i] & 1) == 0) {
                while ((nums[j] & 1) == 0 && j > i) {
                    j--;
                }
                if (j > i) {
                    int tmp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = tmp;
                }
            }
        }
    }


    private static void exchange2(int [] nums) {
        int left = 0;
        int right = nums.length - 1;
        int tmp;

        // 移动 left 和 right 直到 left 在 right d的右侧 或者相遇为止
        while (left < right) {
            // 如果 left 指针指向的元素值是奇数，那么说明该元素在左侧了，观察其它的元素，即让 left 向右移动
            while (left < right && ((nums[left] & 1)== 1)) {
                left++;
            }

            // 如果 right 指针指向的元素是偶数，那么说明该元素已经在右侧了，即让 right 向左移动
            while ((left < right &&((nums[right] & 1) == 0))) {
                right--;
            }
            // 否则就说明，此时要么 left 指向的元素值为偶数，要么 right 指向的元素值为奇数
            // 交换这两个位置的元素
            if (left < right) {
                tmp = nums[left];
                nums[left] = nums[right];
                nums[right] = tmp;
            }
        }
    }

    /**
     * 从 nums 数组中找2个元素之和等于 target 的位置
     * @param nums
     * @param target
     */
    private static void findNum(int [] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0, len = nums.length; i < len; i++){
            if (map.containsKey(target - nums[i])){
                System.out.println("i = "+i+", j = " + map.get(target - nums[i]));
            } else  {
                map.put(nums[i], i);
            }
        }
    }
}
