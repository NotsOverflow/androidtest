package amc.androidmanagementcheck;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;

    private boolean locked;
    private int[]   sequence = {0,0,1,1};
    private long    timestamp;
    private int     index = 0;
    private int     ty;
    List<Item> itemList;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locked = true ;
        timestamp = System.currentTimeMillis()/1000;


        itemList = getListData();
        final ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new ItemListAdapter(this, itemList));

        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Object o = listView.getItemAtPosition(position);
                //Item item = (Item) o;
                //Toast.makeText(MainActivity.this, "Selected : " + position + " " + item, Toast.LENGTH_LONG).show();
                clicked(position);
            }
        });

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();
        ty =  this.sharedpreferences.getInt("ty",  0);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

        SmsReceiver myReceiver = new  SmsReceiver();

        this.registerReceiver(myReceiver, new IntentFilter(
                "android.provider.Telephony.SMS_RECEIVED"));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SMS_RECEIVE) {
            // YES!!
            Log.i("TAG", "MY_PERMISSIONS_REQUEST_SMS_RECEIVE --> YES");
        }
    }

    private  List<Item> getListData() {
        List<Item> list = new ArrayList<Item>();
        Item net = new Item("Network", "00:22:45:EF:35", "globe");
        Item om = new Item("Online Messages", "n°551-15-351-412" ,"message");
        Item serial = new Item("Serial", "s°5521112-45-1995", "phone");


        list.add(net);
        list.add(om);
        list.add(serial);

        return list;
    }
    private void clicked(int position){
        if(locked) lockedStuff(position);
        else{
            if(position == 0) {
                Intent i = new Intent(this, AskPassPhrase.class);
                startActivity(i);
            }
            else{
                locked = true;
                index = 0;
                typlus();
            }
        }
    }
    private void lockedStuff(int position){
        long delta = (System.currentTimeMillis() / 1000) - timestamp;
        timestamp += delta;
        if (delta > 10) index = 0;

        if (position == sequence[index]) {
            index++;
            if (index == sequence.length) {
                printT(""+ty);
                index = 0;
                tyclear();
                locked = false;
            }
        } else {
            index = 0;
            typlus();
        }
    }
    private void printT(String string){
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }

    private void typlus(){
        ty++;
        editor.putInt("ty", ty);
        editor.commit();
    }
    private void tyclear(){
        ty = 0;
        editor.putInt("ty", ty);
        editor.commit();
    }
}
