package com.wildan.weighbridge.ui.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wildan.weighbridge.R
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.ui.base.BaseRecycleViewAdapter
import com.wildan.weighbridge.core.ui.helper.textColorAndBackground
import com.wildan.weighbridge.databinding.ItemTicketBinding

/*
 * Created by Wildan Nafian on 17/05/24.
 * Github https://github.com/Wildanafian
 * wildanafian8@gmail.com
 */

class TicketListAdapter(fragment: Fragment) :
        BaseRecycleViewAdapter<TicketItem, TicketListAdapter.ViewHolder>(fragment) {

    companion object {

        const val FIRST_CHAR = 1
        const val ZERO = 0.0
    }

    var onTapTicket: ((TicketItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflateViewBinding(parent))
    }

    override fun getBindViewHolder(holder: ViewHolder, position: Int, data: TicketItem) {
        holder.binding(data)
        holder.itemView.setOnClickListener {
            onTapTicket?.invoke(data)
        }
    }

    inner class ViewHolder(private val bind: ItemTicketBinding) : RecyclerView.ViewHolder(bind.root) {

        fun binding(data: TicketItem) {
            bind.tvTicketId.text = data.id.drop(FIRST_CHAR)
            bind.tvDriverName.text = data.driverName
            bind.tvLicenseNumber.text = data.licenseNumber
            bind.tvNetWeight.text = bind.tvTicketStatus.context?.getString(
                R.string.set_tonnage, data.netWeight.toString()
            )
            bind.tvTicketDate.text = data.date
            bind.tvTicketTime.text = data.time

            if (data.outboundWeight == ZERO) {
                bind.tvTicketStatus.text = bind.tvTicketStatus.context?.getString(R.string.inbound)
                bind.tvTicketStatus.textColorAndBackground(R.color.yellow, R.drawable.bg_light_yellow_r6)

            } else {
                bind.tvTicketStatus.text = bind.tvTicketStatus.context?.getString(R.string.outbound)
                bind.tvTicketStatus.textColorAndBackground(R.color.primary, R.drawable.bg_light_green_r6)
            }
        }
    }

}
