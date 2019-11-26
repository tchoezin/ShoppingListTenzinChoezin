package com.ait.shoppinglisttenzinchoezin.ItemsAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ait.shoppinglisttenzinchoezin.R
import com.ait.shoppinglisttenzinchoezin.activities.ScrollingActivity
import com.ait.shoppinglisttenzinchoezin.data.AppDatabase
import com.ait.shoppinglisttenzinchoezin.data.Items
import com.ait.shoppinglisttenzinchoezin.touch.ItemsTouchHelperCallback
import kotlinx.android.synthetic.main.items_row.view.*
import java.util.*


class ItemsAdapter: RecyclerView.Adapter<ItemsAdapter.ViewHolder>, ItemsTouchHelperCallback {

    var itemsList = mutableListOf<Items>()

    val context: Context

    constructor(context: Context, listTodos:List<Items>){
        this.context = context

        itemsList.addAll(listTodos)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemsRow = LayoutInflater.from(context).inflate(
            R.layout.items_row, parent, false
        )

        return ViewHolder(itemsRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var items = itemsList.get(holder.adapterPosition)



        holderAssigner(holder, items)


        pressDelete(holder)

        pressCBPur(holder, items)

        pressEdit(holder, items)

        pressDetails(holder, items)
    }

    private fun pressDetails(
        holder: ViewHolder,
        items: Items
    ) {
        holder.btnItemDetails.setOnClickListener {
            (context as ScrollingActivity).showDetailsItemsDialog(
                items, holder.adapterPosition
            )
        }
    }

    private fun pressEdit(
        holder: ViewHolder,
        items: Items
    ) {
        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditItemsDialog(
                items, holder.adapterPosition
            )
        }
    }

    private fun pressCBPur(
        holder: ViewHolder,
        items: Items
    ) {
        holder.cbEditPurchased.setOnClickListener {
            items.purchased = holder.cbEditPurchased.isChecked
            updateItems(items)
        }
    }

    private fun pressDelete(holder: ViewHolder) {
        holder.btnDelete.setOnClickListener {
            deleteItems(holder.adapterPosition)
        }
    }

    private fun holderAssigner(
        holder: ViewHolder,
        items: Items
    ) {
        holder.tvItemName.text = items.itemsName
        holder.tvItemPrice.text = items.itemsPrice
        holder.cbEditPurchased.isChecked = items.purchased

        iconDecider(items, holder)
    }

    private fun iconDecider(
        items: Items,
        holder: ViewHolder
    ) {
        if (items.itemsCat == 0) {
            holder.ivIcon.setImageResource(R.drawable.book_512)
        } else if (items.itemsCat == 1) {
            holder.ivIcon.setImageResource(R.drawable.clothes_512)
        } else if (items.itemsCat == 2) {
            holder.ivIcon.setImageResource(R.drawable.electronics_512)
        } else if (items.itemsCat == 3) {
            holder.ivIcon.setImageResource(R.drawable.food)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun updateItems(items:Items) {
        Thread{
            AppDatabase.getInstance(context).itemsDao().updateItems(items)
        }.start()
    }

    fun deleteItems(index: Int) {
        Thread{
            AppDatabase.getInstance(context).itemsDao().deleteItems(itemsList[index])

            (context as ScrollingActivity).runOnUiThread{
                itemsList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun updateItemsOnPosition(items: Items, index: Int){
        itemsList.set(index, items)
        notifyItemChanged(index)
    }

    fun deleteAllTodo(){
        Thread{
            AppDatabase.getInstance(context).itemsDao().deleteAllItems()

            (context as ScrollingActivity).runOnUiThread{
                itemsList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addItems(items: Items) {
        itemsList.add(items)
        notifyItemInserted(itemsList.lastIndex)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(itemsList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onDismissed(position: Int) {
        deleteItems(position)
    }

    inner class ViewHolder(objectView: View) : RecyclerView.ViewHolder(objectView){

        val ivIcon = objectView.ivIcon
        val tvItemName = objectView.tvItemName
        val tvItemPrice = objectView.tvItemPrice
        val cbEditPurchased = objectView.cbEditPurchase
        val btnDelete = objectView.btnDelete
        val btnEdit = objectView.btnEdit
        val btnItemDetails = objectView.btnItemDetails


    }



}