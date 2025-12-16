package com.practicum.playlistmaker

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
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: TrackAdapter
    private lateinit var viewMessageNotFound: MaterialTextView
    private lateinit var viewMessageError: LinearLayout
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

        val buttonBack = findViewById<MaterialToolbar>(R.id.btn_search_back)
        val inputEditText = findViewById<EditText>(R.id.et_search)
        val buttonClear = findViewById<ImageView>(R.id.iv_clear_text)
        val buttonRefresh = findViewById<Button>(R.id.btn_refresh)
        viewMessageNotFound = findViewById(R.id.view_nothing_found)
        viewMessageError = findViewById(R.id.view_connection_problems)

        recyclerView = findViewById(R.id.rv_songs_list)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        adapter = TrackAdapter(trackList)
        recyclerView.adapter  = adapter

        inputEditText.setText(saveText)

        buttonBack.setNavigationOnClickListener {
            finish()
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
                buttonClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            },
            afterTextChanged= { s: Editable? ->
                saveText = s.toString()
            }
        )

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                inputEditText.clearFocus()
                searchTrack()
                true
            }
            false
        }

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
                                showStausMessageSearch(StatusSearchMessage.OK)
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
            StatusSearchMessage.OK ->{
                recyclerView.visibility = View.VISIBLE
                viewMessageNotFound.visibility = View.GONE
                viewMessageError.visibility = View.GONE
            }
            StatusSearchMessage.NOT_FOUND ->{
                recyclerView.visibility = View.GONE
                viewMessageError.visibility = View.GONE
                viewMessageNotFound.visibility = View.VISIBLE
            }
            StatusSearchMessage.ERROR ->{
                recyclerView.visibility = View.GONE
                viewMessageNotFound.visibility = View.GONE
                viewMessageError.visibility = View.VISIBLE
            }
            StatusSearchMessage.HIDDEN -> recyclerView.visibility = View.GONE
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
    }

}