package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zjt.startmodepro.my_kotlin.KotlinHelper
import com.zjt.startmodepro.my_kotlin.KotlinHelper.isInstanceOf
import com.zjt.startmodepro.my_kotlin.MyKotlinManager
import com.zjt.startmodepro.my_kotlin.OnSuccessListener
import com.zjt.startmodepro.viewmodel.JetPack3ViewModel

class JetPack3Activity : AppCompatActivity() {

    companion object {
        fun enter(context: Context) {
            var intent = Intent(context, JetPack3Activity::class.java)
            if (context !is Activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private lateinit var mDataChangeTv: TextView
    private lateinit var mShowBtn: Button
    private lateinit var mRecycleView: RecyclerView

    lateinit var mViewModel: JetPack3ViewModel
    private lateinit var mMyAdapter: MyAdapter
    private val originalData = MutableLiveData<String>()
    private var curPage = 1

    private val mapData = Transformations.map(originalData) {
        it.plus(" world ...")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack_3_layout)

        mapData.observe(this) {
            Log.e("xxaa", "mapData it = $it")
        }

        mDataChangeTv = findViewById(R.id.txt_title)
        mShowBtn = findViewById(R.id.btn_change_data)
        mRecycleView = findViewById(R.id.recycle_view)


        mDataChangeTv.setOnClickListener {
            originalData.value = "hello"
        }

        mRecycleView.layoutManager = LinearLayoutManager(this)
        mMyAdapter = MyAdapter()
        mMyAdapter.setListener(object : MyOnClickListener {
            override fun onClick(position: Int) {
                mMyAdapter.deleteSpecifyPositionData(position)
            }
        })
        mRecycleView.adapter = mMyAdapter

        mRecycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition = manager.findLastVisibleItemPosition();
                val cnt = manager.itemCount
                val itemCount = mMyAdapter.itemCount

                Log.e("RecycleView", "dx = $dx , dy = $dy")

//                Log.e(
//                    "RecycleView",
//                    "lastVisiblePosition = $lastVisiblePosition , cnt = $cnt, itemCount = $itemCount"
//                )
                /**
                 * recycleview 分页加载逻辑
                 */
//                if (dy > 0 && (lastVisiblePosition + 2 > cnt)) {
//                    getNextPage()
//                }
            }
        })

        mViewModel = ViewModelProvider(this).get(JetPack3ViewModel::class.java)
        mViewModel.mStudent.observe(this, Observer {
            mDataChangeTv.text = "name = ${it.name}, age = ${it.age}"
        })

        mShowBtn.setOnClickListener {
            var a: String? = null
            val b = a ?: "zz" // 表示如果 a 为 null 的话，那么 b的值等于后面的 "zz"
            Log.e("zjt", "b = $b, a length = ${a?.length}")

            val myKotlinManager = MyKotlinManager("zjt", 32)
            myKotlinManager.showTops()
            myKotlinManager.setOnSuccessListener(object : OnSuccessListener {
                override fun onSuccess(msg: String) {
                    Log.e("zjt", "------ msg = $msg ------")
                }
            })

            myKotlinManager.doInterface()
            var res = myKotlinManager.ff()
            Log.e("zjt", "res = $res")
            myKotlinManager.fff()
            myKotlinManager.testWhen()
            myKotlinManager.doPrintln("1234")

            var isPositiveNum = KotlinHelper.isPositiveNum(x = 3)
            Log.e("zjt", "isPositiveNum = $isPositiveNum")

            var isKotlinHelper = KotlinHelper.isInstanceOf<KotlinHelper>()
            Log.e("zjt", "isKotlinHelper = $isKotlinHelper")

            var isString = "123".isInstanceOf<String>()
            Log.e("zjt", "isString = $isString")

            mViewModel.getStudentInfo()
            val list = mutableListOf<String>()
            for (i in 1..10) {
                list.add("数据--$i")
            }
            mMyAdapter.setDataList(list)
        }
    }

    private fun getNextPage() {
        Log.e("RecycleView", "getNextPage ------- curPage = $curPage -------  ")
        curPage++
        val start = (curPage - 1) * 10 + 1
        val end = curPage * 10
        val list = mutableListOf<String>()
        for (i in start..end) {
            list.add("数据--$i")
        }
        mMyAdapter.addData(list)
    }

}