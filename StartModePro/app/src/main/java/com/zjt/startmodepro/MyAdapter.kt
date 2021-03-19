package com.zjt.startmodepro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter : RecyclerView.Adapter<MyHolder>() {

    private var mDataList = mutableListOf<String>()

    fun setDataList(list: List<String>) {
        mDataList.clear()
        mDataList.addAll(list)
        notifyDataSetChanged()
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

}

class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mText: TextView = itemView.findViewById(R.id.tv_item)

    fun bindData(position: Int, list: List<String>) {
        mText.text = list[position]
    }

}