package test.circularlistviewapp.Fragement;

import static test.circularlistviewapp.JavaObjectClass.App.getPersonalInfoGlo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.ConnectionFactory;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import test.circularlistviewapp.ActivityClass.DBHelper;
import test.circularlistviewapp.ActivityClass.HealthStatusActivity;
import test.circularlistviewapp.ActivityClass.MainActivity1;
import test.circularlistviewapp.ActivityClass.MainActivityPagerAdapter;
import test.circularlistviewapp.ActivityClass.MyAdapter;
import test.circularlistviewapp.DataBase.CaregiverDBTools;
import test.circularlistviewapp.DataBase.DB;
import test.circularlistviewapp.DataBase.PersonDataDBTools;
import test.circularlistviewapp.JavaObjectClass.App;
import test.circularlistviewapp.R;
import test.circularlistviewapp.models.CaregiverInfo;
import test.circularlistviewapp.models.CaregiverRequestResponse;
import test.circularlistviewapp.models.PersonalInfo;
import test.circularlistviewapp.observer.CaregiverInfoObserver;
import test.circularlistviewapp.observer.HealthDataObserver;
import test.circularlistviewapp.observer.HealthDataUpdater;
import test.circularlistviewapp.observer.OfficialDocObserver;
import test.circularlistviewapp.observer.PublicInfoObserver;
import test.circularlistviewapp.observer.SymptomsUpdateObserver;
import test.circularlistviewapp.utils.DirectMessaging;
import test.circularlistviewapp.utils.MessagingUtils;
import test.circularlistviewapp.utils.ModelUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ezekiel_main_screen_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ezekiel_main_screen_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<String> s1;
    ArrayList<String> usernames;
    ArrayList<String> lastNames;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newcontactpopup_username, newcontactpopup_firstname, newcontactpopup_lastname,newcontactpopup_email,newcontactpopup_birthdate,newcontactpopup_primarycarephys,
            newcontactpopup_powerOfAttorney, newcontactpopup_location, newcontactpopup_comfortcare,newcontactpopup_emergencyContact;
    private String newName,newEmail,newPhone,newRole;
    private Button newcontactpopup_cancel,newcontactpopup_save;
    private ImageButton add_user;
    private MyAdapter myAdapter;
    private DBHelper DB;

    private MainActivity1 main1;

    //end

    public static final int REQ_CODE_CREATEGROUP = 113;
    private static final String TAG = "MainActivity1";
    private FragmentManager fm;
    private MessagingUtils messagingUtils;

    private DirectMessaging directMessaging;

    private PublicInfoObserver publicInfoObserver;
    private HealthDataObserver healthDataUpdater;
    private CaregiverInfoObserver caregiverInfoObserver;
    private OfficialDocObserver officialDocObserver;
    private SymptomsUpdateObserver symptomsUpdateObserver;
    private ArrayList<PersonalInfo> patientsList = new ArrayList<PersonalInfo>();
    private HashMap<String,PersonalInfo> patientsMap = new HashMap<String, PersonalInfo>();

    private PersonalInfo user;
    private String userName;
    private String userGrpBindKey;

    private MainActivityPagerAdapter pagerAdapter;
    ViewPager pager;

    public ezekiel_main_screen_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ezekiel_main_screen_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ezekiel_main_screen_fragment newInstance(String param1, String param2) {
        ezekiel_main_screen_fragment fragment = new ezekiel_main_screen_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ezekiel_main_screen_fragment, container, false);

        main1 = (MainActivity1) getActivity();
        recyclerView = view.findViewById(R.id.recyclerView1);
        add_user = (ImageButton) view.findViewById(R.id.add_patient_button1);
        s1 = new ArrayList<String>();
        usernames = new ArrayList<String>();
        lastNames = new ArrayList<String>();

        DB = new DBHelper(getContext());
        //this.deleteDatabase("Userdata.db");
        Context context = App.getContext();
        messagingUtils = main1.getMessagingUtils();
        directMessaging = main1.getDirectMessaging();

        user = getPersonalInfoGlo();
        Log.i(TAG, "accountName in PersonInfo in MainActivity1: " + user.accountName);
        Log.i(TAG, "password in PersonInfo in MainActivity1 " + user.password);
        Log.i(TAG, "Name in PersonInfo in MainActivity1 " + user.name);
        userGrpBindKey = user.grpBindKey;
        patientsList = CaregiverDBTools.getPatientListByCaregiver(main1, user);
        patientsMap.clear();
        for (int i = 0; i < patientsList.size(); i++) {
            PersonalInfo p = patientsList.get(i);
            patientsMap.put(p.position, p);
        }



        Cursor res = DB.getdata();



        while(res.moveToNext())
        {
            usernames.add(res.getString(0));
            s1.add(res.getString(1));
            lastNames.add(res.getString(2));

        }




        add_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                createNewContactDialog();
            }
        });
        user = getPersonalInfoGlo();
        Log.i(TAG, "accountName in PersonInfo in MainActivity1: " + user.accountName);
        Log.i(TAG, "password in PersonInfo in MainActivity1 " + user.password);
        Log.i(TAG, "Name in PersonInfo in MainActivity1 " + user.name);

        //Ezekiel set name on screen
        TextView text = view.findViewById(R.id.textView4);
        text.setText(user.name);

        userName = user.name;
        userGrpBindKey = user.grpBindKey;

        long total_count = DB.getProfilesCount();
        myAdapter = new MyAdapter(main1,s1,usernames,lastNames,patientsMap, total_count);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(main1));
        ImageView reload = view.findViewById(R.id.reload1);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase myDB = DB.getReadableDatabase();
                Cursor curs = myDB.rawQuery("Select * from Userdetails", null);
                test.circularlistviewapp.DataBase.DB databse = new DB(getContext());
                ArrayList<PersonalInfo> arrayList = CaregiverDBTools.getPatientListByCaregiver(main1, user);
                int count = 0;
                for(PersonalInfo p : arrayList){
                    if(count >= total_count){
                        DB.insertuserdata(p.accountName, p.name, "", p.email, "","","","","","","");
                    }
                    count += 1;
                }

                myAdapter.notifyDataSetChanged();
            }
        });


        //pagerAdapter = new MainActivityPagerAdapter(this, fm, messagingUtils, directMessaging);
        //pager = findViewById(R.id.pager);

        //Ezekiel comments this out because it was breaking his version
        /*
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(2);

        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.bringToFront();
        tabLayout.setupWithViewPager(pager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.color_viewPager));
        */
        //if (getCallingActivity() != null) {
        //    tabLayout.getTabAt(1).select();
        //}

        //App.setContext(this);


        ImageView profile_pic = view.findViewById(R.id.imageView);
        profile_pic.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), HealthStatusActivity.class);
                intent.putExtra("IS_FAR", true);
                intent.putExtra("NTitile","0");
                PersonalInfo user = ((MainActivity1)getActivity()).getLoggedOnUser();
                intent.putExtra(FamilyPageFragment.PATIENT_OBJ,(Serializable) user);


                main1.startActivity(intent);
            }
        });
        return view;

    }



    public void createNewContactDialog(){
        dialogBuilder = new AlertDialog.Builder(getContext());

        final View contactPopupView = getLayoutInflater().inflate(R.layout.add_patient,null);
        newcontactpopup_username = (EditText) contactPopupView.findViewById(R.id.Username);
        newcontactpopup_firstname = (EditText) contactPopupView.findViewById(R.id.FirstName);
        newcontactpopup_lastname = (EditText) contactPopupView.findViewById(R.id.lastNameSpecial);
        newcontactpopup_email = (EditText) contactPopupView.findViewById(R.id.editTextTextEmailAddress);
        newcontactpopup_birthdate = (EditText) contactPopupView.findViewById(R.id.editTextDate);
        newcontactpopup_primarycarephys = (EditText) contactPopupView.findViewById(R.id.primary_care_physician);
        newcontactpopup_powerOfAttorney = (EditText) contactPopupView.findViewById(R.id.power_of_attorney);
        newcontactpopup_location = (EditText) contactPopupView.findViewById(R.id.location);
        newcontactpopup_comfortcare = (EditText) contactPopupView.findViewById(R.id.level_of_comfort);
        newcontactpopup_emergencyContact = (EditText) contactPopupView.findViewById(R.id.emergency_contact);

        newcontactpopup_cancel = (Button) contactPopupView.findViewById(R.id.cancel);
        newcontactpopup_save = (Button) contactPopupView.findViewById(R.id.save);



        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        newcontactpopup_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String userName = newcontactpopup_username.getText().toString();
                String name = newcontactpopup_firstname.getText().toString();
                String nameLast = newcontactpopup_lastname.getText().toString();
                String email = newcontactpopup_email.getText().toString();
                String birthdate = newcontactpopup_birthdate.getText().toString();


                String primaryCarePhys = "";
                String powerOfAttorney = "";
                String location = "";
                String comfortcare = "";
                String emergencyContact = "";
                String URI = "";



                DB.insertuserdata(userName,name,nameLast,email,birthdate,primaryCarePhys,powerOfAttorney,location,comfortcare,emergencyContact,URI);

                // Add Member From original code // imported into Ezekiel's
                newName = name;
                newEmail = email;
                newPhone = "";
                newRole = "1";
                //To.do Merge PersonData.db with Userdata.db
                Log.i(TAG,"newRole:" + newRole);



                //if (!validate(newName, newEmail, newPhone)) {
                //return;
                //}

                //
                Cursor res = DB.getdata();

                while(res.moveToNext())

                {
                    res.getString(0);
                    if(!s1.contains(res.getString(1)))
                    {
                        s1.add(res.getString(1));
                    }
                    if(!usernames.contains(res.getString(0))){
                        usernames.add(res.getString(0));
                    }

                    lastNames.add(res.getString(2));



                }
                res.close();

                myAdapter.notifyDataSetChanged();

                dialog.dismiss();

                updateData update = new updateData();
                update.execute();





            }
        });



        newcontactpopup_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                dialog.cancel();
            }
        });



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Context context = App.getContext();



    }



    public MessagingUtils getMessagingUtils() {
        return this.messagingUtils;
    }

    public DirectMessaging getDirectMessaging() {return this.directMessaging;}

    public PersonalInfo getLoggedOnUser() { return this.user; }

    public ArrayList<PersonalInfo> getPatientsList() { return this.patientsList; }

    public HashMap<String, PersonalInfo> getPatientsMap() { return this.patientsMap; }



    private class updateData extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        private PersonalInfo personalInfo;
        boolean RSAKey = true;
        boolean acctCreate = true;
        boolean MaxPosVal = true;
        String nextPatientPosStr="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            progressDialog= new ProgressDialog(AddMemberActivity.this);
//            progressDialog.setIndeterminate(true);
//            progressDialog.setMessage("Update Info...");
//            progressDialog.show();
            //进度条加载；
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected Void doInBackground(Void... voids) {

            PersonalInfo ownerInfo = getPersonalInfoGlo();

            //Commented Out By Ezekiel
            //HealthStatusActivity.personalInfo.name = newName;
            //HealthStatusActivity.personalInfo.email = newEmail;
            //HealthStatusActivity.personalInfo.phone = newPhone;
            //HealthStatusActivity.personalInfo.accessRight = newRole;
            //HealthStatusActivity.personalInfo.relation = ownerInfo.position; // 12/4/21 cyeh add this

            KeyPairGenerator kpg;
            PublicKey publicKey;
            PrivateKey privateKey;

            PublicKey GroupPublicKey;  // 2
            PrivateKey GroupPrivateKey;
            try {
                kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(1024);
                KeyPair kp = kpg.genKeyPair();
                publicKey = kp.getPublic();
                privateKey = kp.getPrivate();

                kp = kpg.genKeyPair(); // need to generate another set of KeyPair for Group
                GroupPublicKey = kp.getPublic(); // 2
                GroupPrivateKey = kp.getPrivate();

            } catch (Exception e) {
                Log.e("error", "RSA key pair error");
                RSAKey = false;
                return null;
            }

            try {
                byte[] keyBytes = publicKey.getEncoded();
                byte[] GroupkeyBytes = GroupPublicKey.getEncoded(); // 2

                String publicKeyString = Base64.encodeToString(keyBytes, Base64.DEFAULT);
                String grpPublicKeyStr = Base64.encodeToString(GroupkeyBytes, Base64.DEFAULT); //2

                Log.v("keykeykey", publicKeyString);
                Log.v("keykeykey", grpPublicKeyStr); // 2

                keyBytes = privateKey.getEncoded();
                GroupkeyBytes = GroupPrivateKey.getEncoded(); // re-use GroupkeyBytes for GroupPrivateKey

                String privateKeyString = Base64.encodeToString(keyBytes, Base64.DEFAULT);
                String grpPrivateKeyStr = Base64.encodeToString(GroupkeyBytes, Base64.DEFAULT); //2

                Log.v("keykeykey", privateKeyString);
                Log.v("keykeykey", grpPrivateKeyStr); //2

                String uniqueId = UUID.randomUUID().toString();

                // Generate this patient group bind key
                String nospaceName = newName.replaceAll("\\s+", "");
                String GrpBindKeyStr = nospaceName + uniqueId;
                Log.i(TAG, "GrpBindKey before hash: " + GrpBindKeyStr);
                int hashGrpBindKey = (GrpBindKeyStr.hashCode() & 0x7FFFFFFF);
                String hashGrpBindKeyStr = Integer.toString(hashGrpBindKey);
                Log.i(TAG, "GrpBindKey after hash in String: " + hashGrpBindKeyStr);
                //Commented Out By Ezekiel
                //HealthStatusActivity.personalInfo.publicKey = publicKeyString;
                //HealthStatusActivity.personalInfo.grpPublicKey = grpPublicKeyStr; //2
                //HealthStatusActivity.personalInfo.privateKey = privateKeyString;
                //HealthStatusActivity.personalInfo.grpPrivateKey = grpPrivateKeyStr; //2
                String createByStr = ownerInfo.id+ownerInfo.name;
                patientsList = CaregiverDBTools.getPatientListByCaregiver(main1, user);


                // cyeh 12/3/21 cyeh add createByStr and newRole, and chnage position to nextPatientPosStr
                personalInfo = new PersonalInfo(uniqueId, newName, "", "", publicKeyString, privateKeyString, newEmail, newPhone, "", "", "",hashGrpBindKeyStr, grpPublicKeyStr, grpPrivateKeyStr, createByStr, newRole,"Y","");
                personalInfo.setRelation(ownerInfo.position);   // 12/4 cyeh change to setRelation of patient to owner's position
                PersonDataDBTools.addLocalPatientInfo(App.getContext(), personalInfo); //Ezekiel edited, might contain error on this line

                // cyeh 12/8/21 Subscribe caregiver/owner to the patient he just created
                messagingUtils.AddGroup(ownerInfo.name,hashGrpBindKeyStr+":"+newName);

                //cyeh 12/8/21 Need to add owner/caregiver to the patientCaregiverGroups table
                //andy 12/02/2022 Remove ParentPos from CaregiverInfo
                //CaregiverInfo caregiverInfo = new CaregiverInfo("", ownerInfo.id, ownerInfo.name, ownerInfo.publicKey, hashGrpBindKeyStr, grpPublicKeyStr, grpPrivateKeyStr, ownerInfo.position, "", "Y", "", CaregiverRequestResponse.STATUS.ACCEPTED.label);
                CaregiverInfo caregiverInfo = new CaregiverInfo("", ownerInfo.id, ownerInfo.name, ownerInfo.publicKey, hashGrpBindKeyStr, grpPublicKeyStr, grpPrivateKeyStr, "", "Y", "", CaregiverRequestResponse.STATUS.ACCEPTED.label);
                //AddPatientRecToMyDB(caregiverInfo,uniqueId,newName);
                CaregiverDBTools.addCaregiverForRemotePatient(main1, personalInfo, caregiverInfo);
                //main1.refreshPatientList();
                patientsList = CaregiverDBTools.getPatientListByCaregiver(main1, user);
                patientsMap.clear();
                for (int i = 0; i < patientsList.size(); i++) {
                    PersonalInfo p = patientsList.get(i);
                    patientsMap.put(p.position, p);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                acctCreate = false;
            }
            return null;

        }


        private void publicInfo() {
            String personalInfoString = ModelUtils.toString(personalInfo, new TypeToken<PersonalInfo>(){});
            Log.v("idddd", personalInfoString);
            String jsonStr = ModelUtils.createJson("PublicInfo", personalInfo.name, "message", personalInfoString);
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            progressDialog.dismiss();
            if (!MaxPosVal) {
                //Toast.makeText(getBaseContext(), "Error getting Max patient Position value in DB", Toast.LENGTH_LONG).show();
            }
            else if (!RSAKey) {
                //Toast.makeText(getBaseContext(), "RSA key pair error", Toast.LENGTH_LONG).show();
            }
            else if (!acctCreate) {
                //Toast.makeText(getBaseContext(), "Account creation failed", Toast.LENGTH_LONG).show();
            } else {
                //Toast toast = Toast.makeText(MainActivity1.this, "Patient Created", Toast.LENGTH_LONG);
                //toast.show();}
            //finish();
        }



    }


}}