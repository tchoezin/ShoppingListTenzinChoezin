package com.ait.shoppinglisttenzinchoezin.items_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.ait.shoppinglisttenzinchoezin.R
import com.ait.shoppinglisttenzinchoezin.activities.ScrollingActivity.Companion.KEY_ITEMS
import com.ait.shoppinglisttenzinchoezin.data.Items
import kotlinx.android.synthetic.main.items_details.view.*

class ViewItemsDialog: DialogFragment(){

    private lateinit var tvName: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDetail: TextView
    private lateinit var cbPurchase: CheckBox
    private lateinit var ivImageType: ImageView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.view_item_details))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.items_details, null
        )

        inputs(rootView)
        builder.setView(rootView)

        var items: Items = (arguments?.getSerializable(KEY_ITEMS) as Items)

        viewSetter(items)

        builder.setPositiveButton(getString(R.string.done)) {
                dialog, witch -> // empty
        }

        return builder.create()
    }

    private fun inputs(rootView: View) {
        tvName = rootView.tvItemName
        tvPrice = rootView.tvItemPrice
        tvDetail = rootView.tvItemDetails
        cbPurchase = rootView.cbPurchase
        ivImageType = rootView.ivIcon
    }

    private fun viewSetter(items: Items) {
        tvName.setText(getString(R.string.view_item_name) + items.itemsName)
        tvPrice.setText(getString(R.string.view_item_price) + items.itemsPrice)
        tvDetail.setText(getString(R.string.view_item_detail) + items.itemsDetails)
        cbPurchase.isChecked = items.purchased

        imageDecider(items)
    }

    private fun imageDecider(items: Items) {
        if (items.itemsCat == 0) {
            ivImageType.setImageResource(R.drawable.book_512)
        } else if (items.itemsCat == 1) {
            ivImageType.setImageResource(R.drawable.clothes_512)
        } else if (items.itemsCat == 2) {
            ivImageType.setImageResource(R.drawable.electronics_512)
        } else if (items.itemsCat == 3) {
            ivImageType.setImageResource(R.drawable.food)
        }
    }

}