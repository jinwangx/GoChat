package com.jw.gochatbase
/**
 * 由 jinwangx 创建于 2017/9/22.
 */
class RefreshHelper {
    private var mRefreshThreshold: Long = 1L
    private var mFirstEnter = true
    private var mResume = false

    fun setRefreshThreshold(refreshThreshold: Long) {
        if (refreshThreshold >= 0L) {
            mRefreshThreshold = refreshThreshold
        }
    }

    private var mPauseTime: Long = 0L

    fun pause() {
        mResume = false
        if (mRefreshThreshold > 0L) {
            mPauseTime = System.currentTimeMillis()
        }
    }

    fun resume() {
        mResume = true
    }

    fun isResume() = mResume

    fun destroy() {
        mResume = false
        mFirstEnter = true
    }

    fun firstEnter(): Boolean {
        if (mFirstEnter) {
            mFirstEnter = false
            return true
        }
        return false
    }

    fun shouldRefresh(): Boolean {
        if (mRefreshThreshold >= 0L) {
            return System.currentTimeMillis() - mPauseTime > mRefreshThreshold
        }
        return false
    }
}