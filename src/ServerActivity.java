package com.example.comp4985androidgpsassignment3;

/**
 * Created by hsiehrobin on 2014-03-01.
 */
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ServerActivity extends Activity {

    private ServerSocket ListeningSocket;
    private Socket NewClientSocket;

    ToggleButton mListenButton;
    //EditText clientCoord;
    TextView clientCoord;


    class ServerThread implements Runnable{

        public void run(){
            try{
                ListeningSocket = new ServerSocket(7700);

                // Listen for connections and accept
                NewClientSocket = ListeningSocket.accept();
                Log.d("AcceptSocket:", NewClientSocket.getRemoteSocketAddress().toString());
            }catch (IOException e){
                e.printStackTrace();
            }

            while(true){
                try{
                    // Get the client string
                    DataInputStream in = new DataInputStream (NewClientSocket.getInputStream());
                    Log.d("ClientSocketData:", in.readUTF());
                    clientCoord.append(in.readUTF().toString());
                    NewClientSocket.close();
                }
                catch (SocketTimeoutException s){
                    System.out.println ("Socket timed out!");
                    break;
                }catch(IOException e){
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from client_activity.xml
        setContentView(R.layout.server_activity);

        mListenButton = (ToggleButton) findViewById(R.id.bListen);

        clientCoord = (TextView)findViewById(R.id.ClientsText);

        mListenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListenButton.isChecked()) {
                    //socket listener
                    ServerThread commThread = new ServerThread();
                    new Thread(commThread).start();
                }
            }
        });
    }
}
