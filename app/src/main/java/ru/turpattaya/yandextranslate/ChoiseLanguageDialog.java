package ru.turpattaya.yandextranslate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;


public class ChoiseLanguageDialog extends DialogFragment {

    public interface ChoiseLanguageDialogHost{
        void today();
        void yesterday();
        void lastSevenDays();
        void allTime();

    }

    static CharSequence[] items = {"Today", "Yesterday", "Last 7 days", "All time"};
    String selection;
    private int item;

   /* @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(items, -1, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        switch (selectedPosition) {
                            case 0:
                                selection = (String) items[selectedPosition];
                                ((SortDialogHost) (getActivity())).today();
                                Log.d("Valo", "id=>" + selectedPosition);
                                onDismiss(dialog);
                                break;

                            case 1:
                                selection = (String) items[selectedPosition];
                                ((SortDialogHost) (getActivity())).yesterday();
                                Log.d("Valo", "id=>" + selectedPosition);
                                onDismiss(dialog);
                                break;

                            case 2:
                                selection = (String) items[selectedPosition];
                                ((SortDialogHost) (getActivity())).lastSevenDays();
                                Log.d("Valo", "id=>" + selectedPosition);
                                onDismiss(dialog);
                                break;

                            case 3:
                                selection = (String) items[selectedPosition];
                                ((SortDialogHost) (getActivity())).allTime();
                                Log.d("Valo", "id=>" + selectedPosition);
                                onDismiss(dialog);
                                break;
                }
            }
        });

        return builder.create();
    }*/


}
