package ch.ffhs.esa.bewegungsmelder;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;




public class KontaktDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.contact_choice_title);
        builder.setItems(R.array.kontakt_wahlmoeglichkeiten_array, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
              
                	   
               }

				

				
        });
        return builder.create();
    }
        
}