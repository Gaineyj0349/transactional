package com.gainwise.transactional.Activities

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.gainwise.transactional.POJO.BarChartData
import com.gainwise.transactional.R
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.levitnudi.legacytableview.LegacyTableView
import com.levitnudi.legacytableview.LegacyTableView.ODD
import kotlinx.android.synthetic.main.activity_label_summary.*
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.text.DateFormat




class LabelSummaryAct : AppCompatActivity() {


    lateinit var legacyTableView: LegacyTableView
    var overallTotalDeposit =  BigDecimal(0)
    var overallTotalPurchase= BigDecimal(0)
    var startTimeInMillis: Long = 0
    var endTimeInMillis: Long = 0
    var dataForBarChart = ArrayList<BarChartData>();
    var entriespurch= ArrayList<PieEntry>()
    var entriesdeposit= ArrayList<PieEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_label_summary)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (MainActivity.db == null) run { finish() } else {
            MAINRL1.visibility = View.GONE
            MAINRL2.visibility = View.GONE
            if (intent.hasExtra("all")) {
                supportActionBar?.title = getFirstAndLastString()
            } else if (intent.hasExtra("startMilli")) {
                supportActionBar?.title = getCustomString()
            } else {

            }


            getTotalOfPurchAndDeposit()
            if (intent.hasExtra("all")) getFirstAndLastString()
            if (intent.hasExtra("startMilli")) getCustomString()

            Log.i("JOSHnew", "caught: " + startTimeInMillis + endTimeInMillis)
            inputData()


            bottomNavView.setOnNavigationItemSelectedListener(
                    object : BottomNavigationView.OnNavigationItemSelectedListener {
                        override fun onNavigationItemSelected(item: MenuItem): Boolean {
                            when (item.getItemId()) {
                                R.id.barchartpurchase -> {
                                    chart2.animateXY(2000, 2000);
                                    MAINRL3.visibility = View.VISIBLE
                                    MAINRL1.visibility = View.GONE
                                    MAINRL2.visibility = View.GONE
                                }
                                R.id.barchartdeposit -> {
                                    chart.animateXY(2000, 2000);
                                    MAINRL2.visibility = View.VISIBLE
                                    MAINRL3.visibility = View.GONE
                                    MAINRL1.visibility = View.GONE
                                }
                                R.id.datatable -> {
                                    MAINRL2.visibility = View.GONE
                                    MAINRL3.visibility = View.GONE
                                    MAINRL1.visibility = View.VISIBLE
                                }
                                else -> {
                                }

                            }
                            return true
                        }
                    })

            var colorsP = ArrayList<Int>()
            var colorsD = ArrayList<Int>()
            var otherF = 0f
            var otherL = "Other"
            var otherFd = 0f



            for (d in dataForBarChart) {
                if (d.total > 0) {
                    if (d.percentage.toFloat() < 3) {
                        otherFd += d.percentage.toFloat()
                    } else {
                        Log.i("JOSHcall", "dep")
                        entriesdeposit.add(PieEntry(d.percentage.toFloat(), d.label))
                        colorsD.add(Integer.parseInt(MainActivity.db.TransactionDAO().getColorOfMain(d.label)))
                    }

                } else {

                    if (d.percentage.toFloat() < 3) {
                        otherF += d.percentage.toFloat()
                    } else {
                        entriespurch.add(PieEntry(d.percentage.toFloat(), d.label))
                        colorsP.add(Integer.parseInt(MainActivity.db.TransactionDAO().getColorOfMain(d.label)))
                    }


                }

            }
            if (otherF > 0) {
                entriespurch.add(PieEntry(otherF, otherL))
                colorsP.add(-2351361)
            }
            if (otherFd > 0) {
                entriesdeposit.add(PieEntry(otherFd, otherL))
                colorsD.add(-2351361)
            }


            var dataSetPurch = PieDataSet(entriespurch, "")
            var dataSetDeposit = PieDataSet(entriesdeposit, "")
            dataSetPurch.setColors(colorsP)
            dataSetPurch.valueTextColor = Color.WHITE
            dataSetPurch.setValueFormatter(PercentFormatter());
            dataSetDeposit.setValueFormatter(PercentFormatter());
            dataSetDeposit.setColors(colorsD)
            dataSetDeposit.valueTextColor = Color.WHITE
            dataSetPurch.valueTextSize = 15f
            dataSetPurch.sliceSpace = 5f
            dataSetPurch.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
            dataSetPurch.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
            dataSetPurch.valueLinePart2Length = .4f
            dataSetPurch.valueLinePart1Length = .2f
            dataSetDeposit.valueTextSize = 15f
            dataSetDeposit.sliceSpace = 5f
            dataSetDeposit.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
            dataSetDeposit.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
            dataSetDeposit.valueLinePart2Length = .4f
            dataSetDeposit.valueLinePart1Length = .2f
            var pieDataPurch = PieData(dataSetPurch)
            var pieDataDeposit = PieData(dataSetDeposit)
            chart2.data = pieDataPurch
            chart.data = pieDataDeposit
            chart2.isUsePercentValuesEnabled
            chart2.setEntryLabelColor(Color.WHITE)
            chart.isUsePercentValuesEnabled
            chart.setUsePercentValues(true)
            chart2.setUsePercentValues(true)
            chart.setEntryLabelColor(Color.WHITE)
            chart.setEntryLabelTextSize(0f)
            chart2.setEntryLabelTextSize(0f)
            chart2.animateXY(2000, 2000);


            val l = chart.legend
            l.isEnabled = true
            l.setFormSize(10f); // set the size of the legend forms/shapes
            l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
             l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
            l.setTextSize(15f);
            l.setTextColor(Color.BLACK);
            l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
            l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
            l.isWordWrapEnabled = true
            val l2 = chart2.legend
            l2.setFormSize(10f); // set the size of the legend forms/shapes
            l2.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
               l2.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
            l2.isWordWrapEnabled = true
            l2.setTextSize(15f);
            l2.setTextColor(Color.BLACK);
            l2.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
            l2.setYEntrySpace(5f); // set the space between the legend entries on the y-axis
            l2.isEnabled = true
            chart.getDescription().setEnabled(false);
            chart2.getDescription().setEnabled(false);
            chart.invalidate()
            chart2.invalidate()
        }
    }



    fun inputData() {
        Log.i("JOSHsum", "helper1")
        //set table title labels
        LegacyTableView.insertLegacyTitle("Label", "Count", "Total", "% In Type")
        //set table contents as string arrays
        //TODO this will change based on spinner
        fillTableAll()

        val legacyTableView = findViewById<View>(R.id.legacy_table_view) as LegacyTableView
        legacyTableView.setTitle(LegacyTableView.readLegacyTitle())
        legacyTableView.setContent(LegacyTableView.readLegacyContent())

        //depending on the phone screen size default table scale is 100
        //you can change it using this method
        //legacyTableView.setInitialScale(100);//default initialScale is zero (0)

        //if you want a smaller table, change the padding setting
        legacyTableView.setTablePadding(7)
        legacyTableView.setContentTextAlignment(LegacyTableView.TEXT_ALIGNMENT_INHERIT)
        legacyTableView.setContentTextSize(40)
        legacyTableView.setTitleTextSize(50)
        legacyTableView.setHighlight(ODD)


        //to enable users to zoom in and out:
        legacyTableView.setZoomEnabled(true)
        legacyTableView.setShowZoomControls(true)

        //remember to build your table as the last step
        legacyTableView.build()
    }

    fun fillTableAll() {


        var cursorMainWithCounts = MainActivity.db.TransactionDAO().STATgetCursorMainWithCounts(startTimeInMillis, endTimeInMillis)

              if (cursorMainWithCounts.moveToFirst() && cursorMainWithCounts != null && cursorMainWithCounts.getString(0)!= null) {
            do {
                LegacyTableView.insertLegacyContent(cursorMainWithCounts.getString(0), cursorMainWithCounts.getString(1),getTotalWithMain(cursorMainWithCounts.getString(0)),
                        getPercentage(getTotalWithMainNoSymbols(cursorMainWithCounts.getString(0)),isPurchaseFun(cursorMainWithCounts.getString(0)))+"%")
                dataForBarChart.add(BarChartData(cursorMainWithCounts.getString(0),
                        getPercentage2(getTotalWithMainNoSymbols(cursorMainWithCounts.getString(0)),isPurchaseFun(cursorMainWithCounts.getString(0))).toString(),
                        getTotalWithMain2(cursorMainWithCounts.getString(0)).toFloat()))
            }
        while (cursorMainWithCounts.moveToNext())
    }else{

              }
        cursorMainWithCounts.close()
    }

    private fun isPurchaseFun(string: String?): Int {
        var i: Int = 0
        var c = MainActivity.db.TransactionDAO().STATgetCursorAmountsAndIsPurchaseWithMain(string, startTimeInMillis, endTimeInMillis)
        if(c!=null && c.moveToFirst()){
            if(c.getInt(1)>0) i = 1
            else i = 0
        }
        c.close()
     return i
    }

    private fun getFirstAndLastString(): String {
        Log.i("JOSHnew", "all method")
        var c = MainActivity.db.TransactionDAO().STATFirstAndLast()
        var f: String
        var l: String

        if(c!=null && c.moveToFirst()&&(c.getString(0)!= null && c.getString(1)!= null)){
            Log.i("JOSHtime", c.getString(0))
            Log.i("JOSHtime", c.getString(1))
            f = c.getString(0)
            l = c.getString(1)
            startTimeInMillis = f.toLong()
            endTimeInMillis = l.toLong()

            f = DateFormat.getDateInstance().format(BigInteger(f))
            l = DateFormat.getDateInstance().format(BigInteger(l))
        }else{
            f = ""
            l = "NO DATA IN THIS DATE RANGE"}



        return " $f - $l"
    }

    private fun getCustomString(): String {
        Log.i("JOSHnew", "custom method")
        startTimeInMillis = intent.getLongExtra("startMilli", 0)
        endTimeInMillis = intent.getLongExtra("endMilli", 0)

        var c = MainActivity.db.TransactionDAO().STATcustomRangeFirstAndLast(startTimeInMillis, endTimeInMillis)
        var f: String
        var l: String
        if(c!=null && c.moveToFirst()&&(c.getString(0)!= null && c.getString(1)!= null)){
            f = c.getString(0)
            l = c.getString(1)
            f = DateFormat.getDateInstance().format(startTimeInMillis)
            l = DateFormat.getDateInstance().format(endTimeInMillis)
        }else{
            f = ""
            l = "NO DATA IN THIS DATE RANGE"}


        return " $f - $l"
    }



    fun getTotalWithMain(main: String): String{
        var total: String
        var c = MainActivity.db.TransactionDAO().STATgetCursorAmountsAndIsPurchaseWithMain(main, startTimeInMillis, endTimeInMillis)
        var tempNum = BigDecimal(0)
        var tempNumHelper = BigDecimal(0)
        if(c.moveToFirst() && c != null){
            do{
                if(c.getInt(1) > 0){
                    tempNumHelper = BigDecimal(c.getString(0))
                    tempNum = tempNum.minus(tempNumHelper)
                }else{
                    tempNumHelper = BigDecimal(c.getString(0))
                    tempNum = tempNum.add(BigDecimal(c.getString(0)))

                }


            }while (c.moveToNext())


        }
        if(tempNum.signum() < 0){
            total ="-" + MainActivity.getCurrencySymbol() + tempNum.abs().toString()
        }else{
            total =  "+" + MainActivity.getCurrencySymbol()+tempNum.toString()
        }
        return total
    }

    fun getTotalWithMain2(main: String): String{
        var total: String
        var c = MainActivity.db.TransactionDAO().STATgetCursorAmountsAndIsPurchaseWithMain(main, startTimeInMillis, endTimeInMillis)
        var tempNum = BigDecimal(0)
        var tempNumHelper = BigDecimal(0)
        if(c.moveToFirst() && c != null){
            do{
                if(c.getInt(1) > 0){
                    tempNumHelper = BigDecimal(c.getString(0))
                    tempNum = tempNum.minus(tempNumHelper)
                }else{
                    tempNumHelper = BigDecimal(c.getString(0))
                    tempNum = tempNum.add(BigDecimal(c.getString(0)))

                }


            }while (c.moveToNext())


        }
        if(tempNum.signum() < 0){
            total ="-"+tempNum.abs().toString()
        }else{
            total =  "+"+tempNum.toString()
        }
        return total
    }
    fun getTotalWithMainNoSymbols(main: String): String{
        var total: String
        var c = MainActivity.db.TransactionDAO().STATgetCursorAmountsAndIsPurchaseWithMain(main, startTimeInMillis, endTimeInMillis)
        var tempNum = BigDecimal(0)
        var tempNumHelper = BigDecimal(0)
        if(c.moveToFirst() && c != null){
            do{
                if(c.getInt(1) > 0){
                    tempNumHelper = BigDecimal(c.getString(0))
                    tempNum = tempNum.minus(tempNumHelper)
                }else{
                    tempNumHelper = BigDecimal(c.getString(0))
                    tempNum = tempNum.add(BigDecimal(c.getString(0)))

                }


            }while (c.moveToNext())


        }
        if(tempNum.signum() < 0){
            total =tempNum.abs().toString()
        }else{
            total =tempNum.toString()
        }
        return total
    }
    fun getTotalOfPurchAndDeposit(){
        Log.i("JOSHyo1", "Helper1")
        val c = MainActivity.db.TransactionDAO().STATgetCursorAmountsAndIsPurchaseOfAll(startTimeInMillis, endTimeInMillis)
        if(c!= null && c.moveToFirst()&&(c.getString(0)!= null && c.getString(1)!= null)){
            Log.i("JOSHy02", "count:" + c.count)
            do{
                if(c.getInt(1) > 0){
                    overallTotalPurchase += BigDecimal(c.getString(0))
                }else{
                    overallTotalDeposit += BigDecimal(c.getString(0))
                }
            }while(c.moveToNext())
        }
        Log.i("JOSHy02", overallTotalDeposit.toString())
        Log.i("JOSHy02", overallTotalPurchase.toString())
    }

    fun getPercentage(string: String, isPurchaseInt: Int): String{
        Log.i("JOSHyo1", "Helper2")
        var s: String
        when(isPurchaseInt){

            0->{s = BigDecimal(string).multiply(BigDecimal(100)).divide(overallTotalDeposit, 2, RoundingMode.HALF_UP).toString()}
            1->{s = BigDecimal(string).multiply(BigDecimal(100)).divide(overallTotalPurchase, 2, RoundingMode.HALF_UP).toString()}
            else->{s = "t"}
        }
        return s
    }

    fun getPercentage2(string: String, isPurchaseInt: Int): Float{
        Log.i("JOSHyo1", "Helper2")
        var s: String
        when(isPurchaseInt){

            0->{s = BigDecimal(string).multiply(BigDecimal(100)).divide(overallTotalDeposit, 2, RoundingMode.HALF_UP).toString()}
            1->{s = BigDecimal(string).multiply(BigDecimal(100)).divide(overallTotalPurchase, 2, RoundingMode.HALF_UP).toString()}
            else->{s = "t"}
        }
        return s.toFloat()
    }
    }
