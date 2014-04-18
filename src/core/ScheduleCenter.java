package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;



public class ScheduleCenter {
	
	public static final ScheduleCenter Center = new ScheduleCenter();
	String queryFile="query.txt";
	///Users/DXY/programming/Myeclipse_workspace/taxiSharing/query.txt
	String taxiFile="taxi.txt";
	///Users/DXY/programming/Myeclipse_workspace/taxiSharing/taxi.txt
	RoadGrid rg;
	List<Rider> Requests = new ArrayList<Rider>();
	List<Taxi> RunTaxi = new ArrayList<Taxi>();//����taxi
	List<Taxi> AvailableTaxi= new ArrayList<Taxi>();//�пյ�taxi
	
	//��ʼ��Requests��RunTaxi
	public ScheduleCenter(){
		
		try {
			rg = new RoadGrid();
			rg.buildStaticIndex();//build only once, spatial & temporal index
			//rg.buildDynamicIndex(RunTaxi);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		readFile(1);//rider query file
		readFile(2);//taxi location file
		
	}
	
	

	private void realTimeUpdate() {
		// TODO Auto-generated method stub
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask(){
		   public void run()
		   {
		       
		   }
		},1000,10000);//1秒后启动任务,以后每隔10秒执行一次线程 
	}



	private void readFile(int category) {
		// TODO Auto-generated method stub
		if(category ==1){
			File file = new File(queryFile);
	
			BufferedReader reader = null;
			
			try {
				reader = new BufferedReader(new FileReader(file));
	
				String content = null;
				
				String[] texts=new String[5];
				int i=0;
				while ((content = reader.readLine()) != null) {
					StringTokenizer token = new StringTokenizer(content, ",");
	
					while (token.hasMoreElements()) {
						texts[i]=token.nextToken();
						//System.out.println(i+"  "+texts[i]);
						i++;
					}
					Requests.add(new Rider(texts));
					i=0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		else {
			File file = new File(taxiFile);
			
			BufferedReader reader = null;
			
			try {
				reader = new BufferedReader(new FileReader(file));
	
				String content = null;
				
				String[] texts=new String[9];
				int i=0;
				while ((content = reader.readLine()) != null) {
					StringTokenizer token = new StringTokenizer(content, ",");
	
					while (token.hasMoreElements()) {
						texts[i]=token.nextToken();
						//System.out.println(i+"  "+texts[i]);
						i++;
					}
					RunTaxi.add(new Taxi(texts));
					i=0;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void schedule(){
		for(Rider rider:Requests){
			System.out.println("Rider: "+rider.toString());
			//Ѱ�����ĳ��⳵
			List<Taxi> Candidates=findNearestTaxis(rider);
			System.out.println("Candidate taxis:");
			for(Taxi t:Candidates){
				System.out.println(t.toString());
			}
			System.out.println();
		}
	}
	
	private List<Taxi> findNearestTaxis(Rider rider) {
		// TODO Auto-generated method stub
		List<Taxi> candidates=new ArrayList<Taxi>();
		for(Taxi taxi:AvailableTaxi){
			if(rider.range180(taxi)){
				candidates.add(taxi);
			}
		}
		return candidates;
	}

	public void addRequest(Rider rider){
		Requests.add(rider);
		
	}

	public List<Rider> getRequests() {
		return Requests;
	}

	public void setRequests(List<Rider> requests) {
		Requests = requests;
	}

	
	
	public List<Taxi> getRunTaxi() {
		return RunTaxi;
	}

	public void setRunTaxi(List<Taxi> runTaxi) {
		RunTaxi = runTaxi;
	}

	public static void main(String[] args) {
		ScheduleCenter sc = ScheduleCenter.Center;
//		for(Rider rider:sc.getRequests()){
//			rider.toString();
//		}
		List<Taxi> rt=sc.getRunTaxi();
		for(int i=0;i<rt.size();i++){
			System.out.println(rt.get(i));
		}
		for(Taxi taxis:rt){
			System.out.println("������");
			System.out.println(taxis.toString());
		}
		//sc.schedule();
	}
}

