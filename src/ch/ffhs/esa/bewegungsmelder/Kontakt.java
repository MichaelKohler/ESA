package ch.ffhs.esa.bewegungsmelder;


/**
 * Diese Klasse definiert das 'Kontakt' Objekt smat setter und getter Methoden, das in verschiedenen anderen Klassen 
 * benutzt wird.
 * @author Mario Aloise
 */


public class Kontakt {
	private String name;
	private String phoneNo;
	private String objektID;
	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getPhoneNo() {
		return phoneNo;
	}
	
	
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	
	public String getObjektID() {
		return objektID;
	}
	
	
	public void setObjektID(String objektID) {
		this.objektID = objektID;
	}
	
	
	
}
