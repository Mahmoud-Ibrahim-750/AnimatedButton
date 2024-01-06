package com.mis.animatedbutton

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.widget.ProgressBar

object Utils {

//    fun Resources.getColor(colorId: Int, context: Context): Int {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            this.getColor(colorId, context.theme)
//        } else {
//            @Suppress("DEPRECATION")
//            this.getColor(colorId)
//        }
//    }

    // todo: what does src in mean?
    fun ProgressBar.setColorFilter(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val filter = BlendModeColorFilter(color, BlendMode.SRC_IN)
            this.indeterminateDrawable.colorFilter = filter

        } else {
            @Suppress("DEPRECATION")
            this.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
    }


    fun TypedArray.getRawDimension(attrIndex: Int, defValue: Float): Float {
        return (this.getDimension(attrIndex, defValue)) / resources.displayMetrics.scaledDensity
    }

    // TODO: Until escaping the pipe symbol is done, this will remain commented
//    fun TypedArray.getResourceOrColor(attrIndex: Int, defValue: Float): Float {
//        return (this.getDimension(attrIndex, defValue)) / resources.displayMetrics.scaledDensity
//    }
}