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
public class KontaktManageDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.contact_choice_title);
        builder.setItems(R.array.kontakt_manage_array, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
              
                	if (which == 0){
                	   
                		Toast.makeText(getActivity(), "erstes item ausgew�hlt", Toast.LENGTH_SHORT).show();
                		
               	   	                		   
               	     }
                	
                	
                	else if (which == 1){
                		
                		Toast.makeText(getActivity(), "zweites item ausgew�hlt", Toast.LENGTH_SHORT).show();
                		
                	}
                	
                	
               	      else
               	      {
               	    	Toast.makeText(getActivity(), "drittes item ausgew�hlt", Toast.LENGTH_SHORT).show();
               	    	  
               	      }
                		
                            	   
                	                                     	   
               }

				
				
        });
        return builder.create();
    }
        
}