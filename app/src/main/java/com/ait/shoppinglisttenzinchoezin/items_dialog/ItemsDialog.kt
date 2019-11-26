package com.ait.shoppinglisttenzinchoezin.items_dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.ait.shoppinglisttenzinchoezin.R
import com.ait.shoppinglisttenzinchoezin.activities.ScrollingActivity.Companion.KEY_ITEMS
import com.ait.shoppinglisttenzinchoezin.data.Items
import kotlinx.android.synthetic.main.new_shopping_list_item.view.*

class ItemsDialog : DialogFragment(){

    interface ItemsHandler {
        fun itemsCreated(items: Items)
        fun itemsUpdated(items: Items)
    }

    private lateinit var itemsHandler: ItemsHandler

    override fun onAttach(context: Context) {
        super.onAttach(context!!)

        if (context is ItemsHandler) {
            itemsHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.items_handler_interface_error))
        }
    }

    private lateinit var etName: EditText
    private lateinit var spinnerItems: Spinner
    private lateinit var etEstimPrice: EditText
    private lateinit var etDesc: EditText
    private lateinit var cbPurchased: CheckBox


    var isEditMode = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_item))

        isEditMode = ((arguments != null) && arguments!!.containsKey(KEY_ITEMS))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_shopping_list_item, null
        )

        inputs(rootView, builder)


        spinnerAdapter()

        editMode(builder)

        builder.setPositiveButton(getString(R.string.ok)) {
                dialog, witch -> // empty
        }

        return builder.create()
    }

    private fun inputs(rootView: View, builder: AlertDialog.Builder) {
        etName = rootView.etName
        spinnerItems = rootView.spinnerItems
        etEstimPrice = rootView.etEstimPrice
        etDesc = rootView.etDesc
        cbPurchased = rootView.cbPurchased
        builder.setView(rootView)
    }

    private fun spinnerAdapter() {
        var itemsAdapter = ArrayAdapter.createFromResource(
            context!!,
            R.array.items_array,
            android.R.layout.simple_spinner_item
        )
        itemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerItems.adapter = itemsAdapter
    }

    private fun editMode(builder: AlertDialog.Builder) {
        if (isEditMode) {
            builder.setTitle(getString(R.string.edit_items))
            var items: Items = (arguments?.getSerializable(KEY_ITEMS) as Items)

            etName.setText(items.itemsName)
            spinnerItems.setSelection(items.itemsCat)
            etEstimPrice.setText(items.itemsPrice)
            etDesc.setText(items.itemsDetails)
            cbPurchased.isChecked = items.purchased
        }
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etName.text.isNotEmpty() && etEstimPrice.text.isNotEmpty()
                && etDesc.text.isNotEmpty()) {
                if(isEditMode){
                    handleItemsEdit()
                } else{
                    handleItemsCreate()
                }
                dialog?.dismiss()
            } else {
                emptyField()
            }
        }
    }

    private fun emptyField() {
        if (etName.text.isEmpty()) {
            etName.error = getString(R.string.empty_error)
        }
        if (etEstimPrice.text.isEmpty()) {
            etEstimPrice.error = getString(R.string.empty_error)
        }
        if (etDesc.text.isEmpty()) {
            etDesc.error = getString(R.string.empty_error)
        }
    }

    private fun handleItemsCreate() {
        itemsHandler.itemsCreated(
            Items(
                null,
                spinnerItems.selectedItemPosition,
                etName.text.toString(),
                cbPurchased.isChecked,
                etEstimPrice.text.toString(),
                etDesc.text.toString()
            )
        )
    }

    private fun handleItemsEdit() {
        val itemsToEdit = arguments?.getSerializable(
            KEY_ITEMS
        ) as Items
        itemsToEdit.itemsCat = spinnerItems.selectedItemPosition
        itemsToEdit.itemsName = etName.text.toString()
        itemsToEdit.itemsPrice = etEstimPrice.text.toString()
        itemsToEdit.itemsDetails = etDesc.text.toString()

        itemsHandler.itemsUpdated(itemsToEdit)
    }

}

