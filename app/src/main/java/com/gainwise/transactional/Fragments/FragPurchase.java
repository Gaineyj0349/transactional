package com.gainwise.transactional.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gainwise.transactional.Activities.LabelEdit;
import com.gainwise.transactional.Activities.MainActivity;
import com.gainwise.transactional.POJO.Transaction;
import com.gainwise.transactional.R;
import com.gainwise.transactional.Utilities.AdapterForFragBottomSheet;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.ghyeok.stickyswitch.widget.StickySwitch;

import static com.gainwise.transactional.Activities.MainActivity.getCurrencySymbol;

public class FragPurchase extends Fragment {

    public static Dialog dialog;
    static String color = null;
    View view = null;
    EditText etAmount;
    public static TextView tvMainLabel;
    public static TextView tvSubLabel;
    public static TextView tvcrossReferenceID;
    public static TextView tvAdditionalInfo;
    StickySwitch stickySwitch;
    InputMethodManager inputMethodManager;
    boolean duckKeyboard = false;
    CheckBox fromCashCheckBox;
    TextInputLayout textInputLayout;

    public static String mainLabelSelected = null;
    public static String subLabelSelected = null;
    public static String crossReferenceID = null;
    public static String additionalInfo = null;

    ImageView editMainLabelIV;
    ImageView editSubLabelIV;
    ImageView addMainLabelIV;
    ImageView pickMainLabelIV;
    ImageView addSubLabelIV;
    ImageView pickSubLabelIV;
    ImageView addAdditionalInfoIV;
    ImageView crossReferenceIDIV;
    CardView cashCard;
    TextView accountName;
    //TODO get mode pref to set this bool
    boolean expenseTrackerOnly = false;

    public static boolean cameFromEditMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_purchase, container, false);

            accountName = (TextView) view.findViewById(R.id.frag_purchase_accountBanner);
            if (expenseTrackerOnly) {

            } else {
                accountName.setText(buildBanner());
            }

            tvAdditionalInfo = (TextView) view.findViewById(R.id.frag_purchase_additional_info_tv);
            etAmount = (EditText) view.findViewById(R.id.frag_purchase_amountET);
            tvMainLabel = (TextView) view.findViewById(R.id.frag_purchase_mainLabelTV);
            tvSubLabel = (TextView) view.findViewById(R.id.frag_purchase_subLabelTV);
            cashCard = (CardView) view.findViewById(R.id.frag_purchase_cash_card);
            //        tvcrossReferenceID = (TextView) view.findViewById(R.id.frag_purchase_cross_ref_TV);
            stickySwitch = (StickySwitch) view.findViewById(R.id.frag_purchase_sticky_switch);
            textInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);
            textInputLayout.setHint(getCurrencySymbol() + " AMOUNT");
            initKeyBoard();

            editMainLabelIV = (ImageView)view.findViewById(R.id.frag_purchase_iv_edit_main_label);
            editSubLabelIV = (ImageView)view.findViewById(R.id.frag_purchase_iv_edit_sub_label);
            addMainLabelIV = (ImageView) view.findViewById(R.id.frag_purchase_iv_add_main_label);
            pickMainLabelIV = (ImageView) view.findViewById(R.id.frag_purchase_iv_pick_main_label);
            addSubLabelIV = (ImageView) view.findViewById(R.id.frag_purchase_iv_add_sub_label);
            pickSubLabelIV = (ImageView) view.findViewById(R.id.frag_purchase_iv_pick_sub_label);
            addAdditionalInfoIV = (ImageView) view.findViewById(R.id.frag_purchase_iv_additionalInfo);
            //       crossReferenceIDIV = (ImageView)view.findViewById(R.id.frag_purchase_iv_link);
            fromCashCheckBox = (CheckBox) view.findViewById(R.id.frag_purchase_checkbox);

            editMainLabelIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> l = MainActivity.db.TransactionDAO().fetchAllTransactionMainLabelsOnly();

                    if(mainLabelSelected!=null){
                        Intent intent = new Intent(getActivity(), LabelEdit.class);
                        intent.putExtra("main1", mainLabelSelected);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), LabelEdit.class);
                        startActivity(intent);
                    }

                    } /*else {
                        Toast.makeText(getActivity(), "You must create a Main Label first", Toast.LENGTH_SHORT).show();
                    }*/

            });
            editSubLabelIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mainLabelSelected == null) {
                        Toast.makeText(getActivity(), "You must select a Main Label to filter Sub Labels for edit mode.", Toast.LENGTH_SHORT).show();
                    }
                    /*else if(MainActivity.db.TransactionDAO().fetchLatestSubWithMain(mainLabelSelected) == null){
                        Toast.makeText(getActivity(), "You must first create a new Sub Label!", Toast.LENGTH_SHORT).show();
                    }*/
                    else {
                        Log.i("JOSH93",""+MainActivity.db.TransactionDAO().fetchAllTransactionStringOnlySubLabelsOnlyWithMainLabel(mainLabelSelected).size());
                        Intent intent = new Intent(getActivity(), LabelEdit.class);
                        intent.putExtra("main", mainLabelSelected);
                        startActivity(intent);
                    }
                }
            });
            addMainLabelIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewMainLabel();
                }
            });
            pickMainLabelIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> l = MainActivity.db.TransactionDAO().fetchAllTransactionMainLabelsOnly();
                    if (l.size() > 0) {
                        pickMainLabel();
                    } else {
                        Toast.makeText(getActivity(), "You must create a Main Label first", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            addSubLabelIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mainLabelSelected == null) {
                        Toast.makeText(getActivity(), "You must select a Main Label before a Sub Label!", Toast.LENGTH_SHORT).show();
                    } else {
                        addNewSubLabel();
                    }


                }
            });
            pickSubLabelIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mainLabelSelected == null) {
                        Toast.makeText(getActivity(), "You must select a Main Label before a Sub Label!", Toast.LENGTH_SHORT).show();
                    }else if(MainActivity.db.TransactionDAO().fetchLatestSubWithMain(mainLabelSelected) == null){
                        Toast.makeText(getActivity(), "You must first create a new Sub Label!", Toast.LENGTH_SHORT).show();
                        }
                    else {
                        Log.i("JOSH93",""+MainActivity.db.TransactionDAO().fetchAllTransactionStringOnlySubLabelsOnlyWithMainLabel(mainLabelSelected).size());
                        pickSubLabel();
                    }
                }
            });
            addAdditionalInfoIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addAdditionalInfo();
                }
            });
/*            crossReferenceIDIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   crossReferenceIDClicked();
                }
            });*/


            Button saveButton = (Button) view.findViewById(R.id.frag_purchase_saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etAmount.getText().length() > 0 && mainLabelSelected != null) {
                        showDialog();
                    } else {
                        Toast.makeText(getActivity(), "You must at least have an amount and main label.", Toast.LENGTH_SHORT).show();

                    }

                }
            });
            Button clearButton = (Button) view.findViewById(R.id.frag_purchase_clearButton);
            clearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearClicked();
                }
            });
            updateModeViews();
        }
        return view;

    }

    public static String buildBanner() {
        if (MainActivity.db.TransactionDAO().getLastBalance() == null) {
            return MainActivity.prefs.getString(SettingsFragment.ACCOUNT_KEY, "") + ": " + "TBD";

        } else {
            return MainActivity.prefs.getString(SettingsFragment.ACCOUNT_KEY, "") + ": " + getCurrencySymbol() + MainActivity.db.TransactionDAO().getLastBalance();

        }
    }


    private void initKeyBoard() {
        inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(
                        Activity.INPUT_METHOD_SERVICE);


    }

    private void showDialog() {
        final boolean purchase;
        if (stickySwitch.getDirection() == StickySwitch.Direction.LEFT) {
            purchase = true;
            Log.i("JOSH", "left");
        } else {
            purchase = false;
            Log.i("JOSH", "right");
        }

        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Presentation);
        } else {
            builder = new AlertDialog.Builder(getActivity());

        }
        builder.setTitle("Transaction Details:")
                .setMessage(getStringOfDetails())
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                        savedClicked(purchase);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }

    private String getStringOfDetails() {
        StringBuilder sb = new StringBuilder();
        if (stickySwitch.getDirection() == StickySwitch.Direction.LEFT) {
            sb.append("EXPENSE\n\n");
        } else {
            sb.append("DEPOSIT\n\n");
        }
        sb.append("AMOUNT:");
        sb.append("\n");
        sb.append(etAmount.getText().toString());
        sb.append("\n\n");
        sb.append("MAIN LABEL:");
        sb.append("\n");
        sb.append(tvMainLabel.getText().toString());
        sb.append("\n\n");
        sb.append("SUB LABEL:");
        sb.append("\n");
        sb.append(tvSubLabel.getText().toString());
        sb.append("\n\n");
        sb.append("ADDITIONAL INFORMATION:");
        sb.append("\n");
        sb.append(tvAdditionalInfo.getText().toString());
        sb.append("\n\n");
        sb.append("Save this transaction?");
        return sb.toString();
    }


    public void savedClicked(boolean isPurchase) {
        //TODO calculation for running balance and possible asynctask for input of data to db

        String amount = etAmount.getText().toString().trim();

        long timeInMillis = System.currentTimeMillis();
        String date = DateFormat.getDateTimeInstance().format(timeInMillis);

        Log.i("JOSH3", amount);

        Transaction transaction = new Transaction();

        if (additionalInfo != null) {
            transaction.setAdditionalInfo(additionalInfo);
        }
        if (mainLabelSelected != null) {
            transaction.setMainLabel(mainLabelSelected);
        }
        if (subLabelSelected != null) {
            transaction.setSubLabel(subLabelSelected);
        }
        if (crossReferenceID != null) {
            transaction.setCrossReferenceID(crossReferenceID);
        }
        if (color != null) {
            transaction.setColor1(color);
        }
        Calendar cal = Calendar.getInstance();
        transaction.setDay(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        transaction.setDayOfWeek(getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)));
        transaction.setMonth(String.valueOf(cal.get(Calendar.MONTH) + 1));
        transaction.setYear(String.valueOf(cal.get(Calendar.YEAR)));


        transaction.setTypeEntry("reg");
        transaction.setAmount(amount);

        transaction.setDate(date);
        transaction.setPurchase(isPurchase);
        if (fromCashCheckBox.isChecked()) {
            transaction.setFromCash("yes");
            transaction.setCurrentBalance(MainActivity.db.TransactionDAO().getLastBalance());
        } else {
            transaction.setFromCash("no");
            transaction.setCurrentBalance(calculateLastBalance(isPurchase, amount));
        }

        Log.i("JOSHcalc", "amount sent: " + amount);
        Log.i("JOSHcalc", "" + calculateLastBalance(isPurchase, amount));


        MainActivity.db.TransactionDAO().insertAll(transaction);
        clearClicked();

    }

    private String calculateLastBalance(boolean isPurchase, String transactionIn) {
        String endbalanceresult;
        String endingBalance = null;
        endingBalance = MainActivity.db.TransactionDAO().getLastBalance();
        Log.i("JOSHcalc", "lastBalance" + endingBalance);
        if (endingBalance == null) {
            endingBalance = String.valueOf(0);
        }

        BigDecimal transaction = new BigDecimal(transactionIn);
        BigDecimal endbalanceBD = new BigDecimal(endingBalance);

        if (isPurchase) {
            endbalanceresult = endbalanceBD.subtract(transaction).toString();
        } else {
            endbalanceresult = endbalanceBD.add(transaction).toString();
        }

        return endbalanceresult;
    }

    public void clearClicked() {
        tvAdditionalInfo.setText("");
        etAmount.setText("");
        tvMainLabel.setText("");
        tvSubLabel.setText("");
//        tvcrossReferenceID.setText("");
        color = null;
        fromCashCheckBox.setChecked(false);
        stickySwitch.setDirection(StickySwitch.Direction.LEFT);


        mainLabelSelected = null;
        subLabelSelected = null;
        crossReferenceID = null;
        additionalInfo = null;

        etAmount.clearFocus();
        hideSoftKeyboard();
        if (expenseTrackerOnly) {

        } else {
            accountName.setText(buildBanner());
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("JOSH frag", "onAttach");
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        Log.i("Josh,", "EXTRA");
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        } else {
            try {

                hideSoftKeyboard();
            } catch (Exception e) {
                Log.i("JOSH3", e.getMessage());
            }

        }
    }

    @Override
    public void onResume() {

        super.onResume();

        if (!getUserVisibleHint()) {
            return;
        } else {

            //custom code here for when frag is visible
            if(cameFromEditMode){
                cameFromEditMode = false;
            clearClicked();}
            updateModeViews();
            textInputLayout.setHint(getCurrencySymbol() + " AMOUNT");
            if (expenseTrackerOnly) {

            } else {
                accountName.setText(buildBanner());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Josh,", "onPause");
    }


    public void hideSoftKeyboard() {
        etAmount.clearFocus();
        if (getActivity().getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void addNewMainLabel() {
        showDialogForCreateNew("main");
    }


    public void pickMainLabel() {
        showBottomSheetForPick("main");
    }

    public void addNewSubLabel() {
        etAmount.clearFocus();
        showDialogForCreateNew("sub");
    }

    public void pickSubLabel() {
        showBottomSheetForPick("sub");

    }

    public void addAdditionalInfo() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.frag_purchase_dialog_additional_info, null);

        final EditText et2 = (EditText) view.findViewById(R.id.frag_purchase_dialog_additional_info_et);
        final Button addButton = (Button) view.findViewById(R.id.frag_purchase_dialog_additional_info_add_button);
        final Button cancelButton = (Button) view.findViewById(R.id.frag_purchase_dialog_additional_info_cancel_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additionalInfo = et2.getText().toString().trim();
                tvAdditionalInfo.setText(additionalInfo);
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.setContentView(view);
        dialog.show();
    }

    public void crossReferenceIDClicked() {
        //TODO
    }

    private void showDialogForCreateNew(final String type) {
        duckKeyboard = true;


        if (type.equals("sub")) {
            color = MainActivity.db.TransactionDAO().getColorOfMain(mainLabelSelected);
        } else {
            color = "-524291";
        }

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

                dialog.dismiss();
                hideSoftKeyboard();

            }
        });
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogColorPicker = new Dialog(getActivity());
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
                    Toast.makeText(getActivity(), "The length must be less than 30 characters.", Toast.LENGTH_LONG).show();
                } else if (et.getText().length() == 0) {
                    Toast.makeText(getActivity(), "The length must not be empty.", Toast.LENGTH_LONG).show();

                } else {
                    String label = et.getText().toString().trim();

                    Transaction t = new Transaction();
                    t.setColor1(color);

                    if (type.equals("main")) {

                        t.setTypeEntry("info");
                        t.setMainLabel(label);

                        if (validate(type, label, mainLabelSelected)) {
                            secureMainLabelInfo(label);
                            MainActivity.db.TransactionDAO().insertAll(t);
                            dialog.dismiss();
                            hideSoftKeyboard();
                        } else {
                            Toast.makeText(getActivity(), "This label already exists here.", Toast.LENGTH_LONG).show();

                        }
                    }

                    if (type.equals("sub")) {
                        t.setTypeEntry("info");
                        t.setMainLabel(mainLabelSelected);
                        t.setSubLabel(label);

                        if (validate(type, label, mainLabelSelected)) {
                            secureSubLabelInfo(label);
                            MainActivity.db.TransactionDAO().insertAll(t);
                            dialog.dismiss();
                            hideSoftKeyboard();
                        } else {
                            Toast.makeText(getActivity(), "This label already exists here.", Toast.LENGTH_LONG).show();

                        }
                    }










                }
            }
        });
        dialog.setContentView(mView);
        dialog.show();


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

    public void showBottomSheetForPick(String type) {


        Log.i("JOSH", "bottomSheetCalled");

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View bottomSheetViewAddNew = getLayoutInflater().inflate(R.layout.frag_purchase_dialog_pick, null);
        final Button button = bottomSheetViewAddNew.findViewById(R.id.frag_purchase_bottom_sheet_cancelButton);


        RecyclerView rv = bottomSheetViewAddNew.findViewById(R.id.frag_purchase_bottom_sheet_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        switch (type) {

            case "main":
                List<Transaction> labels = removeDuplicatesForMain(MainActivity.db.TransactionDAO().fetchAllTransactionsLabels());

                AdapterForFragBottomSheet adapterForFragBottomSheet = new AdapterForFragBottomSheet(getActivity(), labels, type);
                rv.setAdapter(adapterForFragBottomSheet);
                break;
            case "sub":

                List<Transaction> labels2 = MainActivity.db.TransactionDAO().fetchAllTransactionSubLabelsOnlyWithMainLabel(mainLabelSelected);
                labels2.remove(0);
                AdapterForFragBottomSheet adapterForFragBottomSheet2 = new AdapterForFragBottomSheet(getActivity(), labels2, type);
                rv.setAdapter(null);
                rv.setAdapter(adapterForFragBottomSheet2);
                break;

            default:

                break;
        }
        dialog.setContentView(bottomSheetViewAddNew);

        dialog.show();


    }

    public static void secureMainLabelInfo(String label) {
        tvMainLabel.setText(label);
        tvSubLabel.setText(null);
        subLabelSelected = null;
        mainLabelSelected = label;
        color = MainActivity.db.TransactionDAO().getColorOfMain(mainLabelSelected);

        if (dialog != null) {
            dialog.dismiss();
        }

    }

    public static void secureSubLabelInfo(String label) {
        tvSubLabel.setText(label);
        subLabelSelected = label;
        color = MainActivity.db.TransactionDAO().getColorOfMain(mainLabelSelected);

        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void secureAdditionalInfo(String label) {
        //TODO
    }

    public static void securecrossReferenceIDInfo(String label) {
        //TODO
    }

    private void updateModeViews() {
        if (MainActivity.expenseTrackerOnlyMode()) {
            flipToExpenseTrackerOnlyMode();
        } else {
            flipToExpenseTrackerWithAccountMode();
        }
    }

    private void flipToExpenseTrackerOnlyMode() {
        cashCard.setVisibility(View.GONE);
        accountName.setVisibility(View.GONE);
    }

    private void flipToExpenseTrackerWithAccountMode() {
        cashCard.setVisibility(View.VISIBLE);
        accountName.setVisibility(View.VISIBLE);
    }

    public static String getDayOfWeek(int dayPosition) {
        String dayOfWeek = "";
        switch (dayPosition) {
            case 1:
                dayOfWeek = "Sunday";
                break;
            case 2:
                dayOfWeek = "Monday";
                break;
            case 3:
                dayOfWeek = "Tuesday";
                break;
            case 4:
                dayOfWeek = "Wednesday";
                break;
            case 5:
                dayOfWeek = "Thursday";
                break;
            case 6:
                dayOfWeek = "Friday";
                break;
            case 7:
                dayOfWeek = "Saturday";
                break;
            default:
                dayOfWeek = "Unknown";
                break;

        }
        return dayOfWeek;
    }

    public List<Transaction> removeDuplicatesForMain(List<Transaction> listIn) {
        List<Transaction> list = new ArrayList<Transaction>();
        List<String> word = new ArrayList<String>();

        for(Transaction t : listIn){
            Log.i("JOSH111", "helper1");

            if(!word.contains(t.getMainLabel())){
              list.add(t);
            }
            word.add(t.getMainLabel());
            }

        return list;
    }

    public static void resetSubLabel(){
        subLabelSelected = null;
        tvSubLabel.setText("");
    }
}
