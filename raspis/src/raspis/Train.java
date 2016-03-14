package raspis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Train {

	private URL trainRouteLink  ;
	private String trainId;
	private String trainNum ;
	private String trainTitle;
	private String trainRaspis;
	private String trainDep;
	private String trainArr;
	private String trainDur;
	public List<RouteItem> trainRoute = new ArrayList<>();

	//------------------------
    public Train(){
		super();
	}
	// Constructor creates Train from a single row of  raspis table (set of <TD> elements)
	public Train(Element row) throws Exception{
		super();

		final URL baseUrl = new URL("http://www.uz.gov.ua/passengers/timetable/");

		Element href = row.select("a").first();
		URL routeUrl = new URL(baseUrl, href.attr("href"));

		setTrainRouteLink(routeUrl);
		setTrainId(parseTrainIdFromRouteLink());

		ArrayList<Element> columns = row.getElementsByTag("td");
			for (int j = 0; j < columns.size(); j++) // for every column
			{
				switch (j) {
					case 0: setTrainNum(columns.get(j).text()); break;
					case 1: setTrainTitle(columns.get(j).text()); break;
					case 2: setTrainRaspis(columns.get(j).text());break;
					case 3:	setTrainArr(columns.get(j).text());break;
					case 4:	setTrainDep(columns.get(j).text());break;
					case 5:	setTrainDur(columns.get(j).text());break;
				}
			}
	}
	public void setTrainId(String trainId){
		this.trainId = trainId;
	}

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


	public String getTrainId(){
		return this.trainId;
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

	public String parseTrainIdFromRouteLink(){
		String str = new String(trainRouteLink.toString());
		return ( str.substring(str.indexOf("=")+1,str.lastIndexOf("&")));
	}
	public void addTrainRouteItem (RouteItem routeItem){
		this.trainRoute.add(routeItem);
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Train)) {
			return false;}

		Train other = (Train) obj;

		return this.trainRouteLink.equals(other.trainRouteLink);
	}

	@Override
	public int hashCode() {
			return this.trainRouteLink.hashCode();
	}

    @Override
	public String toString(){
		return (getTrainId()+"|"+getTrainNum()+" | "+getTrainTitle()+" | "+getTrainRaspis()+" | "+getTrainArr()+" | "+getTrainDep()+" | "+getTrainDur()+" | "+getTrainRouteLink());
	}

	public void parseRoute() throws Exception {
        System.out.println("    >> Parsing train "+ getTrainNum());
		Document doc = Jsoup.connect(getTrainRouteLink().toString()).get();

		Element table = doc.getElementById("cpn-timetable");

		Element tbody = table.select("tbody").last();

		ArrayList<Element> rows = tbody.getElementsByTag("tr");
		for (Element row : rows) // for every row of route of train
		{
			this.addTrainRouteItem( new RouteItem(row));
		}
	}

	public static Comparator<Train> compareByTrainId = new Comparator<Train>(){
		public int compare (Train train1, Train train2) {
			return Integer.parseInt(train1.getTrainId()) - Integer.parseInt(train2.getTrainId());
		}
	};

	public static Comparator<Train> compareByTrainNum = new Comparator<Train>(){
		public int compare (Train train1, Train train2) {
			return train1.getTrainNum().compareTo(train2.getTrainNum());
		}
	};

	public void saveToDb()
	{

		System.out.println("��� ����� ���������� � ��");
	}
	
	public void saveToFile() 
	{

		System.out.println("��� ����� ���������� � ����");
	}
	
}
