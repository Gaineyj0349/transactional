package com.gainwise.transactional.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;

import com.gainwise.transactional.Activities.EntireHistoryWFilter;
import com.gainwise.transactional.Activities.MainActivity;
import com.gainwise.transactional.POJO.Transaction;
import com.gainwise.transactional.R;
import com.gainwise.transactional.Utilities.AdapterTransRV;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;
import net.frederico.showtipsview.ShowTipsViewInterface;

import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

import static android.content.Context.MODE_PRIVATE;
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
            Button button = view.findViewById(R.id.frag_trans_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), EntireHistoryWFilter.class));
                    Bungee.slideDown(getActivity());
                }
            });
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
            SharedPreferences prefs = getActivity().getSharedPreferences("TRANS", MODE_PRIVATE);
            String restoredText = prefs.getString("FragTransactions", "yes");
            if (restoredText != null) {
                String name = prefs.getString("FragTransactions", "yes");//"No name defined" is the default value.
                if(name.equals("yes")){
                    showTip();
                }
            }

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
        adapter = new AdapterTransRV(transactionList,getActivity(),true, false);
        recyclerView.setAdapter(adapter);
    }

    private void flipToExpenseTrackerWithAccountMode(){
        accountName.setVisibility(View.VISIBLE);
        accountName.setText(buildBanner());
        transactionList = MainActivity.db.TransactionDAO().getRecentHistoryTransactions();
     //   Collections.reverse(transactionList);
        adapter = new AdapterTransRV(transactionList,getActivity(),false, false);
        recyclerView.setAdapter(adapter);
    }

    public void showTip(){
        ShowTipsView showtips = new ShowTipsBuilder(getActivity())
                .setTarget(recyclerView,500,500,500)
                .setTitle("The 50 most recent transactions")
                .setDescription("This scrollable list is your 50 latest transactions. Each item can be clicked to see the details and/or edit. You can also long press the item to quickly delete it if necessary" )
                .setDelay(500)
                .build();

        showtips.show(getActivity());
        showtips.setCallback(new ShowTipsViewInterface(){
            @Override
            public void gotItClicked() {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("TRANS", MODE_PRIVATE).edit();
                editor.putString("FragTransactions", "no");
                editor.apply();
            }
        });
    }

}
