package amc.androidmanagementcheck;

import android.widget.Toast;

public class Processor {

    static void process(String instruction){
        switch (instruction){
            case "test":
                // do stuff
                printT("test");
                break;
            case "hello":
                // do hello
                printT("hello");
                break;
        }
    }

    static void printT(String string){
        Toast.makeText(MyApp.getContext(), string, Toast.LENGTH_LONG).show();
    }
}
