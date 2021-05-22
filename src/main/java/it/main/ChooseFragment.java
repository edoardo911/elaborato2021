package it.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class ChooseFragment extends Fragment
{
    public static String[] data;

    public ChooseFragment() { super(R.layout.choose_fragment); }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.scene = -1;

        String dateReg = "^([0-3]\\d{1})\\/((0|1|2)\\d{1})\\/((19|20)\\d{2})";
        String emailReg = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        EditText grav = view.findViewById(R.id.grav_date);
        CheckBox gravidanza = view.findViewById(R.id.gravidanza);
        gravidanza.setOnCheckedChangeListener((buttonView, isChecked) -> {
            view.findViewById(R.id.grav_label).setEnabled(isChecked);
            grav.setEnabled(isChecked);
        });

        EditText all = view.findViewById(R.id.all_date);
        CheckBox allattamento = view.findViewById(R.id.allattamento);
        allattamento.setOnCheckedChangeListener((buttonView, isChecked) -> {
            view.findViewById(R.id.all_label).setEnabled(isChecked);
            all.setEnabled(isChecked);
        });

        Button confirm = view.findViewById(R.id.confirm);
        confirm.setOnClickListener(v -> {
            EditText email = view.findViewById(R.id.email_choose);
            if((grav.isEnabled() && !grav.getText().toString().matches(dateReg)))
                grav.requestFocus();
            else if((all.isEnabled() && !all.getText().toString().matches(dateReg)))
                all.requestFocus();
            else if(!email.getText().toString().matches(emailReg))
                email.requestFocus();
            else
            {
                JSONObject obj = new JSONObject();
                try
                {
                    obj.put("eta", getEta(data[3]));
                    obj.put("email", email.getText().toString());
                    obj.put("anafilassi", ((CheckBox) view.findViewById(R.id.anafilassi)).isChecked());
                    obj.put("asma", ((CheckBox) view.findViewById(R.id.asma)).isChecked());
                    obj.put("mastocitosi", ((CheckBox) view.findViewById(R.id.mastocitosi)).isChecked());
                    obj.put("polisorbati", ((CheckBox) view.findViewById(R.id.polisorbati)).isChecked());
                    obj.put("conservanti", ((CheckBox) view.findViewById(R.id.conservanti)).isChecked());
                    obj.put("saccarosio", ((CheckBox) view.findViewById(R.id.saccarosio)).isChecked());
                    obj.put("agrumi", ((CheckBox) view.findViewById(R.id.agrumi)).isChecked());
                    obj.put("polietilenico", ((CheckBox) view.findViewById(R.id.polietilenico)).isChecked());
                    obj.put("trometamina", ((CheckBox) view.findViewById(R.id.trometamina)).isChecked());
                    obj.put("orticaria", ((CheckBox) view.findViewById(R.id.orticaria)).isChecked());
                    obj.put("colite_ulcerosa", ((CheckBox) view.findViewById(R.id.col_ul)).isChecked());
                    obj.put("insf_renale", ((CheckBox) view.findViewById(R.id.ins_ren)).isChecked());
                    if(gravidanza.isChecked())
                        obj.put("tempo_grav", grav.getText().toString());
                    if(allattamento.isChecked())
                        obj.put("tempo_allatt", all.getText().toString());
                } catch (JSONException | ParseException e) { e.printStackTrace(); }
                new PostData().execute(obj.toString());
            }
        });
    }

    private int getEta(String date) throws ParseException
    {
        GregorianCalendar userDate = new GregorianCalendar();
        userDate.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(date));
        float diff = (new GregorianCalendar().getTimeInMillis() - userDate.getTimeInMillis());
        diff /= 1000;
        diff /= 60;
        diff /= 60;
        diff /= 24;
        diff /= 364.75;
        return (int) diff;
    }
}
