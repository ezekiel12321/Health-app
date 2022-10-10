package test.circularlistviewapp.ActivityClass;

import static test.circularlistviewapp.Fragement.FamilyPageFragment.PATIENT_OBJ;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import test.circularlistviewapp.DataBase.DB;
import test.circularlistviewapp.Fragement.FamilyPageFragment;
import test.circularlistviewapp.JavaObjectClass.App;
import test.circularlistviewapp.R;
import test.circularlistviewapp.models.PersonalInfo;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Serializable {


    private final ArrayList<String> data1;
    private final ArrayList<String> lastName;
    private final DBHelper DataBase;
    private final Context context;
    public MainActivity1 main1;
    private final ArrayList<String> username;
    private boolean changed_dataset;
    private HashMap<String, PersonalInfo> patientsMap;
    private long num_entries;
    public MyAdapter(Context ct, ArrayList<String> s1, ArrayList<String> usernames, ArrayList<String> lastNames, HashMap<String, PersonalInfo> map, long totalCount){

        context = ct;
        main1 = (MainActivity1) ct;
        data1 = s1;
        DataBase = new DBHelper(this.context);
        username = usernames;
        lastName = lastNames;
        changed_dataset = false;
        patientsMap = map;
        num_entries = totalCount;


        //DataBase = DB;
        //usernames = usernames;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //(MainActivity1) main1 = (MainActivity1) App.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        if(changed_dataset == true){
            //notifyDataSetChanged();
            changed_dataset = false;
        }

        return new MyViewHolder(view);
    }


    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        /*
            String s = data1.get(position);
        String s2 = lastName.get(position);
        s = s + " " + s2;
        holder.patient1.setText(s);
         */


        Cursor res = DataBase.getdata();

        while(res.moveToNext())

        {


            String s = res.getString(1);



            String s2 = res.getString(2);

            s = s + " " + s2;
            holder.patient1.setText(s);



        }
        res.close();

        Cursor cursor = DataBase.getdata();
        cursor.moveToPosition(position);
        String s9;


        if(cursor.getColumnIndex("URI") != -1) {
            s9 = cursor.getString( cursor.getColumnIndex("URI") );
            if(s9 != null){
                //holder.profile_pic.setImageURI(Uri.fromFile(new File(s9)));
            }

            changed_dataset = true;
            //notifyDataSetChanged();
        }






        holder.patient1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                int position_to_send = position + 1;

                Intent intent = new Intent(context, HealthStatusActivity.class);
                intent.putExtra("IS_FAR", true);
                intent.putExtra("NTitile",position_to_send + "");
                intent.putExtra(FamilyPageFragment.PATIENT_OBJ, (Serializable) (patientsMap.get(position_to_send+"")));
                context.startActivity(intent);
            }
        });


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MainActivity2.class);
                intent.putExtra("data1",data1.get(position));
                intent.putExtra("position",position + "");
                intent.putExtra("username",username.get(position));

                //intent.putExtra("username",usernames.get(position));
                //intent.putExtra("database",DataBase);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int)num_entries;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView patient1;

        ConstraintLayout mainLayout;
        ImageView profile_pic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);



            patient1 = itemView.findViewById(R.id.textView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            profile_pic = itemView.findViewById(R.id.Profile_pic);
            //notifyDataSetChanged();
            /*
            access_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,MainActivity2.class);
                    context.startActivity(intent);
                }
            });
             */

        }
    }
}
