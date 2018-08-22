package com.mredrock.cyxbs.freshman.mvp.contract

import com.mredrock.cyxbs.freshman.mvp.presenter.IBasePresenter

class MainContract {

    interface IMainView : BaseContract.ISomethingView {
        /**
         * @param pos 解锁到的位置
         * 如 解锁到2.校园攻略
         * pos=2
         */
        fun initBuilding(pos: Int)

        fun unlockBuilding(pos: Int)

        fun initCars(pos: Int)

        /**
         * @param pos 目标车位
         * 如 1.入学必备 -> 2.校园攻略
         * pos=2
         */
        fun driveAnimator(pos: Int)
    }

    interface IMainModel {
        fun getNowStop(): Int
        fun setNowStop(nowStop: Int)
    }

    interface IMainPresenter : IBasePresenter<IMainView> {
        fun onBuildingClick(id: Int)
        /**
         * 这里要换车
         */
        fun onResume()

        /**
         * 回调炼狱
         */
        fun onDriveEnd(pos: Int)
    }
}