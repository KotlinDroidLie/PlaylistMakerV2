package com.practicum.playlistmaker.features.search.ui.activtiy

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.databinding.SearchStatusConnectionProblemsViewBinding
import com.practicum.playlistmaker.databinding.SearchStatusHistoryViewBinding
import com.practicum.playlistmaker.databinding.SearchStatusNothingFoundViewBinding
import com.practicum.playlistmaker.features.player.ui.activity.AudioPlayerFragment
import com.practicum.playlistmaker.features.search.domain.model.TrackModel
import com.practicum.playlistmaker.features.search.ui.view_model.SearchState
import com.practicum.playlistmaker.features.search.ui.view_model.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _mainBinding: FragmentSearchBinding? = null
    private val mainBinding get() = _mainBinding!!

    private var _statusConnectionBinding: SearchStatusConnectionProblemsViewBinding? = null
    private val statusConnectionBinding get() = _statusConnectionBinding!!

    private var _statusHistoryBinding: SearchStatusHistoryViewBinding? = null
    private val statusHistoryBinding get() = _statusHistoryBinding!!

    private var _statusNothingFoundBinding: SearchStatusNothingFoundViewBinding? = null
    private val statusNothingFoundBinding get() = _statusNothingFoundBinding!!
    private val viewModel: SearchViewModel by viewModel()
    private var isClickAllowed = true
    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TrackAdapter(
            onTrackClickListener = { track ->
                addToSearchHistory(track)
                openAudioPlayer(track)
            }
        )
        historyAdapter = TrackAdapter(
            onTrackClickListener = { track ->
                openAudioPlayer(track)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _mainBinding = FragmentSearchBinding.inflate(inflater, container, false)
        return mainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _statusConnectionBinding = SearchStatusConnectionProblemsViewBinding.bind(mainBinding.viewConnectionProblems.root)
        _statusHistoryBinding = SearchStatusHistoryViewBinding.bind(mainBinding.viewSearchHistory.root)
        _statusNothingFoundBinding = SearchStatusNothingFoundViewBinding.bind(mainBinding.viewNothingFound.root)

        statusHistoryBinding.rvHistorySongsList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        statusHistoryBinding.rvHistorySongsList.adapter  = historyAdapter

        mainBinding.rvSongsList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mainBinding.rvSongsList.adapter  = adapter

        viewModel.historyTracks.observe(viewLifecycleOwner){
            updateHistoryList(it)
        }

        viewModel.state.observe(viewLifecycleOwner){
            render(it)
        }

        statusHistoryBinding.btnClearHistory.setOnClickListener {
            viewModel.clearHistory()
            viewModel.resetToDefault()
        }

        mainBinding.ivClearText.setOnClickListener {
            mainBinding.etSearch.setText("")
            mainBinding.etSearch.clearFocus()
            hideKeyboard(mainBinding.etSearch)
            viewModel.resetToDefault()
        }

        statusConnectionBinding.btnRefresh.setOnClickListener {
            viewModel.retryErrorSearch()
        }

        mainBinding.etSearch.addTextChangedListener(
            onTextChanged = { s: CharSequence?, start: Int, before: Int, count: Int ->
                mainBinding.ivClearText.isVisible = if (s.isNullOrEmpty()) false else true
                if (mainBinding.etSearch.hasFocus() && s.isNullOrEmpty() && historyAdapter.trackList.isNotEmpty()){
                    viewModel.displayHistory()
                }
                viewModel.searchDebounce(s?.toString()?.trim() ?: "")
            }
        )

        mainBinding.etSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && mainBinding.etSearch.text.isEmpty() && historyAdapter.trackList.isNotEmpty()){
                viewModel.displayHistory()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isClickAllowed = true
        _mainBinding = null
        _statusConnectionBinding = null
        _statusHistoryBinding = null
        _statusNothingFoundBinding = null
    }
    private fun addToSearchHistory(track: TrackModel){
        viewModel.saveToHistory(track)
    }

    private fun openAudioPlayer(track: TrackModel){
        if (clickDebounce()){
            findNavController().navigate(
                R.id.action_searchFragment_to_audioPlayerFragment,
                AudioPlayerFragment.createARgs(track)
            )
        }
    }

    private fun hideKeyboard(view: View){
        val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun render(state: SearchState){
        when(state){
            is SearchState.SearchResult -> showContent(state.tracks)
            is SearchState.Empty -> showEmpty(state.emptyMessage)
            is SearchState.Error -> showError(state.errorMessage)
            SearchState.Loading -> showLoading()
            SearchState.HistoryResult -> showHistory()
            SearchState.Default -> showDefault()
        }
    }

    private fun showDefault(){
        mainBinding.rvSongsList.isVisible = false
        statusHistoryBinding.root.isVisible = false
        mainBinding.pbSearch.isVisible = false
    }

    private fun showHistory(){
        mainBinding.rvSongsList.isVisible = false
        statusNothingFoundBinding.root.isVisible = false
        statusConnectionBinding.root.isVisible = false
        statusHistoryBinding.root.isVisible = true
        mainBinding.pbSearch.isVisible = false
    }
    private fun showContent(newSearchList: List<TrackModel>){
        mainBinding.rvSongsList.isVisible = true
        statusNothingFoundBinding.root.isVisible = false
        statusConnectionBinding.root.isVisible = false
        statusHistoryBinding.root.isVisible = false
        mainBinding.pbSearch.isVisible = false
        updateSearchList(newSearchList)
    }
    private fun showEmpty(emptyMessage: Int){
        mainBinding.rvSongsList.isVisible = false
        statusConnectionBinding.root.isVisible = false
        statusHistoryBinding.root.isVisible = false
        statusNothingFoundBinding.root.isVisible = true
        mainBinding.pbSearch.isVisible = false
        statusNothingFoundBinding.root.text = getString(emptyMessage)
    }
    private fun showError(errorMessage: Int){
        mainBinding.rvSongsList.isVisible = false
        statusHistoryBinding.root.isVisible = false
        statusNothingFoundBinding.root.isVisible = false
        statusConnectionBinding.root.isVisible = true
        mainBinding.pbSearch.isVisible = false
        statusConnectionBinding.tvConnectionProblems.text = getString(errorMessage)
    }
    private fun showLoading(){
        mainBinding.rvSongsList.isVisible = false
        statusNothingFoundBinding.root.isVisible = false
        statusConnectionBinding.root.isVisible = false
        statusHistoryBinding.root.isVisible = false
        mainBinding.pbSearch.isVisible = true
    }
    private fun updateHistoryList(newHistoryList: List<TrackModel>){
        historyAdapter.trackList.clear()
        historyAdapter.trackList.addAll(newHistoryList)
        historyAdapter.notifyDataSetChanged()
    }
    private fun updateSearchList(newSearchList: List<TrackModel>){
        adapter.trackList.clear()
        adapter.trackList.addAll(newSearchList)
        adapter.notifyDataSetChanged()
    }
    private fun clickDebounce(): Boolean{
        val current = isClickAllowed
        if (current){
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    companion object {
        private const val CLICK_DELAY = 1000L
    }
}