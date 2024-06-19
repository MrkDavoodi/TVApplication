package com.example.tvapplication.ui.video

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvapplication.data.ApiURL.URL_VIDEO
import com.example.tvapplication.data.local.alarm.ScheduleAlarmManager
import com.example.tvapplication.model.version.OnOffTimeSchedule
import com.example.tvapplication.model.version.VsersionModel
import com.example.tvapplication.model.video.VideoItem
import com.example.tvapplication.repository.GetVersionRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getVersionRepo: GetVersionRepo,
    private val scheduleAlarmManager: ScheduleAlarmManager,
) : ViewModel() {
    var versionDetails = mutableStateOf<VsersionModel?>(null)
    val videoList: MutableList<VideoItem> = mutableListOf()
    val videos = MutableStateFlow<List<VideoItem>>(listOf())
    val videos2 = MutableStateFlow<List<VideoItem>>(listOf())

    private val onOffTimeScheduleList: MutableList<OnOffTimeSchedule> = mutableListOf()
    val scheduleList = MutableStateFlow<List<OnOffTimeSchedule>>(listOf())

    private val _videosFromDB : MutableList<VideoItem> = mutableListOf()
    val videosFromDB  = _videosFromDB


    init {
        getVersion()
//        getLocalFiles()

    }
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
                                            mediaUrl = URL_VIDEO + it,
                                            thumbnail = ""
                                        )
                                    )
                                }
                                videos.value = videoList
                                versionDetails.value?.onOffTimeSchedule?.forEach {
                                    val time = it
                                    onOffTimeScheduleList.add(
                                        OnOffTimeSchedule(
                                            day = it.day,
                                            onTime1 = it.onTime1,
                                            offTime1 = it.offTime1
                                        )
                                    )

                                }
                                scheduleList.value = onOffTimeScheduleList
                            }
                        }
                    } else {
                        Log.i("TAG", "ERROR GET VERSION !!!${it.body()} ")
                    }
                }
        }

    }

    fun getLocalFiles(){
        try {
            val path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath.toString()+"/Folder"
//            val path="/storage/emulated/0/Download/Folder"
            val parentDir = File(path)

            if (parentDir.exists() && parentDir.isDirectory) {
                val files = parentDir.listFiles()?.filter { it.isFile }
                files?.forEach { file ->
                    val videoFile=VideoItem(id=file.name.substringBefore(".mp4"), mediaUrl = file.path,"")
                    if (!_videosFromDB.contains(videoFile)){
                        _videosFromDB.add(
                            VideoItem(
                                id = file.name.substringBefore("."),
                                mediaUrl = file.path,
                                thumbnail = ""
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.i("Exception Of File", e.message.toString())

        }
    }


}