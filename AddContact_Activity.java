package com.andy.pru.phone_contact_diary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class AddContact_Activity extends AppCompatActivity {
    Toolbar toolbar;
    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText phoneNumber;
    private EditText address;
    private EditText company;
    final ContactDatabase PhonebookDB = new ContactDatabase(this);
    View v;
    Snackbar snackbar;
    Spinner spinner;
    ImageView gene,gphone,gemail,gcompany,gloc;
    String gender;
    int solve=1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addcontact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.submit:
                addToDB(v);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Contact");
        v= findViewById(R.id.mainLayout1);
        firstName= findViewById(R.id.firstName);
        lastName= findViewById(R.id.lastName);
        emailAddress= findViewById(R.id.emailID);
        phoneNumber= findViewById(R.id.phone);
        company = findViewById(R.id.company);
        address = findViewById(R.id.address);
        gene = findViewById(R.id.gene);
        gphone = findViewById(R.id.gphone);
        gemail= findViewById(R.id.gemail);
        gcompany= findViewById(R.id.gcompany);
        gloc= findViewById(R.id.gloc);
        Glide.with(AddContact_Activity.this).load(R.drawable.smartphone).into(gphone);
        Glide.with(AddContact_Activity.this).load(R.drawable.letter).into(gemail);
        Glide.with(AddContact_Activity.this).load(R.drawable.factory).into(gcompany);
        Glide.with(AddContact_Activity.this).load(R.drawable.placeholder).into(gloc);
        snack("You are set to create an user");

        //spinner
        spinner= findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender,
                R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = (String) parent.getItemAtPosition(position);
                if(gender.equals("MR.")){

                    Glide.with(AddContact_Activity.this).load(R.drawable.man).into(gene);
                }else{
                    Glide.with(AddContact_Activity.this).load(R.drawable.woman).into(gene);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void snack(String msg){
        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setDuration(3000);
        snackbar.setAction("ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);

        View v=snackbar.getView();
        v.setBackgroundColor(getResources().getColor(R.color.snackbackground));
        TextView txt= v.findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextColor(getResources().getColor(R.color.snacktext));

        snackbar.show();
    }
    public void addToDB(View view) {
        try {
            long intNewID = 0;
            firstName.setError(null);
            phoneNumber.setError(null);
            emailAddress.setError(null);
            String strFirstName = firstName.getText().toString();
            String strLastName = lastName.getText().toString();
            String strEmailID = emailAddress.getText().toString();
            String strMobile01 = phoneNumber.getText().toString();
            String strCompany = company.getText().toString();
            String strAddress = address.getText().toString();

            boolean cancel = false;
            View focusView = null;
            if (TextUtils.isEmpty(strFirstName)) {
                firstName.setError(getString(R.string.error_field_required));
                focusView = firstName;
                cancel = true;
            }
            if (TextUtils.isEmpty(strMobile01)) {
                phoneNumber.setError(getString(R.string.error_field_required));
                focusView = phoneNumber;
                cancel = true;
            }else if (!isMobileValid(strMobile01)) {
                phoneNumber.setError(getString(R.string.error_invalid_mobile));
                focusView = phoneNumber;
                cancel = true;
            }
            if (!isEmailValid(strEmailID)) {
                emailAddress.setError(getString(R.string.error_invalid_email));
                focusView = emailAddress;
                cancel = true;
            }


            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // TODO: Call create inserting contacts here
                PhonebookDB.open();
                intNewID = PhonebookDB.insertContacts(strFirstName, strLastName, strEmailID, strMobile01, strCompany, strAddress, gender);
                PhonebookDB.close();

                Intent contactList = new Intent(this, MainActivity.class);
                Toast.makeText(this, strFirstName + " has been created successfully", Toast.LENGTH_SHORT).show();
                startActivity(contactList);

            }

           /* if (strFirstName.trim().matches("")&& solve==1) {
                solve=0;
                Intent contactList = new Intent(this, MainActivity.class);
                Toast.makeText(this, "At least add the first name or phone number", Toast.LENGTH_SHORT).show();
                startActivity(contactList);
            } else if(solve == 1) {

                PhonebookDB.open();
                intNewID = PhonebookDB.insertContacts(strFirstName, strLastName, strEmailID, strMobile01, strCompany, strAddress, gender);
                PhonebookDB.close();

                Intent contactList = new Intent(this, MainActivity.class);
                Toast.makeText(this, strFirstName + " has been created successfully", Toast.LENGTH_SHORT).show();
                startActivity(contactList);
            }*/
        }
        catch(Exception e)
        {

            e.printStackTrace();
        }
    }

    private boolean isMobileValid(String strMobile01) {
        return ((strMobile01.length() == 10) && (strMobile01.charAt(0) == '9' || strMobile01.charAt(0) == '8' || strMobile01.charAt(0) == '7'));
    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.equals("") || Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
