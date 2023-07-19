package com.kotlin.android_kotlin.data.savingTips

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SavingTipsDao {

    private val db = FirebaseFirestore.getInstance()
    private val savingtipsref = db.collection("saving_tips")

    suspend fun addSavingTip(savingTip: SavingTips) {
        savingtipsref.document().set(savingTip).await()
    }


    suspend fun getSavingTipById(savingTipId: String): SavingTips {
        val snapshot = savingtipsref.document(savingTipId).get().await()
        return snapshot.toObject(SavingTips::class.java)!!
    }

    // retrieve all savingtip documents
    suspend fun getAllSavingTips(): List<SavingTips> {
        val snapshot = savingtipsref.get().await()
        return snapshot.toObjects(SavingTips::class.java)
    }

    // update the likes count of a savingtip document
    suspend fun updateLikes(savingTipId: String, likes: Int) {
        val docref = savingtipsref.document(savingTipId)
        docref.update("likes", likes).await()
    }

    // delete a savingtip document
    suspend fun deleteSavingTip(savingTipId: String) {
        savingtipsref.document(savingTipId).delete().await()
    }

}