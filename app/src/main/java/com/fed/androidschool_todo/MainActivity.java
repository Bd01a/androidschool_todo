package com.fed.androidschool_todo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReadToDo readToDo = new ReadToDo();
        readToDo.execute();

         findViewById(R.id.btn_add_activity).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                 final EditText editText = new EditText(MainActivity.this);
                 alertDialogBuilder.setView(editText);
                 alertDialogBuilder.setTitle(getString(R.string.alert_dialog_label));
                 alertDialogBuilder.setPositiveButton(R.string.alert_dialog_positiv_button, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         AddToDo addToDo = new AddToDo();
                         addToDo.execute(editText.getText().toString());
                     }
                 });
                 alertDialogBuilder.show();

             }
         });

        mRecyclerView = findViewById(R.id.recyclerview_todo);



    }

    private void showPopupMenu(View v, ToDoSchema.ToDoElement element) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popup_menu_item);
        final ToDoSchema.ToDoElement elemFinal = element;
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.item_delete) {
                    DeleteToDo deleteToDo = new DeleteToDo();
                    deleteToDo.execute(elemFinal);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    class AddToDo extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            SQLiteDatabase sqLiteDatabase = new ToDoHelper(MainActivity.this).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ToDoSchema.Cols.ACTIVITY, strings[0]);
            contentValues.put(ToDoSchema.Cols.FLAG, 0);
            sqLiteDatabase.insert(ToDoSchema.NAME, null, contentValues);
            sqLiteDatabase.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ReadToDo readToDo = new ReadToDo();
            readToDo.execute();
        }
    }

    class ReadToDo extends AsyncTask<Void,Void,Cursor>{


        @Override
        protected Cursor doInBackground(Void... voids) {
            SQLiteDatabase sqLiteDatabase = new ToDoHelper(MainActivity.this).getReadableDatabase();
            String[] projection = {BaseColumns._ID, ToDoSchema.Cols.ACTIVITY, ToDoSchema.Cols.FLAG};
            return sqLiteDatabase.query(ToDoSchema.NAME, projection,
                    null, null,null, null,null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            List<ToDoSchema.ToDoElement> activities = new ArrayList<>();

            try {
                while (cursor.moveToNext()) {
                    String activity = cursor.getString(cursor.getColumnIndex(ToDoSchema.Cols.ACTIVITY));
                    int flag = cursor.getInt(cursor.getColumnIndex(ToDoSchema.Cols.FLAG));
                    ToDoSchema.ToDoElement element = new ToDoSchema.ToDoElement(activity, flag);
                    activities.add(element);
                }
            }
            finally {
                cursor.close();
            }
            ToDoAdapter toDoAdapter = new ToDoAdapter(activities, MainActivity.this,
                    new ToDoAdapter.OnDataChanged() {
                        @Override
                        public void onUpdate(ToDoSchema.ToDoElement element) {
                            UpdateToDo updateToDo = new UpdateToDo();
                            updateToDo.execute(element);
                        }

                        @Override
                        public void onDelete(ToDoSchema.ToDoElement element, View v) {
                            showPopupMenu(v, element);
                        }
                    });
            mRecyclerView.setAdapter(toDoAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(linearLayoutManager);


        }
    }
    class UpdateToDo extends AsyncTask<ToDoSchema.ToDoElement,Void, Void> {
        @Override
        protected Void doInBackground(ToDoSchema.ToDoElement... elements) {
            SQLiteDatabase sqLiteDatabase = new ToDoHelper(MainActivity.this).getWritableDatabase();
            ContentValues values = new ContentValues();
            int flag = elements[0].isFlag() ? 1:0;
            values.put(ToDoSchema.Cols.FLAG, flag);
            String selection = ToDoSchema.Cols.ACTIVITY + " = ?";
            String[] selectionArgs = {elements[0].getActivity()};
            sqLiteDatabase.update(ToDoSchema.NAME, values, selection, selectionArgs);
            return null;
        }
    }
    class DeleteToDo extends AsyncTask<ToDoSchema.ToDoElement,Void, Void> {
        @Override
        protected Void doInBackground(ToDoSchema.ToDoElement... elements) {
            SQLiteDatabase sqLiteDatabase = new ToDoHelper(MainActivity.this).getWritableDatabase();
            String selection = ToDoSchema.Cols.ACTIVITY + " = ?";
            String[] selectionArgs = {elements[0].getActivity()};
            sqLiteDatabase.delete(ToDoSchema.NAME,selection, selectionArgs);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ReadToDo readToDo = new ReadToDo();
            readToDo.execute();
        }
    }
}
