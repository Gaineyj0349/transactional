package com.gainwise.transactional.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gainwise.transactional.Activities.MainActivity;
import com.gainwise.transactional.POJO.Transaction;
import com.gainwise.transactional.R;
import com.gainwise.transactional.Utilities.AdapterTransRV;

import java.util.List;

import static com.gainwise.transactional.Activities.MainActivity.getCurrencySymbol;

public class FragTransactions extends Fragment {


    View view = null;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Transaction> transactionList;
    TextView accountName;
    String account;
    //TODO get mode pref to set this bool


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_transactions, container, false);

            accountName = (TextView)view.findViewById(R.id.frag_trans_accountBanner);


            recyclerView = view.findViewById(R.id.frag_trans_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateModeViews();
        }
        return view;

    }

    public void setUserVisibleHint(boolean visible)
    {
        Log.i("Josh,","EXTRA1");
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            Log.i("Josh,","EXTRA2");
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }else{
            Log.i("Josh,","EXTRA3");
        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if (!getUserVisibleHint()) {
            Log.i("Josh,","EXTRA4");
            return;
        }else{
            //custom code here for when frag is on top
            updateModeViews();

            Log.i("Josh,","EXTRA5");

        }

    }

    public static String buildBanner() {
        if(MainActivity.db.TransactionDAO().getLastBalance() == null){
            return MainActivity.prefs.getString(SettingsFragment.ACCOUNT_KEY, "") + ": "+"TBD";

        }else{
            return MainActivity.prefs.getString(SettingsFragment.ACCOUNT_KEY, "") + ": "+getCurrencySymbol()+ MainActivity.db.TransactionDAO().getLastBalance();

        }    }
    private void updateModeViews(){
        if(MainActivity.expenseTrackerOnlyMode()){
            flipToExpenseTrackerOnlyMode();
        }else{
            flipToExpenseTrackerWithAccountMode();
        }
    }

    private void flipToExpenseTrackerOnlyMode(){
        accountName.setVisibility(View.GONE);
        transactionList = MainActivity.db.TransactionDAO().getRecentHistoryTransactions();
    //    Collections.reverse(transactionList);
        adapter = new AdapterTransRV(transactionList,getActivity(),true);
        recyclerView.setAdapter(adapter);
    }

    private void flipToExpenseTrackerWithAccountMode(){
        accountName.setVisibility(View.VISIBLE);
        accountName.setText(buildBanner());
        transactionList = MainActivity.db.TransactionDAO().getRecentHistoryTransactions();
     //   Collections.reverse(transactionList);
        adapter = new AdapterTransRV(transactionList,getActivity(),false);
        recyclerView.setAdapter(adapter);
    }

}
