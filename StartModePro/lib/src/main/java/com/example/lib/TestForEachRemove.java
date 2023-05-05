package com.example.lib;

import java.util.ArrayList;
import java.util.List;

public class TestForEachRemove {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>(5);
        strings.add("1");
        strings.add("2");
        strings.add("3");

        for (String d : strings) {
            if ("2".equals(d)){
                strings.remove(d);
            }
        }
    }
}
