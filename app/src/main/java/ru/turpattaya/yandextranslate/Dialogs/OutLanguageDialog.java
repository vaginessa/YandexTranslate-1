package ru.turpattaya.yandextranslate.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import ru.turpattaya.yandextranslate.MainActivity;
import ru.turpattaya.yandextranslate.R;


public class OutLanguageDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LinkedHashMap<String, String> languageMap = new LinkedHashMap<>();
        final String[] keys = getContext().getResources().getStringArray(R.array.languages_keys);
        String[] languages = getContext().getResources().getStringArray(R.array.languages);
        for(int i =0; i<keys.length; i++){
            languageMap.put(keys[i], languages[i]);
        }

        builder.setTitle("Choice language")
                .setItems(languages, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String languageOut = (new ArrayList<>(languageMap.values())).get(which);
                        String key = (new ArrayList<>(languageMap.keySet()).get(which));
                        Log.d("передаем в Main", languageOut + key);
                        MainActivity callingActivity = (MainActivity) getActivity();
                        callingActivity.getOutLanguageFromDialog(languageOut, key);
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
