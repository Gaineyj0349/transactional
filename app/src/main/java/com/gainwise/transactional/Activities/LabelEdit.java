package com.gainwise.transactional.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asksira.dropdownview.DropDownView;
import com.gainwise.transactional.Fragments.FragPurchase;
import com.gainwise.transactional.POJO.Transaction;
import com.gainwise.transactional.R;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.util.Collections;
import java.util.List;

public class LabelEdit extends AppCompatActivity {


    DropDownView dropDownView;
    List<String> labelList;
    String currentInDropDown;
    String type;
    String color;
    String mainLabelSelected;
    Dialog dialogz;
    String colorIn;
    String oldLabel;
    int currentSpot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragPurchase.cameFromEditMode = true;
        dropDownView = findViewById(R.id.dropdownview);

        initList();


        Button editButton = findViewById(R.id.label_edit_edit_button);
        Button deleteButton = findViewById(R.id.label_edit_delete_button);
        Button addButton = findViewById(R.id.label_edit_add_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentInDropDown == null){
                    Toast.makeText(LabelEdit.this, "Please Make a Selection First!", Toast.LENGTH_SHORT).show();
                }else{
                showDialogForEdit(type, currentInDropDown);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentInDropDown == null){
                    Toast.makeText(LabelEdit.this, "Please Make a Selection First!", Toast.LENGTH_SHORT).show();
                }else{
                    showDialogforDelete(type);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showDialogForCreateNew(type);
            }
        });
    }

    private void initList() {
        currentInDropDown = null;
        if(labelList != null){
            labelList.clear();
        }




        if(getIntent().hasExtra("main")){
            type = "sub";
            mainLabelSelected = getIntent().getStringExtra("main");
            getSupportActionBar().setTitle("Main: " +mainLabelSelected);
            labelList = MainActivity.db.TransactionDAO().fetchAllTransactionStringOnlySubLabelsOnlyWithMainLabel(mainLabelSelected);

            //this next part removes the null from the list that comes with this fetch, then organizes by sorting alphabetically

            labelList.remove(0);



        }
            else
        {
            type = "main";
            labelList = MainActivity.db.TransactionDAO().fetchAllTransactionMainLabelsOnly();
            Log.i("JOSHsizeLabel   ", ""+labelList.size());
        }

        if(labelList.size() == 0){
            labelList.add("Please Select");
            dropDownView.setDropDownListItem(labelList);
            dropDownView.setSelectingPosition(0);
            labelList.remove(0);
            currentInDropDown = null;
        }
        Collections.sort(labelList);
        dropDownView.setDropDownListItem(labelList);

        dropDownView.setOnSelectionListener(new DropDownView.OnSelectionListener() {
            @Override
            public void onItemSelected(DropDownView view, int position) {
                currentInDropDown = labelList.get(position);
                currentSpot = position;
                Log.i("JOSHspot ", ""+position);
            }
        });
if(labelList.size() != 0){


        if(currentSpot < labelList.size()) {
            dropDownView.setSelectingPosition(currentSpot);
        }else{
            dropDownView.setSelectingPosition(0);
            currentSpot = 0;
        }
}

if(getIntent().hasExtra("main1")){
    int pos = labelList.indexOf(getIntent().getStringExtra("main1"));
    dropDownView.setSelectingPosition(pos);
}
    }

    private void showDialogForCreateNew(final String type) {
             if (type.equals("sub")) {
            color = MainActivity.db.TransactionDAO().getColorOfMain(mainLabelSelected);
        } else {
            color = "-524291";
        }

        dialogz = new Dialog(LabelEdit.this);
        dialogz.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View mView = getLayoutInflater().inflate(R.layout.frag_purchase_dialog_create, null);

        final Button cancelButton = (Button) mView.findViewById(R.id.frag_purchase_dialog_cancel_button);
        final Button createButton = (Button) mView.findViewById(R.id.frag_purchase_dialog_create_button);
        final EditText et = (EditText) mView.findViewById(R.id.frag_purchase_dialog_create_et);
        final Button colorButton = (Button) mView.findViewById(R.id.frag_purchase_dialog_color_button);

        if (type.equals("sub")) {
            colorButton.setVisibility(View.GONE);
        } else {
            colorButton.setBackgroundColor(Integer.parseInt(color));

        }


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogz.dismiss();
               

            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogColorPicker = new Dialog(LabelEdit.this);
                dialogColorPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
                final View viewColorPicker = getLayoutInflater().inflate(R.layout.dialog_color_picker, null);

                final Button CPsave = (Button) viewColorPicker.findViewById(R.id.color_picker_save);
                final Button CPcancel = (Button) viewColorPicker.findViewById(R.id.color_picker_cancel);
                final ColorPickerView colorPickerView = (ColorPickerView) viewColorPicker.findViewById(R.id.colorPickerView);
                final RelativeLayout relativeLayout = (RelativeLayout) viewColorPicker.findViewById(R.id.colorPickerBackground);

                colorPickerView.setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope colorEnvelope) {
                        color = String.valueOf(colorEnvelope.getColor());
                        Log.i("JOSHcolor", color);
                        //  relativeLayout.setBackgroundColor(Integer.parseInt(color));
                        relativeLayout.setBackgroundColor(colorEnvelope.getColor());
                    }
                });
                CPsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        colorButton.setBackgroundColor(Integer.parseInt(color));
                        dialogColorPicker.dismiss();
                    }
                });
                CPcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogColorPicker.dismiss();
                    }
                });

                dialogColorPicker.setContentView(viewColorPicker);
                dialogColorPicker.show();


            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et.getText().length() > 30) {
                    Toast.makeText(LabelEdit.this, "The length must be less than 30 characters.", Toast.LENGTH_LONG).show();
                } else if (et.getText().length() == 0) {
                    Toast.makeText(LabelEdit.this, "The length must not be empty.", Toast.LENGTH_LONG).show();

                } else {
                    String label = et.getText().toString().trim();

                    Transaction t = new Transaction();
                    t.setColor1(color);

                    if (type.equals("main")) {

                        t.setTypeEntry("info");
                        t.setMainLabel(label);

                        if (validate(type, label, mainLabelSelected)) {
                            MainActivity.db.TransactionDAO().insertAll(t);
                            initList();
                            dialogz.dismiss();
                        } else {
                            Toast.makeText(LabelEdit.this, "This label already exists here.", Toast.LENGTH_LONG).show();

                        }
                    }

                    if (type.equals("sub")) {
                        t.setTypeEntry("info");
                        t.setMainLabel(mainLabelSelected);
                        t.setSubLabel(label);

                        if (validate(type, label, mainLabelSelected)) {
                            MainActivity.db.TransactionDAO().insertAll(t);
                            initList();
                            dialogz.dismiss();
                        } else {
                            Toast.makeText(LabelEdit.this, "This label already exists here.", Toast.LENGTH_LONG).show();

                        }
                    }










                }
            }
        });
        dialogz.setContentView(mView);
        dialogz.show();


    }

    private boolean validate(String type, String label, String mainLabelIn) {
        boolean ok = false;

        if (type.equals("main")) {
            List<String> labels = MainActivity.db.TransactionDAO().fetchAllTransactionMainLabelsOnly();
            if (labels.contains(label)) {
                ok = false;
            } else {
                ok = true;
            }
        } else if (type.equals("sub")) {
            List<String> labels = MainActivity.db.TransactionDAO().fetchAllTransactionStringOnlySubLabelsOnlyWithMainLabel(mainLabelIn);
            if (labels.contains(label)) {
                ok = false;
            } else {
                ok = true;
            }

        }
        return ok;
    }

    private void showDialogForEdit(final String type, String labelIn) {
       oldLabel = labelIn;
        if (type.equals("sub")) {
            colorIn = MainActivity.db.TransactionDAO().getColorOfMain(mainLabelSelected);
            color = MainActivity.db.TransactionDAO().getColorOfMain(mainLabelSelected);
        } else {
            colorIn = MainActivity.db.TransactionDAO().getColorOfMain(currentInDropDown);
            color = MainActivity.db.TransactionDAO().getColorOfMain(currentInDropDown);
        }


        dialogz = new Dialog(LabelEdit.this);

        View mView = getLayoutInflater().inflate(R.layout.frag_purchase_dialog_create, null);

        final Button cancelButton = (Button) mView.findViewById(R.id.frag_purchase_dialog_cancel_button);
        final Button createButton = (Button) mView.findViewById(R.id.frag_purchase_dialog_create_button);
        final EditText et = (EditText) mView.findViewById(R.id.frag_purchase_dialog_create_et);
        final Button colorButton = (Button) mView.findViewById(R.id.frag_purchase_dialog_color_button);
        createButton.setText("SAVE");
        et.setText(currentInDropDown);

        if (type.equals("sub")) {
            colorButton.setVisibility(View.GONE);
        } else {
            colorButton.setBackgroundColor(Integer.parseInt(color));

        }


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogz.dismiss();


            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogColorPicker = new Dialog(LabelEdit.this);
                dialogColorPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
                final View viewColorPicker = getLayoutInflater().inflate(R.layout.dialog_color_picker, null);

                final Button CPsave = (Button) viewColorPicker.findViewById(R.id.color_picker_save);
                final Button CPcancel = (Button) viewColorPicker.findViewById(R.id.color_picker_cancel);
                final ColorPickerView colorPickerView = (ColorPickerView) viewColorPicker.findViewById(R.id.colorPickerView);
                final RelativeLayout relativeLayout = (RelativeLayout) viewColorPicker.findViewById(R.id.colorPickerBackground);

                colorPickerView.setColorListener(new ColorListener() {
                    @Override
                    public void onColorSelected(ColorEnvelope colorEnvelope) {
                        color = String.valueOf(colorEnvelope.getColor());

                        Log.i("JOSHcolor", color);
                        //  relativeLayout.setBackgroundColor(Integer.parseInt(color));
                        relativeLayout.setBackgroundColor(colorEnvelope.getColor());
                    }
                });
                CPsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        colorButton.setBackgroundColor(Integer.parseInt(color));
                        dialogColorPicker.dismiss();
                    }
                });
                CPcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogColorPicker.dismiss();
                    }
                });

                dialogColorPicker.setContentView(viewColorPicker);
                dialogColorPicker.show();


            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et.getText().length() > 30) {
                    Toast.makeText(LabelEdit.this, "The length must be less than 30 characters.", Toast.LENGTH_LONG).show();
                } else if (et.getText().length() == 0) {
                    Toast.makeText(LabelEdit.this, "The length must not be empty.", Toast.LENGTH_LONG).show();

                } else if(et.getText().toString().trim().equals(currentInDropDown) && color.equals(colorIn)){
                    Toast.makeText(LabelEdit.this, "No Change Detected.", Toast.LENGTH_LONG).show();
                    dialogz.dismiss();
                }
                else if(et.getText().toString().trim().equals(currentInDropDown) && !color.equals(colorIn)){
                    MainActivity.db.TransactionDAO().updateAllColorsWithMain(currentInDropDown,color);
                    initList();
                    dialogz.dismiss();
                }
                else {
                    
                    
                    
                   final String newLabel = et.getText().toString().trim();




                    if (type.equals("main")) {



                        if (validate(type, newLabel, mainLabelSelected)) {

                            AlertDialog.Builder builder;

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                builder = new AlertDialog.Builder(LabelEdit.this, android.R.style.Theme_Material_Dialog_Alert);
                            }else{
                                builder = new AlertDialog.Builder(LabelEdit.this);

                            }
                            builder.setTitle("Confirm this change!")
                                    .setMessage("This will update the currently selected label everywhere in the database, continue?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateAllLabelsWith(type,oldLabel, newLabel, color);
                                            initList();
                                            dialogz.dismiss();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();




                        } else {
                            Toast.makeText(LabelEdit.this, "This label already exists here.", Toast.LENGTH_LONG).show();

                        }
                    }

                    if (type.equals("sub")) {


                        if (validate(type, newLabel, mainLabelSelected)) {

                            AlertDialog.Builder builder;

                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                builder = new AlertDialog.Builder(LabelEdit.this, android.R.style.Theme_Material_Dialog_Alert);
                            }else{
                                builder = new AlertDialog.Builder(LabelEdit.this);

                            }
                            builder.setTitle("Confirm this change!")
                                    .setMessage("This will update the currently selected label everywhere in the database, continue?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateAllLabelsWith(type,oldLabel, newLabel, color);
                                            initList();
                                            dialogz.dismiss();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        } else {
                            Toast.makeText(LabelEdit.this, "This label already exists here.", Toast.LENGTH_LONG).show();

                        }
                    }










                }
            }
        });
        dialogz.setContentView(mView);
        dialogz.show();


    }

    private void updateAllLabelsWith(String type, String oldLabel, String newLabel, String color) {

        if(type.equals("main")){
            MainActivity.db.TransactionDAO().updateAllColorsWithMain(oldLabel,color);
            MainActivity.db.TransactionDAO().updateAllMainLabels(oldLabel,newLabel);

        }else if (type.equals("sub")){
            MainActivity.db.TransactionDAO().updateAllSubLabels(oldLabel,newLabel);

        }
        initList();
        dropDownView.setPlaceholderText("Please select");
    }

    private void showDialogforDelete(final String typeIn){

        AlertDialog.Builder builder;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            builder = new AlertDialog.Builder(LabelEdit.this, android.R.style.Theme_Material_Dialog_Alert);
        }else{
            builder = new AlertDialog.Builder(LabelEdit.this);

        }
        builder.setTitle("WARNING - Confirm this delete!")
                .setMessage("This will delete any and all entries that have the label " + currentInDropDown + "!\n" +
                        "No currency transaction values will be affected (if applicable, meaning your current balance will remain unaffected)," +
                        " however all of the records that had this label will all be deleted.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllWithLabel(typeIn, currentInDropDown);
                        initList();

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.stat_sys_warning)
                .show();
    }
private void deleteAllWithLabel(String typeIn, String label){
    if(typeIn.equals("main")){
        MainActivity.db.TransactionDAO().deleteTransactionWithMainLabel(label);


    }else if (typeIn.equals("sub")){
        MainActivity.db.TransactionDAO().deleteTransactionWithSubLabel(label);

    }
    initList();

}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("JOSHlabel", "ondestroy");

    }
}
