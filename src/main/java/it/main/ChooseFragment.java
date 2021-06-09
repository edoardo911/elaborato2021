package it.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

public class ChooseFragment extends Fragment
{
    private static boolean navigate = false;

    public static String[] data;
    private static EditText email;

    private static ChooseFragment instance;

    public ChooseFragment() { super(R.layout.choose_fragment); }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.scene = -1;
        instance = this;

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
            email = view.findViewById(R.id.email_choose);
            if((grav.isEnabled() && !grav.getText().toString().matches(dateReg)))
                grav.requestFocus();
            else if((all.isEnabled() && !all.getText().toString().matches(dateReg)))
                all.requestFocus();
            else if(!email.getText().toString().matches(emailReg))
                email.requestFocus();
            else
            {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("vaccinati").whereEqualTo("codiceFiscale", data[1]).get().addOnCompleteListener(task -> {
                    if(task.getResult().getDocuments().size() == 0)
                    {
                        JSONObject obj = new JSONObject();
                        try
                        {
                            obj.put("nome", data[7]);
                            obj.put("cognome", data[2]);
                            obj.put("eta", getEta(data[3]));
                            obj.put("email", email.getText().toString());
                            obj.put("anafilassi", ((CheckBox) view.findViewById(R.id.anafilassi)).isChecked());
                            obj.put("asma", ((CheckBox) view.findViewById(R.id.asma)).isChecked());
                            obj.put("mastocitosi", ((CheckBox) view.findViewById(R.id.mastocitosi)).isChecked());
                            obj.put("polisorbati", ((CheckBox) view.findViewById(R.id.polisorbati)).isChecked());
                            obj.put("conservanti", ((CheckBox) view.findViewById(R.id.conservanti)).isChecked());
                            obj.put("saccarosio", ((CheckBox) view.findViewById(R.id.saccarosio)).isChecked());
                            obj.put("diabete", ((CheckBox) view.findViewById(R.id.diabete)).isChecked());
                            obj.put("agrumi", ((CheckBox) view.findViewById(R.id.agrumi)).isChecked());
                            obj.put("polietilenico", ((CheckBox) view.findViewById(R.id.polietilenico)).isChecked());
                            obj.put("trometamina", ((CheckBox) view.findViewById(R.id.trometamina)).isChecked());
                            obj.put("orticaria", ((CheckBox) view.findViewById(R.id.orticaria)).isChecked());
                            obj.put("colite_ulcerosa", ((CheckBox) view.findViewById(R.id.col_ul)).isChecked());
                            obj.put("insf_renale", ((CheckBox) view.findViewById(R.id.ins_ren)).isChecked());
                            obj.put("gravidanza", gravidanza.isChecked() ? grav.getText().toString() : false);
                            obj.put("allattamento", allattamento.isChecked() ? all.getText().toString() : false);
                        } catch (JSONException e) { e.printStackTrace(); }
                        new PostData(this).execute(obj);
                    }
                    else if(!navigate)
                    {
                        NavController nav = Navigation.findNavController(view);
                        nav.navigate(R.id.action_choose_fragment_to_alredy_fragment);
                        navigate = true;
                    }
                });
            }
        });
    }

    public void focusEmail() { email.requestFocus(); }

    public int getEta(String date)
    {
        String todaysDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        int year = Integer.parseInt(todaysDate.split("/")[2]) - Integer.parseInt(date.split("/")[2]);
        if((Integer.parseInt(todaysDate.split("/")[1]) * 31) + Integer.parseInt(todaysDate.split("/")[0]) <
                (Integer.parseInt(date.split("/")[1]) * 31) + Integer.parseInt(date.split("/")[0]))
            return year - 1;
        return year;
    }

    public void userHasBooked()
    {
        NavController nav = Navigation.findNavController(instance.getView());
        nav.navigate(R.id.action_choose_fragment_to_booked_fragment);
    }
}
