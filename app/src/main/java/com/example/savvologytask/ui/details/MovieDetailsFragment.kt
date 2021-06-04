package com.example.savvologytask.ui.details

import android.R.color
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import certif.id.app.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.bmsassessment.ui.moviedetails.adapter.CastAdapter
import com.example.bmsassessment.ui.moviedetails.adapter.CrewAdapter
import com.example.bmsassessment.ui.moviedetails.adapter.ReviewAdapter
import com.example.bmsassessment.ui.moviedetails.adapter.SimilarMovieAdapter
import com.example.savvologytask.R
import com.example.savvologytask.data.remote.MovieListResponse
import com.example.savvologytask.data.remote.MovieReviewResponse
import com.example.savvologytask.databinding.FragmentMovieDetailsBinding
import com.example.savvologytask.helper.DrawableAlwaysCrossFadeFactory
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.model.MovieCredits
import com.example.savvologytask.model.MovieDetails
import com.example.savvologytask.ui.movielist.MovieListAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.view_collapse_toolbar.*
import kotlinx.android.synthetic.main.view_credits.*
import kotlinx.android.synthetic.main.view_reviews.*
import kotlinx.android.synthetic.main.view_similar_movies.*
import kotlinx.android.synthetic.main.view_synopsis.*
import org.koin.android.viewmodel.ext.android.viewModel


class MovieDetailsFragment() :
    BaseFragment<MovieDetailsViewModel, FragmentMovieDetailsBinding>(R.layout.fragment_movie_details) {

    override val viewModel: MovieDetailsViewModel by viewModel()

    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var toolbar: Toolbar? = null


    private var movieId: Long = 0
    private var movieName: String = ""

    private lateinit var castAdapter: CastAdapter
    private lateinit var crewAdapter: CrewAdapter
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var movieListAdapter: SimilarMovieAdapter

    val bookMarkCheckChangeListener = object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            buttonView!!.animate().scaleX(1.05f).scaleX(1.05f).setDuration(50).withEndAction {
                buttonView.animate().scaleX(1.0f).scaleX(1.0f).setDuration(50).withEndAction {
                    if (isChecked) {
                        viewModel.bookmarkMovie()
                    } else {
                        viewModel.removeBookmarkedMovie()
                    }
                }
            }
        }
    }

    override fun setViewModel() {
        binding.viewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        /*showCustomUI()*/

        arguments?.let {
            movieId = it.getLong("movie_id")
            movieName = it.getString("movie_name", "")
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setListeners()

        viewModel.getMovieDetails(movieId)
        viewModel.getMovieCredits(movieId)
        viewModel.getMovieReviews(movieId)
        viewModel.getSimilarMovies(movieId)
    }

    override fun initUi() {
        appBarLayout = view?.findViewById(R.id.appBarIncViewCollpsTlbr)
        collapsingToolbarLayout = view?.findViewById(R.id.ctlIncViewCollpsTlbr)
        toolbar = view?.findViewById(R.id.toolbarIncViewCollpsTlbr)
        collapsingToolbarLayout?.title = movieName
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_white_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(false)
            setDisplayShowTitleEnabled(false)
        }





        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            /*sample_synopsis.text = it.title*/
            viewModel.isBookMarked()
            initSynopsisView(it)
        })

        //Observers for credits data
        viewModel.movieCredits.observe(viewLifecycleOwner, Observer {
            initCreditViews(it)
        })

        //Observers for review data
        viewModel.movieReviews.observe(viewLifecycleOwner, Observer {
            initReviewView(it)
        })

        //Observers for similar movies
        viewModel.similarMovies.observe(viewLifecycleOwner, Observer {
            initSimilarMovie(it)
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setListeners() {
        appBarLayout?.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (collapsingToolbarLayout?.getHeight()!! + verticalOffset < 2 * ViewCompat.getMinimumHeight(
                    collapsingToolbarLayout!!
                )
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    toolbar!!.navigationIcon!!.setColorFilter(
                        BlendModeColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            ), BlendMode.SRC_ATOP
                        )
                    )
                    cb_movie_bookmark.buttonDrawable?.setColorFilter(
                        BlendModeColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.teal_700),
                            BlendMode.SRC_ATOP
                        )
                    )
                } else {
                    toolbar!!.navigationIcon!!.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                    cb_movie_bookmark.buttonDrawable?.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.teal_700
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    toolbar!!.navigationIcon!!.setColorFilter(
                        BlendModeColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            ), BlendMode.SRC_ATOP
                        )
                    )
                    cb_movie_bookmark.buttonDrawable?.setColorFilter(
                        BlendModeColorFilter(
                            ContextCompat.getColor(requireContext(), R.color.white),
                            BlendMode.SRC_ATOP
                        )
                    )
                } else {
                    toolbar!!.navigationIcon!!.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                    cb_movie_bookmark.buttonDrawable?.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                }
            }
        })


        viewModel.isBookMarked.observe(viewLifecycleOwner, Observer {
            cb_movie_bookmark.setOnCheckedChangeListener(null)
            cb_movie_bookmark.isChecked = it
            cb_movie_bookmark.setOnCheckedChangeListener(bookMarkCheckChangeListener)
        })


        cb_movie_bookmark.setOnCheckedChangeListener(bookMarkCheckChangeListener)
    }


    /**
     * This method will initialize synopsis(details) views in movie details screen
     * @param movieDetails: Movie Details object received from server
     */
    private fun initSynopsisView(movieDetails: MovieDetails?) {
        movieDetails?.let { movieData ->
            cl_synopsis.visibility = View.VISIBLE
            movieData.backdropPath?.let {
                Glide.with(requireContext())
                    .load(MovieListAdapter.IMAGE_BASE_URL + it)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_movie_banner_placeholder)
                    .error(R.drawable.ic_movie_banner_placeholder)
                    .into(iv_toolbar_poster)
            }
            tv_movie_title_synopsis.text = movieData.title
            tv_overview_movie_synopsis.setContent(movieData.overview)
            tv_duration_synopsis.text =
                getString(R.string.movie_duration, movieData.runtime.toString())
            tv_release_date_synopsis.text =
                movieData.releaseDate?.substring(0, movieData.releaseDate!!.indexOf("-"))
            tv_total_vote_synopsis.text =
                getString(R.string.movie_total_votes, movieData.voteCount.toString())
            tv_movie_average_vote_synopsis.text = movieData.voteAverage.toString()
            ll_other_details_synopsis.removeAllViews()

            movieData.genres?.let {
                if (it.isNotEmpty()) {
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.movie_other_details_view, cl_synopsis, false)
                    (view as AppCompatTextView).text = buildSpannedString {
                        bold { append(getString(R.string.movie_type_label)) }
                        append(it.joinToString(", ") { genre -> genre?.name!! })
                    }
                    ll_other_details_synopsis.addView(view)
                    ll_other_details_synopsis.invalidate()
                }
            }

            movieData.spokenLanguages?.let {
                if (it.isNotEmpty()) {
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.movie_other_details_view, cl_synopsis, false)
                    (view as AppCompatTextView).text = buildSpannedString {
                        bold { append(getString(R.string.movie_spoken_lang)) }
                        append(it.joinToString(", ") { lang -> lang?.name!! })
                        ll_other_details_synopsis.addView(view)
                        ll_other_details_synopsis.invalidate()
                    }
                }
            }


        }
    }


    /**
     * This method will initialize credits views in movie details screen
     * @param movieCredits: Movie Details object received from server
     */
    private fun initCreditViews(movieCredits: MovieCredits) {
        if (movieCredits.cast != null && movieCredits.cast!!.isNotEmpty() || movieCredits.crew != null && movieCredits.crew!!.isNotEmpty()) {
            cl_credits.visibility = View.VISIBLE
        } else {
            cl_credits.visibility = View.GONE
        }

        if (movieCredits.cast != null && movieCredits.cast!!.isNotEmpty()) {
            grp_cast.visibility = View.VISIBLE
            castAdapter = CastAdapter(movieCredits.cast!!) {
                if (NetInfo.isInternetOn()) {
                    val bundle = bundleOf("person_id" to it.id, "person_name" to it.name)
                    findNavController().navigate(
                        R.id.action_movieDetailsFragment_to_personDetailsFragment,
                        bundle
                    )
                } else {
                    toast(getString(R.string.no_internet_message))
                }
            }
            rv_cast.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = castAdapter
            }
        } else {
            grp_cast.visibility = View.GONE
        }

        if (movieCredits.crew != null && movieCredits.crew!!.isNotEmpty()) {
            grp_crew.visibility = View.VISIBLE
            crewAdapter = CrewAdapter(movieCredits.crew!!) {
                if (NetInfo.isInternetOn()) {
                    val bundle = bundleOf("person_id" to it.id, "person_name" to it.name)
                    findNavController().navigate(
                        R.id.action_movieDetailsFragment_to_personDetailsFragment,
                        bundle
                    )
                } else {
                    toast(getString(R.string.no_internet_message))
                }
            }
            rv_crew.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = crewAdapter
            }
        } else {
            grp_crew.visibility = View.GONE
        }

    }


    /**
     * This method will initialize reviews in movie details screen
     * @param reviews: Movie Details object received from server
     */
    private fun initReviewView(reviews: MovieReviewResponse) {
        if (reviews.results.isNotEmpty()) {
            cl_reviews.visibility = View.VISIBLE
            reviewAdapter = ReviewAdapter(reviews.results)
            rv_review.apply {
                itemAnimator = DefaultItemAnimator()
                adapter = reviewAdapter
            }
        } else {
            cl_reviews.visibility = View.GONE
        }
    }

    /**
     * This method will initialize similar movies in movie details screen
     * @param movieListResponse: Movie List object received from server
     */
    private fun initSimilarMovie(movieListResponse: MovieListResponse?) {
        movieListResponse?.let { movieListData ->
            if (movieListData.results.isNotEmpty()) {
                cl_similar_movies.visibility = View.VISIBLE
                movieListAdapter = SimilarMovieAdapter(
                    movieListData.results
                ) {
                    if (NetInfo.isInternetOn()) {
                        val bundle = bundleOf("movie_name" to it.title, "movie_id" to it.id)
                        findNavController().navigate(R.id.action_movieDetailsFragment_self, bundle)
                    } else {
                        toast(getString(R.string.no_internet_message))
                    }
                }

                rv_similar_movie.apply {
                    itemAnimator = DefaultItemAnimator()
                    adapter = movieListAdapter
                }
            } else {
                cl_similar_movies.visibility = View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}