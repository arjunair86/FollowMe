package BluetoothFunctions;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.example.lenser.followme.MainActivity;
import com.example.lenser.followme.MyBluetoothService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lenser on 3/26/17.
 */

public class ConnectedBT extends Thread {
    private InputStream inputStream;
    private OutputStream outputStream;
    Activity activity;

    public ConnectedBT(InputStream inputStream, OutputStream outputStream, Activity activity) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.activity = activity;
    }

    public void write(String message){
        message+='\n';
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final byte[] mmBuffer = new byte[1];
        while (true) {
            try {
                // Read from the InputStream.
                inputStream.read(mmBuffer);

                final String mess = new String(mmBuffer);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyBluetoothService.tvRcvText.setText(mess);
                        if (mess.compareTo("V") == 0){
                            Vibrator vibrator = (Vibrator)activity.getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(300);
                        }
                    }
                });
                Log.d("Service", "String: "+ mess);
                mmBuffer[0] = '\0';
            } catch (IOException e) {
                Log.d("Service", "Input stream was disconnected", e);
                break;
            }
        }
    }

    public void disconnect(){
        if(MainActivity.mmSocket != null){
            try {
                MainActivity.mmSocket.close();
                activity.finish();
            } catch (IOException e) {
                Log.d("Service", "Socket could not be closed");
                e.printStackTrace();
            }
        }
    }
}
