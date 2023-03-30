package com.example.gestionnairesante.divers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.View

class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
    private val shadow = ColorDrawable(Color.BLUE)
    private val wth = 180
    private val hgt = 180


    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = wth
        val height: Int = hgt
        /*val width: Int = view.width/2
        val height: Int = view.height/2*/
        shadow.setBounds(0, 0, width, height)
        size.set(wth, hgt)
        touch.set(wth, hgt)
        /*size.set(width, height)
        touch.set(width/2, height/2)*/
    }

    override fun onDrawShadow(canvas: Canvas) {
        shadow.draw(canvas)
    }
}