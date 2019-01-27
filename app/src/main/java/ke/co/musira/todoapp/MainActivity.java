package ke.co.musira.todoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText itemET;
    private Button btn;
    private ListView itemsList;

    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemET = findViewById(R.id.list_item_edit_text);
        btn = findViewById(R.id.btn_add);
        itemsList = findViewById(R.id.items_list);

        items = FileHelper.readData(this);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        itemsList.setAdapter(adapter);

        btn.setOnClickListener(this);
        itemsList.setOnItemClickListener(this);

        btn.setEnabled(false);

        itemET.addTextChangedListener(new EmptyTextWatcher() {
            @Override
            public void onEmptyField() {
                btn.setEnabled(false);
            }

            @Override
            public void onFilledField() {
                btn.setEnabled(true);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                String itemEntered = itemET.getText().toString();
                adapter.add(itemEntered);
                itemET.setText("");
                FileHelper.writeData(items, this);
                Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();

            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        FileHelper.writeData(items, this);
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
    }

    // object oriented solution for text watcher
    public abstract class EmptyTextWatcher implements TextWatcher {

        public abstract void onEmptyField();

        public abstract void onFilledField();

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){
            if (s.toString().trim().length()==0) {
                onEmptyField();
            } else {
                onFilledField();
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after){

        }
        @Override
        public void afterTextChanged(Editable s){

        }
    }
}
