
public class LendingFirm {

	int firmid,tau;
	double lfprice,interest;

	int taucounter;

	double installment;//割賦
	double balance;//割賦全額
	double repayprincipal;//貸出元本
	double interestpayment;//t期に返済される金利
	double remainingprincipal;//残りの貸出元本

	public LendingFirm(int firmid, double lfprice, double interest, int tau) {
		// TODO Auto-generated constructor stub
		this.firmid=firmid;
		this.lfprice=lfprice;
		this.interest=interest;
		this.tau=tau;
		this.taucounter=0;

		setInst();
	}

	private void setInst() {
		// TODO 自動生成されたメソッド・スタブ
		balance=(1+interest)*lfprice;
		installment=balance/tau;
		repayprincipal=lfprice;
		remainingprincipal=lfprice;
		interestpayment=installment-(lfprice/tau);
	}

	public void updateList() {
		// TODO Auto-generated method stub
		balance-=installment;
		remainingprincipal-=lfprice/tau;
		taucounter++;

	}

	public int getTaucoungter() {
		// TODO 自動生成されたメソッド・スタブ
		return taucounter;
	}

	public int getTau() {
		// TODO 自動生成されたメソッド・スタブ
		return tau;
	}

	public double getRemainingPrincipal() {
		// TODO 自動生成されたメソッド・スタブ
		return remainingprincipal;
	}




}
