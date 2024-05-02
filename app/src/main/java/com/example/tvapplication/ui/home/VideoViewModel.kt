package com.example.tvapplication.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvapplication.commons.Resource
import com.example.tvapplication.commons.Error
import com.example.tvapplication.model.VideosUIState
import com.example.tvapplication.model.VsersionModel
import com.example.tvapplication.repository.GetVersionRepo
import com.example.tvapplication.use_cases.GetVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.security.auth.login.LoginException

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val getVersionRepo: GetVersionRepo
) :
    ViewModel() {
    private val _uiVideosState = MutableStateFlow(VideosUIState())
    val uiVideosState: StateFlow<VideosUIState> = _uiVideosState.asStateFlow()
    var versionDetails = mutableStateOf<VsersionModel?>(null)

    fun getVersion() {

        viewModelScope.launch {
            getVersionRepo.getVersion()
                .onStart {
                }.catch {
                    Log.i("TAG", "getCandidateDetails: ${it.message} ")
                }.collect {
                    if (it.isSuccessful) {
                        it.body().let { response ->
                            versionDetails.value = response
                        }
                    } else {
                        Log.i("TAG", "ERROR GET VERSION !!!${it.body()} ")
                    }
                }
        }

    }
}