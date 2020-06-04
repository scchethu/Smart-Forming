package com.example.scram.farming;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {
Switch sw;
 static String TOPIC="iot/xyz/pump";
  final   static String SUB1="iot/xyz/a";
  final   static String SUB2="iot/xyz/b";
    final   static String SUB3="iot/xyz/c";
    final   static String SUB4="iot/xyz/d";
    final   static String SUB5="iot/xyz/e";
    final   static String SUB6="iot/xyz/f";
    TextView gas,mois,hum,temp,fan,intruder;

    MqttAndroidClient c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MemoryPersistence mPer = new MemoryPersistence();
        String clientId = MqttClient.generateClientId();
        String brokerUrl = "tcp://broker.mqtt-dashboard.com:1883";

        c= new MqttAndroidClient(getApplicationContext(),brokerUrl, clientId);
        mois=findViewById(R.id.moisture);
        sw=findViewById(R.id.pump);
        temp=findViewById(R.id.temp);
        hum=findViewById(R.id.hum);
        gas=findViewById(R.id.gas);
        intruder=findViewById(R.id.intru);
        fan=findViewById(R.id.fan);
        try {

            c.connect();

        } catch (MqttException e) {
            Toast.makeText(this, "not connected", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        c.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                try {
                    c.subscribe(SUB1,0);
                    c.subscribe(SUB2,0);
                    c.subscribe(SUB3,0);
                    c.subscribe(SUB4,0);
                    c.subscribe(SUB5,0);
                    c.subscribe(SUB6,0);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Toast.makeText(MainActivity.this,new String(topic.toString()), Toast.LENGTH_SHORT).show();

                if(topic.compareTo(SUB1)==0){
                    mois.setText(new String(message.getPayload()));

                }
                else if(topic.compareTo(SUB2)==0){
                    temp.setText(new String(message.getPayload())+"°C");

                }
                else if(topic.compareTo(SUB3)==0){
                    hum.setText(new String(message.getPayload()));

                }
                else if(topic.compareTo(SUB4)==0){
                    gas.setText(new String(message.getPayload()));

                }
                else if(topic.compareTo(SUB5)==0){
                    intruder.setText(new String(message.getPayload()));

                }
                else if(topic.compareTo(SUB6)==0){
                    fan.setText(new String(message.getPayload()));

                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });








        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                // do something, the isChecked will be

                String msg = "on";
                MqttMessage m = new MqttMessage();
                m.setPayload(msg.getBytes());
                    try {
                        c.publish(TOPIC, m);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
                else {

                    String msg = "off";
                    MqttMessage m = new MqttMessage();
                    m.setPayload(msg.getBytes());
                    try {
                        c.publish(TOPIC, m);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });








    }
}
