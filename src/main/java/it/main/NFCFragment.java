package it.main;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class NFCFragment extends Fragment
{
    public NFCFragment() { super(R.layout.nfc_fragment); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.scene = 0;
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getActivity());
        String text = "";
        if(adapter == null)
            text = "NFC non disponibile";
        else if(!adapter.isEnabled())
            text = "Abilita l'NFC dalle impostazioni";
        if(!text.equals(""))
            Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_LONG).show();
    }
}
