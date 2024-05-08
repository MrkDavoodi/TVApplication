package com.example.tvapplication.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvapplication.data.ApiURL.URL_VIDEO
import com.example.tvapplication.forTest.VideoItem
import com.example.tvapplication.model.VideosUIState
import com.example.tvapplication.model.VsersionModel
import com.example.tvapplication.repository.GetVersionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getVersionRepo: GetVersionRepo
) :
    ViewModel() {
    private val _uiVideosState = MutableStateFlow(VideosUIState())
    val uiVideosState: StateFlow<VideosUIState> = _uiVideosState.asStateFlow()
    var versionDetails = mutableStateOf<VsersionModel?>(null)
    private val videoList: MutableList<VideoItem> = mutableListOf()
    val videos = MutableStateFlow<List<VideoItem>>(listOf())

    val videosFromDB = MutableStateFlow<List<VideoItem>>(listOf())

//init {
//    getVersion()
//}

    fun getVersion() {
        viewModelScope.launch {
            getVersionRepo.getVersion()
                .onStart {
                }.catch {
                    Log.i("TAG", "getCandidateDetails: ${it.message} ")
                }.collect { it ->
                    if (it.isSuccessful) {
                        it.body().let { response ->
                            versionDetails.value = response
                            versionDetails.let {
                                versionDetails.value?.VideoList?.forEach {
                                    videoList.add(
                                        VideoItem(
                                            id = it.substringBefore("."),
                                            mediaUrl = URL_VIDEO+it,
                                            thumbnail = ""
                                        )
                                    )
                                }
                                videos.value=videoList
                            }
                        }
                    } else {
                        Log.i("TAG", "ERROR GET VERSION !!!${it.body()} ")
                    }
                }
        }

    }

}