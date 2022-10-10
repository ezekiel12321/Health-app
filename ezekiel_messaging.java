package test.circularlistviewapp.ActivityClass;

import static test.circularlistviewapp.JavaObjectClass.App.getContext;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import test.circularlistviewapp.Fragement.MessageFragment;
import test.circularlistviewapp.JavaObjectClass.App;
import test.circularlistviewapp.R;
import test.circularlistviewapp.utils.DirectMessaging;
import test.circularlistviewapp.utils.MessagingUtils;

public class ezekiel_messaging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = findViewById(R.id.back_button2);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),HealthStatusActivity.class);
                startActivity(intent);
            }
        });
        setContentView(R.layout.ezekiel_message_fragment);

        Intent intent = getIntent();
        String Position = intent.getStringExtra("position");

        MainActivity1 main1 = (MainActivity1) getContext();
        MessagingUtils messagingUtils = main1.getMessagingUtils();
        DirectMessaging directMessaging = main1.getDirectMessaging();
        FragmentManager fm = main1.getSupportFragmentManager();

        Fragment fragment = MessageFragment.newInstance(null);
        messagingUtils.register(((MessageFragment) fragment));
        ((MessageFragment) fragment).addMessagingUtil(messagingUtils);
        ((MessageFragment) fragment).addDirectMessaging(directMessaging);
        setContentView(R.layout.ezekiel_message_fragment);
        //CaregiverMainActivity main1 = (CaregiverMainActivity) App.getContext();
        //int Position = main1.getPosition();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.frame_layout,fragment, "fragment:"+Position);
        transaction.commitAllowingStateLoss();
    }
    @Override public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity1.class);
        startActivity(intent);
    }

}