package com.mredrock.cyxbs.freshman.mvp.presenter

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.mvp.contract.MainContract.*
import com.mredrock.cyxbs.freshman.mvp.model.MainModel
import com.mredrock.cyxbs.freshman.ui.activity.*
import com.mredrock.cyxbs.freshman.ui.activity.campus.CampusStrategyEntranceActivity
import com.mredrock.cyxbs.freshman.utils.kt.BasePresenter

class MainPresenter : BasePresenter<IMainView, IMainModel>(), IMainPresenter {

    override fun attachView(mvpView: IMainView) {
        super.attachView(mvpView)
        mvpView.initBuilding(model.getNowStop())
    }

    override fun onResume() {
        val now = model.getNowStop()
        mvpView?.initCars(if (now == 0) 1 else now)
        if (isAnimationPlaying && now != 1) {
            mvpView?.unlockBuilding(now)
            isAnimationPlaying = false
        }
    }

    private var isAnimationPlaying = false
    private inline fun Boolean.then(then: () -> Unit) {
        if (this) then()
    }

    override fun onBuildingClick(id: Int) = (!isAnimationPlaying).then {
        when (id) {
            R.id.freshman_building_request1 -> 1
            R.id.freshman_building_strategy2 -> 2
            R.id.freshman_building_chat3 -> 3
            R.id.freshman_building_process4 -> 4
            R.id.freshman_building_words5 -> 5
            R.id.freshman_building_mien -> 6
            R.id.freshman_building_military -> 7
            else -> throw Exception("it's not building")
        }.let {
            val nextStop = model.getNowStop() + 1
            if (it < nextStop || it > 5) {
                startActivity(it)
            } else if (it == nextStop) {
                isAnimationPlaying = true
                mvpView?.driveAnimator(it)
            }
            Unit
        }
    }

    override fun onDriveEnd(pos: Int) {
        model.setNowStop(pos)
        startActivity(pos)
    }

    /**
     * 点击建筑物启动的Activity，改成对应的class
     */
    private fun startActivity(pos: Int) = mvpView?.context?.let {
        it.startActivity(Intent(it, when (pos) {
            1 -> AdmissionRequestActivity::class
            2 -> CampusStrategyEntranceActivity::class
            3 -> ChatOnlineActivity::class
            4 -> ReportingProcessActivity::class
            5 -> WordsActivity::class
            6 -> CquptMienActivity::class
            7 -> MilitaryTrainingActivity::class
            else -> throw Exception("no activity found")
        }.java).apply { flags = FLAG_ACTIVITY_SINGLE_TOP })
    }

    override fun createModel(): IMainModel = MainModel()

}