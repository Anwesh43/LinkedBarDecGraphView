package com.anwesh.uiprojects.linkedbardecgraphview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.bardecgraphview.BarDecGraphView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BarDecGraphView.create(this)
    }
}
