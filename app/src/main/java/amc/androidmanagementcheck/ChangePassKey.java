package amc.androidmanagementcheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePassKey extends AppCompatActivity {
    EditText firstEdit;
    EditText secondEdit;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_key);

        firstEdit = findViewById(R.id.firstText);
        secondEdit = findViewById(R.id.secondText);

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedpreferences.edit();

    }

    public void validate(View v){
        String field1 = this.firstEdit.getText().toString();
        String field2 = this.secondEdit.getText().toString();

        if(field1.equals("")) backToMain(v);
        if(field1.equals(field2)){
            String encrypted = "";
            try {
                encrypted = AESUtils.encrypt(field1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.putString("key",encrypted);
            editor.commit();
        }
        backToMain(v);
    }



    public void backToMain(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    private void printT(String string){
        Toast.makeText(ChangePassKey.this, string, Toast.LENGTH_LONG).show();
    }
}
