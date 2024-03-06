package rs.djokafioka.mvvmunsplash.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import rs.djokafioka.mvvmunsplash.R
import rs.djokafioka.mvvmunsplash.data.UnsplashPhoto
import rs.djokafioka.mvvmunsplash.databinding.FragmentGalleryBinding

/**
 * Created by Djordje on 18.8.2022..
 */
@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashPhotoAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null //zbog nekih bugova kod osvezavanja i kada u novoj pretrazi postoji ista sklika da se ona ne pomera unutar recycler view. Ovo je ok za ovu aplikaciju jer ne radimo update stavki u rec view. Inace ne.
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter {
                    adapter.retry() //function from paging adapter that knows how to retry
                },
                footer = UnsplashPhotoLoadStateAdapter {
                    adapter.retry()
                }
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        viewModel.photos.observe(viewLifecycleOwner) {//treba proslediti viewLifecycleOwner, a ne sam fragment jer zelimo da prestenemo da radimo update UI kada se fragment stavi u backstack
            adapter.submitData(viewLifecycleOwner.lifecycle, it) //ne treba samo proslediti lifecycle vec ovaj koji ima scope view-a fragmenta
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        //Ako ovo GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment ne postoji,
        // mora da se uradi rebuild jer to pripada SafeArgs pluginu od navigation component
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true //ne zelimo nista da radimo na samo kucanje u search view, vec samo kada se submituje
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //Uklanjamo referencu na view kada se on unisti da ne bismo imali memory leak
    }
}