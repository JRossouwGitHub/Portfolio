package studio.crunchyiee.degreeprogrammemapper;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditModulesAdapter extends ArrayAdapter<String> {
    //Get reference to Context and Database
    Context context;
    DBHandler dbHandler;
    String pathwayExtra;

    public EditModulesAdapter(Context context, String[] modules, String pathway) {
        super(context, R.layout.module_card, modules);
        this.context = context;
        this.pathwayExtra = pathway;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Create custom popup for Cards and Popup
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        View editModuleView = inflater.inflate(R.layout.edit_modules_card, parent, false);
        final View removeModuleView = inflater.inflate(R.layout.remove_module_popup, parent, false);
        final View editModulePopupView = inflater.inflate(R.layout.edit_module_popup, parent, false);

        //Connect to Database
        dbHandler = new DBHandler(getContext(), null, null, 1);
        //Get Card Module Code
        final String moduleItem = getItem(position);
        //Create Array of Modules based on Module Code
        final String[] info = dbHandler.getModuleInfo(moduleItem);

        //Get reference to Object in edit_modules_card.xml
        TextView moduleCode = (TextView) editModuleView.findViewById(R.id.codeTv);
        TextView moduleLevel = (TextView) editModuleView.findViewById(R.id.levelTv);
        TextView moduleCredits = (TextView) editModuleView.findViewById(R.id.creditsTv);
        TextView modulePrereq = (TextView) editModuleView.findViewById(R.id.prereqTv);
        ImageView cogBtn = (ImageView) editModuleView.findViewById(R.id.imageView4);
        ImageView removeBtn = (ImageView) editModuleView.findViewById(R.id.imageView5);

        //Get reference to Object in remove_module_popup.xml
        final TextView rModuleCode = (TextView) removeModuleView.findViewById(R.id.removeModuleTv);
        Button deleteModuleBtn = (Button) removeModuleView.findViewById(R.id.deleteBtn);

        //Get reference to Object in module_edit_popup.xml
        final TextView mEditTitle = (TextView) editModulePopupView.findViewById(R.id.codeTv);
        final EditText mName = (EditText) editModulePopupView.findViewById(R.id.mNameInput);
        final EditText mCode = (EditText) editModulePopupView.findViewById(R.id.editText2);
        final EditText mLevel = (EditText) editModulePopupView.findViewById(R.id.editText3);
        final EditText mCredits = (EditText) editModulePopupView.findViewById(R.id.editText4);
        final EditText mPrereq = (EditText) editModulePopupView.findViewById(R.id.editText5);
        final EditText mAim = (EditText) editModulePopupView.findViewById(R.id.editText6);
        final EditText mSemester = (EditText) editModulePopupView.findViewById(R.id.editText7);
        Button mUpdateBtn = (Button) editModulePopupView.findViewById(R.id.updateModulesBtn);

        //Create Dialog
        final Dialog dialog = new Dialog(getContext());

        cogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTitle.setText("Edit Module: " + info[0]);
                mName.setText(info[1]);
                mCode.setText(info[0]);
                mLevel.setText(info[2]);
                mCredits.setText(info[3]);
                mPrereq.setText(info[4]);
                mAim.setText(info[5]);
                mSemester.setText(info[6]);
                dialog.setContentView(editModulePopupView);
                dialog.show();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rModuleCode.setText(moduleItem);
                //Show Dialog and disable cancel on outside touch
                dialog.setContentView(removeModuleView);
                dialog.show();
            }
        });

        deleteModuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    dbHandler.removeModule(pathwayExtra, moduleItem);
                    ((EditModulesPage)context).resetModules(context);
                    Toast.makeText(context, "Module: " + moduleItem + " has been removed!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }catch(Exception e){
                    Toast.makeText(context, "Error Removing Module: " + moduleItem, Toast.LENGTH_LONG).show();
                }
            }
        });

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Toast.makeText(context, "Module: " + moduleItem + " has been updated!", Toast.LENGTH_LONG).show();
                    dbHandler.updateModule(moduleItem,
                            pathwayExtra,
                            mName.getText().toString(),
                            mCode.getText().toString(),
                            Integer.parseInt(mLevel.getText().toString()),
                            Integer.parseInt(mCredits.getText().toString()),
                            mPrereq.getText().toString(),
                            mAim.getText().toString(),
                            Integer.parseInt(mSemester.getText().toString()));
                    ((EditModulesPage)context).resetModules(context);
                    dialog.dismiss();

                }catch (Exception e){
                    Toast.makeText(context, "Error Updating Module: " + moduleItem, Toast.LENGTH_LONG).show();
                }

            }
        });

        //Set Text on Card
        moduleCode.setText(String.valueOf(info[0]));
        moduleLevel.setText(String.valueOf(info[2]));
        moduleCredits.setText(String.valueOf(info[3]));
        modulePrereq.setText(String.valueOf(info[4]));

        return editModuleView;
    }
}
