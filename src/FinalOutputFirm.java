import java.util.ArrayList;
import java.util.Date;

public class FinalOutputFirm {
	
	private int id;
	private double avgdeg;
	private double sddeg;

	public FinalOutputFirm(int i, Double double1, Double double2) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.id=i;
		this.avgdeg=double1;
		this.sddeg=double2;
	}

	
	public double getAvgDegree() {
		return avgdeg;
	}


	public double getSDDegree() {
		// TODO 自動生成されたメソッド・スタブ
		return sddeg;
	}
}
