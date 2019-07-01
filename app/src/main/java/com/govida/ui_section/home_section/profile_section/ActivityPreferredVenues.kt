/**
 * @Class : ActivityCheckin
 * @Usage : This activity is used for check in timer functionality.
 * @Author : 1769
 */
package com.govida.ui_section.home_section.profile_section

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.ui_section.base_class_section.BaseActivity
import com.govida.ui_section.home_section.challenges_section.rv_touch_listeners.RecyclerviewTouchListener
import com.govida.ui_section.home_section.checkin_section.adapters.VenueListAdapter
import com.govida.ui_section.home_section.checkin_section.model.VenueResponse
import com.govida.ui_section.home_section.checkin_section.mvp.VenueMVP
import com.govida.ui_section.home_section.checkin_section.mvp.VenuePresenterImplementer
import com.govida.ui_section.home_section.profile_section.adapter.PreferredVenueAdapter
import com.govida.ui_section.home_section.profile_section.item_decorator_for_list.SimpleDividerItemDecoration

class ActivityPreferredVenues : BaseActivity(), VenueMVP.VenueView {

    private lateinit var mVenuePresenter: VenueMVP.VenuePresenter
    private lateinit var mAppPreference: AppPreference
    private lateinit var mVenueListAdapter: PreferredVenueAdapter
    private var mVenueList: MutableList<VenueResponse.Data> = mutableListOf()
    private lateinit var mRvPreferredVenues: androidx.recyclerview.widget.RecyclerView
    private lateinit var mTvEmptyMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferred_venue)
        setupUI()
    }

    /**
     *  @Function : setupUI()
     *  @params   : void
     *  @Return   : void
     * 	@Usage	  : Setup listener and Initialise Ui component
     *  @Author   : 1769
     */
    private fun setupUI() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_settings)
        val toolbarTitle: TextView =toolbar.findViewById(R.id.title)
        toolbarTitle.text=getString(R.string.preferred_venue)
        toolbarTitle.textSize = resources.getDimension(R.dimen.s10sp)
        mAppPreference = AppPreference(this)
        mVenuePresenter = VenuePresenterImplementer(this, null)
        mVenueListAdapter= PreferredVenueAdapter(mVenueList, this)
        mRvPreferredVenues = findViewById(R.id.rv_preferred_venues)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mRvPreferredVenues.layoutManager = mLayoutManager
        mRvPreferredVenues.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        mRvPreferredVenues.adapter = mVenueListAdapter
        mRvPreferredVenues.addItemDecoration(SimpleDividerItemDecoration(this))
        mRvPreferredVenues.addOnItemTouchListener(
            RecyclerviewTouchListener(this, mRvPreferredVenues,
                object : RecyclerviewTouchListener.ClickListener {
                    override fun onLongClick(view: View?, position: Int) {
                    }
                    override fun onClick(view: View, position: Int) {
                    }
                })
        )
        mTvEmptyMsg = findViewById(R.id.tv_empty_msg)
        mVenuePresenter.onPreferredVenueRequested(mAppPreference.authorizationToken)
    }

    /**
     *  @Function : onPreferredVenueReceivedSuccesfully()
     *  @params   : venueList: List<VenueResponse.Data>?
     *  @Return   : void
     * 	@Usage	  : execute if preferred venue api successful
     *  @Author   : 1769
     */
    override fun onPreferredVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?) {
        if (venueList.isNullOrEmpty()) {
            mRvPreferredVenues.visibility = View.GONE
            mTvEmptyMsg.visibility = View.VISIBLE
        } else {
            mTvEmptyMsg.visibility = View.GONE
            mRvPreferredVenues.visibility = View.VISIBLE
            mVenueList.clear()
            mVenueList.addAll(venueList!!)
            mVenueListAdapter.notifyDataSetChanged()
        }
    }

    /**
     *  @Function : onPreferredVenueReceivedFailed()
     *  @params   : warnings: String
     *  @Return   : void
     * 	@Usage	  : execute if preferred venue api successful
     *  @Author   : 1769
     */
    override fun onPreferredVenueReceivedFailed(warnings: String) {
        mRvPreferredVenues.visibility = View.GONE
        mTvEmptyMsg.visibility = View.VISIBLE
        mTvEmptyMsg.text = warnings
    }

    override fun onVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?) {

    }

    override fun onVenueReceivedFailed(warnings: String) {

    }

    override fun onVerifiedVenueReceivedSuccesfully(venueList: List<VenueResponse.Data>?) {

    }

    override fun onVerifiedVenueReceivedFailed(warnings: String) {

    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String?) {

    }
}
