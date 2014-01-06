// Author: Mario Aloise (MAS-Student)

package ch.ffhs.esa.bewegungsmelder.models;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import ch.ffhs.esa.bewegungsmelder.R;
import ch.ffhs.esa.bewegungsmelder.activities.AddContact;


@SuppressLint("UseValueOf")
public class KontaktManageDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.contact_choice_title);
        builder.setItems(R.array.kontakt_manage_array, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
              
                	if (which == 0){
                	   
                		((AddContact)getActivity()).deleteKontakt();
                	Intent intent = new Intent(getActivity(), AddContact.class);
                	startActivity(intent);
                		
                		//Toast.makeText(getActivity(), "erstes item ausgew�hlt", Toast.LENGTH_SHORT).show();
                		
               	   	                		   
               	     }
                	
                	
                	else if (which == 1){
                		
                		
                		((AddContact)getActivity()).modifyKontakt();
                		//Toast.makeText(getActivity(), "zweites item ausgew�hlt", Toast.LENGTH_SHORT).show();
                		
                	}
                	
                	
               	      else
               	      {
               	    	Toast.makeText(getActivity(), "Funktionier leider noch nicht!!", Toast.LENGTH_SHORT).show();
               	    	  
               	      }
                		
                            	   
                	                                     	   
               }

				
				
        });
        return builder.create();
    }
        
}