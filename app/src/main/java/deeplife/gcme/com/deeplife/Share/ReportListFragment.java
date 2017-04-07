package deeplife.gcme.com.deeplife.Share;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.DeepLife;
import deeplife.gcme.com.deeplife.Models.ReportItem;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.SyncService.SyncService;

/**
 * Created by BENGEOS on 3/27/16.
 */
public class ReportListFragment extends Fragment {
    private static RecyclerView myRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static Context myContext;
    private Button SendReport;
    private TextView tv_last_date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_list_page,container,false);
        myRecyclerView = (RecyclerView) view.findViewById(R.id.report_recycler_view);
        tv_last_date = (TextView) view.findViewById(R.id.tv_last_report_date);
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);


        tv_last_date.setText(getLastReportSentDate());
        Log.i("PreferenceTest",getLastReportSentDate());

       // ArrayList<ReportItem> items = DeepLife.myDATABASE.get_All_Report();
        ArrayList<ReportItem> items = new ArrayList<>();

        ReportItem reportItem = new ReportItem();
        reportItem.setId("1");
        reportItem.setCategory("1");
        reportItem.setQuestion("Question 1");
        reportItem.setValue("1");
        reportItem.setReport_ID("1");

        items.add(reportItem);

        reportItem = new ReportItem();
        reportItem.setId("2");
        reportItem.setCategory("2");
        reportItem.setQuestion("Question 2");
        reportItem.setValue("2");
        reportItem.setReport_ID("2");

        items.add(reportItem);

        reportItem = new ReportItem();
        reportItem.setId("2");
        reportItem.setCategory("2");
        reportItem.setQuestion("Question 2");
        reportItem.setValue("2");
        reportItem.setReport_ID("2");

        items.add(reportItem);

        reportItem = new ReportItem();
        reportItem.setId("2");
        reportItem.setCategory("2");
        reportItem.setQuestion("Question 2");
        reportItem.setValue("2");
        reportItem.setReport_ID("2");

        items.add(reportItem);

        reportItem = new ReportItem();
        reportItem.setId("2");
        reportItem.setCategory("2");
        reportItem.setQuestion("Question 2");
        reportItem.setValue("2");
        reportItem.setReport_ID("2");

        items.add(reportItem);

        reportItem = new ReportItem();
        reportItem.setId("2");
        reportItem.setCategory("2");
        reportItem.setQuestion("Question 2");
        reportItem.setValue("2");
        reportItem.setReport_ID("2");

        items.add(reportItem);

        reportItem = new ReportItem();
        reportItem.setId("2");
        reportItem.setCategory("2");
        reportItem.setQuestion("Question 2");
        reportItem.setValue("2");
        reportItem.setReport_ID("2");

        items.add(reportItem);
        mAdapter = new ReportListAdapter(getActivity(),items);
        myRecyclerView.setAdapter(mAdapter);
        myContext = getActivity();
        SendReport = (Button) view.findViewById(R.id.btn_report_send);
        SendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmSend();
            }
        });
        return view;
    }

    public void send(){
        ArrayList<ReportItem> ReportLists = ReportListAdapter.ReportLists;
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < ReportLists.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(Database.ReportColumn.ID.toString(), ReportLists.get(i).getReport_ID());
            cv.put(Database.ReportColumn.VALUE.toString(), ReportLists.get(i).getValue());
            cv.put(Database.ReportColumn.DATE.toString(), cal.getTime().toString());
            Long val = DeepLife.myDATABASE.insert(deeplife.gcme.com.deeplife.Database.Database.Table_Reports, cv);
            if (val > 0) {
                ContentValues log = new ContentValues();
                log.put(Database.LogsColumn.TYPE.toString(), "Report");
                log.put(Database.LogsColumn.TASK.toString(), SyncService.API_SERVICE.SEND_REPORT.toString());
                log.put(Database.LogsColumn.VALUE.toString(), val);
                long x = DeepLife.myDATABASE.insert(deeplife.gcme.com.deeplife.Database.Database.Table_LOGS, log);

            }
        }

        setLastReportSentDate();
        tv_last_date.setText(getLastReportSentDate());
        Show_Dialog("Your report has sent successfully!");
    }

    public static void Show_Dialog(String message) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setTitle(R.string.app_name).setMessage(message)
                .setPositiveButton("Ok ", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .show();
    }


    public void confirmSend(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        send();
                        //Yes button clicked
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Deep Life:").setMessage("Are you sure you want to send this report? ")
            .setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener)//.setNeutralButton(" ", dialogClickListener)
            .show();
    }



    public static String getLastReportSentDate(){
        SharedPreferences settings = myContext.getSharedPreferences(DeepLife.PREFS_LAST_SENT_DATE, 0);
        String lastDate = settings.getString("last_date","");
        return lastDate;
    }

    public static void setLastReportSentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Log.i("PreferenceTest",formattedDate);

        SharedPreferences settings = myContext.getSharedPreferences(DeepLife.PREFS_LAST_SENT_DATE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("last_date",formattedDate);
        editor.commit();
    }

}


