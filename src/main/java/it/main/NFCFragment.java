package it.main;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

/*
    se ha dai 16 (compresi) ai 18 (esclusi) anni: Pfizer
    tra i 18 (compresi) e i 60 (esclusi): Moderna (preferibile)/Pfizer
    oltre i 60 (compresi): AstraZeneca/Johnson & Johnson

    se hanno queste allergie: anafilassi severa, asma, mastocitosi, polisorbati;
    serve una visita allergologica, quindi non generare le date e tutto

    evita in caso di gravidanza/allattamento, nel caso setta la data dopo quei periodi

    Johnson e Johnson non contiene adiuvanti, conservanti, materiali di origine animale,
    quindi sarebbe preferibile per gente allergica/intollerante a quella roba

    Pfizer: fai una nota di prestare attenzione se ha intolleranza al saccarosio;
    Moderna: fai una nota di prestare attenzione se ha intolleranza al saccarosio; evita per intolleranza a polietilenico o trometamina;
    AstraZeneca: fai una nota di prestare attenzione se ha intolleranza al saccarosio, evita per allergia ad agrumi polietilenico
    o insufficienza renale o colite ulcerosa
    Johnson & Johnson: evita per allergia ad agrumi, intolleranza a polietilenico.

    Distanza tra prima e seconda dose:
    Pfizer: 21 giorni;

    Moderna: 28 giorni;
    AstraZeneca; 4-12 settimane;
    J&J: singola dose.
 */
public class NFCFragment extends Fragment
{
    private static View globalView;

    public NFCFragment() { super(R.layout.nfc_fragment); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.scene = 0;
        globalView = view;
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getActivity());
        String text = "";
        if(adapter == null)
            text = "NFC non disponibile";
        else if(!adapter.isEnabled())
            text = "Abilita l'NFC dalle impostazioni";
        if(!text.equals(""))
            Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_LONG).show();
    }

    public static void elaborate(String content)
    {
        if(content.split("&").length == 10)
        {
            ChooseFragment.data = content.split("&");
            NavController nav = Navigation.findNavController(globalView);
            nav.navigate(R.id.action_nfc_fragment_to_choose_fragment);
        }
    }
}
