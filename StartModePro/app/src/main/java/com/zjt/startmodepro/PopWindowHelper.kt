package com.zjt.startmodepro

import android.content.Context
import android.util.Log
import android.view.View
import com.zjt.startmodepro.widget.MyPopWindow
import java.util.*

typealias BubbleDismissedNotify = (ticket: Int) -> Unit
typealias BubbleFuncBlock = () -> Unit

class PopWindowHelper(val context: Context) {

    private var isShowing = false

    // 一次只能展示一个ticket
    private val willOnStageBubbleQueue = LinkedList<Pair<BubbleFuncBlock, BubbleFuncBlock>>()

    private fun add2BubbleQueued(display: BubbleFuncBlock, dismiss: BubbleFuncBlock) {
        willOnStageBubbleQueue.add(Pair(display, dismiss))
        tryDisplayBubble()
    }

    private fun tryDisplayBubble() {
        if (!isShowing && willOnStageBubbleQueue.isNotEmpty()) {
            val bubble = willOnStageBubbleQueue.pollFirst()!!
            isShowing = true
            bubble.first.invoke()
        }
    }

    fun show(anchorView: View, content: String, pos :Int) {
        val myPopWindow = MyPopWindow(context)
        add2BubbleQueued({
            myPopWindow.showBubble(anchorView, content, pos) {
                Log.e("MyPopWindow", "----- end -----")
                isShowing = false
                tryDisplayBubble()
            }
        }, {
            Log.e("MyPopWindow", "----- do nothing -----")
        })
    }

}