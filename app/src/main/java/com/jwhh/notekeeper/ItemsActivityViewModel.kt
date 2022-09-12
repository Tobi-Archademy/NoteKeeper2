package com.jwhh.notekeeper

import android.os.Bundle
import androidx.lifecycle.ViewModel

class ItemsActivityViewModel : ViewModel() {

    var isNewlyCreated = true

    var navDrawerDisplaySelectionName =
        "com.jwhh.notekeeper.ItemsActivityViewModel.navDrawerDisplaySelection"

    var recentlyViewedNoteIdsName =  "com.jwhh.notekeeper.ItemsActivityViewModel.recentlyViewedNotes"

    var navDrawerDisplaySelection = R.id.nav_notes

    private val maxRecentlyViewedNotes = 5
    val recentlyViewedNotes = ArrayList<NoteInfo>(maxRecentlyViewedNotes)

    fun addToRecentlyViewedNotes(note: NoteInfo) {
        // Check if selection is already in the list
        val existingIndex = recentlyViewedNotes.indexOf(note)
        if (existingIndex == -1) {
            // it isn't in the list...
            // Add new one to beginning of list and remove any beyond max we want to keep
            recentlyViewedNotes.add(0, note)
            for (index in recentlyViewedNotes.lastIndex downTo maxRecentlyViewedNotes)
                recentlyViewedNotes.removeAt(index)
        } else {
            // it is in the list...
            // Shift the ones above down the list and make it first member of the list
            for (index in (existingIndex - 1) downTo 0)
                recentlyViewedNotes[index + 1] = recentlyViewedNotes[index]
            recentlyViewedNotes[0] = note
        }
    }

    fun saveState(outState: Bundle?) {
        outState?.putInt(navDrawerDisplaySelectionName, navDrawerDisplaySelection)

        val notesId = DataManager.noteIdsAsIntArray(recentlyViewedNotes)
        outState?.putIntArray("recentlyViewedNoteIdsName", notesId)
    }

    fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            navDrawerDisplaySelection = savedInstanceState.getInt(navDrawerDisplaySelectionName)
        }
        val noteIds = savedInstanceState?.getIntArray(recentlyViewedNoteIdsName)
        val noteList = noteIds?.let { DataManager.loadNotes(*it) }
        if (noteList != null) {
            recentlyViewedNotes.addAll(noteList)
        }
    }

}