import java.util.ArrayList;

public class ResultList implements Cloneable{
	
	private int atp;//試行回数
	
	ArrayList<Double> Cplus=new ArrayList<Double>();
	ArrayList<Double> Closs=new ArrayList<Double>();
	ArrayList<Double> Assell=new ArrayList<Double>();
	ArrayList<Integer> Br=new ArrayList<Integer>();
	

	public ResultList(int atp) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.atp=atp;
	}

	
	
	public ResultList clone(){
		
		try{
			ResultList b=(ResultList)super.clone();
			b.Cplus=new ArrayList<Double>(Cplus);
			b.Closs=new ArrayList<Double>(Closs);
			b.Assell=new ArrayList<Double>(Assell);
			b.Br=new ArrayList<Integer>(Br);
		
		return b;
		
	}catch(CloneNotSupportedException e){
		throw new InternalError(e.toString());

	}
	}
	
}
