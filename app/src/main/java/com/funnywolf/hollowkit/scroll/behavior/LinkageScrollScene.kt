package com.funnywolf.hollowkit.scroll.behavior

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bytedance.scene.group.UserVisibleHintGroupScene
import com.funnywolf.hollowkit.R
import com.funnywolf.hollowkit.drawable.RoundRectDrawable
import com.funnywolf.hollowkit.utils.*
import com.funnywolf.hollowkit.view.scroll.behavior.BottomSheetLayout
import com.funnywolf.hollowkit.view.scroll.behavior.LinkageScrollLayout

/**
 * @author https://github.com/funnywolfdadada
 * @since 2020/9/26
 */
class LinkageScrollScene: UserVisibleHintGroupScene() {

    private lateinit var layoutTop: FrameLayout
    private lateinit var rvTop: RecyclerView

    private lateinit var layoutBottom: FrameLayout
    private lateinit var rvBottom: RecyclerView

    private lateinit var bottomSheet: BottomSheetLayout

    private lateinit var linkageScroll: LinkageScrollLayout

    private var floating = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): ViewGroup {
        return inflater.inflate(R.layout.scene_linkage_scroll, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linkageScroll = view.findViewById(R.id.linkageScroll)
        layoutTop = view.findViewById(R.id.layoutTop)
        rvTop = view.findViewById(R.id.rvTop)
        layoutBottom = view.findViewById(R.id.layoutBottom)
        rvBottom = view.findViewById(R.id.rvBottom)
        bottomSheet = view.findViewById(R.id.bottomSheet)

        rvTop.initPictures()
        rvBottom.initPictures()
        rvBottom.background = RoundRectDrawable(0xFFBD5B2B.toInt(), 20.dp, 20.dp, 0, 0)

        linkageScroll.topScrollTarget = { rvTop }
        linkageScroll.onScrollChangedListeners.add {
            updateFloatState()
        }

        bottomSheet.setup(BottomSheetLayout.POSITION_MIN, 100.dp)
        updateFloatState()
    }

    private fun updateFloatState() {
        if (floating) {
            if (linkageScroll.scrollY != 0) {
                floating = false
                bottomSheet.visibility = View.GONE
                bottomSheet.removeView(rvBottom)
                layoutBottom.addView(rvBottom)
                linkageScroll.bottomScrollTarget = { rvBottom }
            }
        } else {
            if (linkageScroll.scrollY == 0) {
                floating = true
                linkageScroll.bottomScrollTarget = null
                layoutBottom.removeView(rvBottom)
                bottomSheet.addView(rvBottom)
                bottomSheet.visibility = View.VISIBLE
            }
        }
    }

}