package com.ivos.bcash.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivos.bcash.R
import com.ivos.bcash.databinding.ExpenseItemLayoutBinding
import com.ivos.bcash.data.model.Expense
import com.ivos.bcash.util.convertToGlobal

class ExpenseAdapter : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {


    private var context: Context? = null

    inner class ExpenseViewHolder(val binding: ExpenseItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val differCallBack = object : DiffUtil.ItemCallback<Expense>() {

            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem == newItem
            }
        }
    }

    val differ = AsyncListDiffer(this,differCallBack)



    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            itemText.text = item.tag
            itemCategory.text = item.type
            itemDate.text = item.date

            when(item.type) {
                context?.getString(R.string.income) -> {
                    itemAmount.setTextColor(
                        ContextCompat.getColor(itemAmount.context, R.color.lime_green)
                    )
                    itemAmount.text = "+ ".plus(convertToGlobal(item.amount))
                }
                context?.getString(R.string.expense)-> {
                    itemAmount.setTextColor(
                        ContextCompat.getColor(itemAmount.context, R.color.red)
                    )
                    itemAmount.text = "- ".plus(convertToGlobal(item.amount))
                }
            }

            when(item.tag) {
                context?.getString(R.string.housing) -> {
                    itemIcon.setImageResource(R.drawable.ic_housing)
                }
                context?.getString(R.string.transportation)-> {
                    itemIcon.setImageResource(R.drawable.ic_transport)
                }
                context?.getString(R.string.food) -> {
                    itemIcon.setImageResource(R.drawable.ic_food)
                }
                context?.getString(R.string.utilities) -> {
                    itemIcon.setImageResource(R.drawable.ic_utilities)
                }
                context?.getString(R.string.insurance) -> {
                    itemIcon.setImageResource(R.drawable.ic_insurance)
                }
                context?.getString(R.string.healthcare) -> {
                    itemIcon.setImageResource(R.drawable.ic_medical)
                }
                context?.getString(R.string.entertainment) -> {
                    itemIcon.setImageResource(R.drawable.ic_entertaiment)
                }
                else -> {
                    itemIcon.setImageResource(R.drawable.ic_income)
                }
            }

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(item) }
            }

            root.setOnLongClickListener {
                onItemLongClickListener?.invoke(item)
                true
            }
        }

    }

    private var onItemClickListener : ((Expense) -> Unit)? = null
    var onItemLongClickListener : ((Expense) -> Unit)? = null

    fun setOnClickListener(listener: (Expense) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ExpenseItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return ExpenseViewHolder(binding)
    }
}