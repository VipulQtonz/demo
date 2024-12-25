package com.example.programingdemo.utlis

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class CenterZoomLayoutManager(
    context: Context, orientation: Int, reverseLayout: Boolean
) : LinearLayoutManager(context, orientation, reverseLayout) {

    var onCenterItemChanged: ((position: Int) -> Unit)? = null

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        adjustOpacity()  // Adjust opacity after laying out children
        findCenterItemAndNotify()  // Find center item and notify the adapter
    }

    override fun scrollVerticallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scrolled = super.scrollVerticallyBy(dx, recycler, state)
        adjustOpacity()  // Adjust opacity during scrolling
        findCenterItemAndNotify()  // Find center item and notify adapter when scrolling
        return scrolled
    }

    private fun adjustOpacity() {
        val midpoint = height / 2f

        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue

            // Reset each view's default properties
            child.scaleX = 1.0f
            child.scaleY = 1.0f
            child.alpha = 1.0f  // Reset to full opacity before applying new changes

            val childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2f
            val distanceFromCenter = abs(midpoint - childMidpoint)

            // Adjust opacity based on how close the item is to the center
            val alpha = if (distanceFromCenter < 200f) {
                1f  // Fully opaque for items close to the center
            } else {
                0.5f  // Reduced opacity for items further away
            }
            child.alpha = alpha
        }
    }

    private fun findCenterItemAndNotify() {
        val midpoint = height / 2f
        var closestChild: View? = null
        var closestChildPosition = -1
        var minDistance = Float.MAX_VALUE

        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            val childMidpoint = (getDecoratedBottom(child) + getDecoratedTop(child)) / 2f
            val distance = abs(midpoint - childMidpoint)
            if (distance < minDistance) {
                minDistance = distance
                closestChild = child
                closestChildPosition = getPosition(child)
            }
        }

        // Notify if the center item has changed
        closestChild?.let {
            onCenterItemChanged?.invoke(closestChildPosition)
        }
    }
}
