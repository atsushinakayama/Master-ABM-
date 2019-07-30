
public class Leverage {


	int levnum;//0-9

	double level;//レバレッジレベル
	double evalue;//評価値
	int x;//ターム
	String state;//選ばれている状態もしくは現在のレバレッジに近い値
	String range;//選択可能範囲か
	String adjacent="No";//近隣かどうか
	String finalrange="No";//このレバレッジの中から次期レバレッジを選択する

	double X;//評価値の強度
	double c=0.1;
	double pl;//選択される確率
	double vi;//strength係数
	


	public Leverage(double level,double evalue,int x,String state,String range,int i,double vi) {
		this.level=level;
		this.evalue=evalue;
		this.x=x;
		this.state=state;
		this.range=range;
		this.levnum=i;
		this.vi=vi;
	}


	public void setStrength() {
		if(evalue<0)evalue=0;
		X=Math.pow(evalue/c,vi);
		
		
	}

	public void setAdjacent(String r) {
		adjacent=r;
	}


}
