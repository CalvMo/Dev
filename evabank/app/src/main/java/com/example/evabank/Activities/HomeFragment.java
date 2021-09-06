package com.example.evabank.Activities;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.evabank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeFragment extends Fragment {
    //ListView myListView;

    private String android_id;
    private TextView balance, user,transaction,price,ref,date;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_home,container,false);
        balance = v.findViewById(R.id.txtBalance);
        transaction = v.findViewById(R.id.txt_transaction);
        user =v.findViewById(R.id.txt_welcome);
        price=v.findViewById(R.id.txt_Price);
        ref = v.findViewById(R.id.txt_transref);
        date = v.findViewById(R.id.txt_trandate);
        android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("transactions");
        databaseReference.child(android_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.child("balance").getValue());
                balance.setText(value);
                String usr = String.valueOf(dataSnapshot.child("name").getValue());
                user.setText(usr);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dbReference.child(android_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = String.valueOf(dataSnapshot.child("transactiontype").getValue());
                transaction.setText(value);
                String dt = String.valueOf(dataSnapshot.child("time").getValue());
                date.setText(dt);
                String pr = String.valueOf(dataSnapshot.child("amount").getValue());
                pr = price.getText().toString() + pr;
                price.setText(pr);
               String rf = String.valueOf(dataSnapshot.child("account").getValue());
                rf = ref.getText().toString() + rf;
                ref.setText(rf);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return  v;
    }
}
