package raspis;

import java.net.URL;
import java.util.List;

public class Train {

	public Train() {
		super();
	}

	private URL trainRouteLink  ;
	private String trainId;
	private String trainNum ;
	private String trainTitle;
	private String trainRaspis;
	private String trainDep;
	private String trainArr;
	private String trainDur;
	private List<RouteItem> trainRoute;
	
	public void setTrainRouteLink(URL trainRouteLink){
		this.trainRouteLink = trainRouteLink;
	}

	public void setTrainNum (String trainNum)
	{
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

	public String getTrainIdFromLink(){
		return "AAA";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Train)) {
			return false;}

		Train other = (Train) obj;

		//return false;
		return this.trainRouteLink.equals(other.trainRouteLink);
	}

	@Override
	public int hashCode() {
		return this.trainRouteLink.hashCode();
	}


	public void parseRoutPage(){
		///
	}

	public void saveToDb()
	{

		System.out.println("��� ����� ���������� � ��");
	}
	
	public void saveToFile() 
	{

		System.out.println("��� ����� ���������� � ����");
	}
	
}
