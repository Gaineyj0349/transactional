package com.gainwise.transactional.Utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gainwise.transactional.Activities.MainActivity;
import com.gainwise.transactional.Activities.TransactionView;
import com.gainwise.transactional.Fragments.SettingsFragment;
import com.gainwise.transactional.POJO.Transaction;
import com.gainwise.transactional.R;

import org.michaelbel.bottomsheet.BottomSheet;
import org.michaelbel.bottomsheet.BottomSheetCallback;

import java.math.BigDecimal;
import java.util.List;

import spencerstudios.com.bungeelib.Bungee;

public class AdapterTransRV extends RecyclerView.Adapter<AdapterTransRV.MyHolder> {
    BottomSheet.Builder builder;

    List<Transaction> transactionList;
    Context context;
    boolean expenseTrackerOnly = false;
    boolean fromEntire = false;

    /* add the following var and assign it a value of -1 */
    private int lastPosition = -1;


    public AdapterTransRV(List<Transaction> transactionList, Context context, boolean expenseTrackerOnly, boolean fromEntire) {
        this.context = context;
        this.transactionList = transactionList;
        this.expenseTrackerOnly = expenseTrackerOnly;
        this.fromEntire = fromEntire;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_transactions_main,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        /* load the animation and fire it... */
        Animation animation = AnimationUtils.loadAnimation(context, (holder.getAdapterPosition() > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);

        /* assign current adapter position to the 'lastPosition' var */
        lastPosition = holder.getAdapterPosition();

        holder.ID.setText(String.valueOf(transactionList.get(position).getId()));
        holder.ID.setVisibility(View.INVISIBLE);
       if(expenseTrackerOnly){
           holder.remainingBalanceTV.setVisibility(View.GONE);
           holder.card_balancetvstatic.setVisibility(View.GONE);
       }else{
           holder.remainingBalanceTV.setVisibility(View.VISIBLE);
                   holder.card_balancetvstatic.setVisibility(View.VISIBLE);

       }


        if(transactionList.get(position).isPurchase()){
            holder.amountTV.setTextColor(context.getResources().getColor(R.color.negativeMoney));
        }else{
            holder.amountTV.setTextColor(context.getResources().getColor(R.color.positiveMoney));
        }
        try {

           //TODO abuse for crash

            boolean negativeBalance;
            BigDecimal bigDecimal = new BigDecimal(transactionList.get(position).getCurrentBalance());
            if (bigDecimal.signum() < 0) {
                holder.remainingBalanceTV.setTextColor(context.getResources().getColor(R.color.negativeMoney));
                negativeBalance = true;
            } else {
                negativeBalance = false;
                holder.remainingBalanceTV.setTextColor(context.getResources().getColor(R.color.positiveMoney));
                if(negativeBalance){
                    try{
                        BigDecimal temp = new BigDecimal(transactionList.get(position).getCurrentBalance());
                        temp.abs();
                        holder.remainingBalanceTV.setText(getCurrencyType()+"("+temp.abs()+")");
                    }catch (Exception e){
                        Log.i("JOSHhandle", e.getMessage());
                    }
                }else{
                    holder.remainingBalanceTV.setText(getCurrencyType()+transactionList.get(position).getCurrentBalance());
                }
            }
        }catch (Exception e){

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


        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder builder;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        }else{
                            builder = new AlertDialog.Builder(context);

                        }
                        builder.setTitle("Confirm this Transaction Delete")
                                .setMessage("This transaction fill be erased forever but will not effect the ending balance, continue?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MainActivity.db.TransactionDAO().deleteTransactionWithID(transactionList.get(position).getId());
                                      //  ((Activity)context).recreate();
                                      transactionList.remove(position);
                                      notifyDataSetChanged();
                                          }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.stat_sys_warning)
                                .show();


                        return true;
                    }
                });
            }
        });

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
        public TextView ID;
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
           ID = itemView.findViewById(R.id.card_id);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, TransactionView.class);
                   intent.putExtra("id", ID.getText().toString());
                   if(fromEntire){
                       intent.putExtra("entire", "yes");
                   }
                   context.startActivity(intent);
                   Bungee.zoom(context);
               }
           });

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
