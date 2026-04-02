package com.practicum.playlistmaker.features.search.ui.activtiy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.player.ui.activity.AudioPlayerActivity
import com.practicum.playlistmaker.features.search.ui.view_model.SearchState
import com.practicum.playlistmaker.features.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var textConnectionProblem: MaterialTextView
    private var isClickAllowed = true
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    lateinit var adapter: SearchTrackAdapter
    private lateinit var inputEditText: EditText
    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var buttonBack: MaterialToolbar
    private lateinit var buttonClear: ImageView
    private lateinit var buttonRefresh: Button
    private lateinit var buttonClearHistory: Button
    private lateinit var historyAdapter: SearchHistoryAdapter
    private lateinit var viewMessageNotFound: MaterialTextView
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var viewMessageError: LinearLayout
    private lateinit var viewHistorySearch: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linear_layout_search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val onItemClickListener = object : OnItemClickListener {
            override fun addToSearchHistory(track: TrackModel) {
                viewModel.saveToHistory(track)
            }

            override fun openAudioPlayer(track: TrackModel) {
                if (clickDebounce()){
                    val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java).apply {
                        putExtra(AudioPlayerActivity.KEY_TRACK, track)
                    }
                    startActivity(intent)
                }
            }
        }
        textConnectionProblem = findViewById(R.id.tv_connection_problems)
        searchProgressBar = findViewById(R.id.pb_search)
        buttonBack = findViewById<MaterialToolbar>(R.id.btn_search_back)
        inputEditText = findViewById(R.id.et_search)
        buttonClear = findViewById<ImageView>(R.id.iv_clear_text)
        buttonRefresh = findViewById<Button>(R.id.btn_refresh)
        buttonClearHistory = findViewById<Button>(R.id.btn_clear_history)
        viewMessageNotFound = findViewById(R.id.view_nothing_found)
        viewMessageError = findViewById(R.id.view_connection_problems)
        viewHistorySearch = findViewById(R.id.view_search_history)

        historyRecyclerView = findViewById(R.id.rv_history_songs_list)
        historyRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        historyAdapter = SearchHistoryAdapter(onItemClickListener)
        historyRecyclerView.adapter  = historyAdapter

        recyclerView = findViewById(R.id.rv_songs_list)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = SearchTrackAdapter(onItemClickListener)
        recyclerView.adapter  = adapter

        viewModel.state.observe(this){
            render(it)
        }

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
            showDefault()
        }

        buttonClear.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            hideKeyboard(inputEditText)
            showDefault()
        }

        buttonRefresh.setOnClickListener {
            viewModel.retryErrorSearch()
        }

        inputEditText.addTextChangedListener(
            onTextChanged = { s: CharSequence?, start: Int, before: Int, count: Int ->
                buttonClear.isVisible = if (s.isNullOrEmpty()) false else true
                if (inputEditText.hasFocus() && s.isNullOrEmpty() && historyAdapter.trackHistory.isNotEmpty()){
                    showHistory()
                }
                viewModel.searchDebounce(s?.toString()?.trim() ?: "")
            }
        )

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && historyAdapter.trackHistory.isNotEmpty()){
                showHistory()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(clickAllowedRunnable)
    }

    private fun hideKeyboard(view: View){
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun render(state: SearchState){
        when(state){
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty(state.emptyMessage)
            is SearchState.Error -> showError(state.errorMessage)
            SearchState.Loading -> showLoading()
            is SearchState.History -> updateHistoryList(state.history)
        }
    }

    private fun showDefault(){
        recyclerView.isVisible = false
        viewHistorySearch.isVisible = false
        searchProgressBar.isVisible = false
    }

    private fun showHistory(){
        recyclerView.isVisible = false
        viewMessageNotFound.isVisible = false
        viewMessageError.isVisible = false
        viewHistorySearch.isVisible = true
        searchProgressBar.isVisible = false
    }
    private fun showContent(newSearchList: List<TrackModel>){
        recyclerView.isVisible = true
        viewMessageNotFound.isVisible = false
        viewMessageError.isVisible = false
        viewHistorySearch.isVisible = false
        searchProgressBar.isVisible = false

        adapter.trackList.clear()
        adapter.trackList.addAll(newSearchList)
        adapter.notifyDataSetChanged()
    }
    private fun showEmpty(emptyMessage: Int){
        recyclerView.isVisible = false
        viewMessageError.isVisible = false
        viewHistorySearch.isVisible = false
        viewMessageNotFound.isVisible = true
        searchProgressBar.isVisible = false
        viewMessageNotFound.text = getString(emptyMessage)
    }
    private fun showError(errorMessage: Int){
        recyclerView.isVisible = false
        viewHistorySearch.isVisible = false
        viewMessageNotFound.isVisible = false
        viewMessageError.isVisible = true
        searchProgressBar.isVisible = false
        textConnectionProblem.text = getString(errorMessage)
    }
    private fun showLoading(){
        recyclerView.isVisible = false
        viewMessageNotFound.isVisible = false
        viewMessageError.isVisible = false
        viewHistorySearch.isVisible = false
        searchProgressBar.isVisible = true
    }
    private fun updateHistoryList(newHistoryList: List<TrackModel>){
        historyAdapter.trackHistory.clear()
        historyAdapter.trackHistory.addAll(newHistoryList)
        historyAdapter.notifyDataSetChanged()
    }

    private val clickAllowedRunnable = Runnable {
        isClickAllowed = true
    }
    private fun clickDebounce(): Boolean{
        val current = isClickAllowed
        if (current){
            isClickAllowed = false
            mainHandler.postDelayed(clickAllowedRunnable, CLICK_DELAY)
        }
        return current
    }

    companion object {
        private const val CLICK_DELAY = 1000L
    }
}