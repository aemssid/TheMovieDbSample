package com.example.savvologytask.ui.person

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import certif.id.app.base.BaseFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.savvologytask.R
import com.example.savvologytask.data.remote.PersonCreditResponse
import com.example.savvologytask.databinding.FragmentPersonDetailsBinding
import com.example.savvologytask.helper.DrawableAlwaysCrossFadeFactory
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.model.PersonDetails
import com.example.savvologytask.ui.movielist.MovieListAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.item_layout_person.*
import kotlinx.android.synthetic.main.view_collapse_toolbar.*
import kotlinx.android.synthetic.main.view_person_credits.*
import kotlinx.android.synthetic.main.view_person_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class PersonDetailsFragment :
    BaseFragment<PersonDetailsViewModel, FragmentPersonDetailsBinding>(R.layout.fragment_person_details) {
    override val viewModel: PersonDetailsViewModel by viewModel()

    override fun setViewModel() {
        binding.viewModel = viewModel
    }

    private var personId: Long = 0
    private var personName: String = ""

    private var appBarLayout: AppBarLayout? = null
    private var collapsingToolbarLayout: CollapsingToolbarLayout? = null
    private var toolbar: Toolbar? = null

    val castList = arrayListOf<PersonCreditResponse.CastDetails>()
    var mCastAdapter: PersonAsCastAdapter? = null

    val crewList = arrayListOf<PersonCreditResponse.CrewDetails>()
    var mCrewAdapter: PersonAsCrewAdapter? = null

    val bookMarkCheckChangeListener = object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            buttonView!!.animate().scaleX(1.05f).scaleX(1.05f).setDuration(50).withEndAction {
                buttonView.animate().scaleX(1.0f).scaleX(1.0f).setDuration(50).withEndAction {
                    if (isChecked) {
                        viewModel.bookmarkPerson()
                    } else {
                        viewModel.removeBookmarkedPerson()
                    }
                }
            }


        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            personId = it.getLong("person_id")
            personName = it.getString("person_name")!!
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setListeners()

        viewModel.getPersonDetails(personId)
        viewModel.getPersonCredits(personId)
    }

    override fun initUi() {
        appBarLayout = view?.findViewById(R.id.appBarIncViewCollpsTlbr)
        collapsingToolbarLayout = view?.findViewById(R.id.ctlIncViewCollpsTlbr)
        toolbar = view?.findViewById(R.id.toolbarIncViewCollpsTlbr)
        /*collapsingToolbarLayout?.title = personName*/
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_white_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(false)
            setDisplayShowTitleEnabled(false)
        }


        mCastAdapter = PersonAsCastAdapter(castList) {
            if (NetInfo.isInternetOn()) {
                val bundle = bundleOf("movie_name" to it.title, "movie_id" to it.id)
                findNavController().navigate(
                    R.id.action_personDetailsFragment_to_movieDetailsFragment,
                    bundle
                )
            } else {
                toast(getString(R.string.no_internet_message))
            }
        }
        rv_cast_credits.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = mCastAdapter
        }

        mCrewAdapter = PersonAsCrewAdapter(crewList) {
            if (NetInfo.isInternetOn()) {
                val bundle = bundleOf("movie_name" to it.title, "movie_id" to it.id)
                findNavController().navigate(
                    R.id.action_personDetailsFragment_to_movieDetailsFragment,
                    bundle
                )
            } else {
                toast(getString(R.string.no_internet_message))
            }
        }

        rv_crew_credits.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = mCrewAdapter
        }

    }

    private fun renderState(personDetailsState: PersonDetailsState) {
        when (personDetailsState) {
            PersonDetailsState.Initial -> {

            }
            PersonDetailsState.Loading -> {
                showProgressDialog()
            }
            PersonDetailsState.NoInternet -> {

            }
            is PersonDetailsState.PersonDetailsFailure -> {


            }
            is PersonDetailsState.PersonDetailsSuccess -> {
                /*hideProgressDialog()
                populateDetails(personDetailsState.personDetails)*/
            }
        }
    }

    private fun populateDetails(personDetails: PersonDetails) {
        personDetails.profile_path.let {
            Glide.with(requireContext())
                .load(MovieListAdapter.IMAGE_BASE_URL + it)
                .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(if (personDetails.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder)
                .error(if (personDetails.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder)
                .into(iv_toolbar_poster)
        }

        tv_person_title.text = personDetails.name
        if (!personDetails.biography.isNullOrEmpty()) {
            tv_person_overview.setContent(personDetails.biography)
        }

        if (personDetails.known_for_department != null && personDetails.known_for_department!!.isNotEmpty()) {
            tv_person_known_for.text =
                getString(R.string.known_for, personDetails.known_for_department)
        } else {
            tv_person_known_for.text = getString(R.string.known_for, "NA")
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setListeners() {
        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
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

        viewModel.state().observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                renderState(it)
            }
        })

        cb_movie_bookmark.setOnCheckedChangeListener(bookMarkCheckChangeListener)

        viewModel.isBookMarked.observe(viewLifecycleOwner, Observer {
            cb_movie_bookmark.setOnCheckedChangeListener(null)
            cb_movie_bookmark.isChecked = it
            cb_movie_bookmark.setOnCheckedChangeListener(bookMarkCheckChangeListener)
        })

        viewModel.personDetails.observe(viewLifecycleOwner, Observer {
            /*sample_synopsis.text = it.title*/
            viewModel.isBookMarked()
            populateDetails(it)
        })

        viewModel.personCredit.observe(viewLifecycleOwner, Observer {
            if (it.cast.isNullOrEmpty() && it.crew.isNullOrEmpty()) {
                cl_person_credits.visibility = View.GONE
                return@Observer
            } else {
                cl_person_credits.visibility = View.VISIBLE
            }

            if (it.cast.isNullOrEmpty()) {
                grp_cast.visibility = View.GONE
            } else {
                grp_cast.visibility = View.VISIBLE
                castList.clear()
                castList.addAll(it.cast)
                mCastAdapter?.notifyDataSetChanged()
            }

            if (it.crew.isNullOrEmpty()) {
                grp_crew.visibility = View.GONE
            } else {
                grp_crew.visibility = View.VISIBLE
                crewList.clear()
                crewList.addAll(it.crew)
                mCrewAdapter?.notifyDataSetChanged()
            }

        })

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