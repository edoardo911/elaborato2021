package it.main;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

public class AuthFragment extends Fragment
{
    private void setAdmin()
    {
        TextView authText = getActivity().findViewById(R.id.authText);
        authText.setText("Utente verificato. Ora sei amministratore!");
    }

    public AuthFragment() { super(R.layout.auth_fragment); }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.scene = 1;
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getActivity());
        String text = "";
        if(adapter == null)
            text = "NFC non disponibile";
        else if(!adapter.isEnabled())
            text = "Abilita l'NFC dalle impostazioni";
        if(!text.equals(""))
            Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_LONG).show();
        if(MainActivity.auth)
            setAdmin();
    }
}
