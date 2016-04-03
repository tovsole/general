package raspis;


import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Train {

	private URL trainRouteLink;
	private String trainId;
	private String trainNum;
	private String trainTitle;
	private String trainRaspis;
	private String trainDur;
	private String firstStation;
	private String lastStation;
	private String firmName;
	private static final String sqlInsert = "Insert into train_tab (id,    train_Id, train_Route_Link,train_Num,train_Title, train_Raspis, train_Dur)" +
			" values (train_seq.nextval, ?, ?, ?,?,?,?)";
	public List<RouteItem> trainRoute = new ArrayList<>();


	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getFirstStation() {
		return firstStation;
	}

	public void setFirstStation(String firstStation) {
		this.firstStation = firstStation;
	}

	public String getLastStation() {
		return lastStation;
	}

	public void setLastStation(String lastStation) {
		this.lastStation = lastStation;
	}


	//------------------------
	public Train() {
		super();
	}

	// Constructor creates Train from a single row of  raspis table (set of <TD> elements)
	public Train(Element row) throws Exception {
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
				case 0:
					setTrainNum(columns.get(j).text());
					break;
				case 1:
					setTrainTitle(columns.get(j).text());
					break;
				case 2:
					setTrainRaspis(columns.get(j).text());
					break;
				case 3:
					//setTrainArr(columns.get(j).text());
					break;
				case 4:
					//setTrainDep(columns.get(j).text());
					break;
				case 5:
					//setTrainDur(columns.get(j).text());
					setTrainDur("0");
					break;
			}
		}
		setFirmName(parseFirmName());

	}

	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}

	public void setTrainRouteLink(URL trainRouteLink) {
		this.trainRouteLink = trainRouteLink;
	}

	public void setTrainNum(String trainNum) {
		this.trainNum = trainNum;
	}

	public void setTrainTitle(String trainTitle) {
		this.trainTitle = trainTitle;
	}

	public void setTrainRaspis(String trainRaspis) {
		this.trainRaspis = trainRaspis;
	}

	public void setTrainDur(String trainDur) {
		this.trainDur = trainDur;
	}


	public String getTrainId() {
		return this.trainId;
	}

	public URL getTrainRouteLink() {
		return this.trainRouteLink;
	}

	public String getTrainNum(String type) {
		String str = new String (trainNum);
		if (type == "SHORT") {
			if (str.indexOf("(") > 0) {
				str= (str.substring(0, str.indexOf("(")));
			}
		} else {
			str= this.trainNum;
		}
		return str;
	}

	public String getTrainTitle() {
		return this.trainTitle;
	}

	public String getTrainTitleSql() {
		return this.trainTitle.replace("'", "''");
	}

	public String getTrainRaspis() {
		return this.trainRaspis;
	}

	public String getTrainRaspisSql() {
		return this.trainRaspis.replace("'", "''");
	}

	public String getTrainDur() {
		return this.trainDur;
	}

	private String parseTrainIdFromRouteLink() {
		String str = new String(trainRouteLink.toString());
		return (str.substring(str.indexOf("=") + 1, str.lastIndexOf("&")));
	}

	private String parseFirmName(){
		String str = new String (getTrainNum("FULL"));

		if (str.indexOf("(")>0) {
			str= (str.substring(str.indexOf("(") + 1, str.lastIndexOf(")")));
		}
		return str;
	}


	public void addTrainRouteItem(RouteItem routeItem) {
		this.trainRoute.add(routeItem);
	}

	public static String getSqlInsert() {
		return sqlInsert;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Train)) {
			return false;
		}

		Train other = (Train) obj;

		return this.trainRouteLink.equals(other.trainRouteLink);
	}

	@Override
	public int hashCode() {
		return this.trainRouteLink.hashCode();
	}

	@Override
	public String toString() {
		return (getTrainId() + "|" +
				getTrainNum("SHORT") + " | " +
				getTrainTitle() + " | " +
				getTrainRaspis() + " | " +
				getFirstStation()+ " | " +
				getLastStation()+ " | " +
				getTrainDur() + " | " +
				getFirmName() + " | " +
				getTrainRouteLink());
	}


	public static Comparator<Train> compareByTrainId = new Comparator<Train>() {
		public int compare(Train train1, Train train2) {
			return Integer.parseInt(train1.getTrainId()) - Integer.parseInt(train2.getTrainId());
		}
	};

	public static Comparator<Train> compareByTrainNum = new Comparator<Train>() {
		public int compare(Train train1, Train train2) {
			return train1.getTrainNum("SHORT").compareTo(train2.getTrainNum("SHORT"));
		}
	};

}
