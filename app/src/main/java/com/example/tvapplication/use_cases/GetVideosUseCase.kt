package com.example.tvapplication.use_cases

import android.content.Context
import com.example.tvapplication.commons.Resource
import com.example.tvapplication.domain.VideoEntity
import com.example.tvapplication.domain.VideoResultEntity
import com.example.tvapplication.use_cases.VideoConstants.ERROR_CODE_SERVICE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(private val context: Context) {
    operator fun invoke(): Flow<Resource<List<VideoEntity>>> = flow {
        emit(Resource.Loading())
        emit(Resource.Success(getVideosFakeData))
    }.catch {
        emit(Resource.Error(ERROR_CODE_SERVICE))
    }
}

object VideoConstants {
    const val ERROR_CODE_SERVICE = "-9"
}

val getVideosFakeData: List<VideoEntity>
    get() = listOf(
        VideoEntity(
            name = "1- Bunny Video",
            description = "This Video is used for Testing, the content is not mine and is free to use",
            extraInfo = "#extra @test, #extra @test, #extra @test, #extra @test, #extra @test, #extra @test",
            videoResultEntity = VideoResultEntity(
                1,
                "https://mrk.co.ir/MRKSignage/Video/rasa3.mp4",
                "Bunny",
                "https://mrk.co.ir/MRKSignage/Video/rasa3.mp4"
            )
        ),
        VideoEntity(
            name = "3",
            description = "Video for Test 3",
            extraInfo = "#extra @test3",
            videoResultEntity = VideoResultEntity(
                3,
                "https://mrk.co.ir/MRKSignage/Video/1.mp4",
                "Bunny 3",
                "https://mrk.co.ir/MRKSignage/Video/1.mp4"
            )
        ),
        VideoEntity(
            name = "4",
            description = "Video for Test 4",
            extraInfo = "#extra @test4",
            videoResultEntity = VideoResultEntity(
                3,
                "https://mrk.co.ir/MRKSignage/Video/3.mp4",
                "Bunny 4",
                "https://mrk.co.ir/MRKSignage/Video/3.mp4"
            )
        )
    )