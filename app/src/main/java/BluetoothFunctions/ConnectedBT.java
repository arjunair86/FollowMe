package BluetoothFunctions;

import com.example.lenser.followme.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lenser on 3/26/17.
 */

public class ConnectedBT {
    private InputStream inputStream;
    private OutputStream outputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public static ConnectedBT getIOStream(){
        ConnectedBT connectedBT = new ConnectedBT();
        try {
            connectedBT.inputStream = MainActivity.mmSocket.getInputStream();
            connectedBT.outputStream = MainActivity.mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectedBT;
    }

    public void write(String message){
        message+='\n';
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
