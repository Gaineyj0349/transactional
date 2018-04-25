package com.gainwise.transactional.Utilities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gainwise.transactional.Fragments.FragPurchase;
import com.gainwise.transactional.POJO.Transaction;
import com.gainwise.transactional.R;

import java.util.List;

public class AdapterForFragBottomSheet extends RecyclerView.Adapter<AdapterForFragBottomSheet.MyViewHolder> {

    Context context;
    List<Transaction> list;
    String type;

    public AdapterForFragBottomSheet(Context context, List<Transaction> list,String type) {
        Log.i("JOSH", "type is "+ type);
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_rv_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i("JOSH", ""+list.size());
        Log.i("JOSH", "HELPER");

        if(type.equals("main")) {
            if (list.get(position).getMainLabel() != null && list.get(position).getMainLabel().length() > 0 && list != null) {
                holder.mtextView.setText(list.get(position).getMainLabel());
            } else {
                Log.i("JOSH", "TEST COMPLETED HERE");
                holder.itemView.setVisibility(View.GONE);
            }
            try {
                holder.ll.setBackgroundColor(Integer.parseInt(list.get(position).getColor1()));
            }catch (NumberFormatException e){
                holder.ll.setBackgroundColor(Color.parseColor("#000000"));

            }
        }else if(type.equals("sub")){

            if (list.get(position).getSubLabel() != null && list.get(position).getSubLabel().length() > 0 && list != null) {
                holder.mtextView.setText(list.get(position).getSubLabel());
                Log.i("JOSH22", "helper1:"+list.get(position).getSubLabel());
            }

            try {
                holder.ll.setBackgroundColor(Integer.parseInt(list.get(position).getColor1()));
            }catch (NumberFormatException e){
                holder.ll.setBackgroundColor(Color.parseColor("#000000"));

            }
        }
    }

    @Override
    public int getItemCount() {
        if(list == null || list.size()<= 0)
        return 0;
        else{
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mtextView;
        public LinearLayout ll;

        public MyViewHolder(View itemView) {
            super(itemView);

             mtextView = (TextView) itemView.findViewById(R.id.frag_purchase_bottom_sheet_card_tv);
             ll = (LinearLayout) itemView.findViewById(R.id.frag_purchase_bottom_sheet_ll);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Log.i("JOSH", "Clicked  type is "+ type);
                     if(type.equals("main")){
                         FragPurchase.resetSubLabel();
                         FragPurchase.secureMainLabelInfo(mtextView.getText().toString());
                     }else if(type.equals("sub")){
                         FragPurchase.secureSubLabelInfo(mtextView.getText().toString());
                     }
                 }
             });
        }
    }
}
