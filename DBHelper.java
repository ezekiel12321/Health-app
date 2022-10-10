package test.circularlistviewapp.ActivityClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context,"Userdata.db",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Userdetails(username Text primary key, name TEXT, nameLast TEXT, email TEXT, birthdate TEXT, primaryCarePhys TEXT, powerOfAttorney TEXT, location TEXT, levelComfort TEXT, emergencyContact TEXT, URI TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Userdetails");

    }

    public Boolean insertuserdata(String username, String name, String nameLast, String email, String birthdate, String primaryCarePhys, String powerOfAttorney, String location, String levelComfort, String emergencyContact, String URI){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("name",name);
        contentValues.put("nameLast", nameLast);
        contentValues.put("email", email);
        contentValues.put("birthdate",birthdate);
        contentValues.put("primaryCarePhys",primaryCarePhys);
        contentValues.put("powerOfAttorney", powerOfAttorney);
        contentValues.put("location",location);
        contentValues.put("levelComfort", levelComfort);
        contentValues.put("emergencyContact", emergencyContact);
        contentValues.put("URI",URI);

        long result = DB.insert("Userdetails",null,contentValues);
        return result != -1;

    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, "Userdetails");
        db.close();
        return count;
    }


    /*
    public Boolean adddata(String username,String data_to_add)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = DB.get
    }
     */



    public Boolean deletedata(String username){


        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails where name = ?", new String[] {username});
        if(cursor.getCount()>0) {
            long result = DB.delete("Userdetails","username=?", new String[]{username});
            return result != -1;
        }else
        {
            return false;
        }

    }


    public Cursor getdata(){


        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails", null);

        return cursor;



    }



    public Boolean updateuserdata(String username, String name, String nameLast, String email, String birthdate, String primaryCarePhys, String powerOfAttorney, String location, String levelComfort, String emergencyContact){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("nameLast",nameLast);
        contentValues.put("email",email);
        contentValues.put("birthdate",birthdate);
        contentValues.put("primaryCarePhys",primaryCarePhys);
        contentValues.put("powerOfAttorney",powerOfAttorney);
        contentValues.put("location",location);
        contentValues.put("levelComfort",levelComfort);
        contentValues.put("emergencyContact",emergencyContact);


        Cursor cursor = DB.rawQuery("Select * from Userdetails where username = ?", new String[] {username});
        if(cursor.getCount()>0) {
            long result = DB.update("Userdetails", contentValues, "name=?", new String[]{name});
            DB.update("Userdetails", contentValues, "email=?", new String[]{email});
            DB.update("Userdetails", contentValues, "birthdate=?", new String[]{birthdate});
            DB.update("Userdetails", contentValues, "primaryCarePhys=?", new String[]{primaryCarePhys});
            DB.update("Userdetails", contentValues, "powerOfAttorney=?", new String[]{powerOfAttorney});
            DB.update("Userdetails", contentValues, "location=?", new String[]{location});
            DB.update("Userdetails", contentValues, "levelComfort=?", new String[]{levelComfort});
            DB.update("Userdetails", contentValues, "emergencyContact=?", new String[]{emergencyContact});



            return result != -1;
        }else
        {
            return false;
        }

    }
    public Boolean updateuserdata(String username, String name, String nameLast, String email, String birthdate, String primaryCarePhys, String powerOfAttorney, String location, String levelComfort, String emergencyContact, String URI){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("nameLast",nameLast);
        contentValues.put("email",email);
        contentValues.put("birthdate",birthdate);
        contentValues.put("primaryCarePhys",primaryCarePhys);
        contentValues.put("powerOfAttorney",powerOfAttorney);
        contentValues.put("location",location);
        contentValues.put("levelComfort",levelComfort);
        contentValues.put("emergencyContact",emergencyContact);
        contentValues.put("URI", URI);


        Cursor cursor = DB.rawQuery("Select * from Userdetails where username = ?", new String[] {username});
        if(cursor.getCount()>0) {
            long result = DB.update("Userdetails", contentValues, "name=?", new String[]{name});
            DB.update("Userdetails", contentValues, "email=?", new String[]{email});
            DB.update("Userdetails", contentValues, "birthdate=?", new String[]{birthdate});
            DB.update("Userdetails", contentValues, "primaryCarePhys=?", new String[]{primaryCarePhys});
            DB.update("Userdetails", contentValues, "powerOfAttorney=?", new String[]{powerOfAttorney});
            DB.update("Userdetails", contentValues, "location=?", new String[]{location});
            DB.update("Userdetails", contentValues, "levelComfort=?", new String[]{levelComfort});
            DB.update("Userdetails", contentValues, "emergencyContact=?", new String[]{emergencyContact});
            DB.update("Userdetails", contentValues, "URI=?", new String[]{URI});



            return result != -1;
        }else
        {
            return false;
        }

    }
    public Boolean updateuserdata(String username, String URI){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("URI",URI);



        Cursor cursor = DB.rawQuery("Select * from Userdetails where username = ?", new String[] {username});
        if(cursor.getCount()>0) {
            long result = DB.update("Userdetails", contentValues, "URI=?", new String[]{URI});




            return result != -1;
        }else
        {
            return false;
        }

    }


}
