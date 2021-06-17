package com.zjt.startmodepro;

public class TestBuilder {
    public String name;
    public int age;

    public TestBuilder(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public static class Builder {
        private String name;
        private int age;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public TestBuilder build() {
            return new TestBuilder(name, age);
        }
    }
}
