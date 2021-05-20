package it.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity
{
    public static int scene = -1;
    public static boolean auth = false;

    private PendingIntent intent;
    private NfcAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        adapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(adapter != null && adapter.isEnabled())
            adapter.enableForegroundDispatch(this, intent, null, null);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(adapter != null) adapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            Parcelable[] raws = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(raws != null)
            {
                NdefMessage[] msgs = new NdefMessage[raws.length];
                for(int i = 0; i < raws.length; i++)
                {
                    msgs[i] = (NdefMessage) raws[i];
                    byte[] payload = msgs[i].getRecords()[0].getPayload();
                    String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                    int langCodeLength = payload[0] & 0063;
                    try
                    {
                        String content = new String(payload, langCodeLength + 1, payload.length - langCodeLength - 1, textEncoding);
                        if(scene == 0)
                            NFCFragment.elaborate(content);
                        if(scene == 1 && !auth)
                            AuthFragment.elaborate(content, this);
                    }
                    catch(UnsupportedEncodingException e) { e.printStackTrace(); }
                }
            }
        }
    }
}