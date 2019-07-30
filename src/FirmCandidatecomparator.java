import java.util.Comparator;

public class FirmCandidatecomparator implements Comparator<CandidateBank> {

	public int compare(CandidateBank a,CandidateBank b){
		
		double no1=a.interest;
		double no2=b.interest;
		if(no1<no2) return -1;
		if(no1==no2){return 0;}else{return 1;}
	}
}
