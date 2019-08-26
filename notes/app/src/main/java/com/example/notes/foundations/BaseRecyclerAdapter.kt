package com.example.notes.foundations

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T>(
    protected val masterList: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun onItemDeleted(indexInList:Int, indexInView:Int){
        masterList.removeAt(indexInList)
        notifyItemRemoved(indexInView)
    }

    fun onItemUpdate(newItem:T, indexInList:Int, indexInView:Int){
        masterList[indexInList] = newItem
        notifyItemChanged(indexInView)
    }

    fun uiUpdate(list: List<T>) {
        masterList.clear()
        masterList.addAll(list)
        notifyDataSetChanged()
        /*val results = DiffUtil.calculateDiff(DiffUtilCallbackImpl(masterList,list))
        masterList.clear()
        masterList.addAll(list)
        results.dispatchUpdatesTo(this)*/
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) {
        TYPE_ADD_BUTTON
    } else {
        TYPE_INFO
    }

    override fun getItemCount(): Int = masterList.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddButtonViewHolder) {
            holder.onBind(Unit, position - 1)
        } else {
            (holder as BaseViewHolder<T>).onBind(masterList[position - 1], position - 1)
        }

    }

    abstract class BaseViewHolder<E>(val view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(data: E, listIndex: Int)
    }

    abstract class AddButtonViewHolder(view: View) : BaseViewHolder<Unit>(view)

    companion object {
        const val TYPE_ADD_BUTTON = 0
        const val TYPE_INFO = 1
    }

    class DiffUtilCallbackImpl<T>(val oldList:List<T>,val newList:List<T>): DiffUtil.Callback(){
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}