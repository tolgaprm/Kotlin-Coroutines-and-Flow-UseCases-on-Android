package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase5

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lukaslechner.coroutineusecasesonandroid.CoroutineUsecasesOnAndroidApplication
import com.lukaslechner.coroutineusecasesonandroid.base.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.base.flowUseCase5Description
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityFlowUsecase1Binding
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase5.database.StockDatabase
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber

class FlowUseCase5Activity : BaseActivity() {

    private val binding by lazy { ActivityFlowUsecase1Binding.inflate(layoutInflater) }
    private val adapter = StockAdapter()

    private val stockPriceRepository by lazy {
        StockPriceRepository(
            remoteDataSource = NetworkStockPriceDataSource(mockApi(applicationContext)),
            localDataSource = StockDatabase.getInstance(applicationContext).stockDao(),
            appScope = (application as CoroutineUsecasesOnAndroidApplication).applicationScope
        )
    }

    private val teslaStockPriceLogger by lazy {
        TeslaStockPriceLogger(
            stockPriceRepository,
            (application as CoroutineUsecasesOnAndroidApplication).applicationScope
        )
    }

    private val viewModel: FlowUseCase5ViewModel by viewModels {
        ViewModelFactory(stockPriceRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter

        Timber.d("onCreate()")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .latestStockList
                    .collect { uiState ->
                        render(uiState)
                    }
            }
        }
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

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
        teslaStockPriceLogger.startLogging()
    }

    override fun onStop() {
        Timber.d("onStop()")
        teslaStockPriceLogger.stopLogging()
        super.onStop()
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
    }

    override fun getToolbarTitle() = flowUseCase5Description
}