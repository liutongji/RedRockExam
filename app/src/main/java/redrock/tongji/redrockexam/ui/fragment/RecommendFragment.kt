package redrock.tongji.redrockexam.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.canScrollVertically
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import redrock.tongji.lib_base.base.BaseBindVMFragment
import redrock.tongji.redrockexam.App
import redrock.tongji.redrockexam.R
import redrock.tongji.redrockexam.bean.CommonData
import redrock.tongji.redrockexam.databinding.FragmentRecBinding
import redrock.tongji.redrockexam.ext.init
import redrock.tongji.redrockexam.ext.initFloatBtn
import redrock.tongji.redrockexam.ext.showToast
import redrock.tongji.redrockexam.ui.activity.PlayVideoActivity
import redrock.tongji.redrockexam.ui.adapter.RecommendAdapter
import redrock.tongji.redrockexam.ui.viewmodel.RecommendViewModel

/**
 * @Author Tongji
 * @Description
 * @Date create in 2022/7/17 19:10
 */
class RecommendFragment : BaseBindVMFragment<RecommendViewModel, FragmentRecBinding>() {

    private lateinit var recAdapter: RecommendAdapter
    private val viewModel by lazy { ViewModelProvider(this)[RecommendViewModel::class.java] }

    override val getLayoutRes: Int
        get() = R.layout.fragment_rec

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        val rvRec = mDatabind.includeList.includeRecyclerview.recyclerView
        rvRec.layoutManager = LinearLayoutManager(App.context)
        rvRec.setItemViewCacheSize(10)
        viewModel.loadRec()
        viewModel.recPathData.observerKt { result ->
            val list = result.getOrNull()
            if (list != null) {
                viewModel.listData.clear()
                viewModel.listData.addAll(list)
                recAdapter = RecommendAdapter(App.context, list)
                rvRec.adapter = recAdapter

                // ?????????????????????????????????
                rvRec.initFloatBtn(mDatabind.includeList.floatbtn)

                // ????????????????????????
                rvRec.layoutAnimation = LayoutAnimationController(AnimationUtils.loadAnimation(App.context, R.anim.animation))

                // ????????????
                mDatabind.includeList.includeRecyclerview.swipeRefresh.init {
                    viewModel.loadRec()
                    recAdapter.notifyDataSetChanged()
                    mDatabind.includeList.includeRecyclerview.swipeRefresh.isRefreshing = false
                }

                recAdapter.setOnItemClickListener(object : RecommendAdapter.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val intent = Intent(context, PlayVideoActivity::class.java)
                        val bundle = Bundle()
                        val videoData = CommonData(
                            list[position].url, list[position].playUrl,
                            list[position].time, list[position].title,
                            list[position].author, list[position].description ?: "",
                            list[position].id, list[position].blurred
                        )
                        bundle.putSerializable("data", videoData)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    override fun onItemLongClick(view: View, position: Int) {

                    }
                })
            } else {
                this.showToast("????????????????????????555~")
            }
        }

        viewModel.morePathData.observerKt { result ->
            val list = result.getOrNull()
            if (list != null) {
                viewModel.listData.addAll(list)
                recAdapter.addMore(list)
                recAdapter.notifyDataSetChanged()
            } else {
                this.showToast("??????????????????~")
            }
        }

        // ???????????????????????????????????????
        rvRec.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!canScrollVertically(recyclerView, 1)) {
                    viewModel.loadMore(viewModel.listData[viewModel.listData.size - 1].nextUrl)
                    this@RecommendFragment.showToast("???????????????")
                }
            }
        })
    }

}