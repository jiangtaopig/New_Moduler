package com.example.lib;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TestJsonParser {
    public static void main(String[] args) {
        String content = "{\n" +
                "\n" +
                "\t\"name\": \"zhujiang\",\n" +
                "\n" +
                "\t\"age\": 23,\n" +
                "\n" +
                "\t\"result\": {\n" +
                "\n" +
                "\t\t\"code\": 200,\n" +
                "\n" +
                "\t\t\"data\": [\n" +
                "\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"title\": \"我成功额\",\n" +
                "\n" +
                "\t\t\t\t\"content\": \"哈哈哈哈\"\n" +
                "\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t]\n" +
                "\n" +
                "\t}\n" +
                "\n" +
                "}";

        JSONObject jsonObject = JSON.parseObject(content);

        // 替换json里面的数据
        jsonObject.put("name", "无穷");
        String name = jsonObject.getString("name");
        System.out.println("name = "+name);

        JSONObject result = jsonObject.getJSONObject("result");
        String code = result.getString("code");
        System.out.println("code = "+code);


        // 获取json 数组
        JSONArray jsonArray = result.getJSONArray("data");
        // 获取数组里面的第一个jsonObject
        JSONObject item0 = jsonArray.getJSONObject(0);
        String title = item0.getString("title");
        System.out.println("title = "+title);
    }
}
