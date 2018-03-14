package com.andy.pru.phone_contact_diary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText contactName;
     SimpleAdapter adapter;
    ListView contactsListView;
    int count;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, String> getRowID=new HashMap<Integer, String>();
    private   List<HashMap<String, String>> listContact=new ArrayList<HashMap<String, String>>();
    ContactDatabase PhonebookDB = new ContactDatabase(this);
    Toolbar toolbar;
    View v,f;
    Snackbar snackbar;
    boolean doubleBackToExitPressedOnce=false;
    FloatingActionButton fab;
    int i;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        i=R.id.search;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.select:
                deleteAll();
                return true;
            case R.id.search:
                editSearch();
                item.setVisible(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editSearch() {
        contactName.setVisibility(View.VISIBLE);
    }

    private void deleteAll() {
        final AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Delete All");
        alert.setMessage("Contacts will be deleted. Delete ?");
        alert.setIcon(R.drawable.trash);
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try
                {
                    if(listContact.isEmpty()){
                        snack(f, "No contacts are found to deleted");
                    }else {
                        listContact.clear();
                        adapter.notifyDataSetChanged();
                        PhonebookDB.open();
                        PhonebookDB.deleteContactsAll();
                        PhonebookDB.close();
                        snack(f, "All the Contacts are deleted");
                    }
                }
                catch(Exception e)
                {

                    e.printStackTrace();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Phone Book");
        v = findViewById(R.id.mainLayout);
        fab= findViewById(R.id.fab);
        f=fab;

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        findViewById(R.id.mainLayout).requestFocus();
        contactName = findViewById(R.id.search_contact);
        contactsListView = findViewById(R.id.lstViewContacts);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, AddContact_Activity.class);
                startActivity(i);
            }
        });

        //inserting dummy contacts

        SharedPreferences pref= MainActivity.this.getSharedPreferences("DATA",MODE_PRIVATE);
        int flag=pref.getInt("flag",0);
        if(flag == 0){
            try
            {
                long intNewID = 0;

                String[] fname = new String[] {"Pru","Rahul","Janu","Papa","Fit","Rajini","Mega","Super","Prince","Happy"};
                String[] lname = new String [] {"Ka","Pa" ,"Ra","Jk","Gm","Lal","Khanth","Star","Star","Star"};
                String[] email = new String[] {"a@gmail.com","a@gmail.com","a@gmail.com","a@gmail.com","a@gmail.com",
                        "a@gmail.com","a@gmail.com","a@gmail.com","a@gmail.com","a@gmail.com"};
                String[] mob = new String[] {"9756841203","9756841203","9756841203","9756841203","9756841203","9756841203",
                        "9756841203","9756841203","9756841203","9756841203"};
                String[] comp= new String[] {"ABC SOFT","ABC SOFT","ABC SOFT","ABC SOFT","ABC SOFT","ABC SOFT",
                        "ABC SOFT","ABC SOFT","ABC SOFT","ABC SOFT"};
                String[] addr= new String[] {"HYDERABAD","HYDERABAD","HYDERABAD","HYDERABAD","HYDERABAD","HYDERABAD",
                        "HYDERABAD","HYDERABAD","HYDERABAD","HYDERABAD"};
                String[] gender=new String[] {"MR.","MR.","MISS.","MISS.","MR.","MISS.","MR.","MR.","MISS.","MR."};

                for(int i=0;i<10;i++){
                    PhonebookDB.open();
                    intNewID = PhonebookDB.insertContacts(fname[i], lname[i],email[i], mob[i],comp[i],addr[i],gender[i]);
                    PhonebookDB.close();
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            pref.edit().putInt("flag",1).apply();
        }

        //get all contacts
        PhonebookDB.open();
        Cursor cursorList = PhonebookDB.getAllContacts();
        count = 0;
        if (cursorList.moveToFirst()) {
            do {
                HashMap<String, String> contactDet = new HashMap<String, String>();
                String rowID = cursorList.getString(0);
                String contactFirstName = cursorList.getString(1);
                String contactLastName = cursorList.getString(2);
                contactDet.put("name", contactFirstName + " " + contactLastName);
                listContact.add(contactDet);
                getRowID.put(count, rowID);
                count++;
            } while (cursorList.moveToNext());
        }
        String[] itemControl = {"name"};
        int[] itemLayout = {R.id.name};
        listContact = sortContact(listContact);
        adapter = new SimpleAdapter(this.getBaseContext()
                , listContact, R.layout.list_contact_layout, itemControl, itemLayout);
        contactsListView.setAdapter(adapter);

        //To view the contact details
        try {
            contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressWarnings("rawtypes")
                public void onItemClick(AdapterView parent, View v, final int position, long id) {

                    Intent contactDetails = new Intent(MainActivity.this, ContactDetails_Activity.class);
                    contactDetails.putExtra("posit", getRowID.get(position));
                    startActivity(contactDetails);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Will be called when we search for a contact
        contactName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
// When user changed the Text
                MainActivity.this.adapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
// TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable arg0) {
// TODO Auto-generated method stub
            }
        });
    }

    //Return updated search list view adapter after search

    //To sort the contacts
    @SuppressLint("UseSparseArrays")
    public List<HashMap<String, String>> sortContact(List<HashMap<String, String>> contacts) {

        List<String> lst = new ArrayList<String>();
        List<HashMap<String, String>> sortContacts = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < contacts.size(); i++) {
            lst.add(contacts.get(i).get("name") + "," + getRowID.get(i));
        }
        Collections.sort(lst);
        getRowID = new HashMap<Integer, String>();
        for (int i = 0; i < lst.size(); i++) {
            HashMap<String, String> hashContacts = new HashMap<String, String>();
            String splitData[] = lst.get(i).split(",");
            hashContacts.put("name", splitData[0]);
            sortContacts.add(hashContacts);
            getRowID.put(i, splitData[splitData.length - 1]);
        }

        return sortContacts;
    }
    private void snack(View v,String msg){
        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setDuration(3000);
        snackbar.setAction("ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(Color.YELLOW);

        View vie=snackbar.getView();
        vie.setBackgroundColor(getResources().getColor(R.color.snackbackground));
        TextView txt= vie.findViewById(android.support.design.R.id.snackbar_text);
        txt.setTextColor(getResources().getColor(R.color.snacktext));

        snackbar.show();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        snack(f,"Tap again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 5000);
    }

}
