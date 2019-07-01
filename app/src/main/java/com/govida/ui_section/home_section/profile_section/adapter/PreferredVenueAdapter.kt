package com.govida.ui_section.home_section.profile_section.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.govida.R
import com.govida.ui_section.home_section.checkin_section.model.VenueResponse


class PreferredVenueAdapter(var venueList: List<VenueResponse.Data>, var mContext: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<PreferredVenueAdapter.VenueItemAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueItemAdapter {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_venue, parent, false)
        return VenueItemAdapter(itemView)
    }

    override fun getItemCount(): Int {
        return venueList.size
    }

    override fun onBindViewHolder(holder:VenueItemAdapter, position: Int) {
        val sampleDataModel= venueList[position]
        if (Build.VERSION.SDK_INT >= 23){
            holder.tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black_effective))
        }
        else{
            holder.tvTitle.setTextColor(mContext.resources.getColor(R.color.black_effective))
        }
        holder.tvTitle.text=sampleDataModel.venueName
        holder.tvAddress.text=sampleDataModel.address
        if (sampleDataModel.govidaVerified!!) {
            holder.tvVerified.text = mContext.getString(R.string.verified)
            if (Build.VERSION.SDK_INT >= 23){
                holder.tvVerified.setBackgroundColor(ContextCompat.getColor(mContext, R.color.notification_color))
            }
            else{
                holder.tvVerified.setBackgroundColor(mContext.resources.getColor(R.color.notification_color))
            }
//            holder.ivNonVerified.visibility = View.GONE
        } else {
            holder.tvVerified.text = mContext.getString(R.string.non_verified)
//            holder.ivNonVerified.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= 23){
                holder.tvVerified.setBackgroundColor(ContextCompat.getColor(mContext, R.color.text_subtext))
            }
            else{
                holder.tvVerified.setBackgroundColor(mContext.resources.getColor(R.color.text_subtext))
            }
        }
    }

    inner class VenueItemAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_gym_name)
        var tvAddress: TextView = itemView.findViewById(R.id.tv_gym_address)
        var tvVerified: TextView = itemView.findViewById(R.id.tv_verified)
//        var ivNonVerified: ImageView = itemView.findViewById(R.id.iv_non_verified)
    }
}