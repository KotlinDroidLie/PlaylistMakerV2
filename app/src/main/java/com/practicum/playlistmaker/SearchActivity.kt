package com.practicum.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    private val trackList = listOf(
        TrackModel(
            "Smells Like Teen Spirit",
            "Nirvana",
            "5:01",
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        ),
        TrackModel(
            "Billie Jean",
            "Michael Jackson",
            "4:35",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
        ),
        TrackModel(
            "Stayin' Alive",
            "Bee Gees",
            "4:10",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        ),
        TrackModel(
            "Whole Lotta Love",
            "Led Zeppelin",
            "5:33",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        ),
        TrackModel(
            "Sweet Child O'Mine",
            "Guns N' Roses",
            "5:03",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )
    )
    private var saveText: String = TEXT_DEF

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

        val recyclerView = findViewById<RecyclerView>(R.id.rv_songs_list)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        recyclerView.adapter  = TrackAdapter(trackList)

        inputEditText.setText(saveText)

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        buttonClear.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        inputEditText.addTextChangedListener(
            onTextChanged = { s: CharSequence?, start: Int, before: Int, count: Int ->
                buttonClear.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            },
            afterTextChanged= { s: Editable? ->
                saveText = s.toString()
            }
        )

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, saveText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveText = savedInstanceState.getString(EDIT_TEXT, TEXT_DEF)
    }

    companion object {
        private const val TEXT_DEF = ""
        private const val EDIT_TEXT = "EDIT_TEXT"
    }

}