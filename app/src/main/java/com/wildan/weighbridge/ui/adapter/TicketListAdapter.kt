package com.wildan.weighbridge.ui.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.wildan.weighbridge.core.model.TicketItem
import com.wildan.weighbridge.core.ui.base.BaseRecycleViewAdapter
import com.wildan.weighbridge.databinding.ItemTicketBinding
import com.wildan.weighbridge.ui.compose.TicketItem

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
        holder.binding(data, onTapTicket)
    }

    inner class ViewHolder(private val bind: ItemTicketBinding) :
        RecyclerView.ViewHolder(bind.root) {

        fun binding(data: TicketItem, onTapTicket: ((TicketItem) -> Unit)?) {
            bind.root.setContent {
                TicketItem(data){
                    onTapTicket?.invoke(data)
                }
            }
        }
    }

}
