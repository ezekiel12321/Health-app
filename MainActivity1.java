package test.circularlistviewapp.ActivityClass;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import test.circularlistviewapp.DataBase.DB;
import test.circularlistviewapp.DataBase.PersonDataDBTools;
import test.circularlistviewapp.Dialog.exitDialogFragment;
import test.circularlistviewapp.Fragement.FriendsListFragment;
import test.circularlistviewapp.Fragement.MessageFragment;
import test.circularlistviewapp.Fragement.ezekiel_main_screen_fragment;
import test.circularlistviewapp.JavaObjectClass.App;
import test.circularlistviewapp.R;
import test.circularlistviewapp.models.CaregiverInfo;
import test.circularlistviewapp.models.CaregiverRequestResponse;
import test.circularlistviewapp.models.PersonalInfo;
import test.circularlistviewapp.observer.CaregiverInfoObserver;
import test.circularlistviewapp.observer.HealthDataObserver;
import test.circularlistviewapp.observer.HealthDataUpdater;
import test.circularlistviewapp.observer.PublicInfoObserver;
import test.circularlistviewapp.observer.OfficialDocObserver;
import test.circularlistviewapp.observer.SymptomsUpdateObserver;
import test.circularlistviewapp.utils.DirectMessaging;
import test.circularlistviewapp.utils.MessagingUtils;
import test.circularlistviewapp.DataBase.CaregiverDBTools;
import test.circularlistviewapp.Fragement.FamilyPageFragment;
import test.circularlistviewapp.models.Person;
import test.circularlistviewapp.utils.ModelUtils;

//Ezekiel's imports:

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;


import static test.circularlistviewapp.JavaObjectClass.App.getContext;
import static test.circularlistviewapp.JavaObjectClass.App.getPersonalInfoGlo;
import static test.circularlistviewapp.JavaObjectClass.App.setContext;

import org.apache.commons.lang3.ObjectUtils;

public class MainActivity1 extends AppCompatActivity {

    @BindView(R.id.family_image)
    ImageView FmailyImage;

    @BindView(R.id.family_name)
    EditText name;
    @BindView(R.id.family_email)
    EditText email;
    @BindView(R.id.family_role)
    EditText role;
    @BindView(R.id.family_phone)
    EditText phone;
    @BindView(R.id.family_save)
    Button saveData;

    //Ezekiel's fields:
    ArrayList<String> s1;
    ArrayList<String> usernames;
    ArrayList<String> lastNames;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newcontactpopup_username, newcontactpopup_firstname, newcontactpopup_lastname, newcontactpopup_email, newcontactpopup_birthdate, newcontactpopup_primarycarephys,
            newcontactpopup_powerOfAttorney, newcontactpopup_location, newcontactpopup_comfortcare, newcontactpopup_emergencyContact;
    private String newName, newEmail, newPhone, newRole;
    private Button newcontactpopup_cancel, newcontactpopup_save;
    private ImageButton add_user;
    private MyAdapter myAdapter;
    private DBHelper DB;
    RecyclerView recyclerView;
    private MainActivity1 main1;
    private Fragment fragment;
    private Fragment main_page;

    //end

    public static final int REQ_CODE_CREATEGROUP = 113;
    private static final String TAG = "MainActivity1";

    private MessagingUtils messagingUtils;

    private DirectMessaging directMessaging;

    private PublicInfoObserver publicInfoObserver;
    private HealthDataObserver healthDataUpdater;
    private CaregiverInfoObserver caregiverInfoObserver;
    private OfficialDocObserver officialDocObserver;
    private SymptomsUpdateObserver symptomsUpdateObserver;
    private ArrayList<PersonalInfo> patientsList = new ArrayList<PersonalInfo>();
    private HashMap<String, PersonalInfo> patientsMap = new HashMap<String, PersonalInfo>();

    private PersonalInfo user;
    private String userName;
    private String userGrpBindKey;

    private MainActivityPagerAdapter pagerAdapter;
    private FragmentManager fm;
    ViewPager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ezekiel);
        App.setContext(this);

        //Ezekiel code copy

        fm = getSupportFragmentManager();
        //setContentView(R.layout.fragment_ezekiel_main_screen_fragment);

        main_page = new ezekiel_main_screen_fragment();

        //setContentView(R.layout.fragment_ezekiel_main_screen_fragment);
        main1 = this;
        user = getPersonalInfoGlo();
        Log.i(TAG, "accountName in PersonInfo in MainActivity1: " + user.accountName);
        Log.i(TAG, "password in PersonInfo in MainActivity1 " + user.password);
        Log.i(TAG, "Name in PersonInfo in MainActivity1 " + user.name);
        userGrpBindKey = user.grpBindKey;
        patientsList = CaregiverDBTools.getPatientListByCaregiver(MainActivity1.this, user);
        patientsMap.clear();
        for (int i = 0; i < patientsList.size(); i++) {
            PersonalInfo p = patientsList.get(i);
            patientsMap.put(p.position, p);
        }
        messagingUtils = new MessagingUtils(user, patientsList, fm);

        publicInfoObserver = new PublicInfoObserver(main1);
        healthDataUpdater = new HealthDataUpdater(main1);
        caregiverInfoObserver = new CaregiverInfoObserver(main1);
        symptomsUpdateObserver = new SymptomsUpdateObserver(main1);
        officialDocObserver = new OfficialDocObserver(main1);
        messagingUtils.register(publicInfoObserver);
        messagingUtils.register(healthDataUpdater);
        messagingUtils.register(caregiverInfoObserver);
        messagingUtils.register(officialDocObserver);
        messagingUtils.register(symptomsUpdateObserver);

        //fm = main1.getSupportFragmentManager();
        //getAllPatientsGrpBindKey(patientsList, user);
        // Retrieve list of patient(s) that is/are taken care by the logged on user


        ConnectionFactory factory = messagingUtils.getFactory();
        try {
            if (factory != null) {
                //DirectMessaging = new DirectMessaging(user, PersonalInforms, factory);
                directMessaging = new DirectMessaging(user, factory);
            } else {
                throw new NullPointerException(TAG + " Connection factory is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        messagingUtils.addDirectMessaging(this.directMessaging);
        messagingUtils.enqueueMessage("online", "");
        messagingUtils.setupCommunication();

        pagerAdapter = new MainActivityPagerAdapter(this, fm, messagingUtils, directMessaging);
        pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(2);

        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.bringToFront();
        tabLayout.setupWithViewPager(pager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.color_viewPager));



        //end

        //Logged on user

        //End

        //need to subscribe to my own queue and my own group (BindKey) and all the patients group that I take care of
        //Here pass personInforms (list of my group and all my patient groups instead of chat2







    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_my_info:
//                MessageFragment.newInstance(null);
                return true;
            case R.id.create_newGroup:
                //todo generate new public key and private key for user and encrypt for all the person in database;
                Intent intent = new Intent(this, FriendsListActivity.class);
                intent.putExtra(FriendsListFragment.KEY_CHOOSING_MODE, true);
                startActivityForResult(intent, REQ_CODE_CREATEGROUP);
//                MessageFragment.newInstance(null);
                return true;

            case R.id.create_addFamilyMember:
                //todo generate public key and private key save it to postion;
//                MessageFragment.newInstance(null);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    public void exit() throws IOException {
        messagingUtils.enqueueMessage("offline", "");
        messagingUtils.closeConnection();

        //2/18/21 cyeh add these
        directMessaging.closeConnection();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exitDialogFragment dialogFragment = exitDialogFragment.newInstance();
            dialogFragment.setRetainInstance(true);
            dialogFragment.show(getSupportFragmentManager(), "ddd");
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() { //2/18/21 cyeh: when user press the back key
        try {
            exit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    public MessagingUtils getMessagingUtils() {
        return this.messagingUtils;
    }

    public DirectMessaging getDirectMessaging() {
        return this.directMessaging;
    }

    public PersonalInfo getLoggedOnUser() {
        return this.user;
    }

    public ArrayList<PersonalInfo> getPatientsList() {
        return this.patientsList;
    }

    public HashMap<String, PersonalInfo> getPatientsMap() {
        return this.patientsMap;
    }

    // TODO: I have done it in a straight forward way, may study whether we can use notifyDataSetChanged() method.
    public void refreshPatientList() {
        patientsList = CaregiverDBTools.getPatientListByCaregiver(MainActivity1.this, user);
        patientsMap.clear();
        for (int i = 0; i < patientsList.size(); i++) {
            PersonalInfo p = patientsList.get(i);
            patientsMap.put(p.position, p);
        }
        FamilyPageFragment f = (FamilyPageFragment) (pagerAdapter.getRegisteredFragment(0));
        if (f != null) {
            f.updatePatientObjects();
        }
    }


    public boolean validate(String Name, String Email, String Phone) {
        boolean valid = true;
        if (Name.isEmpty() || Name.length() < 3) {
            name.setError("at least 3 characters for Name");
            valid = false;
        } else {
            name.setError(null);
        }
        // Email is now optional
        //if (Email.isEmpty()) {
        //    email.setError("Email is empty");
        //    valid = false;
        //} else {
        email.setError(null);
        //}
        // Phone is now optional
        //if (Phone.isEmpty()) {
        //    phone.setError("Phone is empty");
        //    valid = false;
        //} else {
        phone.setError(null);
        //}

        /*

         */

        // need to validate if this patient already a patient on this device or not. If not, create a new patient record
        try {
            // need to validate if this patient already a patient on this device or not. If not, create a new patient record
            if (PersonDataDBTools.isContactInfoExist(MainActivity1.this, Name, Email, Phone)) {
                name.setError("Already a patient with this name, email or phone number on this device!");
                email.setError("Already a patient with this name, email or phone number on this device!");
                phone.setError("Already a patient with this name, email or phone number on this device!");
                valid = false;
            } else {
                name.setError(null);
                email.setError(null);
                phone.setError(null);
            }
        } catch (Exception e) {
            Log.e(TAG, "Retrieving patient record error");
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }
}