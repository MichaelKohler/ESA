// Author: Mario Aloise (MAS-Student)

package ch.ffhs.esa.bewegungsmelder;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;




@SuppressLint("UseValueOf")
public class KontaktDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.contact_choice_title);
        builder.setItems(R.array.kontakt_wahlmoeglichkeiten_array, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
              
                	if (which == 0){
                	   
                		Intent intent = new Intent(getActivity(), ContactListActivity.class);
                		startActivity(intent);
                		
               	   	 //Toast.makeText(getActivity(), "erstes item ausgew�hlt", Toast.LENGTH_LONG).show();
               		   
               	     }
               	      else
               	      {
               	    	  Intent intent = new Intent(getActivity(), KontaktManuellHinzu.class);
                  		startActivity(intent);
               	    	  
               	      }
                		
                

                	   
                	
                     
                	   
               }

				

				
        });
        return builder.create();
    }
        
}