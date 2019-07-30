
public class LevProbably {
	
	int levnum;//レバレッジナンバー0-9
	double count;//強度値割合の累積値
	double level;//レバレッジレベル

	
	
	public LevProbably(int levnum, double count, double level) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.levnum=levnum;
		this.count=count;
		this.level=level;
	}
	


	public double getCount() {
		return count;
	}
	
	

	public double getLevel() {
		// TODO 自動生成されたメソッド・スタブ
		return level;
	}
}
