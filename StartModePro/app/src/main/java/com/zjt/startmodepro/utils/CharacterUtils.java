package com.zjt.startmodepro.utils;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/26 1:42 下午
 * @Description : CharacterUtils
 */


public class CharacterUtils {
    /**
     * 获取字符串的长度，汉字2个字符
     * @param txt
     * @return
     */
    public static int getTextLength(String txt) {
        char[] chars = txt.toCharArray();
        int counter = 0;
        for (char str : chars) {
            if (isChineseByBlock(str)) {
                counter += 2;
            } else {
                counter++;
            }
        }
        return counter;
    }

    //使用UnicodeBlock方法判断是否为中文字符
    public static boolean isChineseByBlock(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
                || ub.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
                || ub.equals(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B)
                || ub.equals(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS)
                || ub.equals(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT)){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 用于处理汉字和英文混合的字符
     *
     * @param origin 待处理的字符串
     * @param limits 最终字符串返回长度
     * @return 指定长度limits的字符串
     */
    public static String getLimitMedal(String origin, int limits) {
        if (origin == null){
            return "";
        }
        char[] chars = origin.toCharArray();
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (char str : chars) {
            if (isChineseByBlock(str)) {
                counter += 2;
            } else {
                counter++;
            }
            if (counter > limits) {
                return sb.toString();
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

}
