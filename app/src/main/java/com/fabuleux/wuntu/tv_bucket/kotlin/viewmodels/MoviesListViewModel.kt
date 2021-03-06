package com.fabuleux.wuntu.tv_bucket.kotlin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.fabuleux.wuntu.tv_bucket.kotlin.models.CommonResponse
import com.fabuleux.wuntu.tv_bucket.kotlin.models.MoviePojo
import com.fabuleux.wuntu.tv_bucket.kotlin.repository.RepositoryImpl
import com.fabuleux.wuntu.tv_bucket.kotlin.retrofitHelper.Resource

class MoviesListViewModel : ViewModel() {

    private var repository:RepositoryImpl = RepositoryImpl()
    private val callObserver:
            Observer<Resource<CommonResponse<MoviePojo>>>
            = Observer { t -> processResponse(t) }
    private val moviesResponse:MutableLiveData<List<MoviePojo>> = MutableLiveData()

    fun fetchMovies() {
        repository.getPopularMovies(1).observeForever { callObserver.onChanged(it) }
    }

    private fun processResponse(response:
                                Resource<CommonResponse<MoviePojo>>?){
        when(response?.status){
            Resource.Status.LOADING -> {
                Log.d("STATUS","LOADING")
            }
            Resource.Status.SUCCESS -> {
                Log.d("STATUS","SUCCESS")
                moviesResponse.value = response.data?.result
            }
            Resource.Status.ERROR -> {
                Log.d("STATUS","ERROR")
            }
        }
    }

    fun getMovies() : LiveData<List<MoviePojo>>
    {
        return moviesResponse
    }
}
