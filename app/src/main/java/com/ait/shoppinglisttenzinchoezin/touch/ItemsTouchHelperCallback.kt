package com.ait.shoppinglisttenzinchoezin.touch

interface ItemsTouchHelperCallback {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}