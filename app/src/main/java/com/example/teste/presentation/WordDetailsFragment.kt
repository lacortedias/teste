package com.example.teste.presentation

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.teste.R
import com.example.teste.databinding.FragmentWordDetailsBinding
import com.example.teste.framework.data.Repositories
import com.example.teste.framework.data.model.RepositoriesAction
import com.example.teste.framework.enum.Action
import com.example.teste.main.data.response.DataWords
import com.example.teste.main.viewmodel.MainViewModel
import com.example.teste.main.viewstate.MainViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WordDetailsFragment : Fragment() {

    private var _binding: FragmentWordDetailsBinding? = null
    private val binding: FragmentWordDetailsBinding get() = _binding!!
    private var mediaPlayer = MediaPlayer()

    private val args: WordDetailsFragmentArgs by navArgs()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentWordDetailsBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataApi = args.keyBundle.responseApi.firstOrNull()
        val wordData = args.keyBundle.repositories

        render(dataApi, wordData)
        setupObserver()
    }

    private fun render(dataApi: DataWords?, wordData: Repositories) {

        val words = dataApi?.word.orEmpty()
        val phoneticsTxt =
            dataApi?.phonetics?.find { it.text != "" }?.text?.replace("/", "").orEmpty()
        val phoneticsAudio =
            dataApi?.phonetics?.find { it.audio != "" && it.audio != null }?.audio.orEmpty()
        val meaningDefinitions =
            dataApi?.meanings?.find { it.definitions != null }?.definitions?.find { it.definition != "" }?.definition.orEmpty()
        val meaningPartOfSpeech =
            dataApi?.meanings?.find { it.partOfSpeech != "" }?.partOfSpeech.orEmpty()

        binding.apply {
            txtWords.text = "$words\n\n$phoneticsTxt"
            txtDefinition.text = "$meaningPartOfSpeech - $meaningDefinitions"

            btnPlay.setOnClickListener {
                setupMediaPlayer(phoneticsAudio)
            }

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnNext.setOnClickListener {
                viewModel.getWordData(wordData.id + INCREMENT, Action.HISTORY)
                binding.seekBar.progress = 0
            }

            addFavorite.setOnClickListener {
                viewModel.getWordData(wordData.id, Action.FAVORITE)
            }
        }
        setupColorFavorite(wordData)
    }

    private fun setupObserver() {
        viewModel.listCallApi.observe(viewLifecycleOwner) {
            render(it.responseApi.firstOrNull(), it.repositories)
        }

        viewModel.getWordData.observe(viewLifecycleOwner) {
            when (it.action) {
                Action.FAVORITE -> {
                    setupBtnFavorite(it)
                }
                Action.HISTORY -> {
                    setupBtnNext(it)
                }
            }
        }

        viewModel.viewState.observe(viewLifecycleOwner) {
            when (it) {
                is MainViewState.Loading -> showLoading(it.show)
                is MainViewState.Error -> findNavController().navigate(R.id.error)
                else -> {}
            }
        }
    }

    private fun setupBtnNext(it: RepositoriesAction) {
        val repositories = Repositories(
            id = it.repositories.id,
            word = it.repositories.word,
            isHistory = true,
            isFavorite = it.repositories.isFavorite
        )
        viewModel.updateRepositories(repositories)
        viewModel.getWord(repositories)
        mediaPlayer.reset()
    }

    private fun setupBtnFavorite(it: RepositoriesAction) {
        val repositories = Repositories(
            id = it.repositories.id,
            word = it.repositories.word,
            isHistory = it.repositories.isHistory,
            isFavorite = !it.repositories.isFavorite
        )
        viewModel.updateRepositories(repositories)
        setupColorFavorite(repositories)
    }

    private fun setupColorFavorite(repositories: Repositories) {
        if (repositories.isFavorite) {
            binding.addFavorite.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(), R.color.color_favorite
            )
        } else {
            binding.addFavorite.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(), R.color.color_default_favorite
            )
        }
    }

    private fun setupMediaPlayer(phoneticsAudio: String?) {

        CoroutineScope(Dispatchers.Main).launch {
            phoneticsAudio.let {
                if (it != "") {
                    initMediaPLayer(phoneticsAudio)
                    setupSeekbar(mediaPlayer)

                } else {
                    mediaPlayer.reset()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.return_audio_empty),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initMediaPLayer(phoneticsAudio: String?) {
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer.create(requireContext(), Uri.parse(phoneticsAudio.toString()))
        mediaPlayer.start()
    }

    private fun setupSeekbar(mediaPlayer: MediaPlayer) {
        binding.seekBar.max = mediaPlayer.duration

        CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer.isPlaying) {
                binding.seekBar.progress = mediaPlayer.currentPosition
                delay(100)
            }
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer.start()
            }
        })
        mediaPlayer.setOnCompletionListener {
            binding.seekBar.progress = it.currentPosition
        }
    }

    private fun showLoading(show: Boolean) {
        (activity as MainActivity).showLoading(show)
    }

    override fun onStop() {
        super.onStop()
        viewModel.listCallApi = MutableLiveData()
        viewModel.getWordData = MutableLiveData()
    }

    companion object {
        private const val INCREMENT = 1
    }
}