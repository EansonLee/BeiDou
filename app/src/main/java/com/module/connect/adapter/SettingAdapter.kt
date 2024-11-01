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
import com.module.connect.databinding.ItemFiveSetBinding
import com.module.connect.databinding.ItemFourSetBinding
import com.module.connect.databinding.ItemOneSetBinding
import com.module.connect.databinding.ItemSevenSetBinding
import com.module.connect.databinding.ItemSixSetBinding
import com.module.connect.databinding.ItemThreeSetBinding
import com.module.connect.databinding.ItemTwoSetBinding
import com.module.connect.holder.FiveItemSetHolder
import com.module.connect.holder.FourItemSetHolder
import com.module.connect.holder.OneItemSetHolder
import com.module.connect.holder.SevenItemSetHolder
import com.module.connect.holder.SixItemSetHolder
import com.module.connect.holder.ThreeItemSetHolder
import com.module.connect.holder.TwoItemSetHolder

class SettingAdapter(private val itemClick: (CommandBean) -> Unit) :
    ListAdapter<CommandBean, RecyclerView.ViewHolder>(RecordDiffCallback()) {

    class RecordDiffCallback : DiffUtil.ItemCallback<CommandBean>() {

        override fun areItemsTheSame(oldItem: CommandBean, newItem: CommandBean): Boolean {
            return oldItem.style == newItem.style
        }

        override fun areContentsTheSame(
            oldItem: CommandBean,
            newItem: CommandBean
        ): Boolean {
            return oldItem.name == newItem.name && oldItem.command == newItem.command
        }

    }

    init {
        setHasStableIds(true)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            STYLE_ONE -> {
                OneItemSetHolder(
                    ItemOneSetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            STYLE_TWO -> {
                TwoItemSetHolder(
                    ItemTwoSetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            STYLE_THIRD -> {
                ThreeItemSetHolder(
                    ItemThreeSetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            STYLE_FOUR -> {
                FourItemSetHolder(
                    ItemFourSetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            STYLE_FIVE -> {
                FiveItemSetHolder(
                    ItemFiveSetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            STYLE_SIX -> {
                SixItemSetHolder(
                    ItemSixSetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                SevenItemSetHolder(
                    ItemSevenSetBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OneItemSetHolder -> holder.render(getItem(position), itemClick)
            is TwoItemSetHolder -> holder.render(getItem(position), itemClick)
            is ThreeItemSetHolder -> holder.render(getItem(position), itemClick)
            is FourItemSetHolder -> holder.render(getItem(position), itemClick)
            is FiveItemSetHolder -> holder.render(getItem(position), itemClick)
            is SixItemSetHolder -> holder.render(getItem(position), itemClick)
            is SevenItemSetHolder -> holder.render(getItem(position), itemClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).style
    }
}