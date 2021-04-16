package com.zjt.startmodepro.my_kotlin.delegate

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/3/31 10:28 上午

 * @Description : RedApple

 */


class RedApple : Fruit by Apple(), Color by Red() {
    fun showName(){
        name()
        color()
    }
}