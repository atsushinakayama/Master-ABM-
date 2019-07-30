import java.util.ArrayList;

public class BrProcess {

	int id;
	double L;//総額
	double lb,lf;

	public BrProcess(int i) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.id=1;
		L=0;

	}



	ArrayList<Double> Llist=new ArrayList<Double>();
	//一応作っておく
	ArrayList<IBBorrow> lblist=new ArrayList<IBBorrow>();
	ArrayList<BorrowingBank> bblist=new ArrayList<BorrowingBank>();



}
