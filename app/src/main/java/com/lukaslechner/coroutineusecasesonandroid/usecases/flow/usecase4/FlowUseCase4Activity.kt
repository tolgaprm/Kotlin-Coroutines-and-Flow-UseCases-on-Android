package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import android.os.Bundle
import androidx.activity.viewModels
import com.lukaslechner.coroutineusecasesonandroid.CoroutineUsecasesOnAndroidApplication
import com.lukaslechner.coroutineusecasesonandroid.base.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.base.flowUseCase4Description
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityFlowUsecase1Binding
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4.database.StockDatabase
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber

class FlowUseCase4Activity : BaseActivity() {

    private val binding by lazy { ActivityFlowUsecase1Binding.inflate(layoutInflater) }
    private val adapter = StockAdapter()

    private val viewModel: FlowUseCase4ViewModel by viewModels {
        ViewModelFactory(
            StockPriceRepository(
                remoteDataSource = NetworkStockPriceDataSource(mockApi(applicationContext)),
                localDataSource = StockDatabase.getInstance(applicationContext).stockDao(),
                appScope = (application as CoroutineUsecasesOnAndroidApplication).applicationScope
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter

        Timber.d("onCreate()")
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                binding.progressBar.setVisible()
                binding.recyclerView.setGone()
            }
            is UiState.Success -> {
                binding.recyclerView.setVisible()
                binding.lastUpdateTime.text =
                    "lastUpdateTime: ${LocalDateTime.now().toString(DateTimeFormat.fullTime())}"
                adapter.stockList = uiState.stockList
                binding.progressBar.setGone()
            }
            is UiState.Error -> {
                toast(uiState.message)
                binding.progressBar.setGone()
            }
        }
    }

    override fun onStop() {
        Timber.d("onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
    }

    override fun getToolbarTitle() = flowUseCase4Description
}