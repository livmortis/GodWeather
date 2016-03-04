package model;

public class Country {
	private String countryName;
	private int countryId;
	private String countryCode;
	private int city_id;
	public void setCountryeName(String countryName){
		this.countryName=countryName;
	}
	public String getCountryName(){
		return countryName;
	}
	public void setCountryCode(String countryCode){
		this.countryCode=countryCode;
	}
	public String getCountryCode(){
		return countryCode;
	}
	public void setCountryId(int countryId){
		this.countryId=countryId;
	}
	public int getCountryId(){
		return countryId;
	}
	public void setCity_id(int city_id){
		this.city_id=city_id;
	}
	public int getCity_id(){
		return city_id;
	}

}
