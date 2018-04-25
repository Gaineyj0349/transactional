package com.gainwise.transactional.Utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gainwise.transactional.Activities.MainActivity;
import com.gainwise.transactional.Fragments.SettingsFragment;
import com.gainwise.transactional.POJO.Transaction;
import com.gainwise.transactional.R;

import org.michaelbel.bottomsheet.BottomSheet;
import org.michaelbel.bottomsheet.BottomSheetCallback;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class AdapterTransRV extends RecyclerView.Adapter<AdapterTransRV.MyHolder> {
    BottomSheet.Builder builder;

    List<Transaction> transactionList;
    Context context;
    boolean expenseTrackerOnly = false;
    public AdapterTransRV(List<Transaction> transactionList, Context context, boolean expenseTrackerOnly) {
        this.context = context;
        this.transactionList = transactionList;
        this.expenseTrackerOnly = expenseTrackerOnly;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transactions_main,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
       if(expenseTrackerOnly){
           holder.remainingBalanceTV.setVisibility(View.GONE);
           holder.card_balancetvstatic.setVisibility(View.GONE);
       }else{
           holder.remainingBalanceTV.setVisibility(View.VISIBLE);
                   holder.card_balancetvstatic.setVisibility(View.VISIBLE);

       }

        boolean negativeBalance;
        if(transactionList.get(position).isPurchase()){
            holder.amountTV.setTextColor(context.getResources().getColor(R.color.negativeMoney));
        }else{
            holder.amountTV.setTextColor(context.getResources().getColor(R.color.positiveMoney));
        }
        BigDecimal bigDecimal = new BigDecimal(transactionList.get(position).getCurrentBalance());
        if(bigDecimal.signum() < 0){
            holder.remainingBalanceTV.setTextColor(context.getResources().getColor(R.color.negativeMoney));
            negativeBalance = true;
        }else{
            negativeBalance = false;
            holder.remainingBalanceTV.setTextColor(context.getResources().getColor(R.color.positiveMoney));
        }

        if(transactionList.get(position).getColor1() != null){
            holder.bg.setBackgroundColor(Integer.parseInt(transactionList.get(position).getColor1()));
        }



        holder.additionalInfoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNewBuilder(transactionList.get(position).getAdditionalInfo());
            }
        });
        if(transactionList.get(position).isPurchase()){
            holder.amountTV.setText("-" +getCurrencyType()+transactionList.get(position).getAmount());
        }else{
            holder.amountTV.setText("+" +getCurrencyType()+transactionList.get(position).getAmount());
        }

        holder.dateTV.setText(transactionList.get(position).getDate());
        holder.mainLabelTV.setText(transactionList.get(position).getMainLabel());
        holder.subLayerTV.setText(transactionList.get(position).getSubLabel());
        if(negativeBalance){
            BigInteger temp = new BigInteger(transactionList.get(position).getCurrentBalance());
            temp.abs();
            holder.remainingBalanceTV.setText(getCurrencyType()+"("+temp.abs()+")");

        }else{
            holder.remainingBalanceTV.setText(getCurrencyType()+transactionList.get(position).getCurrentBalance());
        }


    }

    @Override
    public int getItemCount() {
        if(transactionList == null){
            return 0;
        }else {
            return transactionList.size();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public TextView amountTV;
        public TextView mainLabelTV;
        public TextView subLayerTV;
        public ImageView additionalInfoImageView;
        public TextView remainingBalanceTV;
        public TextView dateTV;
        public TextView card_balancetvstatic;
        RelativeLayout bg;

        public MyHolder(View itemView) {
            super(itemView);
            card_balancetvstatic = itemView.findViewById(R.id.card_balancetvstatic);
           amountTV = itemView.findViewById(R.id.card_amount);
           mainLabelTV= itemView.findViewById(R.id.card_main_label);
           subLayerTV= itemView.findViewById(R.id.card_sub_label);
           additionalInfoImageView= itemView.findViewById(R.id.card_info_imageview);
           remainingBalanceTV= itemView.findViewById(R.id.card_remaining_balance);
           dateTV= itemView.findViewById(R.id.card_date);
           bg = itemView.findViewById(R.id.card_mainBG);


        }
    }
    public void initNewBuilder(String dataIn){
        builder = new BottomSheet.Builder(context);


        builder.setWindowDimming(80)
                .setTitleMultiline(true)
                .setBackgroundColor(Color.parseColor("#fff8e1"))
                .setTitleTextColor(Color.parseColor("#263238"));


        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

        builder.setCallback(new BottomSheetCallback() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismissed() {
            }
        });

        builder.setTitle(dataIn);
        builder.show();
    }


    public String getCurrencyType(){
        String currencySymbol = MainActivity.prefs.getString(SettingsFragment.CURRENCY_SYMBOL, "$");
        return currencySymbol;
    }

}
