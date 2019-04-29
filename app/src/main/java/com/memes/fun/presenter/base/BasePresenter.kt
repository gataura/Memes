package com.memes.`fun`.presenter.base

abstract class BasePresenter<T: BaseView> {

    var view: T? = null

    fun bind(view: T) {
        this.view = view
    }

    fun unbind() {
        this.view = null
    }

}