package com.msoula.catlife.core.adapter

import app.cash.sqldelight.ColumnAdapter

class WeightAdapter: ColumnAdapter<Float, Double> {

    override fun decode(databaseValue: Double) = databaseValue.toFloat()

    override fun encode(value: Float) = value.toDouble()
}