package com.example.evabank.Activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evabank.R;

import java.util.List;

public class HelperAdapter extends RecyclerView.Adapter {
    List<fetchData> fetchDataList;
    private ViewHolderClass ViewHolderClass;

    public HelperAdapter(List<fetchData> fetchDataList) {
        this.fetchDataList = fetchDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        ViewHolderClass viewHolderClass =new ViewHolderClass(view);

        return ViewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass)holder;
        fetchData fetchData = fetchDataList.get(position);
        viewHolderClass.type.setText((fetchData.getType()));
        viewHolderClass.balance.setText((fetchData.getBalance()));
        viewHolderClass.account.setText((fetchData.getAccount()));
        viewHolderClass.amount.setText((fetchData.getAmount()));


    }

    @Override
    public int getItemCount() {
        return fetchDataList.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView type,account,amount,balance;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.lv_transtype);
            account = itemView.findViewById(R.id.lv_account);
            amount = itemView.findViewById(R.id.lv_amount);
            balance = itemView.findViewById(R.id.lv_balance);

        }
    }
}
