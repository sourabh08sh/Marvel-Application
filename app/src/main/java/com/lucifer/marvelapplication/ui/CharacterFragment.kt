package com.lucifer.marvelapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucifer.marvelapplication.MainActivity
import com.lucifer.marvelapplication.MainApplication
import com.lucifer.marvelapplication.R
import com.lucifer.marvelapplication.adapters.CharacterAdapter
import com.lucifer.marvelapplication.adapters.CharacterItemClicked
import com.lucifer.marvelapplication.models.character.Result
import com.lucifer.marvelapplication.repository.Response
import com.lucifer.marvelapplication.viewmodel.CharacterViewModel
import com.lucifer.marvelapplication.viewmodel.CharacterViewModelFactory
import com.mancj.materialsearchbar.MaterialSearchBar


/**
 * A simple [Fragment] subclass.
 * Use the [CharacterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterFragment : Fragment(), CharacterItemClicked,
    MaterialSearchBar.OnSearchActionListener {
    lateinit var characterViewModel: CharacterViewModel
    private lateinit var characterRecyclerView: RecyclerView
    private lateinit var mAdapter: CharacterAdapter
    lateinit var layoutManager: GridLayoutManager
    private lateinit var charProgressBar: ProgressBar
    private lateinit var searchBar: MaterialSearchBar

    private var isLoading: Boolean = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var previousTotal: Int = 0
    private var viewThreshold: Int = 10

    private var offset: Int = 0
    private var searchString: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val characterView =  inflater.inflate(R.layout.fragment_character, container, false)

        characterRecyclerView = characterView.findViewById(R.id.recycler_view_character) as RecyclerView
        charProgressBar = characterView.findViewById(R.id.pg_bar_char) as ProgressBar

        searchBar = characterView.findViewById(R.id.searchBar) as MaterialSearchBar
        searchBar.setHint("Search by name")
        searchBar.setSpeechMode(true)
        searchBar.setOnSearchActionListener(this)

        characterRecyclerView.setHasFixedSize(true)
        layoutManager = GridLayoutManager(activity?.applicationContext, 2)
        characterRecyclerView.layoutManager =layoutManager
        mAdapter = CharacterAdapter(this)
        characterRecyclerView.adapter = mAdapter

        val repository = (activity?.application as MainApplication).repository
        characterViewModel = ViewModelProvider(
            activity as MainActivity, CharacterViewModelFactory(
                repository
            )
        ).get(CharacterViewModel::class.java)

        // observing the live data if anything changes in live data this observer will observe that and make that change.
        characterViewModel.characters.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Response.Loading -> {
                    showProgressBar()
                    Toast.makeText(activity, "Data is loading", Toast.LENGTH_SHORT).show()
                }
                is Response.Success -> {
                    hideProgressBar()
                    it.data?.let {
                        mAdapter.updateList(it.data.results)
                    }
                }
                is Response.Error -> {
                    hideProgressBar()
                    Log.d("error1", it.errorMessage.toString())
                    Toast.makeText(activity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        })

        scrollChecker()

        return characterView
    }

    // for checking scrolling in recycler view
    private fun scrollChecker() {
        characterRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()


                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false
                            previousTotal = totalItemCount
                        }
                    }

                    if (!isLoading && (totalItemCount - visibleItemCount) <= (pastVisibleItems + viewThreshold)) {
                        offset += 20
                        characterViewModel.pagination(searchString, offset)
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun hideProgressBar() {
        charProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        charProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onItemClicked(character: Result) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("imagePath", character.thumbnail.path)
        intent.putExtra("imageExt", character.thumbnail.extension)
        intent.putExtra("name", character.name)
        intent.putExtra("description", character.description)
        startActivity(intent)
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        val s = if (enabled) "enabled" else "disabled"
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        searchString = if (text.toString().isEmpty()){
            null
        } else{
            text.toString()
        }
        offset = 0
        characterViewModel.pagination(searchString, offset)
    }

    override fun onButtonClicked(buttonCode: Int) {
        TODO("Not yet implemented")
    }

}