import java.util.ArrayList;

public class OutPut {

	/*
	 * 各試行ごとのの結果をリストに格納
	 * output.size()==atp
	 * リストget(x)⇒termごとの結果
	 */

	int atp;//atp試行目
	int term;

	public OutPut(int i,int term) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.atp=i;
		this.term=term;
	}

	//各ターム出力結果
	ArrayList<Double> Grossproduction=new ArrayList<Double>();
	ArrayList<Double> Productgrowthrate=new ArrayList<Double>();
	ArrayList<Integer> FFnum=new ArrayList<Integer>();
	ArrayList<Double> Avginterest=new ArrayList<Double>();
	ArrayList<Integer> Rejectfinancing=new ArrayList<Integer>();

	//企業（平均)
	ArrayList<Double> Avgfirmlev=new ArrayList<Double>();

	//銀行（平均）
	ArrayList<Double> Avgbankclientnum=new ArrayList<Double>();
	
	//エージェント
	ArrayList<Institution> Finalbank=new ArrayList<Institution>();
	ArrayList<Enterprise> Finalfirm=new ArrayList<Enterprise>();
	
	ArrayList<Institution> Initbank=new ArrayList<Institution>();

	public int getBanksize() {
		// TODO 自動生成されたメソッド・スタブ
		return Finalbank.size();
	}
	
	public int getFirmSize() {
		return Finalfirm.size();
	}
	
	

}
