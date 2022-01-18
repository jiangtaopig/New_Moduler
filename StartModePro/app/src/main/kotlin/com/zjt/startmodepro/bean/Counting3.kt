package com.zjt.startmodepro.bean

/**
 * 委托
 */
class Counting3<T>(
    private val innerSet: MutableSet<T> = HashSet<T>()
) : MutableSet<T> by innerSet {

}