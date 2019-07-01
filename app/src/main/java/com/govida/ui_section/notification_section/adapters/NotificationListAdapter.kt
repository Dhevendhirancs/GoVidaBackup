package com.govida.ui_section.notification_section.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.govida.R
import com.govida.database_section.AppDatabase
import com.govida.ui_section.notification_section.database.NotificationEntity

class NotificationListAdapter(var notificationList: MutableList<NotificationEntity>, var mContext: Context, var mAppDb: AppDatabase?) : androidx.recyclerview.widget.RecyclerView.Adapter<NotificationListAdapter.NotificationItemAdapter>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationItemAdapter {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_notification, parent, false)
        return NotificationItemAdapter(itemView)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder:NotificationItemAdapter, position: Int) {
        val sampleDataModel= notificationList[position]
        holder.tvTitle.text=sampleDataModel.notificationTitle
        holder.tvInfo.text=sampleDataModel.notificationInfo
        holder.tvReceivedAt.text=sampleDataModel.notificationReceivedAt

        if(sampleDataModel.notificationRead!!){
            holder.ivNotificationIcon.visibility=View.INVISIBLE
        }else{
            holder.ivNotificationIcon.visibility=View.VISIBLE
        }
    }

    inner class NotificationItemAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivNotificationIcon: ImageView = itemView.findViewById(R.id.notification_iv)
        var tvTitle: TextView = itemView.findViewById(R.id.notification_tv_title)
        var tvInfo: TextView = itemView.findViewById(R.id.notification_tv_description)
        var tvReceivedAt: TextView = itemView.findViewById(R.id.notification_tv_date)
        init {

        }
        override fun onClick(v: View?) {
        }



    }
}