
public class IBLoan {


	int bankid,tau;
	double ibloanmoney,r;
	int taucounter;

	double installment;//割賦
	double balance;//割賦全額
	double interestpayment;
	double repayprincipal;//貸出元本
	double remainingprincipal;//残りの貸出元本


	public IBLoan(int bankid, double ibloanmoney, double r, int tau) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.bankid=bankid;
		this.ibloanmoney=ibloanmoney;
		this.r=r;
		this.tau=tau;
		this.taucounter=0;

		setInst();
	}

	private void setInst() {
		// TODO 自動生成されたメソッド・スタブ
		balance=(1+r)*ibloanmoney;
		installment=balance/tau;
		repayprincipal=ibloanmoney;
		remainingprincipal=ibloanmoney;
		interestpayment=installment-(repayprincipal/tau);
	}

	public double getInstallment() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void updatelist() {
		// TODO 自動生成されたメソッド・スタブ
		balance-=installment;
		remainingprincipal-=ibloanmoney/tau;
		taucounter++;

	}

	public int getTaucounter() {
		// TODO 自動生成されたメソッド・スタブ
		return taucounter;
	}

	public int getTau() {
		// TODO 自動生成されたメソッド・スタブ
		return tau;
	}



	public double getIBLRemainingPrincipal() {
		// TODO 自動生成されたメソッド・スタブ
		return remainingprincipal;
	}

}
