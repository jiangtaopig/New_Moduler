package com.example.lib;

import java.util.Calendar;

public class TestAge {

    public static void main(String[] args) {

        String idCard = "340823202022234438";

        int age = getPersonAgeFromIdCard(idCard);
        System.out.println("age = " +age + "周岁");
    }



    public static Integer getPersonAgeFromIdCard(String idCard) {

        //截取身份证中出行人出生日期中的年、月、日
        Integer personYear = Integer.parseInt(idCard.substring(6, 10));
        Integer personMonth = Integer.parseInt(idCard.substring(10, 12));
        Integer personDay = Integer.parseInt(idCard.substring(12, 14));

        Calendar cal = Calendar.getInstance();
        // 得到当前时间的年、月、日
        Integer yearNow = cal.get(Calendar.YEAR);
        Integer monthNow = cal.get(Calendar.MONTH) + 1;
        Integer dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去生日年月日
        Integer yearMinus = yearNow - personYear;
        Integer monthMinus = monthNow - personMonth;
        Integer dayMinus = dayNow - personDay;

        Integer age = yearMinus; //先大致赋值

        if (yearMinus <= 0) { //出生年份为当前年份
            age = 0;
        }else{ //出生年份大于当前年份
            if (monthMinus < 0) {//出生月份小于当前月份时，还没满周岁
                age = age - 1;
            }
            if (monthMinus == 0) {//当前月份为出生月份时，判断日期
                if (dayMinus < 0) {//出生日期小于当前月份时，没满周岁
                    age = age - 1;
                }
            }
        }
        return age;
    }
}
