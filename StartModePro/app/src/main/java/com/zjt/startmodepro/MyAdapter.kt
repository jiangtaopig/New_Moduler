package com.zjt.startmodepro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyHolder>() {

    private var mDataList = mutableListOf<String>()
    private var listener : MyOnClickListener? = null

    fun setListener(listener: MyOnClickListener) {
        this.listener = listener
    }

    fun setDataList(list: List<String>) {
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<String>) {
        val start = mDataList.size
        mDataList.addAll(list)
//        notifyDataSetChanged()
        notifyItemRangeChanged(start, mDataList.size -1)
    }

    fun deleteSpecifyData(position: Int) {
        mDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_item_view_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bindData(position, mDataList)
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mText: TextView = itemView.findViewById(R.id.tv_item)

        fun bindData(position: Int, list: List<String>) {
            mText.text = list[position]
            itemView.setOnClickListener {
                listener?.onClick(position)
            }
        }
    }
}

interface MyOnClickListener {
    fun onClick(position :Int)
}