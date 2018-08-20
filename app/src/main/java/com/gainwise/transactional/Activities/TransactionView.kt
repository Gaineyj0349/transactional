package com.gainwise.transactional.Activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.dekoservidoni.omfm.OneMoreFabMenu
import com.gainwise.transactional.Fragments.SettingsFragment
import com.gainwise.transactional.POJO.Transaction
import com.gainwise.transactional.R
import com.gainwise.transactional.R.id.resultingTV
import com.gainwise.transactional.Utilities.AdapterForFragBottomSheet
import com.skydoves.colorpickerpreference.ColorPickerView
import io.ghyeok.stickyswitch.widget.StickySwitch
import kotlinx.android.synthetic.main.activity_transaction_view.*
import org.michaelbel.bottomsheet.BottomSheet
import org.michaelbel.bottomsheet.BottomSheetCallback
import spencerstudios.com.fab_toast.FabToast
import java.math.BigDecimal


class TransactionView : AppCompatActivity() {

    var id: String? = null
    lateinit var t: Transaction
    lateinit var chooseDialog: Dialog
    lateinit var dialog: Dialog
    lateinit var color: String
    var fromEntire: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (MainActivity.db == null) run { finish() } else {
            if (intent.hasExtra("entire")) fromEntire = true

            if(MainActivity.expenseTrackerOnlyMode()){
                resultingTV.visibility = View.GONE
                viewTrans_tvbalance.visibility = View.GONE
            }




            id = intent.getStringExtra("id")
            supportActionBar?.setTitle("Transaction ID#: $id")

            t = MainActivity.db.TransactionDAO().getTransactionWithID(Integer.parseInt(id))
            supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.BLACK
            }
            val type = when (t.isPurchase) {
                true -> "EXPENSE"
                false -> "DEPOSIT"
            }
            val cash = when (t.fromCash) {
                "yes" -> " FROM CASH"
                "no" -> ""
                else -> ""
            }
            viewTrans_tvtype.setText(type + cash)

            viewTrans_mainbg.setBackgroundColor(Integer.parseInt(t.color1))
            viewTrans_tvdate.setText(t.date)
            viewTrans_tvmain.setText(t.mainLabel)
            viewTrans_tvsub.setText(t.subLabel)
            viewTrans_tvadditionalInfo.setText(t.additionalInfo)
            viewTrans_tvamount.setText(getCurrencyType() + t.amount)
            if(!MainActivity.expenseTrackerOnlyMode()){
                viewTrans_tvbalance.setText(getCurrencyType() + t.currentBalance)
                    }


            fab.setOptionsClick(object : OneMoreFabMenu.OptionsClick {
                override fun onOptionClick(integer: Int?) {

                    when (integer) {
                        R.id.main_option -> {
                            FabToast.makeText(applicationContext, "Edit Canceled", Toast.LENGTH_LONG,FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()
                        }
                        R.id.option1 -> editAmountClicked()
                        R.id.option2 -> editMainLabelClicked()
                        R.id.option3 -> editSubLabelClicked()
                        R.id.option4 -> editAdditionalInfoClicked()
                        R.id.option5 -> deleteTrans()
                        else -> FabToast.makeText(applicationContext, "error", Toast.LENGTH_LONG,
                                FabToast.ERROR, FabToast.POSITION_DEFAULT).show()
                    }
                }
            })
        }
    }

    fun deleteTrans() {
        var builder: AlertDialog.Builder
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = AlertDialog.Builder(this);

        }

        builder.setTitle("Confirm this Transaction Delete")
                .setMessage("This transaction fill be erased forever but will not effect the ending balance, continue?")
                .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogz: DialogInterface?, which: Int) {

    MainActivity.db.TransactionDAO().deleteTransactionWithID(t.id)
                        finish()
                    }
                })
                .setNegativeButton("NO", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }
                })
                .setIcon(android.R.drawable.stat_sys_warning)
                .show();

    }


    fun getCurrencyType(): String {

            val currencySymbol = MainActivity.prefs.getString(SettingsFragment.CURRENCY_SYMBOL, "$")
            return currencySymbol

    }


    fun editAmountClicked(){
        var showAlert: Boolean = true
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = layoutInflater.inflate(R.layout.dialog_amount_type_edit, null)

        val stickySwitch = view.findViewById<StickySwitch>(R.id.dialog_sticky)
        val cancelButton = view.findViewById<Button>(R.id.dialog_amount_type_cancel)
        val confirmButton = view.findViewById<Button>(R.id.dialog_amount_type_confirm)
        val editText = view.findViewById<EditText>(R.id.dialog_amount_type_et)

        cancelButton.setOnClickListener{dialog.dismiss()}
        if(t.isPurchase){
            stickySwitch.setDirection(StickySwitch.Direction.LEFT)
        }else{
            stickySwitch.setDirection(StickySwitch.Direction.RIGHT)
        }




        confirmButton.setOnClickListener {


            var startingNum = BigDecimal(t.amount)
            var amountToChange = BigDecimal(0)
            var plan = 0
            var wordChange = "hello world"
            var checkET = BigDecimal(editText.text.toString())
            if (!startingNum.equals(checkET)||((t.isPurchase && stickySwitch.getDirection() == StickySwitch.Direction.RIGHT)||
                            (!t.isPurchase && stickySwitch.getDirection() == StickySwitch.Direction.LEFT))) {
                when {
                    (!t.isPurchase) && (stickySwitch.getDirection() == StickySwitch.Direction.LEFT) -> {
                        amountToChange = BigDecimal(editText.text.toString()).add(startingNum)
                        plan = 1
                        wordChange = "subtract"
                    }
                    (t.isPurchase) && (stickySwitch.getDirection() == StickySwitch.Direction.RIGHT) -> {
                        amountToChange = BigDecimal(editText.text.toString()).add(startingNum)
                        plan = 2
                        wordChange = "add"
                    }
                    (!t.isPurchase) && (stickySwitch.getDirection() == StickySwitch.Direction.RIGHT) -> {
                        var etNum = BigDecimal(editText.text.toString())
                        if (etNum > startingNum) {
                            amountToChange = etNum.minus(startingNum)
                            plan = 3
                            wordChange = "add"
                        } else if (startingNum > etNum) {
                            amountToChange = startingNum - etNum
                            plan = 4
                            wordChange = "subtract"
                        }
                    }
                    (t.isPurchase) && (stickySwitch.getDirection() == StickySwitch.Direction.LEFT) -> {
                        var etNum = BigDecimal(editText.text.toString())
                        if (etNum > startingNum) {
                            amountToChange = etNum.minus(startingNum)
                            plan = 5
                            wordChange = "subtract"
                        } else if (startingNum > etNum) {
                            amountToChange = startingNum - etNum
                            plan = 6
                            wordChange = "add"
                        }
                    }

                    else -> {
                        Log.i("JOSHyoyo", "helper1")
                        FabToast.makeText(this, "No amount change detected, edit canceled.", Toast.LENGTH_LONG,
                                FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()
                    }


                }


                var builder: AlertDialog.Builder
                Log.i("JOSHplan", plan.toString())
                Log.i("JOSHplan", "amountToChange:" + amountToChange)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = AlertDialog.Builder(this);

                }
                var depositpurch: String = ""
                val amountInET = editText.text.toString()
                if (wordChange.equals("subtract")) depositpurch = "purchase"
                else if (wordChange.equals("add")) depositpurch = "deposit"

                builder.setTitle("Confirm this Transaction Edit")
                        .setMessage("Clicking yes will make this transaction a $depositpurch in the amount of " + getCurrencyType() + "$amountInET." +
                                " All post transactions will be adjusted accordingly to accept this new change" +
                                " (if applicable)")
                        .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                            override fun onClick(dialogz: DialogInterface?, which: Int) {
                                var allTransToChange = MainActivity.db.TransactionDAO().fetchAllTransactionsWithAmountsOnlyAbove(t.id - 1)
                                var newBalance: BigDecimal
                                var tempBalance: BigDecimal
                          //     var timeInMillis: Long = System.currentTimeMillis()

                                for (trans in allTransToChange) {
                              //      MainActivity.db.TransactionDAO().updateTimeInMillis(trans.id, timeInMillis)
                              //      MainActivity.db.TransactionDAO().updateTypeIncludeForStatsWithMain(trans.id)
                                    if (t.fromCash.equals("no")) {
                                        when (plan) {
                                            1 -> {
                                                tempBalance = BigDecimal(trans.currentBalance)
                                                newBalance = tempBalance.subtract(amountToChange)
                                                MainActivity.db.TransactionDAO().updateTransactionEndBalance(trans.id, newBalance.toString())

                                            }
                                            2 -> {
                                                tempBalance = BigDecimal(trans.currentBalance)
                                                newBalance = tempBalance.add(amountToChange)
                                                MainActivity.db.TransactionDAO().updateTransactionEndBalance(trans.id, newBalance.toString())
                                            }
                                            3 -> {
                                                tempBalance = BigDecimal(trans.currentBalance)
                                                newBalance = tempBalance.add(amountToChange)
                                                MainActivity.db.TransactionDAO().updateTransactionEndBalance(trans.id, newBalance.toString())
                                            }
                                            4 -> {
                                                tempBalance = BigDecimal(trans.currentBalance)
                                                newBalance = tempBalance.subtract(amountToChange)
                                                MainActivity.db.TransactionDAO().updateTransactionEndBalance(trans.id, newBalance.toString())
                                            }
                                            5 -> {
                                                tempBalance = BigDecimal(trans.currentBalance)
                                                newBalance = tempBalance.subtract(amountToChange)
                                                MainActivity.db.TransactionDAO().updateTransactionEndBalance(trans.id, newBalance.toString())
                                            }
                                            6 -> {
                                                tempBalance = BigDecimal(trans.currentBalance)
                                                newBalance = tempBalance.add(amountToChange)
                                                MainActivity.db.TransactionDAO().updateTransactionEndBalance(trans.id, newBalance.toString())
                                            }
                                            else -> {

                                            }
                                        }
                                    }
                                }


                                MainActivity.db.TransactionDAO().updateTransactionAmountWithID(t.id, editText.text.toString())
                                if (plan == 1 || plan == 2) {
                                    MainActivity.db.TransactionDAO().updateIsPurchaseWithID(t.id, !t.isPurchase)
                                }

                                dialog.dismiss()
                                recreate()

                            }
                        })
                        .setNegativeButton("NO", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();


            }else{ Log.i("JOSHyoyo", "helper2")
                FabToast.makeText(this, "No amount change detected, edit canceled.", Toast.LENGTH_LONG,
                        FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()}}



        //TODO calculate VERY IMPORTANT take into consideration "from cash"(dont want balances updating if they from cash - shall be a
        //TODO conditional in the for loop) and app mode to not affect balance



        dialog.setContentView(view)
        dialog.show()

        editText.setText(t.amount)
        editText.selectAll()
        editText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            editText.post {
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        editText.requestFocus()

    }

    fun editMainLabelClicked(){
        chooseDialog = Dialog(this)
        chooseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = layoutInflater.inflate(R.layout.dialog_choose_new_or_exist, null)
        val existButton = view.findViewById<Button>(R.id.dialog_choose_existing)
        val cancelButton = view.findViewById<Button>(R.id.dialog_choose_cancel)
        val newButton = view.findViewById<Button>(R.id.dialog_choose_new)

        cancelButton.setOnClickListener{chooseDialog.dismiss()}


        existButton.setOnClickListener {
            dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val bottomSheetViewAddNew = layoutInflater.inflate(R.layout.frag_purchase_dialog_pick, null)
            val button = bottomSheetViewAddNew.findViewById<Button>(R.id.frag_purchase_bottom_sheet_cancelButton)


            val rv = bottomSheetViewAddNew.findViewById<RecyclerView>(R.id.frag_purchase_bottom_sheet_rv)
            rv.layoutManager = LinearLayoutManager(this)
            button.setOnClickListener { dialog.dismiss() }
            val labels = removeDuplicatesForMain(MainActivity.db.TransactionDAO().fetchAllTransactionsLabels())

            val adapterForFragBottomSheet = AdapterForFragBottomSheet(this, labels, "main", "transview", id)
            rv.adapter = adapterForFragBottomSheet

            dialog.setContentView(bottomSheetViewAddNew)
            dialog.show()

        }
        newButton.setOnClickListener{showDialogForCreateNew("main")}

        chooseDialog.setContentView(view)
        chooseDialog.show()

    }

    fun editSubLabelClicked(){
        chooseDialog = Dialog(this)
        chooseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = layoutInflater.inflate(R.layout.dialog_choose_new_or_exist, null)
        val existButton = view.findViewById<Button>(R.id.dialog_choose_existing)
        val cancelButton = view.findViewById<Button>(R.id.dialog_choose_cancel)
        val newButton = view.findViewById<Button>(R.id.dialog_choose_new)

        var subLabels = MainActivity.db.TransactionDAO().fetchAllTransactionSubLabelsOnlyWithMainLabel(t.mainLabel)

        cancelButton.setOnClickListener{chooseDialog.dismiss()}
        existButton.setOnClickListener {
            if (subLabels == null || subLabels.size == 0) {
                FabToast.makeText(this, "There are no Sub Labels available with Main Label: ${t.mainLabel}, create one.", Toast.LENGTH_LONG,
                        FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()
            } else {

                dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                val bottomSheetViewAddNew = layoutInflater.inflate(R.layout.frag_purchase_dialog_pick, null)
                val button = bottomSheetViewAddNew.findViewById<Button>(R.id.frag_purchase_bottom_sheet_cancelButton)
                val rv = bottomSheetViewAddNew.findViewById<RecyclerView>(R.id.frag_purchase_bottom_sheet_rv)
                rv.layoutManager = LinearLayoutManager(this)
                button.setOnClickListener { dialog.dismiss() }
                val labels2 = MainActivity.db.TransactionDAO().fetchAllTransactionSubLabelsOnlyWithMainLabel(t.mainLabel)
                labels2.removeAt(0)
                val adapterForFragBottomSheet2 = AdapterForFragBottomSheet(this, labels2, "sub", "transview", id)
                rv.setAdapter(null)
                rv.setAdapter(adapterForFragBottomSheet2)

                dialog.setContentView(bottomSheetViewAddNew)
                dialog.show()
            }
        }
        newButton.setOnClickListener{
            showDialogForCreateNew("sub")
        }

        chooseDialog.setContentView(view)
        chooseDialog.show()

    }

    fun editAdditionalInfoClicked(){
         dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = layoutInflater.inflate(R.layout.frag_purchase_dialog_additional_info, null)
        val cancelButton = view.findViewById<Button>(R.id.frag_purchase_dialog_additional_info_cancel_button)
        val saveButton = view.findViewById<Button>(R.id.frag_purchase_dialog_additional_info_add_button)
        val et = view.findViewById<EditText>(R.id.frag_purchase_dialog_additional_info_et)

        saveButton.setOnClickListener{
            if(et.text.length>0){
                MainActivity.db.TransactionDAO().updateTransactionAdditionalInfoWithID(Integer.parseInt(id),et.text.toString())
               recreate()

            }
                    dialog.dismiss()


        }
        cancelButton.setOnClickListener{dialog.dismiss()}
        et.setText(t.additionalInfo)
        et.selectAll()
        et.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            et.post {
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        et.requestFocus()
        dialog.setContentView(view)
        dialog.show()
    }

    fun removeDuplicatesForMain(listIn: List<Transaction>): List<Transaction> {
        val list = ArrayList<Transaction>()
        val word = ArrayList<String>()

        for (t in listIn) {
                if (!word.contains(t.mainLabel)) {
                list.add(t)
            }
            word.add(t.mainLabel)
        }

        return list
    }


    fun closeDialogs() {
        dialog.dismiss()
        chooseDialog.dismiss()
    }

    private fun showDialogForCreateNew(type: String) {
     
             if (type == "sub") {
            color = MainActivity.db.TransactionDAO().getColorOfMain(t.mainLabel)
        } else {
            color = "-6035713"
        }

        val dialogNew = Dialog(this)
        dialogNew.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val mView = layoutInflater.inflate(R.layout.frag_purchase_dialog_create, null)

        val cancelButton = mView.findViewById<View>(R.id.frag_purchase_dialog_cancel_button) as Button
        val createButton = mView.findViewById<View>(R.id.frag_purchase_dialog_create_button) as Button
        val et = mView.findViewById<View>(R.id.frag_purchase_dialog_create_et) as EditText
        val colorButton = mView.findViewById<View>(R.id.frag_purchase_dialog_color_button) as Button
        val info = mView.findViewById(R.id.infoswitch) as ImageView
        val ll = mView.findViewById(R.id.ll1) as LinearLayout
        val switchy = mView.findViewById(R.id.frag_purchase_switch_include) as Switch


        info.setOnClickListener { initNewBuilder() }
        if (type == "sub") {
            ll.visibility = View.GONE
            colorButton.visibility = View.GONE
        } else {
            colorButton.setBackgroundColor(Integer.parseInt(color))

        }


        cancelButton.setOnClickListener {
            dialogNew.dismiss()
        }
        colorButton.setOnClickListener {
            val dialogColorPicker = Dialog(this)
            dialogColorPicker.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val viewColorPicker = layoutInflater.inflate(R.layout.dialog_color_picker, null)

            val CPsave = viewColorPicker.findViewById<View>(R.id.color_picker_save) as Button
            val CPcancel = viewColorPicker.findViewById<View>(R.id.color_picker_cancel) as Button
            val colorPickerView = viewColorPicker.findViewById<View>(R.id.colorPickerView) as ColorPickerView
            val relativeLayout = viewColorPicker.findViewById<View>(R.id.colorPickerBackground) as RelativeLayout

            colorPickerView.setColorListener { colorEnvelope ->
                color = colorEnvelope.color.toString()
                Log.i("JOSHcolor", color)
                //  relativeLayout.setBackgroundColor(Integer.parseInt(color));
                relativeLayout.setBackgroundColor(colorEnvelope.color)
            }
            CPsave.setOnClickListener {
                colorButton.setBackgroundColor(Integer.parseInt(color))
                dialogColorPicker.dismiss()
            }
            CPcancel.setOnClickListener { dialogColorPicker.dismiss() }

            dialogColorPicker.setContentView(viewColorPicker)
            dialogColorPicker.show()
        }
        createButton.setOnClickListener {
            if (et.text.length > 30) {
                FabToast.makeText(this, "The length must be less than 30 characters.", Toast.LENGTH_LONG,
                        FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()
            } else if (et.text.length == 0) {
                FabToast.makeText(this, "The length must not be empty.", Toast.LENGTH_LONG,
                        FabToast.WARNING, FabToast.POSITION_DEFAULT).show()

            } else {
                val label = et.text.toString().trim { it <= ' ' }

                val transNew = Transaction()
                transNew.color1 = color
                Log.i("JOSHcolor", color)
                if (type == "main") {
                    if (switchy.isChecked()) {
                        transNew.crossReferenceID = "1"
                    } else if(!switchy.isChecked()){
                        transNew.crossReferenceID = "0"
                    }
                    transNew.typeEntry = "info"
                    transNew.mainLabel = label

                    if (validate(type, label, transNew.mainLabel)) {
                        MainActivity.db.TransactionDAO().insertAll(transNew)
                        Log.i("JOSHtest", "this entered: " + transNew.crossReferenceID)
                        MainActivity.db.TransactionDAO().updateTransactionSubLabelWithID(Integer.parseInt(id), null)
                        MainActivity.db.TransactionDAO().updateTransactionMainLabelWithID(Integer.parseInt(id),label)
                        MainActivity.db.TransactionDAO().updateTransactionColorWithID(Integer.parseInt(id), color)
                        dialogNew.dismiss()
                        chooseDialog.dismiss()
                        recreate()
                        FabToast.makeText(this, "If applicable, the sub label has been reset upon the creation of a new Main Label. Create a new one with under Main Label.", Toast.LENGTH_LONG,
                                FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show()
                    } else {
                        FabToast.makeText(this, "This label already exists here.", Toast.LENGTH_LONG,
                                FabToast.WARNING, FabToast.POSITION_DEFAULT).show()

                    }
                }

                if (type == "sub") {
                    transNew.typeEntry = "info"
                    transNew.mainLabel = t.mainLabel
                    transNew.subLabel = label

                    if (validate(type, label, t.mainLabel)) {
                        MainActivity.db.TransactionDAO().insertAll(transNew)
                        MainActivity.db.TransactionDAO().updateTransactionSubLabelWithID(Integer.parseInt(id), label)
                        chooseDialog.dismiss()
                        dialogNew.dismiss()
                        recreate()
                      
                    } else {
                        FabToast.makeText(this, "This label already exists here.", Toast.LENGTH_LONG,
                                FabToast.WARNING, FabToast.POSITION_DEFAULT).show()

                    }
                }


            }
        }
        dialogNew.setContentView(mView)
        dialogNew.show()


    }

    private fun validate(type: String, label: String, mainLabelIn: String): Boolean {
        var ok = false

        if (type == "main") {
            val labels = MainActivity.db.TransactionDAO().fetchAllTransactionMainLabelsOnly()
            if (labels.contains(label)) {
                ok = false
            } else {
                ok = true
            }
        } else if (type == "sub") {
            val labels = MainActivity.db.TransactionDAO().fetchAllTransactionStringOnlySubLabelsOnlyWithMainLabel(mainLabelIn)
            if (labels.contains(label)) {
                ok = false
            } else {
                ok = true
            }
        }

        return ok
    }

override fun getSupportParentActivityIntent(): Intent {
    return getParentActivityIntentImpl();
}

    override fun getParentActivityIntent(): Intent {
    return getParentActivityIntentImpl();
}

fun getParentActivityIntentImpl(): Intent {

    var i: Intent
    if (fromEntire) {

        i = Intent(this, EntireHistoryWFilter::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

    } else {
        i = Intent(this, MainActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    return i;
}

    fun initNewBuilder() {
        val builder: BottomSheet.Builder
        builder = BottomSheet.Builder(this@TransactionView)


        builder.setWindowDimming(80)
                .setTitleMultiline(true)
                .setBackgroundColor(Color.parseColor("#fff8e1"))
                .setTitleTextColor(Color.parseColor("#263238"))


        builder.setOnShowListener { }
        builder.setOnDismissListener { }

        builder.setCallback(object : BottomSheetCallback {
            override fun onShown() {}

            override fun onDismissed() {}
        })

        builder.setTitle("Only change this setting if this label uses" +
                " money already accounted for. For example - savings/checking transfers. Transferring money to Savings then back to Checking " +
                "will result in duplicate deposits/withdrawals of the same money, which will affect percentages in statistics.")
        builder.show()
    }


}


