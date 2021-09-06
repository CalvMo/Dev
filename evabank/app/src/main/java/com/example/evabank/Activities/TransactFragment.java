package com.example.evabank.Activities;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evabank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Date;
import java.util.Random;
import java.util.Calendar;

public class TransactFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button btn_completeTrans;
    private TextView balance,missingAuth;
    private EditText amount,restAuth, account;
    private String android_id,auth,transtype;


    FirebaseDatabase rootNode;
    DatabaseReference reference, transRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_transact,container,false);
        Spinner spinner = v.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.transaction_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        account = v.findViewById(R.id.transAccount);
        balance = v.findViewById(R.id.text_transbalance);
        amount = v.findViewById(R.id.trans_amount);
        btn_completeTrans = v.findViewById(R.id.btn_transact);
        missingAuth = v.findViewById(R.id.txt_IncompletePWD);
        restAuth = v.findViewById(R.id.txt_completepwd);
        android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.child(android_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bal = String.valueOf(dataSnapshot.child("balance").getValue());
                balance.setText(bal);
                auth = String.valueOf(dataSnapshot.child("authpassword").getValue());
                missingAuth.setText(getRandomString(5,auth));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //missingAuth.setText(getRandomString(5,auth));

        btn_completeTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double num1 = Double.parseDouble(balance.getText().toString());
                double num2 = Double.parseDouble(amount.getText().toString());

                if(num1>num2){
                    double difference = num1 - num2;

                    String authString = restAuth.getText().toString();

                    boolean success = true;
                    for (int i = 0; i < authString.length(); i++){
                        char c = authString.charAt(i);
                        //Process char
                        if(!auth.contains(Character.toString(c))){
                            Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            success = false;
                            break;
                        }
                    }
                    if(success){
                        balance.setText(String.valueOf(difference));
                        Date currentTime = Calendar.getInstance().getTime();

                        final String android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

                        rootNode = FirebaseDatabase.getInstance();
                        reference = rootNode.getReference("users");
                        //UserHelperClass helperClass = new UserHelperClass(android_id,String.valueOf(difference));
                        reference.child(android_id).child("balance").setValue(String.valueOf(difference));

                        transRef = rootNode.getReference("transactions");
                        transRef.child(android_id).child("transactiontype").setValue(transtype);
                        transRef.child(android_id).child("amount").setValue(String.valueOf(num2));
                        transRef.child(android_id).child("balance").setValue(String.valueOf(difference));
                        transRef.child(android_id).child("account").setValue(account.getText().toString());
                        transRef.child(android_id).child("time").setValue(currentTime.toString());


                        Toast.makeText(getActivity(), "Transaction Completed", Toast.LENGTH_SHORT).show();
                    }}
                else{
                    Toast.makeText(getActivity(), "Insufficient Funds", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }

    public static String getRandomString(int i,String characters) {
        //final String characters = "P@s5w0rd";
        StringBuilder result = new StringBuilder();
        while(i >0){
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            i--;
        }
        return result.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        //Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
        transtype = text;


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
