package edu.purdue.tada;

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
	private boolean radio0 = true;
	private boolean radio1 = false;
	private boolean radio2 = false;
	private String angle1;
	private String angle2;
	private ActivityBridge(){
		
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
	
}