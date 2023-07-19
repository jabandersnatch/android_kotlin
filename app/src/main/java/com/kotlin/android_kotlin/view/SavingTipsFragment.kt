package com.kotlin.android_kotlin.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.kotlin.android_kotlin.R
import com.kotlin.android_kotlin.data.savingTips.SavingTipsDao
import com.kotlin.android_kotlin.domain.savingTips.SavingTipsRepository
import com.kotlin.android_kotlin.view.adapters.CarouselRVAdapter
import com.kotlin.android_kotlin.viewmodel.savingTips.SavingTipsViewModel
import com.kotlin.android_kotlin.viewmodel.savingTips.SavingTipsViewModelFactory
import kotlin.math.abs

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SavingTipsFragment : Fragment(R.layout.fragment_saving_tips) {

    private lateinit var viewModel: SavingTipsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = SavingTipsDao()
        val repository = SavingTipsRepository(dao)
        viewModel = ViewModelProvider(
            this,
            SavingTipsViewModelFactory(repository)
        )[SavingTipsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)

        viewPager.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }

        viewModel.getAllSavingTips()

        viewModel.savingTipsList.observe(viewLifecycleOwner) { savingTipsList ->
            val carouselDataList = ArrayList<String>()
            for (savingTip in savingTipsList) {
                carouselDataList.add(savingTip.tip)
            }

            viewPager.adapter = CarouselRVAdapter(carouselDataList)

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer((40 * resources.displayMetrics.density).toInt()))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.80f + r * 0.2f
            }
            viewPager.setPageTransformer(compositePageTransformer)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavingTipsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
