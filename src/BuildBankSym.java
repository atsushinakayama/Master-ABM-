import java.util.ArrayList;

public class BuildBankSym {

	int banknode;
	double A0;
	
	ArrayList<Institution> Bank=new ArrayList<Institution>();

	public BuildBankSym(int banknode, double a0) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.banknode=banknode;
		this.A0=a0;
	}



	public void isBuild() {
		
		Network BA=new Network(banknode);
		BA.createBA();

		for(int i=0;i<banknode;i++) {
			Institution B=new Institution(i+1,A0);
			Bank.add(B);
		}
		
		for(int i=0;i<banknode;i++) {
			Bank.get(i).Inid=BA.getInid(i);// <arraylist>i番目の銀行の入ってきている銀行のidリスト
			Bank.get(i).Outid=BA.getOutid(i);//出て行ってい金融機関のid
			Bank.get(i).state="Alive";
		}
		
		
		
	}



	public ArrayList<Institution> getFinalBank() {
		// TODO 自動生成されたメソッド・スタブ
		return Bank;
	}


}
