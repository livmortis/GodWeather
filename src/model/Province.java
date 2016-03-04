package model;

public class Province {
	private String provinceName;
	private int provinceId;
	private String provinceCode;
	public void setProvinceName(String provinceName){
		this.provinceName=provinceName;
	}
	public String getProvinceName(){
		return provinceName;
	}
	public void setProvinceCode(String provinceCode){
		this.provinceCode=provinceCode;
	}
	public String getProvinceCode(){
		return provinceCode;
	}
	public void setProvinceId(int provinceId){
		this.provinceId=provinceId;
	}
	public int getProvinceId(){
		return provinceId;
	}

}
