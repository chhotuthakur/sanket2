package com.sanket.royaltele;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fcmsender.FCMSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, FCMSender.ResponseListener {
    String[] services={"BSNL FIBER","RAILWIRE"};
    Spinner spintitle;
    String ttime ,ttitle,ccontent;
    Button btn ,send;
    ImageButton time;
    TextView texttime;
    EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content=(EditText)findViewById(R.id.content);
        time =(ImageButton) findViewById(R.id.time);
        texttime =(TextView)findViewById(R.id.textView) ;
        spintitle =(Spinner)findViewById(R.id.spinner);
        spintitle.setOnItemSelectedListener(this);
        send=(Button)findViewById(R.id.button);





        ArrayAdapter service = new ArrayAdapter(this, R.layout.spin_layout,services);
        spintitle.setAdapter(service);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendNoti();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void showTimePicker(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {

            private TimePicker timePicker;
            private int selectedHour;
            private int selectedMinute;



            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                this.timePicker = timePicker;
                this.selectedHour = selectedHour;
                this.selectedMinute = selectedMinute;
                texttime.setText(selectedHour+":"+selectedMinute);
                ttime=texttime.getText().toString();



            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ttitle =spintitle.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void sendNoti() throws JSONException {

        JSONObject data=new JSONObject();
        data.put("title",ttitle+" @ " + ttime);
        data.put("message", content.getText().toString());
        data.put("type", "new_message");

        FCMSender fcmSender= new FCMSender.Builder()
                .serverKey("AAAAXEjv0fg:APA91bH26w6e6Ovaq6CLE6WG9SF1RqGa1z3lZbBI-ED-KTSG63Qc19vhxXlIy732XD3vvF1OLuGSQN835IhFruBARiySkf4kRrgJiNi7LhvYFYPGXmr2_06gg-60VTOxe5PqjErqecEl")
                .toTokenOrTopic("/topics/all") //use either topic or user registration token
//             .toMultipleTokens(listOfToken)
                .responseListener(this)
//             .setTimeToLive(30) // 0 to 2,419,200 seconds (4 weeks)
//             .setDryRun(false)  //test a request without actually sending a message.
                .setData(data)
                .build();
        fcmSender.sendPush(this);
    }

    @Override
    public void onFailure(int i, String s) {
        Toast.makeText(this,"sending Failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(String s) {
        Toast.makeText(this,"Send Success", Toast.LENGTH_LONG).show();

    }
}