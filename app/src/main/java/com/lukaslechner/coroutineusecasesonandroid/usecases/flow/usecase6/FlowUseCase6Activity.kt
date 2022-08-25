package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.base.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.base.flowUseCase5Description
import com.lukaslechner.coroutineusecasesonandroid.base.flowUseCase6Description
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityFlowUsecase1Binding
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Currency
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.datasources.NetworkExchangeRateDataSource
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase6.datasources.NetworkStockPriceDataSource
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber

class FlowUseCase6Activity : BaseActivity() {

    private val binding by lazy { ActivityFlowUsecase1Binding.inflate(layoutInflater) }
    private val adapter = StockAdapter()
    private lateinit var menu: Menu

    private val viewModel: FlowUseCase6ViewModel by viewModels {
        ViewModelFactory(NetworkStockPriceDataSource(mockApi(applicationContext)), NetworkExchangeRateDataSource(mockApi(applicationContext)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter
        setSupportActionBar(binding.toolbarLayout.toolbar)

        Timber.d("onCreate()")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentStockPriceAsSharedFlow.collect { uiState ->
                    render(uiState)
                }
            }
        }.invokeOnCompletion { throwable ->
            Timber.d("Coroutine completed: $throwable")
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

                val currencyIcon = when (uiState.stockList.first().currency) {
                    Currency.DOLLAR -> R.drawable.ic_euro_24
                    Currency.EURO -> R.drawable.ic_dollar_24
                }

                menu.findItem(R.id.change_currency).setIcon(currencyIcon)

                adapter.stockList = uiState.stockList
                binding.progressBar.setGone()
            }
            is UiState.Error -> {
                toast(uiState.message)
                binding.progressBar.setGone()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.flow_usecase_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_currency -> viewModel.changeCurrency()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        Timber.d("onStop()")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
    }

    override fun getToolbarTitle() = flowUseCase6Description
}