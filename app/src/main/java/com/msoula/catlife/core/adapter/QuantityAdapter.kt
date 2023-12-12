package com.msoula.catlife.core.adapter

import app.cash.sqldelight.ColumnAdapter

class QuantityAdapter : ColumnAdapter<Int, Long> {

    override fun decode(databaseValue: Long) = databaseValue.toInt()

    override fun encode(value: Int) = value.toLong()
}