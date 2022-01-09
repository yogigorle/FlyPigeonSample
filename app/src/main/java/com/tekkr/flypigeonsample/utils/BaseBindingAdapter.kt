package com.tekkr.flypigeonsample.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseBindingAdapter<T, B : ViewDataBinding>(
    private val layoutId: Int,
    private val diffCallBack: DiffUtil.ItemCallback<T>? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val defaultScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Default) }
    private val uiScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    var currentList = listOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return Holder<B>(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    fun onItemRangeMoved(callback: () -> Unit) {
        registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
                callback.invoke()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                callback.invoke()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                super.onItemRangeRemoved(positionStart, itemCount)
                callback.invoke()
            }
        })
    }

    abstract fun onBindViewHolder(holder: Holder<B>, position: Int)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (holder is Holder<*>) {
                onBindViewHolder(holder as Holder<B>, position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getItemAt(position: Int) = currentList[position]


    class Holder<B : ViewDataBinding>(val itemBinding: B) : RecyclerView.ViewHolder(itemBinding.root)

    open fun submitList(newList: List<T>?) {
        newList?.let {
            if (diffCallBack == null && currentList.isEmpty()) {
                currentList = newList
                notifyDataSetChanged()
            } else {
                //here we are doing list updation
                defaultScope.launch {
                    val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                        override fun getOldListSize(): Int {
                            return currentList.size
                        }

                        override fun getNewListSize(): Int {
                            return newList.size
                        }

                        override fun areItemsTheSame(
                            oldItemPosition: Int,
                            newItemPosition: Int
                        ) = try {
                            diffCallBack!!.areItemsTheSame(
                                currentList[oldItemPosition]!!, newList[newItemPosition]!!
                            )
                        } catch (e: Exception) {
                            false
                        }


                        override fun areContentsTheSame(
                            oldItemPosition: Int,
                            newItemPosition: Int
                        ) = try {
                            diffCallBack!!.areContentsTheSame(
                                currentList[oldItemPosition]!!, newList[newItemPosition]!!
                            )
                        } catch (e: Exception) {
                            false
                        }

                    })

                    uiScope.launch {
                        result.dispatchUpdatesTo(this@BaseBindingAdapter)
                        currentList = newList
                    }
                }

            }
        }
    }

}