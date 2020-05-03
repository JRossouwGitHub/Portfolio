package studio.crunchyiee.degreeprogrammemapper;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ModulesAdapter extends ArrayAdapter<String> {
    //Get reference to Context and Database
    Context context;
    DBHandler dbHandler;
    String pathwayExtra;

    public ModulesAdapter(Context context, String[] modules, String pathway) {
        super(context, R.layout.module_card, modules);
        this.context = context;
        this.pathwayExtra = pathway;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Create custom popup for Cards and Popup
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View moduleView = inflater.inflate(R.layout.module_card, parent, false);
        final View popupView = inflater.inflate(R.layout.module_popup, parent, false);
        //Connect to Database
        dbHandler = new DBHandler(getContext(), null, null, 1);
        //Get Card Module Code
        final String moduleItem = getItem(position);
        //Create Array of Modules based on Module Code
        String[] info = dbHandler.getModuleInfo(moduleItem);
        //Create Shared Preference name based on Pathway + Code
        final String prefName = pathwayExtra + info[0];
        //Get reference to Check Box Object
        final CheckBox checkBox = (CheckBox) moduleView.findViewById(R.id.checkBox);
        //Get Shared Preference for Check Box
        final SharedPreferences pref = context.getSharedPreferences("ModulePref", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        boolean isChecked = pref.getBoolean(prefName, false);
        //If Check Box is not checked, set it to FALSE, otherwise set it to TRUE
        if(!isChecked){
            checkBox.setChecked(false);
        }
        else{
            checkBox.setChecked(true);
        }
        //Check Box onClick()
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Check Box values
                boolean isChecked = checkBox.isChecked();
                //Set Shared Preference
                editor.putBoolean(prefName, isChecked);
                editor.apply();
                editor.commit();
                //Refresh modules when checkbox is clicked
                ((ModulesPage)context).resetModules(context);
            }
        });

        //Get reference to Object in module_popup.xml
        TextView name = (TextView) popupView.findViewById(R.id.moduleNameTv);
        TextView code = (TextView) popupView.findViewById(R.id.codeTv);
        TextView mcode = (TextView) popupView.findViewById(R.id.moduleCodeTv);
        TextView level = (TextView) popupView.findViewById(R.id.levelTv);
        TextView credits = (TextView) popupView.findViewById(R.id.creditTv);
        TextView req = (TextView) popupView.findViewById(R.id.reqTv);
        TextView aim = (TextView) popupView.findViewById(R.id.aimTv);
        final TextView cancel = (TextView) popupView.findViewById(R.id.dismissBtn);

        //Set Text on popup
        name.setText("Module Name:\n" + String.valueOf(info[1]));
        code.setText("Module Code:\n" + String.valueOf(info[0]));
        mcode.setText(String.valueOf(info[0]));
        level.setText("Module Level:\n" + String.valueOf(info[2]));
        credits.setText("Module Credits:\n" + String.valueOf(info[3]));
        req.setText("Module Prerequisites:\n" + String.valueOf(info[4]));
        aim.setText("Module Aim:\n" + String.valueOf(info[5]));

        //Create Dialog
        final Dialog dialog = new Dialog(getContext());
        //Cancel button on Dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close Dialog
                dialog.dismiss();
            }
        });
        //Open popup Dialog if Module is clicked
        moduleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show Dialog and disable cancel on outside touch
                dialog.setContentView(popupView);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        //Get reference to Object in module_card.xml
        ImageView lock = (ImageView) moduleView.findViewById(R.id.lockImage);
        TextView moduleCode = (TextView) moduleView.findViewById(R.id.codeTv);
        TextView moduleLevel = (TextView) moduleView.findViewById(R.id.levelTv);
        TextView moduleCredits = (TextView) moduleView.findViewById(R.id.creditsTv);
        TextView modulePrereq = (TextView) moduleView.findViewById(R.id.prereqTv);

        //Set Text on Card
        moduleCode.setText(String.valueOf(info[0]));
        moduleLevel.setText(String.valueOf(info[2]));
        moduleCredits.setText(String.valueOf(info[3]));
        modulePrereq.setText(String.valueOf(info[4]));

        //Set Arrays for unlocking Modules
        String[] preReqs = info[4].split("-");
        String[] preReqModules = new String[preReqs.length];
        //Updates Modules from Prerequisites
        updateModules(preReqs, preReqModules, lock, pref);
        //If a lock is visible, disable checkbox (prevent checking underneath lock image)
        if(lock.getVisibility() == View.VISIBLE){
            checkBox.setEnabled(false);
            checkBox.setChecked(false);
            //Get Check Box values
            isChecked = checkBox.isChecked();
            //Set Shared Preference
            editor.putBoolean(prefName, isChecked);
            editor.apply();
            editor.commit();
        }
        else{
            checkBox.setEnabled(true);
        }

        //Return Module Cards
        return moduleView;
    }

    //Method for updating Modules that have Prerequisites checked
    private void updateModules(String[] preReqs, String[] preReqModules, ImageView lock, SharedPreferences pref){
        for(int i = 0; i < preReqs.length; i++){
            preReqModules[i] = pathwayExtra + preReqs[i];
        }

        if(preReqModules[0].equals(pathwayExtra + "NONE")){
            lock.setVisibility(View.INVISIBLE);
        }
        else{
            if(preReqModules.length == 1){
                boolean module1 = pref.getBoolean(preReqModules[0], false);
                if(module1){
                    lock.setVisibility(View.INVISIBLE);
                }
            }

            else if(preReqModules.length == 2){
                boolean module1 = pref.getBoolean(preReqModules[0], false);
                boolean module2 = pref.getBoolean(preReqModules[1], false);
                if(module1 && module2){
                    lock.setVisibility(View.INVISIBLE);
                }
            }

            else if(preReqModules.length == 3){
                boolean module1 = pref.getBoolean(preReqModules[0], false);
                boolean module2 = pref.getBoolean(preReqModules[1], false);
                boolean module3 = pref.getBoolean(preReqModules[2], false);
                if(module1 && module2 && module3){
                    lock.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
