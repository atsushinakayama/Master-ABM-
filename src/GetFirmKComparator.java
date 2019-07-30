import java.util.Comparator;

public class GetFirmKComparator implements Comparator<Enterprise> {

	@Override
	public int compare(Enterprise a, Enterprise b) {
		// TODO 自動生成されたメソッド・スタブ
		
		double no1=a.K;
		double no2=b.K;
		if(no1<no2)return 1;
		if(no1==no2) {return 0;}else{return -1;}
		
	}

	

}
