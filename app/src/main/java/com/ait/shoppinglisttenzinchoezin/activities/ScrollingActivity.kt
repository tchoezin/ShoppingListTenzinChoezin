package com.ait.shoppinglisttenzinchoezin.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.ait.shoppinglisttenzinchoezin.ItemsAdapter.ItemsAdapter
import com.ait.shoppinglisttenzinchoezin.R
import com.ait.shoppinglisttenzinchoezin.data.AppDatabase
import com.ait.shoppinglisttenzinchoezin.data.Items
import com.ait.shoppinglisttenzinchoezin.items_dialog.ItemsDialog
import com.ait.shoppinglisttenzinchoezin.items_dialog.ViewItemsDialog
import com.ait.shoppinglisttenzinchoezin.touch.ItemsReyclerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class ScrollingActivity : AppCompatActivity(), ItemsDialog.ItemsHandler {

    companion object{
        const val KEY_ITEMS = "KEY_ITEMS"
        const val KEY_STARTED = "KEY_STARTED"
        const val TAG_ITEMS_DIALOG = "TAG_ITEMS_DIALOG"
        const val TAG_ITEMS_EDIT = "TAG_ITEMS_EDIT"
        const val TAG_ITEMS_DETAIL = "TAG_ITEMS_DETAIL"
    }

    lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)

        initRecyclerView()

        fab.setOnClickListener{
            showAddItemsDialog()
        }

        fabDeleteAll.setOnClickListener{
            itemsAdapter.deleteAllTodo()
        }

        if(!wasStartedBefore()) {
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setPrimaryText(getString(R.string.new_item))
                .setSecondaryText(getString(R.string.click_here_new_item))
                .show()
            saveWasStarted()
        }
    }

    fun saveWasStarted(){
        val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        var editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_STARTED, true)
        editor.apply()
    }

    fun  wasStartedBefore(): Boolean{
        var sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        return sharedPref.getBoolean(KEY_STARTED, false)
    }

    private fun initRecyclerView() {
        Thread {
            var itemsList =
                AppDatabase.getInstance(this@ScrollingActivity).itemsDao().getAllItems()

            runOnUiThread {
                itemsAdapter = ItemsAdapter(this, itemsList)
                recyclerItems.adapter = itemsAdapter

                var itemDecoration = DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
                recyclerItems.addItemDecoration(itemDecoration)

                val callback = ItemsReyclerTouchCallback(itemsAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerItems)
            }
        }.start()
    }

    fun showAddItemsDialog(){
        ItemsDialog().show(supportFragmentManager,
            TAG_ITEMS_DIALOG
        )
    }

    var editIndex: Int = -1

    fun showEditItemsDialog(itemsToEdit: Items, idx: Int){

        editIndex = idx

        val editDialog = ItemsDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEMS, itemsToEdit)
        editDialog.arguments = bundle
        editDialog.show(supportFragmentManager,
            TAG_ITEMS_EDIT
        )

    }

    var detailsIndex: Int = -1

    fun showDetailsItemsDialog(itemsToDetail: Items, idx: Int){

        detailsIndex = idx

        val detailsDialog = ViewItemsDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEMS, itemsToDetail)
        detailsDialog.arguments = bundle
        detailsDialog.show(supportFragmentManager,
            TAG_ITEMS_DETAIL
        )

    }

    fun saveItems(items:Items){
        Thread{
            var newID = AppDatabase.getInstance(this@ScrollingActivity).itemsDao().insertItems(items)

            items.itemsId = newID

            runOnUiThread{
                itemsAdapter.addItems(items)
            }
        }.start()
    }

    override fun itemsCreated(item: Items) {
        saveItems(item)
    }

    override fun itemsUpdated(item: Items) {
        Thread{
            AppDatabase.getInstance(this@ScrollingActivity).itemsDao().updateItems(item)

            runOnUiThread{
                itemsAdapter.updateItemsOnPosition(item, editIndex)
            }
        }.start()
    }

}
