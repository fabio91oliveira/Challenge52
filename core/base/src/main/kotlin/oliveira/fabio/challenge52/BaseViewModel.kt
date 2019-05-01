package br.com.brmalls.store.androidapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.brmalls.store.androidapp.vo.ErrorVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main
    private val job by lazy { SupervisorJob() }

    val mutableLiveDataLoading by lazy { MutableLiveData<Boolean>() }
    val mutableLiveDataError by lazy { MutableLiveData<ErrorVO>() }
    val resources by lazy { getApplication<Application>().resources }


    public override fun onCleared() {
        super.onCleared()
        if (job.isActive) job.cancel()
    }
}