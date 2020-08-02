package co.sih.presso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PrescriptionActivity extends AppCompatActivity {

    Button openBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    HashMap<String, List> sim_med = new HashMap<>();
    HashMap<String, List> sim_sym = new HashMap<>();


    String TAG = "PrescriptionActivity";
    String jsonString = null;
    String medicines = null;
    String symptoms = null;
    String dose = null;
    String days = null;
    RecyclerView rv_med, rv_sym;
    ArrayList<String> Medicines = new ArrayList<>();
    ArrayList<String> Symptoms = new ArrayList<>();
    ArrayList<String> Dose = new ArrayList<>();
    ArrayList<String> Days = new ArrayList<>();

    TextView t1, t2, t3, t4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        t1 = findViewById(R.id.recommendation_1);
        t2 = findViewById(R.id.recommendation_2);
        t3 = findViewById(R.id.recommendation_3);
        t4 = findViewById(R.id.recommendation_4);
        sheetBehavior.setPeekHeight(0);


//            openBottomSheet.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        openBottomSheet.setText("Close sheet");
//                    } else {
//                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        openBottomSheet.setText("Expand sheet");
//
//                    }
//                }
//            });
//
//        // callback for do something
//        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        openBottomSheet.setText("Close Sheet");
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        openBottomSheet.setText("Expand Sheet");
//                        sheetBehavior.setPeekHeight(0);
//
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//
//            }
//        });


        Intent in = getIntent();
        jsonString = in.getStringExtra("jsonString");

        convert2json(jsonString);


        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            medicines = jsonObject.getString("Medicines");

            String med[] = medicines.split("]|,|\\[");
            for (int i = 0; i < med.length; i++) {
                Log.e(TAG, "onCreate: " + med[i]);
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }
//-----------------------MEDICINES-----------------------------------------------------------------
        rv_med = (RecyclerView)

                findViewById(R.id.rv_med);
        rv_med.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_med.setLayoutManager(new

                LinearLayoutManager(this));


        final ArrayList<String> medList = new ArrayList<>();
        Log.e("length", String.valueOf(Medicines.size()));
        for (
                int i = 0; i < Medicines.size(); i++) {
            if (Medicines.get(i) == null) {
                break;
            }
            medList.add(Medicines.get(i));
        }
        final ArrayList<String> dosList = new ArrayList<>();

        for (int i = 0; i < Dose.size(); i++) {
            if (Dose.get(i) == null) {
                break;
            }
            dosList.add(Dose.get(i));
        }

        final ArrayList<String> dayList = new ArrayList<>();

        for (int i = 0; i < Days.size(); i++) {
            if (Days.get(i) == null) {
                break;
            }
            dayList.add(Days.get(i));
        }

        //creating recyclerview adapter
        final MedicineAdapter adapter = new MedicineAdapter(this, medList, dosList, dayList);

        //setting adapter to recyclerview
        rv_med.setAdapter(adapter);
        adapter.setOnItemClickListener(new MedicineAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String temp = medList.get(position);
                //clicked(temp);
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                adapter.notifyItemChanged(position);

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        openBottomSheet.setText("Close sheet");

                    ArrayList<String> r = new ArrayList<>(sim_med.get(temp));
                    t1.setText(r.get(0));
                    t2.setText(r.get(1));
                    t3.setText(r.get(2));
                    t4.setText(r.get(3));

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        openBottomSheet.setText("Expand sheet");

                }
            }
        });

//-------------------------------------------SYMPTOMS------------------------------------------------
        rv_sym = (RecyclerView)

                findViewById(R.id.rv_sym);
        rv_sym.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rv_sym.setLayoutManager(new

                LinearLayoutManager(this));

        final ArrayList<String> symList = new ArrayList<>();
        Log.e("length", String.valueOf(Symptoms.size()));
        for (
                int i = 0; i < Symptoms.size(); i++) {
            if (Symptoms.get(i) == null) {
                break;
            }
            symList.add(Symptoms.get(i));
        }

        //creating recyclerview adapter
        final SymptomAdapter adapter2 = new SymptomAdapter(this, symList);

        //setting adapter to recyclerview
        rv_sym.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new SymptomAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String temp = symList.get(position);
                //clicked(temp);
                Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                adapter2.notifyItemChanged(position);

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        openBottomSheet.setText("Close sheet");

                    ArrayList<String> r = new ArrayList<>(sim_sym.get(temp));
                    t1.setText(r.get(0));
                    t2.setText(r.get(1));
                    t3.setText(r.get(2));
                    t4.setText(r.get(3));

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        openBottomSheet.setText("Expand sheet");

                }
            }
        });
    }

    public void convert2json(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            medicines = jsonObject.getString("Medicines");
            int count = 0;
            ArrayList<String> temp = new ArrayList<String>();
            String med[] = medicines.split("]|,|\\[");
            for (int i = 0; i < med.length; i++) {
                if (med[i].equals("")) {
                    continue;
                }
                if (count % 5 == 0) {
                    Medicines.add(med[i]);
                } else {
                    temp.add(med[i]);
                }
                count++;
            }
            int index = 0;
            ArrayList<String> sim = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                if (index % 4 == 0 && index != 0) {
                    sim_med.put(Medicines.get((index / 4) - 1), sim);
                    sim = new ArrayList<>();
                }
                sim.add(temp.get(i));
                index++;

            }
            sim_med.put(Medicines.get((index / 4) - 1), sim);
            Log.e(TAG, "convert2json: " + sim_med);
//          ----------------------------------------------------------------


            symptoms = jsonObject.getString("Symptoms");
            count = 0;
            temp = new ArrayList<String>();
            String sym[] = symptoms.split("]|,|\\[");
            for (int i = 0; i < sym.length; i++) {
                if (sym[i].equals("")) {
                    continue;
                }
                if (count % 5 == 0) {
                    Symptoms.add(sym[i]);
                } else {
                    temp.add(sym[i]);
                }
                count++;
            }
            index = 0;
            sim = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                if (index % 4 == 0 && index != 0) {
                    sim_sym.put(Symptoms.get((index / 4) - 1), sim);
                    sim = new ArrayList<>();
                }
                sim.add(temp.get(i));
                index++;

            }
            sim_sym.put(Symptoms.get((index / 4) - 1), sim);
            Log.e(TAG, "convert2json: " + sim_sym);

//            ----------------------------------------------------

            dose = jsonObject.getString("Dose");
            String dos[] = dose.split("]|,|\\[");
            for (int i = 0; i < dos.length; i++) {
                if (dos[i].equals("")) {
                    continue;
                }
                Dose.add(dos[i]);
            }

//            ---------------------------
            days = jsonObject.getString("Days");
            String day[] = days.split("]|,|\\[");
            for (int i = 0; i < day.length; i++) {
                if (day[i].equals("")) {
                    continue;
                }
                Days.add(day[i]);
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
    }
}






