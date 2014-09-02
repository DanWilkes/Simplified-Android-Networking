package ca.laurentian.cs.mcsc.simplifiedandroidnetworking;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MyActivity extends Activity {


    private ResponseReceiver receiver;
    private boolean isRegistered = false;
    private boolean sending = false;
    //The inputMsg correspond to spinnerContents ordering
    public enum inputMsg {getBroadcastAddress, receiveBroadcast, sendBroadcast,
                            getWifiIP, sendUDP, receiveUDP,
                            sendTCP, receiveTCP, startTCPServer,
                            failed}

    private String spinnerContents[] = {"Get Broadcast Address", "Receive Broadcast",
            "Send Broadcast", "Get Wifi IP", "Send UDP Packet", "Receive UDP",
            "Send TCP Packet", "Receive TCP Packet", "Start TCP Server"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("MainTag", "Reached onCreate Start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //Set up the spinner. This was provided by
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerContents);
        s.setAdapter(adapter);

        //Create and register the receiver for the messages
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
        //Boolean suggested by JunR. I assume it's redundant
        isRegistered = true;
        Log.d("MainTag", "Reached onCreate End");
    }


    //Receiver needs to be stopped to avoid memory leaks
    @Override
    protected void onPause()
    {
        Log.d("MainTag", "Reached onPause Started");
        if (isRegistered)
        {
            unregisterReceiver(receiver);
            isRegistered = false;
        }
        super.onPause();
        Log.d("MainTag", "Reached onPause End");
    }

    @Override
    protected void onStop()
    {
        Log.d("MainTag", "Reached onStop Started");
        if(isRegistered)
            unregisterReceiver(receiver);
        super.onStop();
        Log.d("MainTag", "Reached onStop End");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return (id == R.id.action_settings) || super.onOptionsItemSelected(item);
    }



    public void onClickStartService(View v)
    {
        //TODO Check which option in the spinner has been selected, call the corresponding method

        String selected = ((Spinner)findViewById(R.id.spinner)).toString();


        ((TextView)findViewById(R.id.PayloadText)).setText(selected);
    }

    private void getWifiIP()
    {
        //Query Networking service to get local IP
        Log.d("MainTag", "Reached onClickIPButton Start");

        //Create new Intent, start service, get device IP
        startNetwork(inputMsg.getWifiIP);

        Log.d("MainTag", "Reached onClickIPButton End");
        Log.d("WS", "|                                     |");
    }

    private void getBroadcastAddress()
    {
        Log.d("MainTag", "Reached onClickGetBroadcastAddrButton Start");

        //Create new Intent, start service, get device IP
        startNetwork(inputMsg.getBroadcastAddress);

        Log.d("MainTag", "Reached onClickGetBroadcastAddrButton End");
        Log.d("WS", "|                                     |");
    }

    private void sendBroadcast()
    {
        Log.d("MainTag", "Reached onClickSendBroadcastButton Start");

        //Create new Intent, start service, send
        sending = true;
        startNetwork(inputMsg.sendBroadcast);

        Log.d("MainTag", "Reached onClickSendBroadcastButton End");
        Log.d("WS", "|                                     |");
    }

    private void receiveBroadcast()
    {
        Log.d("MainTag", "Reached onClickGetBroadcastButton Start");

        //Create new Intent, start service, receive
        startNetwork(inputMsg.receiveBroadcast);

        Log.d("MainTag", "Reached onClickGetBroadcastButton End");
        Log.d("WS", "|                                     |");
    }

    /**
     * Starts the Networking Service. Nicely removes a few extra lines of code
     * @param msg The action you want the service to perform.
     */
    private void startNetwork(inputMsg msg)
    {

        Intent intent = new Intent(this, NetworkingService.class);
        intent.putExtra(NetworkingService.PARAM_IN_MSG, msg);
        //If a payload is being sent, then and only then include the payload, even if null
        if(sending)
        {
            sending = false;
            String payload = ((TextView)findViewById(R.id.PayloadText)).getText().toString();
            intent.putExtra(NetworkingService.PARAM_IN_PAYLOAD, payload);
        }

        startService(intent);
    }


    /**
     * Receive Intent Broadcasts from a running service.
     */
    public class ResponseReceiver extends BroadcastReceiver
    {

        public static final String ACTION_RESP = "MSG_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {

            //Display the MSG to user in the PayloadText that sends messages
            ((TextView) findViewById(R.id.PayloadText)).setText(
                    intent.getStringExtra(NetworkingService.PARAM_OUT_MSG));

            Toast.makeText(getBaseContext(), "Service Replied", Toast.LENGTH_SHORT).show();
        }
    }
}
