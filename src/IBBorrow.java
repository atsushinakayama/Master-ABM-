
public class IBBorrow {

	int bankid,tau;
	double ibborrowmoney,r;
	int taucounter;
	String state;

	double installment;
	double balance;
	double interestpayment;
	double grosspayment;
	double principal;
	double remainingprincipal;


	public IBBorrow(int iBborrowid, double iBborrowmoney, double r, int tau) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.bankid=iBborrowid;
		this.ibborrowmoney=iBborrowmoney;
		this.r=r;
		this.tau=tau;
		this.remainingprincipal=iBborrowmoney;
		state="Alive";

		setInst();
	}


	private void setInst() {
		// TODO Auto-generated method stub
		balance=(1+r)*ibborrowmoney;
		installment=((1+r)*ibborrowmoney)/tau;
		principal=ibborrowmoney/tau;
		interestpayment=installment-principal;
		grosspayment=(1+r)*ibborrowmoney;
	}


	public double getInstallment() {
		// TODO Auto-generated method stub
		//remainingprincipal-=ibborrowmoney/tau;
		return installment;
	}


	public double getPrincipal() {
		// TODO Auto-generated method stub
		return principal;
	}


	public int getTaucounter() {
		// TODO Auto-generated method stub
		return taucounter;
	}


	public int getTau() {
		// TODO Auto-generated method stub
		return tau;
	}




	public double getIBBRemainingprincipal() {
		// TODO 自動生成されたメソッド・スタブ
		return remainingprincipal;
	}


	public void updatelist() {
		// TODO 自動生成されたメソッド・スタブ
		balance-=installment;
		remainingprincipal-=principal;
		taucounter++;

	}




}
