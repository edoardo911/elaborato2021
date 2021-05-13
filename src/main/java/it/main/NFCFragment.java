package it.main;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;

public class NFCFragment extends Fragment
{
    public NFCFragment() { super(R.layout.nfc_fragment); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
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
