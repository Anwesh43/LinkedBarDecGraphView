package com.anwesh.uiprojects.bardecgraphview

/**
 * Created by anweshmishra on 20/08/18.
 */

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.view.MotionEvent
import android.content.Context

val nodes : Int = 5

fun Canvas.drawBarDecNode(i : Int, scale : Float, useI : Boolean, cb : () -> Unit, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    var x : Float = 0f
    val sc1 : Float = Math.min(0.5f, scale) * 2
    val sc2 : Float = Math.min(0.5f, Math.max(0f, scale - 0.5f)) * 2
    if (useI) {
        x = gap * i + gap / 2 + gap * sc2
    }
    val size : Float = gap / 2
    val wSize : Float = size / nodes
    val hSize : Float = (h / 3) / nodes
    val kx = -(size)/2 + (wSize) * i
    val hBar : Float = (i + 1) * hSize
    save()
    translate(x, h / 2)
    save()
    translate(kx, -hBar * (1 - sc1))
    drawRect(RectF(0f, 0f, wSize, hBar * (1 - sc1)), paint)
    restore()
    restore()
}

class BarDecGraphView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            this.scale += 0.1f * this.dir
            if (Math.abs(this.scale - this.prevScale) > 1) {
                this.scale = this.prevScale + this.dir
                this.dir = 0f
                this.prevScale = this.scale
                cb(this.prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (this.dir == 0f) {
                this.dir = 1 - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun start(cb : () -> Unit) {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }
    }

    data class BarDecGraphNode(var i : Int, val state : State = State()) {

        private var next : BarDecGraphNode? = null
        private var prev : BarDecGraphNode? = null

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = BarDecGraphNode(i)
                next?.prev = this
            }
        }

        init {
            addNeighbor()
        }

        fun draw(canvas : Canvas, paint : Paint, useI : Boolean) {
            canvas.drawBarDecNode(i, state.scale, useI, {
                next?.draw(canvas, paint, false)
            }, paint)
        }

        fun getNext(dir : Int, cb : () -> Unit) : BarDecGraphNode {
            var curr : BarDecGraphNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }
}
