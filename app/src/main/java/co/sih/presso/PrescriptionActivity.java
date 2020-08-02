package co.sih.presso;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class PrescriptionActivity extends AppCompatActivity {

    Button openBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import co.sih.presso.MedicineAdapter;
import co.sih.presso.R;
import co.sih.presso.SymptomAdapter;

public class PrescriptionActivity extends AppCompatActivity {
String TAG="PrescriptionActivity";
    String jsonString = null;
    String medicines=null;
    RecyclerView rv_med,rv_sym;
    ArrayList<String> Medicines=new ArrayList<>();
    ArrayList<String> Symptoms=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        openBottomSheet = findViewById(R.id.getBottomSheet);


        sheetBehavior.setPeekHeight(0);


        openBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    openBottomSheet.setText("Close sheet");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    openBottomSheet.setText("Expand sheet");

                }
            }
        });

        // callback for do something
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        openBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        openBottomSheet.setText("Expand Sheet");
                        sheetBehavior.setPeekHeight(0);

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }
}
        Intent in = getIntent();
        jsonString = in.getStringExtra("jsonString");


        try {
            JSONObject jsonObject=new JSONObject(jsonString);
            medicines=jsonObject.getString("Medicines");

            String med[]=medicines.split("]|,|\\[");
            for(int i=0;i<med.length;i++)
            {
                Log.e(TAG, "onCreate: "+med[i] );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//-----------------------MEDICINES-----------------------------------------------------------------
        rv_med = (RecyclerView) findViewById(R.id.rv_med);
        rv_med.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_med.setLayoutManager(new LinearLayoutManager(this));

        Medicines=new ArrayList<>();
        Medicines.add("med1");
        Medicines.add("med2");
        Medicines.add("med3");
        Medicines.add("med4");
        final ArrayList<String> medList= new ArrayList<>();
        Log.e("length", String.valueOf(Medicines.size()));
        for(int i=0;i<Medicines.size();i++)
        {
            if(Medicines.get(i)==null)
            {
                break;
            }
            medList.add(Medicines.get(i));
        }

        //creating recyclerview adapter
        final MedicineAdapter adapter = new MedicineAdapter(this, medList);

        //setting adapter to recyclerview
        rv_med.setAdapter(adapter);
        adapter.setOnItemClickListener(new MedicineAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String temp=medList.get(position);
                //clicked(temp);
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                adapter.notifyItemChanged(position);
            }
        });

//-------------------------------------------SYMPTOMS------------------------------------------------
    rv_sym = (RecyclerView) findViewById(R.id.rv_sym);
        rv_sym.setHasFixedSize(true);
    //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_sym.setLayoutManager(new LinearLayoutManager(this));

    Symptoms=new ArrayList<>();
        Symptoms.add("sym1");
        Symptoms.add("sym2");
        Symptoms.add("sym3");
        Symptoms.add("sym4");
    final ArrayList<String> symList= new ArrayList<>();
        Log.e("length", String.valueOf(Medicines.size()));
        for(int i=0;i<Symptoms.size();i++)
    {
        if(Symptoms.get(i)==null)
        {
            break;
        }
        symList.add(Symptoms.get(i));
    }

    //creating recyclerview adapter
    final SymptomAdapter adapter2 = new SymptomAdapter(this, medList);

    //setting adapter to recyclerview
        rv_sym.setAdapter(adapter);
        adapter2.setOnItemClickListener(new SymptomAdapter.onItemClickListener() {
        @Override
        public void onItemClick(int position) {
            String temp=symList.get(position);
            //clicked(temp);
            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
            adapter2.notifyItemChanged(position);
        }
    });
    }
}


