package com.kotlin.android_kotlin.domain.savingTips

import com.kotlin.android_kotlin.data.savingTips.SavingTips
import com.kotlin.android_kotlin.data.savingTips.SavingTipsDao

class SavingTipsRepository(private val savingTipsDao: SavingTipsDao) {
    // Retrieve all SavingTips
    suspend fun getAllSavingTips(): List<SavingTips> {
        return savingTipsDao.getAllSavingTips()
    }

    // Retrieve a specific SavingTip by its ID
    suspend fun getSavingTipById(savingTipId: String): SavingTips {
        return savingTipsDao.getSavingTipById(savingTipId)
    }

    // Add a new SavingTip
    suspend fun addSavingTip(savingTip: SavingTips) {
        savingTipsDao.addSavingTip(savingTip)
    }

    // Increment the likes count of a SavingTip
    suspend fun likeSavingTip(savingTipId: String) {
        val savingTip = getSavingTipById(savingTipId)
        val likes = savingTip.likes + 1
        savingTipsDao.updateLikes(savingTipId, likes)
    }

    // Decrement the likes count of a SavingTip
    suspend fun dislikeSavingTip(savingTipId: String) {
        val savingTip = getSavingTipById(savingTipId)
        val likes = savingTip.likes - 1
        savingTipsDao.updateLikes(savingTipId, likes)
    }

    // Delete a SavingTip
    suspend fun deleteSavingTip(savingTipId: String) {
        savingTipsDao.deleteSavingTip(savingTipId)
    }
}