/**
 * @Class : LeaderboardListAdapter
 * @Usage : Adapter to render the leaderboard data in recyclerview
 * @Author : 1769
 */
package com.govida.ui_section.leaderboard_section.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.govida.R
import com.govida.app_sharedpreference.AppPreference
import com.govida.database_section.AppDatabase
import com.govida.ui_section.leaderboard_section.database.LeaderboardEntity
import com.govida.ui_section.leaderboard_section.model.AdapterObject
import com.govida.ui_section.leaderboard_section.model.LeaderboardResponse

class LeaderboardListAdapter(private var leaderboardList: MutableList<AdapterObject>, var mContext: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<LeaderboardListAdapter.LeaderboardItemAdapter>() {

    /**
     *  @Function : onCreateViewHolder()
     *  @params   : parent: ViewGroup, viewType: Int
     *  @Return   : LeaderboardItemAdapter
     * 	@Usage	  : Creating view for every list data
     *  @Author   : 1769
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardItemAdapter {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_leaderboard, parent, false)
        return LeaderboardItemAdapter(itemView)
    }

    /**
     *  @Function : getItemCount()
     *  @params   : void
     *  @Return   : Int
     * 	@Usage	  : Used to find the list size which are going to render in recycler view
     *  @Author   : 1769
     */
    override fun getItemCount(): Int {
        return leaderboardList.size
    }

    /**
     *  @Function : onBindViewHolder()
     *  @params   : holder: LeaderboardItemAdapter, position: Int
     *  @Return   : void
     * 	@Usage	  : Binding data to the recycler view
     *  @Author   : 1769
     */
    override fun onBindViewHolder(holder:LeaderboardItemAdapter, position: Int) {
        var appPreference: AppPreference = AppPreference(mContext)
        val sampleDataModel= leaderboardList[position]
        holder.tvName.text=sampleDataModel.employeeName
        holder.tvSteps.text=sampleDataModel.stepsWalked.toString() + " Steps"
        holder.tvDistance.text=" (" + sampleDataModel.distanceTravelled.toString() + "km)"
        if (appPreference.employeeId == sampleDataModel.employeeId) {
            holder.ivPosition.setImageResource(R.drawable.leaderboard_position_colored)
            holder.tvPosition.text= sampleDataModel.rank.toString()
            if (Build.VERSION.SDK_INT >= 23){
                holder.tvPosition.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
            }
            else{
                holder.tvPosition.setTextColor(mContext.getResources().getColor(R.color.colorWhite))
            }
            holder.tvName.text = appPreference.userFirstName
            setProfilePicture(holder)
        } else {
            holder.ivPosition.setImageResource(R.drawable.leaderboard_position)
            holder.tvPosition.text= sampleDataModel.rank.toString()
            if (Build.VERSION.SDK_INT >= 23){
                holder.tvPosition.setTextColor(ContextCompat.getColor(mContext, R.color.text_subtext))
            }
            else{
                holder.tvPosition.setTextColor(mContext.getResources().getColor(R.color.text_subtext))
            }
            holder.ivCurrentUserPicture.setImageResource(R.drawable.profile_pic_3)
        }
    }

    /**
     *  @Function : setProfilePicture()
     *  @params   : holder:LeaderboardItemAdapter
     *  @Return   : void
     * 	@Usage	  : Used to set the user profile picture from URL
     *  @Author   : 1276
     */
    private fun setProfilePicture(holder:LeaderboardItemAdapter) {
        var appPreference = AppPreference(mContext)
        if (appPreference.profilePicturePath.isNotEmpty()) {
            Glide.with(mContext).load(appPreference.profilePicturePath).into(holder.ivCurrentUserPicture)
        } else {
            holder.ivCurrentUserPicture.setImageResource(R.drawable.profile_pic_3)
        }
    }

    /**
     *  @Class : LeaderboardItemAdapter
     * 	@Usage	  : Initializing the list ui
     *  @Author   : 1769
     */
    inner class LeaderboardItemAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var tvSteps: TextView = itemView.findViewById(R.id.tv_steps)
        var tvDistance: TextView = itemView.findViewById(R.id.tv_distance)
        var tvPosition: TextView = itemView.findViewById(R.id.tv_position)
        var ivPosition: ImageView = itemView.findViewById(R.id.iv_position)
        var ivCurrentUserPicture: ImageView = itemView.findViewById(R.id.iv_current_user)
        init {

        }
        override fun onClick(v: View?) {
        }
    }
}