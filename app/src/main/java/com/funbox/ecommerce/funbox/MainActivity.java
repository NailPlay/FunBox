package com.funbox.ecommerce.funbox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private ViewFlipper viewFlipper;
    private ListView lv1;
    private float initialX;
    ArrayList<FieldCsv> list;
    int size = 0;
    TextView tv1, tv2, tv3;
    Button btn1;
    WorkCSVFiles csvFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<FieldCsv>();
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        lv1 = (ListView) findViewById(R.id.listView);
        TabHost tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("store");
        tabSpec.setContent(R.id.tab_store);
        tabSpec.setIndicator("Store-Front");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("back");
        tabSpec.setContent(R.id.tab_end);
        tabSpec.setIndicator("Back-End");
        tabHost.addTab(tabSpec);
        AssetManager assetManager = getAssets();
        try {

            InputStream inputStream = new FileInputStream(Environment.getExternalStorageDirectory()
                    + "/data.csv");
            csvFiles = new WorkCSVFiles(inputStream);
            list = csvFiles.readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            addViewFlipperItem(list.get(i).getTitle(), list.get(i).getPrice(), list.get(i).getNumber());
        }
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);

        DataAdapter adapter = new DataAdapter(this, list);
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editDialog(i);
            }
        });

    }

    public void addViewFlipperItem(String name, String price, int number) {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.flippercontainer, null);
        viewFlipper.addView(view);
        tv1 = (TextView) view.findViewById(R.id.name);
        tv2 = (TextView) view.findViewById(R.id.price);
        tv3 = (TextView) view.findViewById(R.id.number);
        tv1.setText(name);
        tv2.setText("Цена: " + price);
        tv3.setText("Количество: " + Integer.toString(number));
        btn1 = (Button) view.findViewById(R.id.buttonBuy);
        btn1.setText("Buy!");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = viewFlipper.getDisplayedChild();
                testShowList();
                Log.d("test", "==========================");
                // Жмем купить и количество товара уменьшается на 1
                list.get(id).increment();
                // После того как пользователь уменьшил товар, это нужно показать на экране, то есть обновит View представление компонента TextVoew
                TextView tv = (TextView) viewFlipper.getChildAt(id).findViewById(R.id.number);
                tv.setText("Количество: " + Integer.toString(list.get(id).getNumber()));
                // Поле того как товар уменьшили нужно проверить, если у товара количество 0 т.е его нет выполняем удаление
                if (list.get(id).getNumber() == 0) {
                    // Удаляем из представления viewFlipper наш обьект что бы пользователь не видел его
                    viewFlipper.removeViewAt(id);
                    // После удаляем из типа данных
                    list.remove(id);
                    // Проверка нужна для убавления позиции, если позиция 0 то убавлять ничего не нужно
                    // если позиция больше 0 т.е допустим 10 и после удаления обьект уменьшается на 1, и id нужно уменьшить на 1
                    if (id != 0) {
                        --id;
                    }
                    testShowList();
                }
            }
        });
    }

    public void testShowList() {
        int id = viewFlipper.getDisplayedChild();
        for (int i = 0; i < list.size(); i++) {
            Log.d("test", list.get(i).getTitle() + " | " + list.get(i).getPrice() + " | " + list.get(i).getNumber());
        }
        Log.d("test", "size: " + (list.size() - 1));
        Log.d("test", "id: " + id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                addDialog();
                return true;
            case R.id.action_save:
                csvFiles.saveFile(list);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public Boolean addList(String title, String price, int number) {
        return list.add(new FieldCsv(title, price, number));
    }


    public void addDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_dialog, null);
        builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText nameEdit = (EditText) view.findViewById(R.id.nameEdit);
                EditText priceEdit = (EditText) view.findViewById(R.id.priceEdit);
                EditText numberEdit = (EditText) view.findViewById(R.id.numberEdit);
                addList(nameEdit.getText().toString(), priceEdit.getText().toString(), Integer.parseInt(numberEdit.getText().toString()));
                lv1.invalidateViews();
                addViewFlipperItem(nameEdit.getText().toString(), priceEdit.getText().toString(), Integer.parseInt(numberEdit.getText().toString()));
                //refrechListView();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void editDialog(final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_dialog, null);
        final EditText nameEdit = (EditText) view.findViewById(R.id.nameEdit);
        EditText priceEdit = (EditText) view.findViewById(R.id.priceEdit);
        final EditText numberEdit = (EditText) view.findViewById(R.id.numberEdit);
        nameEdit.setText(list.get(id).getTitle());
        priceEdit.setText(list.get(id).getPrice());
        numberEdit.setText(Integer.toString(list.get(id).getNumber()));
        builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.get(id).setTitle(nameEdit.getText().toString());
                list.get(id).setPrice(numberEdit.getText().toString());
                list.get(id).setNumber(Integer.parseInt(numberEdit.getText().toString()));
                lv1.invalidateViews();
                TextView tv = (TextView) viewFlipper.getChildAt(id).findViewById(R.id.number);
                tv.setText("Количество: " + Integer.toString(list.get(id).getNumber()));
                //refrechListView();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float finalX = touchevent.getX();
                if (initialX > finalX) {
                    if (viewFlipper.getDisplayedChild() == (list.size() - 1))
                        break;
                    Log.d("test", "id: " + viewFlipper.getDisplayedChild());
                    viewFlipper.showNext();
                } else {
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;
                    Log.d("test", "id: " + viewFlipper.getDisplayedChild());
                    viewFlipper.showPrevious();
                }
                break;
        }
        return false;
    }
}
