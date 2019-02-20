package com.example.week_06_02;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> itemsAdapter;
    private ProgressBar progressBar;
    private ListView listview;
    private static final String _FILE = "numbers.txt";

// to show progress on the bar
    private void setProgressValue (final int progress) { progressBar.setProgress(progress); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
// will call myAsyncTask to do the work
    protected void createSomething(View view) { new myAsyncTask().execute(); }

    protected void loadSomething(View view) { new myAsyncTask2().execute(); }

    protected void clearSomething(View view) {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setProgressValue(0);
        itemsAdapter.clear();
    }
// myAsync Classes to run on background
    class myAsyncTask extends AsyncTask<String, String, String> {

        @Override
        //the progress bar needs to start from 0
        protected void onPreExecute() {
            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            progressBar.setProgress(0);
        }

        @Override
        // create the file in the background
        protected String doInBackground(String... strings) {
            FileOutputStream reading = null;
            try {
                // for space in memory
                 reading = openFileOutput( _FILE, MODE_PRIVATE);
                 int i = 1;
                while(i <= 10)
                 {
                 //define type every number in variable t
                    String t = Integer.toString(i) + "\n";
                    reading.write(t.getBytes());
                    setProgressValue(i * 10);
                    Thread.sleep(250);
                    i++;
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (reading != null) {
                try {
                    reading.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    // result from background
    protected void onPostExecute(String result) {
        Toast toast = Toast.makeText(MainActivity.this, "File " + _FILE + " is created ", Toast.LENGTH_LONG);
        toast.show();
    }

    }

    class myAsyncTask2 extends AsyncTask<String, String, ArrayList<String>>{

        @Override
        // progressBar set to 0
        protected void onPreExecute() {
            progressBar = (ProgressBar)findViewById(R.id.progressBar);
            progressBar.setProgress(0);
        }

// doInBackground()
        @Override
        // use an ArrayList
        protected ArrayList<String>doInBackground(String... strings) {
            //reading the space in memory
            FileInputStream open = null;
            // to store every one of the number read
            ArrayList<String> lines = null;
            try {
                //open the file in internal memory
                open = openFileInput(_FILE);
                FileInputStream input;
                BufferedReader reader;

                lines = new ArrayList<String>();
                final File file = new File(getFilesDir(), _FILE);
                input = new FileInputStream(file);

                //reading the info
                reader = new BufferedReader(new InputStreamReader(input));
                String everyNumber = reader.readLine();

                // read the file
                int i = 1;
                while (everyNumber != null) {
                    Log.d("Counting...", everyNumber);
                    lines.add(everyNumber);
                    Thread.sleep(250);
                    everyNumber = reader.readLine();
                    // progressBar advancing
                    setProgressValue(i * 10);
                    i++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (open != null) {
                    try {
                        open.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return lines;
        }
        //after doInBackground the results are:
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            listview = (ListView)findViewById(R.id.numsList);
            itemsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,
                    result);
            listview.setAdapter(itemsAdapter);
// toast the result of load
            Toast toast = Toast.makeText(MainActivity.this, "Loaded file: " + result,
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
