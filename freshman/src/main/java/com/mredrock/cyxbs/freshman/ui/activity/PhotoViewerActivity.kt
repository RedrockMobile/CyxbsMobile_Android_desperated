package com.mredrock.cyxbs.freshman.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.ui.adapter.BasePagerAdapter
import uk.co.senab.photoview.PhotoView

fun start(context: Context, photoList: List<String>, pos: Int = 0) {
    context.startActivity(
            Intent(context, PhotoViewerActivity::class.java).apply {
                putExtra("photos", photoList.toTypedArray())
                putExtra("position", pos)
            })
}

class PhotoViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        val list = intent.extras.getStringArray("photos")
        val g = Glide.with(this)
        ViewPager(this).apply {
            setContentView(this@apply)
            adapter = object : BasePagerAdapter<FrameLayout, String>(list.toList()) {
                override fun createView(context: Context) =
                        FrameLayout(context).apply {
                            addView(PhotoView(context).apply {
                                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                                id = R.id.photoview_activity
                                visibility = View.INVISIBLE
                            })
                            addView(ProgressBar(context).apply bar@{
                                layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                                    gravity = Gravity.CENTER
                                }
                                id = R.id.progressbar_activity
                            })
                            setOnClickListener { finish() }
                        }

                override fun FrameLayout.initView(mData: String) {
                    g.load(mData).thumbnail(0.1f).into(object : SimpleTarget<GlideDrawable>() {
                        override fun onResourceReady(resource: GlideDrawable?, glideAnimation: GlideAnimation<in GlideDrawable>?) {
                            findViewById<PhotoView>(R.id.photoview_activity).apply {
                                visibility = View.VISIBLE
                                this@initView.findViewById<ProgressBar>(R.id.progressbar_activity).visibility = View.GONE
                                setImageDrawable(resource)
                            }
                        }
                    })
                }
            }
            currentItem = intent.extras.getInt("position")
            setBackgroundColor(Color.parseColor("#000000"))
        }
    }
}
