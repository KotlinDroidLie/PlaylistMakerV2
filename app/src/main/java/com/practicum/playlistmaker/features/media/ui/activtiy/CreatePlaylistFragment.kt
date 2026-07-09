package com.practicum.playlistmaker.features.media.ui.activtiy

import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.features.main.BottomNavigationOwner
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistState
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.PlaylistUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class CreatePlaylistFragment: Fragment() {
    private val viewModel: CreatePlaylistViewModel by viewModel()
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var onBackCallBack: OnBackPressedCallback
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var cornerRadius by Delegates.notNull<Int>()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
        uri?.let {
            viewModel.onUriChanged(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.create_playlist_confirm_dialog_title))
            .setMessage(getString(R.string.create_playlist_confirm_dialog_message))
            .setPositiveButton(getString(R.string.create_playlist_confirm_dialog_positive_message)) { dialog, which ->
                findNavController().navigateUp()
            }
            .setNeutralButton(getString(R.string.create_playlist_confirm_dialog_neutral_message), null)

        onBackCallBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val currentState = viewModel.state.value ?: return
                if(hasData(currentState)){
                    showConfirmDialog()
                } else{
                    this.isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        cornerRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            requireContext().resources.displayMetrics
        ).toInt()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackCallBack)

        binding.btnCreatePlaylistBack.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.etTitle.addTextChangedListener(
            afterTextChanged = { s ->
                viewModel.onTitleChanged(s.toString())
            }
        )
        binding.etDescription.addTextChangedListener(
            afterTextChanged = { s ->
                viewModel.onDescriptionChanged(s.toString())
            }
        )
        binding.ivPoster.setOnClickListener{
            requestImage()
        }
        binding.btnCreatePlaylist.setOnClickListener {
            viewModel.createPlaylist()
        }
        viewModel.state.observe(viewLifecycleOwner){
            handleState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        hideBottomNav()
    }

    override fun onPause() {
        super.onPause()
        showBottomNav()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleState(state: CreatePlaylistState) {
        when(state){
            is CreatePlaylistState.Created -> handleCreated(state.title)
            is CreatePlaylistState.Editing -> handleEditing(state.playlist)
            CreatePlaylistState.Creating -> handleCreating()
            is CreatePlaylistState.Error -> handleError(state.resIdErrorMessage)
        }
    }

    private fun handleCreating(){
        enableInput(false)
    }

    private fun handleError(resIdErrorMessage: Int) {
        enableInput(true)
        showErrorNotification(resIdErrorMessage)
    }

    private fun enableInput(enable: Boolean){
        isButtonEnable(enable)
        binding.apply {
            etTitleContainer.isEnabled = enable
            etDescriptionContainer.isEnabled = enable
            ivPoster.isEnabled = enable
        }
    }

    private fun handleEditing(playlist: PlaylistUiModel) {
        isButtonEnable(playlist.isButtonEnable)
        playlist.coverImagePath?.let {
            setPoster(it.toUri())
        }
    }

    private fun setPoster(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(cornerRadius)
                )
            )
            .placeholder(R.drawable.placeholder_poster_playlist)
            .into(binding.ivPoster)
    }

    private fun handleCreated(title: String) {
        showCreatedNotification(title)
        findNavController().navigateUp()
    }

    private fun hideBottomNav(){
        (requireActivity() as? BottomNavigationOwner)?.hideBottomNav()
    }

    private fun showBottomNav(){
        (requireActivity() as? BottomNavigationOwner)?.showBottomNav()
    }

    private fun isButtonEnable(flag: Boolean){
        binding.btnCreatePlaylist.isEnabled = flag
    }

    private fun requestImage(){
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showConfirmDialog() {
        confirmDialog.show()
    }
    private fun showCreatedNotification(title: String){
        Snackbar.make(
            binding.root,
            getString(R.string.create_playlist_notification, title),
            Snackbar.LENGTH_SHORT
        ).show()
    }
    private fun showErrorNotification(resIdErrorMessage: Int){
        Snackbar.make(
            binding.root,
            getString(resIdErrorMessage),
            Snackbar.LENGTH_SHORT
        ).show()
    }
    private fun hasData(state: CreatePlaylistState): Boolean{
        return when(state){
            is CreatePlaylistState.Created -> false
            is CreatePlaylistState.Editing -> {
                val playlist = state.playlist
                val hasTitle = playlist.title.isNotBlank()
                val hasDescription = playlist.description.isNotBlank()
                val hasPoster = playlist.coverImagePath != null
                (hasTitle || hasDescription || hasPoster)
            }

            CreatePlaylistState.Creating -> false
            is CreatePlaylistState.Error -> true
        }
    }
}