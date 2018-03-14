package com.andy.pru.phone_contact_diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class ContactDetails_Activity extends AppCompatActivity {
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmailAddress;
    private EditText edtPhoneNumber;
    private EditText edtCompany;
    private EditText edtAddress;
    private Long rowID;
    ContactDatabase PhonebookDB = new ContactDatabase(this);
    Toolbar toolbar;
    View v;
    Snackbar snackbar;
    String numb;
    Spinner spinner;
    ImageView gene,gphone,gemail,gcompany,gloc;
    String gender;
    final String TAG="At";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editcontact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.submit:
                saveContact(v);
                return true;
            case R.id.delete:
                deleteContact(v);
                return true;

            case R.id.edit:
                editEnable(true);
                spin();
                return true;

            case R.id.dial:
                dial();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void spin() {
        //spinner
        spinner= findViewById(R.id.gender);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender,
                R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gender = (String) parent.getItemAtPosition(position);
                if(gender.equals("MR.")){

                    Glide.with(ContactDetails_Activity.this).load(R.drawable.man).into(gene);
                }else{
                    Glide.with(ContactDetails_Activity.this).load(R.drawable.woman).into(gene);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void dial() {
        Intent i =new Intent(Intent.ACTION_DIAL);
        if(numb.trim().isEmpty()){
         snack("No Number found to dial ");
        }else{
            i.setData(Uri.parse("tel:"+numb));
            startActivity(i);
        }

    }

    private void editEnable(Boolean b) {
        edtFirstName.setEnabled(b);
        edtLastName.setEnabled(b);
        edtPhoneNumber.setEnabled(b);
        edtEmailAddress.setEnabled(b);
        edtCompany.setEnabled(b);
        edtAddress.setEnabled(b);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        v= findViewById(R.id.mainLayout2);
        PhonebookDB.open();

        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmailAddress = findViewById(R.id.edtEmailID);
        edtPhoneNumber = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtCompany = findViewById(R.id.edtCompany);
        gene = findViewById(R.id.gene);
        gphone = findViewById(R.id.gphone);
        gemail= findViewById(R.id.gemail);
        gcompany= findViewById(R.id.gcompany);
        gloc= findViewById(R.id.gloc);
        Glide.with(this).load(R.drawable.smartphone).into(gphone);
        Glide.with(this).load(R.drawable.letter).into(gemail);
        Glide.with(this).load(R.drawable.factory).into(gcompany);
        Glide.with(this).load(R.drawable.placeholder).into(gloc);
        String position = getIntent().getStringExtra("posit");
        rowID = Long.parseLong(position);
        Cursor cursorList = PhonebookDB.getContacts(rowID);
        edtFirstName.setText(cursorList.getString(1));
        String fname =cursorList.getString(1);
        setTitle(fname);
        edtLastName.setText(cursorList.getString(2));
        edtEmailAddress.setText(cursorList.getString(3));
        edtPhoneNumber.setText(cursorList.getString(4));
        numb=cursorList.getString(4);
        edtCompany.setText(cursorList.getString(5));
        edtAddress.setText(cursorList.getString(6));
        gender=cursorList.getString(7);
        if(gender.equals("MR.")){
            Glide.with(this).load(R.drawable.man).into(gene);


        }
        else if(gender.equals("MISS.")){
            Glide.with(this).load(R.drawable.woman).into(gene);

        }

        editEnable(false);
        snack("You are set to edit an user");

        //spinner

    }

    //Save the edited the content
    public void saveContact(View view) {
        String strFirstName = edtFirstName.getText().toString();
        String strLastName = edtLastName.getText().toString();
        String strEmailID = edtEmailAddress.getText().toString();
        String strMobile01 = edtPhoneNumber.getText().toString();
        String strCompany = edtCompany.getText().toString();
        String strAddress = edtAddress.getText().toString();

        Log.i(TAG,"error"+gender);
        Toast.makeText(this,"MSG"+strFirstName,Toast.LENGTH_LONG).show();
        PhonebookDB.updateContacts(rowID, strFirstName, strLastName, strEmailID, strMobile01,strCompany,strAddress,gender);
        PhonebookDB.close();
        //finish();
        Intent contactDetails = new Intent(this, MainActivity.class);
        //contactDetails.putExtra("posit", rowID.toString());
        Toast.makeText(this,strFirstName+" has been saved successfully",Toast.LENGTH_SHORT).show();
        startActivity(contactDetails);
    }

    public void deleteContact(View view) {
        final AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle("Delete ");
        alert.setMessage("Contact will be deleted. Delete ?");
        alert.setIcon(R.drawable.trash);
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    PhonebookDB.deleteContacts(rowID);
                    //finish();
                    PhonebookDB.close();
                    edtFirstName.setText("");
                    edtLastName.setText("");
                    edtEmailAddress.setText("");
                    edtPhoneNumber.setText("");
                    edtCompany.setText("");
                    edtAddress.setText("");
                    Intent contactList = new Intent(ContactDetails_Activity.this, MainActivity.class);
                    Toast.makeText(ContactDetails_Activity.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(contactList);
                } catch (Exception e) {
                    Log.e("Phonebook_TAG", "Got an error while deleting the contact", e);
                }
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}