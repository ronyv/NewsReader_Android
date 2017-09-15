package hackearth.com.hackearthtest.dialogue;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import hackearth.com.hackearthtest.MainActivity;
import hackearth.com.hackearthtest.R;
import hackearth.com.hackearthtest.font.MediumFont;
import hackearth.com.hackearthtest.utils.AppContext;

/**
 * Created by apple on 14/09/17.
 */

public class AppDialogues {


    public static void publisherSelect(final Context context, String heading) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.radio_btn_layout, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(false);




        final RadioGroup radioGroup = (RadioGroup) promptsView.findViewById(R.id.radiogroup);
        MediumFont mfDialogueHeading = (MediumFont) promptsView.findViewById(R.id.mfDialogueHeading);

        mfDialogueHeading.setText(heading);



        for (int i=0;i<AppContext.publisherList.size();i++){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(AppContext.publisherList.get(i));
            radioButton.setId(i);//set radiobutton id and store it somewhere
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

            radioGroup.addView(radioButton, params);
        }


        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                View radioButton = radioGroup.findViewById(checkedId);
                int radioId = radioGroup.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                String selection = (String) btn.getText();

                Toast.makeText(context, ""+ selection, Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();

            }
        });

    }



    public static void categorySelect(final Context context, final String heading) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.radio_btn_layout, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);

        final RadioGroup radioGroup = (RadioGroup) promptsView.findViewById(R.id.radiogroup);
        final MediumFont mfDialogueHeading = (MediumFont) promptsView.findViewById(R.id.mfDialogueHeading);

        mfDialogueHeading.setText(heading);


        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);




        for (int i=0;i<AppContext.categoryList.size();i++){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(AppContext.categoryList.get(i));
            radioButton.setId(i);//set radiobutton id and store it somewhere
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

            radioGroup.addView(radioButton, params);
        }



        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                View radioButton = radioGroup.findViewById(checkedId);
                int radioId = radioGroup.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                String selection = (String) btn.getText();

                Toast.makeText(context, "News category : "+selection, Toast.LENGTH_SHORT).show();


                MainActivity.updateListBasedOnCategory(context,selection);

                alertDialog.dismiss();

            }
        });

    }


    public static void sortSelect(final Context context, final String heading) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.radio_btn_layout, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);

        final RadioGroup radioGroup = (RadioGroup) promptsView.findViewById(R.id.radiogroup);
        final MediumFont mfDialogueHeading = (MediumFont) promptsView.findViewById(R.id.mfDialogueHeading);

        mfDialogueHeading.setText(heading);


        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);




            RadioButton radioButton1 = new RadioButton(context);
            radioButton1.setText("Ascending");
            //radioButton.setId(2);//
            RadioGroup.LayoutParams params1 = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

            radioGroup.addView(radioButton1, params1);

            RadioButton radioButton2 = new RadioButton(context);
            radioButton2.setText("Descending");
            //radioButton.setId(2);//
            RadioGroup.LayoutParams params2 = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

            radioGroup.addView(radioButton2, params2);



        final android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                View radioButton = radioGroup.findViewById(checkedId);
                int radioId = radioGroup.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                String selection = (String) btn.getText();



                if(selection.equalsIgnoreCase("ascending")){
                    MainActivity.SortRecyclerView(context, true);
                }else{
                    MainActivity.SortRecyclerView(context, false);
                }


                alertDialog.dismiss();

            }
        });



    }



}
