package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class SearchActivity : AppCompatActivity() {
    private lateinit var historySharedPreferences: SharedPreferences
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    lateinit var searchHistory: SearchHistory
    lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter
    private lateinit var viewMessageNotFound: MaterialTextView
    private lateinit var viewMessageError: LinearLayout
    private lateinit var viewHistorySearch: LinearLayout
    private val trackList = ArrayList<TrackModel>()
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesApi = retrofit.create(ITunesApi::class.java)
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
        historySharedPreferences = getSharedPreferences(HISTORY_PREFERENCES, MODE_PRIVATE)

        searchHistory = SearchHistory(historySharedPreferences)
        searchHistory.loadFromPreference()

        val onItemClickListener = object : OnItemClickListener {
            override fun addToSearchHistory(track: TrackModel) {
                searchHistory.write(track)
                historyAdapter.trackHistory = searchHistory.read()
                historyAdapter.notifyDataSetChanged()
            }
        }

        val buttonBack = findViewById<MaterialToolbar>(R.id.btn_search_back)
        val inputEditText = findViewById<EditText>(R.id.et_search)
        val buttonClear = findViewById<ImageView>(R.id.iv_clear_text)
        val buttonRefresh = findViewById<Button>(R.id.btn_refresh)
        val buttonClearHistory = findViewById<Button>(R.id.btn_clear_history)
        viewMessageNotFound = findViewById(R.id.view_nothing_found)
        viewMessageError = findViewById(R.id.view_connection_problems)
        viewHistorySearch = findViewById(R.id.view_search_history)

        historyRecyclerView = findViewById(R.id.rv_history_songs_list)
        historyRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        historyAdapter = SearchHistoryAdapter()
        historyAdapter.trackHistory = searchHistory.read()
        historyRecyclerView.adapter  = historyAdapter

        recyclerView = findViewById(R.id.rv_songs_list)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        adapter = TrackAdapter(trackList, onItemClickListener)
        recyclerView.adapter  = adapter

        inputEditText.setText(saveText)

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        buttonClearHistory.setOnClickListener {
            searchHistory.clear()
            historyAdapter.trackHistory = searchHistory.read()
            historyAdapter.notifyDataSetChanged()
            showStausMessageSearch(StatusSearchMessage.HIDDEN)
            inputEditText.clearFocus()
            hideKeyboard(inputEditText)
        }

        buttonClear.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            hideKeyboard(inputEditText)
            showStausMessageSearch(StatusSearchMessage.HIDDEN)
        }

        buttonRefresh.setOnClickListener {
                searchTrack(lastText)
            }

        inputEditText.addTextChangedListener(
            onTextChanged = { s: CharSequence?, start: Int, before: Int, count: Int ->
                buttonClear.isVisible = if (s.isNullOrEmpty()) false else true
                if (inputEditText.hasFocus() && s.isNullOrEmpty() && searchHistory.read().isNotEmpty()) showStausMessageSearch(StatusSearchMessage.SEARCH_HISTORY)
                else showStausMessageSearch(StatusSearchMessage.HIDDEN)
                },
            afterTextChanged = { s: Editable? ->
                saveText = s.toString()
            }
            )

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && searchHistory.read().isNotEmpty()) showStausMessageSearch(StatusSearchMessage.SEARCH_HISTORY)
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && inputEditText.text.isNotEmpty()) {
                inputEditText.clearFocus()
                searchTrack()
                true
            }
            false
        }
    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveToPreference()
    }
    private fun searchTrack(text: String = saveText){
        iTunesApi.search(text)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse?>,
                    response: Response<TrackResponse?>
                ) {
                    when{
                        response.isSuccessful -> {
                            trackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                adapter.notifyDataSetChanged()
                            }
                            if (trackList.isEmpty()){
                                showStausMessageSearch(StatusSearchMessage.NOT_FOUND)
                            } else {
                                showStausMessageSearch(StatusSearchMessage.DEFAULT)
                            }
                        }
                        else -> {
                            lastText = saveText
                            showStausMessageSearch(StatusSearchMessage.ERROR)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<TrackResponse?>,
                    t: Throwable
                ) {
                    lastText = saveText
                    showStausMessageSearch(StatusSearchMessage.ERROR)
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
            }
            StatusSearchMessage.NOT_FOUND ->{
                recyclerView.isVisible = false
                viewMessageError.isVisible = false
                viewHistorySearch.isVisible = false
                viewMessageNotFound.isVisible = true
            }
            StatusSearchMessage.ERROR ->{
                recyclerView.isVisible = false
                viewHistorySearch.isVisible = false
                viewMessageNotFound.isVisible = false
                viewMessageError.isVisible = true
            }
            StatusSearchMessage.HIDDEN -> {
                recyclerView.isVisible = false
                viewHistorySearch.isVisible = false
            }
            StatusSearchMessage.SEARCH_HISTORY ->{
                recyclerView.isVisible = false
                viewMessageNotFound.isVisible = false
                viewMessageError.isVisible = false
                viewHistorySearch.isVisible = true
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

    companion object {
        private const val TEXT_DEF = ""
        private const val EDIT_TEXT = "EDIT_TEXT"
        private const val LAST_TEXT = "LAST_TEXT"
        const val HISTORY_PREFERENCES = "history_preferences"
    }
}