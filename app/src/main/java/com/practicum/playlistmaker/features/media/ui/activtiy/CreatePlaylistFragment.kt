package com.practicum.playlistmaker.features.media.ui.activtiy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.practicum.playlistmaker.features.main.BottomNavigationOwner
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistState
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.CreatePlaylistViewModel
import com.practicum.playlistmaker.features.media.ui.viewModel.create_playlist.PlaylistUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment: Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var onBackCallBack: OnBackPressedCallback
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var isPosterSet = false

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri ->
        uri?.let {

            isPosterSet = true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { dialog, which ->
                findNavController().navigateUp()
            }
            .setNeutralButton("Отмена", null)

        onBackCallBack = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val hasTitle = !binding.etTitle.text.isNullOrBlank()
                val hasDescription = !binding.etDescription.text.isNullOrBlank()
                val hasPoster = isPosterSet
                if(hasTitle || hasDescription || hasPoster){
                    showConfirmDialog()
                } else{
                    this.isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }
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
                isButtonEnable(!s.isNullOrBlank())
            }
        )
        binding.ivPoster.setOnClickListener{
            requestImage()
        }
        binding.btnCreatePlaylist.setOnClickListener {

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
    private fun showSnackBar(playlistTitle: String){
        Snackbar.make(binding.root, "Плейлист $playlistTitle создан", Snackbar.LENGTH_SHORT).show()
    }
}