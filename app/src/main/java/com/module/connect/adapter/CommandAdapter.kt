package com.module.connect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.bean.STYLE_FIVE
import com.module.connect.bean.STYLE_FOUR
import com.module.connect.bean.STYLE_ONE
import com.module.connect.bean.STYLE_SIX
import com.module.connect.bean.STYLE_THIRD
import com.module.connect.bean.STYLE_TWO
import com.module.connect.databinding.ItemFiveBinding
import com.module.connect.databinding.ItemFourBinding
import com.module.connect.databinding.ItemOneBinding
import com.module.connect.databinding.ItemSevenBinding
import com.module.connect.databinding.ItemSixBinding
import com.module.connect.databinding.ItemThreeBinding
import com.module.connect.databinding.ItemTwoBinding
import com.module.connect.holder.FiveItemHolder
import com.module.connect.holder.FourItemHolder
import com.module.connect.holder.OneItemHolder
import com.module.connect.holder.SevenItemHolder
import com.module.connect.holder.SixItemHolder
import com.module.connect.holder.ThreeItemHolder
import com.module.connect.holder.TwoItemHolder

class CommandAdapter(private val itemClick: (CommandBean) -> Unit) :
    ListAdapter<CommandBean, RecyclerView.ViewHolder>(RecordDiffCallback()) {

    class RecordDiffCallback : DiffUtil.ItemCallback<CommandBean>() {

        override fun areItemsTheSame(oldItem: CommandBean, newItem: CommandBean): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CommandBean,
            newItem: CommandBean
        ): Boolean {
            return oldItem.name == newItem.name && oldItem.command == newItem.command
                    && oldItem.res == newItem.res
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == STYLE_ONE) {
            OneItemHolder(
                ItemOneBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == STYLE_TWO) {
            TwoItemHolder(
                ItemTwoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == STYLE_THIRD) {
            ThreeItemHolder(
                ItemThreeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == STYLE_FOUR) {
            FourItemHolder(
                ItemFourBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == STYLE_FIVE) {
            FiveItemHolder(
                ItemFiveBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == STYLE_SIX) {
            SixItemHolder(
                ItemSixBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            SevenItemHolder(
                ItemSevenBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OneItemHolder -> holder.render(getItem(position), itemClick)
            is TwoItemHolder -> holder.render(getItem(position), itemClick)
            is ThreeItemHolder -> holder.render(getItem(position), itemClick)
            is FourItemHolder -> holder.render(getItem(position), itemClick)
            is FiveItemHolder -> holder.render(getItem(position), itemClick)
            is SixItemHolder -> holder.render(getItem(position), itemClick)
            is SevenItemHolder -> holder.render(getItem(position), itemClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).style
    }
}