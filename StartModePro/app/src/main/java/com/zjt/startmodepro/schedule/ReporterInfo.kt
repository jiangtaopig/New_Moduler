package com.zjt.startmodepro.schedule

class ReporterInfo() {
    private var cpuTotal = 0.0f
    private var averageCpu = 0.0f
    private var memory = 0.0f
    private var averageMemory = 0.0f

    fun setCpuTotal(cpuTotal :Float){
        this.cpuTotal = cpuTotal
    }

    fun setAverageCpu(averageCpu :Float){
        this.averageCpu = averageCpu
    }

    fun setMemory(memory :Float){
        this.memory = memory
    }

    fun setAverageMemory(averageMemory :Float){
        this.averageMemory = averageMemory
    }

    fun getCpuTotal() = this.cpuTotal


    fun getAverageCpu() = this.averageCpu

    fun getMemory() = this.memory

    fun getAverageMemory() = this.averageMemory

}