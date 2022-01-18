package com.zjt.startmodepro.bean

@ExperimentalStdlibApi
class Person {
    var name :String = ""
    set(value) {
        field = value.lowercase().replaceFirstChar { it.uppercase() } // 首字母大写，其余的小写
    }

    get() {
        return field + "-" + field.length // 返回数值后加上数值的长度
    }

}