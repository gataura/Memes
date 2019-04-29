package com.memes.`fun`.presenter

import com.memes.`fun`.adapter.MemesRowView

interface IImgPresenter {
    fun onNextPage()
    fun onBindMemesRowViewAtPosition(position: Int, rowView: MemesRowView)
    fun getMemesCount(): Int
}