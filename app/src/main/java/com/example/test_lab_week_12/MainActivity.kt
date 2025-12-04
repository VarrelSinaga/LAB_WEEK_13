package com.example.test_lab_week_13

import android.R.id.message
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.example.test_lab_week_13.databinding.ActivityMainBinding
import com.example.test_lab_week_13.model.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_main)


        val recyclerView: RecyclerView = findViewById(R.id.movie_list)
        recyclerView.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel = ViewModelProvider(
            this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MovieViewModel(movieRepository) as T
                }
            })[MovieViewModel::class.java]

        binding.viewModel = movieViewModel
        binding.lifecycleOwner = this
        // fetch movies from the API
        // lifecycleScope is a lifecycle-aware coroutine scope
        /*lifecycleScope.launch {
            // repeatOnLifecycle is a lifecycle-aware coroutine builder
            // Lifecycle.State.STARTED means that the coroutine will run
            // when the activity is started
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    // collect the list of movies from the StateFlow
                    movieViewModel.popularMovies.collect {
                        // add the list of movies to the adapter
                            movies ->
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
                        val filteredMovies = movies
                            .filter { movie ->
                            //filter film yang rilis pada tahun ini
                            movie.releaseDate?.startsWith(currentYear) == true
                        }
                            .sortedByDescending { it.popularity }
                        //movieAdapter.addMovies(movies)
                        movieAdapter.addMovies(filteredMovies)
                    }
                }
                launch {
                    // collect the error message from the StateFlow
                    movieViewModel.error.collect { error ->
                        // if an error occurs, show a Snackbar with the error
                        message
                        if (error.isNotEmpty()) Snackbar
                            .make(
                                recyclerView, error, Snackbar.LENGTH_LONG
                            ).show()
                    }
                }
            }
        }

        /*
        movieViewModel.popularMovies.observe(this) { popularMovies ->
            val currentYear =
                Calendar.getInstance().get(Calendar.YEAR).toString()
            movieAdapter.addMovies(
                popularMovies
                    .filter { movie ->
                    // aman dari null
                        movie.releaseDate?.startsWith(currentYear) == true
                    }
                    .sortedByDescending { it.popularity }
            )
        }
        movieViewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Snackbar.make(recyclerView, error, Snackbar.LENGTH_LONG).show()
            }
        }*/
    }*/
    }
    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}