package edu.purdue.tada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* 
 * 	----ABOUT (Spring 2014)----
 * The ActivityBridge is a "structure" where important data can be stored while in one activity
 *  and retrieved later while in another. You will need to use ActivityBridge.getInstance to return
 *  the "INSTANCE" ActivityBridge object and then use the various get and set methods to access and set its data.
 */

public class ActivityBridge {
	
	private static final ActivityBridge INSTANCE = new ActivityBridge();
	private String filepath;
	private String filepath2;
	private String httpsresponse;
	private int imgNum;
	private String latitude1;
	private String latitude2;
	private String longitude1;
	private String longitude2;
	private int imgFlag = 0;
	private boolean checked1 = true;
	private boolean checked2 = true;
	private boolean checked3 = true;
	private boolean radio0 = true;
	private boolean radio1 = false;
	private boolean radio2 = false;
	private String angle1;
	private String angle2;
    private String userID;
	private Map<String, ArrayList<String>> foodPins = new HashMap<String, ArrayList<String>>();  // Initialize mapping elements
	/* NOTE
	 * THE BARCODES ARE STORED IN AN ARRAYLIST<STRING>
	 * THE ACTIVITYBRIDGE INSTANCE KEEPS TRACK OF THE INDEX OF 
	 * THE CURRENT BARCODE IN bIndex
	 */
	
	private ArrayList<String> barcodes;
	private int bIndex;
	private String reviewImagePath;
	private ActivityBridge(){
		this.barcodes = new ArrayList<String>(); 
		bIndex = 0;
	}
	public static ActivityBridge getInstance(){
		return INSTANCE;
	}
	
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getHttpsresponse() {
		return httpsresponse;
	}
	public void setHttpsresponse(String httpsresponse) {
		this.httpsresponse = httpsresponse;
	}
	public int getImgNum() {
		return imgNum;
	}
	public void setImgNum(int imgNum) {
		this.imgNum = imgNum;
	}
	public String getFilepath2() {
		return filepath2;
	}
	public void setFilepath2(String filepath2) {
		this.filepath2 = filepath2;
	}
	public String getLatitude1() {
		return latitude1;
	}
	public void setLatitude1(String latitude1) {
		this.latitude1 = latitude1;
	}
	public String getLatitude2() {
		return latitude2;
	}
	public void setLatitude2(String latitude2) {
		this.latitude2 = latitude2;
	}
	public String getLongitude1() {
		return longitude1;
	}
	public void setLongitude1(String longitude1) {
		this.longitude1 = longitude1;
	}
	public String getLongitude2() {
		return longitude2;
	}
	public void setLongitude2(String longitude2) {
		this.longitude2 = longitude2;
	}
	public int getImgFlag() {
		return imgFlag;
	}
	public void setImgFlag(int imgFlag) {
		this.imgFlag = imgFlag;
	}
	public boolean isChecked1() {
		return checked1;
	}
	public void setChecked1(boolean checked1) {
		this.checked1 = checked1;
	}
	public boolean isChecked2() {
		return checked2;
	}
	public void setChecked2(boolean checked2) {
		this.checked2 = checked2;
	}
	
	public void setChecked3() {
		this.checked3 = false;
	}
	
	public boolean getChecked3() {
		return checked3;
	}
	
	public boolean isRadio0() {
		return radio0;
	}
	public void setRadio0(boolean radio0) {
		this.radio0 = radio0;
	}
	public boolean isRadio1() {
		return radio1;
	}
	public void setRadio1(boolean radio1) {
		this.radio1 = radio1;
	}
	public boolean isRadio2() {
		return radio2;
	}
	public void setRadio2(boolean radio2) {
		this.radio2 = radio2;
	}
	public String getAngle1() {
		return angle1;
	}
	public void setAngle1(String angle1) {
		this.angle1 = angle1;
	}
	public String getAngle2() {
		return angle2;
	}
	public void setAngle2(String angle2) {
		this.angle2 = angle2;
	}
	//returns barcode at index
	public String getBarCode(int index){
		return this.barcodes.get(index);
	}
	//sets barcode to current element
	public void setBarCode(String barcode){
		this.barcodes.add(bIndex,barcode);
		bIndex++;
	}
	//returns size of arraylist
	public int numOfBarCodes(){
		return this.barcodes.size();
	}
	//sets index to 0 and clears the data
	public void clearBarcodes(){
		bIndex = 0;
		this.barcodes.clear();
	}
    public void setUserID(String id) {this.userID = id;}
	public int getfoodPinsSize() {
		return this.foodPins.size();
	}
	public Set<String> getfoodPinsKeys() {
		return this.foodPins.keySet();
	}
	public ArrayList<String> getfoodPinsNames(String key) {
		return this.foodPins.get(key);
	}
	public void setfoodPins(String key, ArrayList<String> value) {
		this.foodPins.put(key, value);
	}
    public void addNewPin(String name, String coord) {
        ArrayList<String> newFood = new ArrayList<String>();
        int i;
        for(i = 0; i < 5; i++) newFood.add(name);
        this.foodPins.put(coord, newFood);
    }
    public void removePin(String coord) {
        this.foodPins.remove(coord);
    }
    public void editPin(String name, String coord) {
        ArrayList<String> temp = new ArrayList<String>();
        temp = this.foodPins.get(coord);
        temp.set(0,name);
        foodPins.put(coord,temp);
    }
    public String newTagfile() {
        String tagfile = this.userID;
        tagfile.concat("\n"+this.foodPins.size()+"\n0\n");
        for(String key : this.foodPins.keySet()) {
            tagfile.concat(key+"\n");
            tagfile.concat("0000 "+foodPins.get(key).get(0)+"\n");
            tagfile.concat("0000 "+foodPins.get(key).get(0)+"\n");
            tagfile.concat("0000 "+foodPins.get(key).get(0)+"\n");
            tagfile.concat("0000 "+foodPins.get(key).get(0)+"\n");
            tagfile.concat("0000 "+foodPins.get(key).get(0)+"\n");
            tagfile.concat("0\n");
        }
        return tagfile;
    }
	public void setReviewImagePath(String reviewImagePath) {
		this.reviewImagePath = reviewImagePath;		
	}
	public String getReviewImagePath(){
		return this.reviewImagePath;
	}
	
	
}