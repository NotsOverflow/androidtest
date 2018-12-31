package amc.androidmanagementcheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AskPassPhrase extends AppCompatActivity {

    EditText mEdit;
    SharedPreferences sharedpreferences;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_pass_phrase);

        mEdit = findViewById(R.id.editText);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String defaultSting = "hello";
        String encrypted = "";
        try {
            encrypted = AESUtils.encrypt(defaultSting);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.key = this.sharedpreferences.getString("key",  encrypted);
    }

    public void checkValidator(View v){

        String input = this.mEdit.getText().toString();
        String encrypted = "";
        try {
            encrypted = AESUtils.encrypt(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(encrypted.equals(this.key)){
            Intent i = new Intent(this, ChangePassKey.class);
            startActivity(i);
        }
        else{
            printT("validated");
            backToMain(v);
        }
    }

    public void backToMain(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void printT(String string){
        Toast.makeText(AskPassPhrase.this, string, Toast.LENGTH_LONG).show();
    }

}
