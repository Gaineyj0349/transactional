package com.gainwise.transactional.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.gainwise.transactional.POJO.Transaction
import com.gainwise.transactional.R
import com.gainwise.transactional.Utilities.AdapterTransRV
import kotlinx.android.synthetic.main.activity_entire_history_wfilter.*
import java.util.*


class EntireHistoryWFilter : AppCompatActivity() {



    lateinit internal var adapter: RecyclerView.Adapter<*>
    lateinit internal var transactionList: List<Transaction>

    var mainLabelSelected: String? = null
    var subLabelSelected: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entire_history_wfilter)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (MainActivity.db == null) {
            finish()
        } else {
            var subList = ArrayList<String>()
            subList.add(0, "All Sub Labels")
            entire_dropdownviewsub.setDropDownListItem(subList)

            val mainList = MainActivity.db.TransactionDAO().fetchAllTransactionMainLabelsOnly()

            mainList.add(0, "All Main Labels")

            entire_dropdownviewmain.setDropDownListItem(mainList)
            entire_dropdownviewmain.setOnSelectionListener { view, position ->
                when (position) {
                    0 -> {
                        mainLabelSelected = null
                        var subList = ArrayList<String>()
                        subList.add(0, "All Sub Labels")
                        entire_dropdownviewsub.setDropDownListItem(subList)
                        entire_dropdownviewsub.selectingPosition = 0
                        entire_dropdownviewsub.setDropDownListItem(subList)
                        updateModeViews(true, false)
                    }

                    else -> {
                        mainLabelSelected = mainList.get(position)

                        var subList = MainActivity.db.TransactionDAO().fetchAllTransactionStringOnlySubLabelsOnlyWithMainLabel(mainLabelSelected)
                        subList.removeAt(0)
                        subList.add(0, "All Sub Labels")
                        entire_dropdownviewsub.setDropDownListItem(subList)
                        entire_dropdownviewsub.selectingPosition = 0
                        prepareRVmain(mainList.get(position))
                        updateSubMenu(subList)
                    }
                }
            }


            entire_rv.layoutManager = LinearLayoutManager(this)
            updateModeViews(true, false)
        }
    }

    private fun updateSubMenu(subList: List<String>) {


        entire_dropdownviewsub.setDropDownListItem(subList)
        entire_dropdownviewsub.setOnSelectionListener { view, position ->
            when (position) {
            0 -> {
                updateModeViews(false, false)
                subLabelSelected = null
            }
            else ->{
                subLabelSelected = subList.get(position)
                updateModeViews(false, true)
            }
        }}
    }

    private fun prepareRVmain(main: String?) {

        transactionList = MainActivity.db.TransactionDAO().fetchAllTransactionsWithMain(main)

        updateModeViews(false, false)


    }


    private fun updateModeViews(all: Boolean, subL: Boolean) {
        if (MainActivity.expenseTrackerOnlyMode()) {
            flipToExpenseTrackerOnlyMode(all,subL)
        } else {
            flipToExpenseTrackerWithAccountMode(all, subL)
        }
    }

    private fun flipToExpenseTrackerOnlyMode(all: Boolean, subL: Boolean) {
        if(all) {transactionList = MainActivity.db.TransactionDAO().allTransactions}
        else {
            if (subL) transactionList = MainActivity.db.TransactionDAO().allTransactionsWithBoth(mainLabelSelected, subLabelSelected)
            else if (!subL) transactionList = MainActivity.db.TransactionDAO().fetchAllTransactionsWithMain(mainLabelSelected)
        }

        adapter = AdapterTransRV(transactionList, this, true, true)
        entire_rv.adapter = adapter
    }

    private fun flipToExpenseTrackerWithAccountMode(all: Boolean, subL: Boolean) {
        Log.i("JOSHyoyo", "main: $mainLabelSelected sub: $subLabelSelected")
       if(all){
           transactionList = MainActivity.db.TransactionDAO().allTransactions
       }else{


        if(subL) {
            transactionList = MainActivity.db.TransactionDAO().allTransactionsWithBoth(mainLabelSelected, subLabelSelected)
        }
        else if(!subL)  transactionList = MainActivity.db.TransactionDAO().fetchAllTransactionsWithMain(mainLabelSelected)
       }
        adapter = AdapterTransRV(transactionList, this, false, true)
        entire_rv.adapter = adapter

    }



}
