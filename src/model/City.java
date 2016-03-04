package model;

public class City {
	private String cityName;
	private int cityId;
	private String cityCode;
	private int province_id;
	public void setCityName(String cityName){
		this.cityName=cityName;
	}
	public String getCityName(){
		return cityName;
	}
	public void setCityCode(String cityCode){
		this.cityCode=cityCode;
	}
	public String getCityCode(){
		return cityCode;
	}
	public void setCityId(int cityId){
		this.cityId=cityId;
	}
	public int getCityId(){
		return cityId;
	}
	public void setProvince_id(int province_id){
		this.province_id=province_id;
	}
	public int getProvince_id(){
		return province_id;
	}
}
