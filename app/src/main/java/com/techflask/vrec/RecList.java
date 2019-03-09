package com.techflask.vrec;


/**
 * Created by IntelliJ IDEA.
 * User: Delph
 * Date: 11/4/10
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.util.Date;

import static android.provider.MediaStore.AUTHORITY;


public class RecList extends Activity {

    private ListView l1;
    private static File[] fileList;
    private static File storageDir;
    EfficientAdapter adapter;


    public static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        Context cont;

        public EfficientAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            cont = context;
            fileList = getRecorderFiles();
        }

        public void adapter_refresh() {
            fileList = getRecorderFiles();
        }

        public int getCount() {
            if (fileList == null)
                return 0;

            return fileList.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_view, null);
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.tvFileName);
                holder.text2 = (TextView) convertView.findViewById(R.id.tvDateTime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text1.setText(fileList[position].getName());
            holder.text2.setText(new Date(fileList[position].lastModified()).toString());
            return convertView;
        }

        static class ViewHolder {
            TextView text1;
            TextView text2;
        }

        //Gets List of all recorded files
        public File[] getRecorderFiles() {
            try {
                storageDir = Environment.getExternalStoragePublicDirectory(cont.getString(R.string.FILEPATH));
            } catch (Exception e) {
                storageDir = new File(Environment.getExternalStoragePublicDirectory(cont.getString(R.string.FILEPATH)) + "");
                storageDir.mkdir();
                //Toast.makeText(cont,"New directory has been created: "+ storageDir,100).show();
            }finally {
                if(storageDir == null)
                    return new File[0];
                else
                    return storageDir.listFiles();
            }
        }
    }

    public void ListViewSetup() {
        l1 = (ListView) findViewById(R.id.listRec);
        storageDir = Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH));

        if (!storageDir.isDirectory()) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(this.getString(R.string.FILEPATH)) + "");
            storageDir.mkdir();
            //Toast.makeText(this,"New directory has been created: "+ storageDir,100).show();
        }
        l1.setAdapter(new EfficientAdapter(this));

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                File file = new File(fileList[position].getAbsolutePath());
                intent.setDataAndType(Uri.fromFile(file), "audio/*");
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // allow to continue work with old URI file instead of new FileProvider API (API 24+)
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        setContentView(R.layout.rlist);
        ListViewSetup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        l1 = null;
        ListViewSetup();
        registerForContextMenu(l1);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.xml.menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        adapter = (EfficientAdapter) l1.getAdapter();

        switch (item.getItemId()) {
            case R.id.rename_file:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText input = new EditText(this);
                alert.setView(input);
                //alert.setTitle("Menu:");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString().trim();
                        renFile(fileList[menuInfo.position], value);
                        adapter.adapter_refresh();
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                alert.show();
                break;


            case R.id.delete_file:
                delFile(fileList[menuInfo.position]);
                adapter.adapter_refresh();
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);

    }

    public void delFile(File fileName) {

        // Make sure the file or directory exists and isn't write protected
        if (!fileName.exists())
            throw new IllegalArgumentException(
                    "Delete: no such file or directory: " + fileName);

        if (!fileName.canWrite())
            throw new IllegalArgumentException("Delete: write protected: "
                    + fileName);

        // If it is a directory, make sure it is empty
        if (fileName.isDirectory()) {
            String[] files = fileName.list();
            if (files.length > 0)
                throw new IllegalArgumentException(
                        "Delete: directory not empty: " + fileName);
        }

        // Attempt to delete it
        boolean success = fileName.delete();

        if (!success)
            throw new IllegalArgumentException("Delete: deletion failed");
    }

    public void renFile(File fileName, String newFileName) {

        // make normal path to a new file (like an old one) for the old file
        // check what extention was previous file and add it to name of new file.
        if (fileName.exists()) {
            String[] ext = fileName.getName().split("\\.");
            File newFile = new File(storageDir + "/" + newFileName + "." + ext[1]);
            boolean success = fileName.renameTo(newFile);

            if (!success)
                throw new IllegalArgumentException("Delete: deletion failed");
        }
    }

    public void DeleteAllFiles() {


    }
}