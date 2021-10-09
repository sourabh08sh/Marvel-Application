package com.lucifer.marvelapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lucifer.marvelapplication.MainActivity
import com.lucifer.marvelapplication.MainApplication
import com.lucifer.marvelapplication.R
import com.lucifer.marvelapplication.adapters.ComicItemClicked
import com.lucifer.marvelapplication.adapters.ComicsAdapter
import com.lucifer.marvelapplication.repository.Response
import com.lucifer.marvelapplication.viewmodel.ComicsViewModel
import com.lucifer.marvelapplication.viewmodel.ComicsViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [ComicsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComicsFragment : Fragment(), ComicItemClicked {
    lateinit var comicsViewModel: ComicsViewModel
    private lateinit var comicsRecyclerView: RecyclerView
    private lateinit var mAdapter: ComicsAdapter
    lateinit var layoutManager: GridLayoutManager
    private lateinit var comicProgressBar: ProgressBar
    private lateinit var comicFilter: RadioGroup

    private var isLoading: Boolean = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var previousTotal: Int = 0
    private var viewThreshold: Int = 10

    private var offset: Int = 0
    private var selectedFilter: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val comicView = inflater.inflate(R.layout.fragment_comics, container, false)

        comicsRecyclerView = comicView.findViewById(R.id.recycler_view_comics) as RecyclerView
        comicProgressBar = comicView.findViewById(R.id.pg_bar_comic) as ProgressBar
        comicFilter = comicView.findViewById(R.id.comicFilerBtn) as RadioGroup

        comicsRecyclerView .setHasFixedSize(true)
        layoutManager = GridLayoutManager(activity?.applicationContext, 2)
        comicsRecyclerView .layoutManager =layoutManager
        mAdapter = ComicsAdapter(this)
        comicsRecyclerView .adapter = mAdapter

        val repository = (activity?.application as MainApplication).repository
        comicsViewModel = ViewModelProvider(activity as MainActivity, ComicsViewModelFactory(repository)).get(ComicsViewModel::class.java)

        comicsViewModel.comic.observe(viewLifecycleOwner, Observer {
            when(it){
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
                    Toast.makeText(activity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        })

        scrollChecker()

        comicFilter.setOnCheckedChangeListener { group, checkedId ->
            val filterBtn: RadioButton = group.findViewById(checkedId)
            val filterText = filterBtn.text.toString()
            selectedFilter = if (filterText == "allComics"){
                null
            }else{
                filterText
            }
            offset = 0
            comicsViewModel.pagination(selectedFilter, offset)
        }

        return comicView
    }

    private fun scrollChecker() {
        comicsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        comicsViewModel.pagination(selectedFilter, offset)
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun hideProgressBar() {
        comicProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar() {
        comicProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onItemClicked(comic: com.lucifer.marvelapplication.models.comic.Result) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("imagePath", comic.thumbnail.path)
        intent.putExtra("imageExt", comic.thumbnail.extension)
        intent.putExtra("name", comic.title)
        intent.putExtra("description", comic.description)
        startActivity(intent)
    }

}