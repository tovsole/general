package raspis;

import java.net.URL;

public class Train {

	public Train() {
		super();
	}

	private URL trainRouteLink  ;
	private String trainNum ;
	private String trainTitle;
	private String trainRaspis;
	private String trainDep;
	private String trainArr;
	private String trainDur;
	
	public void setTrainRouteLink(URL trainRouteLink){
		this.trainRouteLink = trainRouteLink;
	}
	public void setTrainNum (String trainNum){
		this.trainNum=trainNum;
	}
	public void setTrainTitle(String trainTitle){
		this.trainTitle = trainTitle;
	}
	public void setTrainRaspis(String trainRaspis){
		this.trainRaspis = trainRaspis;
	}
	public void setTrainDep(String trainDep){
		this.trainDep=trainDep;
	}
	public void setTrainArr(String trainArr){
		this.trainArr=trainArr;
	}
	public void setTrainDur(String trainDur){
		this.trainDur=trainDur;
	}
	

	public URL getTrainRouteLink(){
		return this.trainRouteLink;
	}
	public String getTrainNum(){
		return this.trainNum;
	}
	public String getTrainTitle(){
		return this.trainTitle;
	}
	public String getTrainRaspis(){
		return this.trainRaspis;
	}
	public String getTrainDep(){
		return this.trainDep;
	}
	public String getTrainArr(){
		return this.trainArr;
	}
	public String getTrainDur(){
		return this.trainDur;
	}
	
	public void saveToDb() 
	{
	  System.out.println("Тут будет сохранение в БД");	
	}
	
	public void saveToFile() 
	{
	  System.out.println("Тут будет сохранение в файл");	
	}
	
}
