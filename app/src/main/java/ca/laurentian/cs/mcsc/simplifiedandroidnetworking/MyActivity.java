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
    //The inputMsg correspond to spinnerContents ordering. Very important that it remain so.
    public enum inputMsg {getBroadcastAddress, receiveBroadcast, sendBroadcast,
                            getWifiIP, sendUDP, receiveUDP,
                            sendTCP, receiveTCP, startTCPServer,
                            discover, declare, failed}

    private String spinnerContents[] = {"Get Broadcast Address", "Receive Broadcast",
            "Send Broadcast", "Get Wifi IP", "Send UDP Packet", "Receive UDP Packet",
            "Send TCP Packet", "Receive TCP Packet", "Start TCP Server", "discover",
            "declare"};

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

        Log.d("Main Tag", "Reached onClickStartService Start");
        startNetwork(inputMsg.values()[((Spinner)findViewById(R.id.spinner)).getSelectedItemPosition()]);

        Log.d("Main Tag", "Reached onClickStartService End");
        //String selected = (String)((Spinner)findViewById(R.id.spinner)).getSelectedItem();
        //((TextView)findViewById(R.id.PayloadText)).setText(selected + " "
        //        + ((Spinner)findViewById(R.id.spinner)).getSelectedItemPosition());
    }

    /**
     * Starts the Networking Service. Nicely removes a few extra lines of code
     * @param msg The action you want the service to perform.
     */
    private void startNetwork(inputMsg msg)
    {
        Log.d("Main Tag", "Reached startNetwork Start");
        Intent intent = new Intent(this, NetworkingService.class);
        intent.putExtra(NetworkingService.PARAM_IN_MSG, msg);

        String ip = ((TextView)findViewById(R.id.ip)).getText().toString();
        //No error checking to see if a valid IP is entered. They could technically enter a URL
        if(!ip.equals(""))
        {
            intent.putExtra(NetworkingService.PARAM_IN_IP, ip);
        }
        //If a payload is being sent, then and only then include the payload, even if null
        if(msg.equals(inputMsg.sendBroadcast) || msg.equals(inputMsg.sendTCP)
                || msg.equals(inputMsg.sendUDP))
        {
            intent.putExtra(NetworkingService.PARAM_IN_PAYLOAD, ((TextView)findViewById(R.id.PayloadText)).getText().toString());
        }

        startService(intent);

        Log.d("Main Tag", "Reached startNetwork End");
    }


    /**
     * Receive Intent Broadcasts from a running service.
     */
    public class ResponseReceiver extends BroadcastReceiver
    {
        public static final String ACTION_RESP = "MSG_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("Response Receiver", "Reached RR Start");
            //Display the MSG to user in the PayloadText that sends messages
            ((TextView) findViewById(R.id.PayloadText)).setText(
                    intent.getStringExtra(NetworkingService.PARAM_OUT_MSG));

            Toast.makeText(getBaseContext(), "Service Replied", Toast.LENGTH_SHORT).show();

            Log.d("Response Receiver", "Reached RR End");
        }
    }
}
