
public class CandidateBank {

	int id;
	double interest;
	double grossl;//(id)銀行からの総借入金
	double price;//(id)銀行からの総借入元本
	String state;//新規先かリレーション先か
	String tradestate;//交渉を行ったか否か

	String Main;//メインバンクかどうか


	public CandidateBank(int bankid, double interest,String state,String tradestate) {
		// TODO Auto-generated constructor stub
		this.id=bankid;
		this.interest=interest;
		this.state=state;
		this.tradestate=tradestate;
		grossl=0;
	}


	public void minusPrincipal(double minusp){
		grossl-=minusp;
	}


	public void addPrincipal(double addp) {
		// TODO Auto-generated method stub
		grossl+=addp;
	}

}
