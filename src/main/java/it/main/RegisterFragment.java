package it.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import it.inps.spid.activity.IdentityProviderSelectorActivityContract;
import it.inps.spid.model.IdentityProvider;
import it.inps.spid.model.SpidParams;
import it.inps.spid.utils.*;

public class RegisterFragment extends Fragment
{
    ActivityResultLauncher launcher = registerForActivityResult(new IdentityProviderSelectorActivityContract(), result -> {
        if(result.getSpidEvent().equals(SpidEvent.GENERIC_ERROR))
            Toast.makeText(getContext(), "Generic Error", Toast.LENGTH_SHORT).show();
        else if(result.equals(SpidEvent.NETWORK_ERROR))
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Attenzione!")
                    .setMessage("Nessuna connessione a internet")
                    .setPositiveButton("Ok", (dialog, which) -> { dialog.dismiss(); })
                    .show();
        else if(result.getSpidEvent().equals(SpidEvent.SESSION_TIMEOUT))
            Toast.makeText(getContext(), "Session Error", Toast.LENGTH_SHORT).show();
        else if(result.getSpidEvent().equals(SpidEvent.SPID_CONFIG_ERROR))
            Toast.makeText(getContext(), "Config Error", Toast.LENGTH_SHORT).show();
        else if(result.getSpidEvent().equals(SpidEvent.SUCCESS))
        {
            Log.i(getClass().getSimpleName(), "cookies = " + result.getSpidResponse().getCookies());
            new MaterialAlertDialogBuilder(getContext())
                    .setMessage("Ha funzionato")
                    .setPositiveButton("Ok", (dialog, which) -> { dialog.dismiss(); })
                    .show();
        }
        else if(result.getSpidEvent().equals(SpidEvent.USER_CANCELLED))
            Toast.makeText(getContext(), "Errore 1", Toast.LENGTH_SHORT).show();
        else Toast.makeText(getContext(), result.toString(), Toast.LENGTH_SHORT).show();
    });

    public RegisterFragment() { super(R.layout.register_fragment); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.scene = -1;
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener((View v) -> {
              SpidParams.Config params = new SpidParams.Config("https://vaccinationdata.duckdns.org:8088/", "https://vaccinationdata.duckdns.org:8088/", 60, "https://www.spid.gov.it/", "https://www.spid.gov.it/richiedi-spid");
              IdentityProvider.Builder prov = new IdentityProvider.Builder()
                      .addCustomIdentityProvider("asd", R.drawable.ic_spid_idp_posteid, "https://vaccinationdata.duckdns.org:8088/");
              SpidParams p = new SpidParams(params, prov.build());
              launcher.launch(p);
        });

        NavController nav = Navigation.findNavController(view);
        button = view.findViewById(R.id.button2);
        button.setOnClickListener(v -> {
            nav.navigate(R.id.action_registerFragment_to_nfc_fragment);
        });

        button = view.findViewById(R.id.button3);
        button.setOnClickListener(v -> {
            nav.navigate(R.id.action_registerFragment_to_login_fragment);
        });
    }
}
