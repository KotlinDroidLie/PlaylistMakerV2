package com.practicum.playlistmaker.features.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
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
import com.practicum.playlistmaker.core.TrackModel
import com.practicum.playlistmaker.core.di.Creator
import com.practicum.playlistmaker.domainTerminated.api.TrackRepositoryResult
import com.practicum.playlistmaker.domainTerminated.api.usecase.LoadSearchHistoryUseCase
import com.practicum.playlistmaker.domainTerminated.api.usecase.SaveSearchHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.IAddTrackToHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.IClearSearchHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.IGetSearchHistoryUseCase
import com.practicum.playlistmaker.features.search.domain.api.usecase.ISearchTracksUseCase
import com.practicum.playlistmaker.features.search.ui.OnItemClickListener
import com.practicum.playlistmaker.features.search.ui.SearchHistoryAdapter
import com.practicum.playlistmaker.features.search.ui.StatusSearchMessage
import com.practicum.playlistmaker.features.search.ui.TrackAdapter
import com.practicum.playlistmaker.features.player.ui.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var addTrackToHistoryUseCase: IAddTrackToHistoryUseCase
    private lateinit var clearSearchHistoryUseCase: IClearSearchHistoryUseCase
    private lateinit var getSearchHistoryUseCase: IGetSearchHistoryUseCase
    private lateinit var ISearchTracksUseCase: ISearchTracksUseCase
    private lateinit var loadSearchHistoryUseCase: LoadSearchHistoryUseCase
    private lateinit var saveSearchHistoryUseCase: SaveSearchHistoryUseCase
    private var isClickAllowed = true
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    lateinit var adapter: TrackAdapter
    private lateinit var inputEditText: EditText
    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var historyAdapter: SearchHistoryAdapter
    private lateinit var viewMessageNotFound: MaterialTextView
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var viewMessageError: LinearLayout
    private lateinit var viewHistorySearch: LinearLayout
    private val trackList = mutableListOf<TrackModel>()
    private var saveText: String = TEXT_DEF
    private var lastText: String = TEXT_DEF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linear_layout_search_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadSearchHistoryUseCase = Creator.getLoadSearchHistoryUseCase(this)
        saveSearchHistoryUseCase = Creator.getSaveSearchHistoryUseCase(this)
        ISearchTracksUseCase = Creator.getSearchTracksUseCase()
        addTrackToHistoryUseCase = Creator.getAddTrackToHistoryUseCase(this)
        clearSearchHistoryUseCase = Creator.getClearSearchHistoryUseCase(this)
        getSearchHistoryUseCase = Creator.getSearchHistoryUseCase(this)

        loadSearchHistoryUseCase.execute()

        val onItemClickListener = object : OnItemClickListener {
            override fun addToSearchHistory(track: TrackModel) {
                addTrackToHistoryUseCase.execute(track)
                historyAdapter.trackHistory = getSearchHistoryUseCase.execute()
                historyAdapter.notifyDataSetChanged()
            }

            override fun openAudioPlayer(track: TrackModel) {
                if (clickDebounce()){
                    val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java).apply {
                        putExtra(AudioPlayerActivity.Companion.KEY_TRACK, track)
                    }
                    startActivity(intent)
                }
            }
        }

        searchProgressBar = findViewById(R.id.pb_search)
        val buttonBack = findViewById<MaterialToolbar>(R.id.btn_search_back)
        inputEditText = findViewById(R.id.et_search)
        val buttonClear = findViewById<ImageView>(R.id.iv_clear_text)
        val buttonRefresh = findViewById<Button>(R.id.btn_refresh)
        val buttonClearHistory = findViewById<Button>(R.id.btn_clear_history)
        viewMessageNotFound = findViewById(R.id.view_nothing_found)
        viewMessageError = findViewById(R.id.view_connection_problems)
        viewHistorySearch = findViewById(R.id.view_search_history)

        historyRecyclerView = findViewById(R.id.rv_history_songs_list)
        historyRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        historyAdapter = SearchHistoryAdapter(onItemClickListener)
        historyAdapter.trackHistory =  getSearchHistoryUseCase.execute()
        historyRecyclerView.adapter  = historyAdapter

        recyclerView = findViewById(R.id.rv_songs_list)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = TrackAdapter(trackList, onItemClickListener)
        recyclerView.adapter  = adapter

        inputEditText.setText(saveText)

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        buttonClearHistory.setOnClickListener {
            clearSearchHistoryUseCase.execute()
            historyAdapter.trackHistory =  getSearchHistoryUseCase.execute()
            historyAdapter.notifyDataSetChanged()
            showStausMessageSearch(StatusSearchMessage.HIDDEN)
        }

        buttonClear.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            hideKeyboard(inputEditText)
            showStausMessageSearch(StatusSearchMessage.HIDDEN)
        }

        buttonRefresh.setOnClickListener {
                executeSearch(lastText)
            }

        inputEditText.addTextChangedListener(
            onTextChanged = { s: CharSequence?, start: Int, before: Int, count: Int ->
                buttonClear.isVisible = if (s.isNullOrEmpty()) false else true
                if (inputEditText.hasFocus() && s.isNullOrEmpty() && getSearchHistoryUseCase.execute().isNotEmpty()) showStausMessageSearch(
                    StatusSearchMessage.SEARCH_HISTORY)
                searchDebounce()
                },
            afterTextChanged = { s: Editable? ->
                saveText = s.toString()
            }
            )

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && getSearchHistoryUseCase.execute().isNotEmpty()) showStausMessageSearch(
                StatusSearchMessage.SEARCH_HISTORY)
        }
    }

    override fun onStop() {
        super.onStop()
        saveSearchHistoryUseCase.execute()
    }
    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(searchRunnable)
        mainHandler.removeCallbacks(clickAllowedRunnable)
    }

    private fun executeSearch(text: String = saveText) {
        showStausMessageSearch(StatusSearchMessage.SEARCH_LOADING)
        ISearchTracksUseCase.searchTracks(text, object : ISearchTracksUseCase.TracksConsumer {
            override fun consume(result: TrackRepositoryResult) {
                    trackList.clear()
                    when (result) {
                        is TrackRepositoryResult.Success -> {
                            trackList.addAll(result.tracks)
                            adapter.notifyDataSetChanged()
                            showStausMessageSearch(StatusSearchMessage.DEFAULT)
                        }
                        is TrackRepositoryResult.NotFound -> {
                            showStausMessageSearch(StatusSearchMessage.NOT_FOUND)
                        }
                        is TrackRepositoryResult.NetworkError -> {
                            lastText = saveText
                            showStausMessageSearch(StatusSearchMessage.ERROR)
                        }
                    }
            }
        })
    }

    private fun showStausMessageSearch(status: StatusSearchMessage){
        when (status) {
            StatusSearchMessage.DEFAULT ->{
                recyclerView.isVisible = true
                viewMessageNotFound.isVisible = false
                viewMessageError.isVisible = false
                viewHistorySearch.isVisible = false
                searchProgressBar.isVisible = false
            }
            StatusSearchMessage.NOT_FOUND ->{
                recyclerView.isVisible = false
                viewMessageError.isVisible = false
                viewHistorySearch.isVisible = false
                viewMessageNotFound.isVisible = true
                searchProgressBar.isVisible = false
            }
            StatusSearchMessage.ERROR ->{
                recyclerView.isVisible = false
                viewHistorySearch.isVisible = false
                viewMessageNotFound.isVisible = false
                viewMessageError.isVisible = true
                searchProgressBar.isVisible = false
            }
            StatusSearchMessage.HIDDEN -> {
                recyclerView.isVisible = false
                viewHistorySearch.isVisible = false
                searchProgressBar.isVisible = false
            }
            StatusSearchMessage.SEARCH_HISTORY ->{
                recyclerView.isVisible = false
                viewMessageNotFound.isVisible = false
                viewMessageError.isVisible = false
                viewHistorySearch.isVisible = true
                searchProgressBar.isVisible = false
            }
            StatusSearchMessage.SEARCH_LOADING ->{
                recyclerView.isVisible = false
                viewMessageNotFound.isVisible = false
                viewMessageError.isVisible = false
                viewHistorySearch.isVisible = false
                searchProgressBar.isVisible = true
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, saveText)
        outState.putString(LAST_TEXT, lastText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveText = savedInstanceState.getString(EDIT_TEXT, TEXT_DEF)
        lastText = savedInstanceState.getString(LAST_TEXT, TEXT_DEF)
    }

    private fun hideKeyboard(view: View){
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private val searchRunnable = Runnable{
        if(inputEditText.text.isNotBlank()) executeSearch()
    }

    private fun searchDebounce(){
        mainHandler.removeCallbacks(searchRunnable)
        mainHandler.postDelayed(searchRunnable, SEARCH_DELAY)
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
        private const val SEARCH_DELAY = 2000L
        private const val TEXT_DEF = ""
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val LAST_TEXT = "LAST_TEXT"
    }
}