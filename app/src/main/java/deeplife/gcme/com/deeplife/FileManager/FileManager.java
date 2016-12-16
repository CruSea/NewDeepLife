package deeplife.gcme.com.deeplife.FileManager;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by bengeos on 12/16/16.
 */

public class FileManager {
    private Context myContext;
    private File myFile;
    public FileManager(Context context){
        myContext = context;
        if(isExternalStorageWritable()){
            myFile = new File(Environment.getExternalStorageDirectory(), "DeepLife");
            if(!myFile.isDirectory()){
                myFile.mkdir();
            }
        }
        else{
            myFile = new File(context.getFilesDir().getPath(), "DeepLife");
            if(!myFile.isDirectory()){
                myFile.mkdir();
            }
        }
    }
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
