
public class BorrowingBank {

	/*企業の銀行からの借り入れ
	 * id
	 * price
	 * 期間
	 * 金利
	 */

	int bankid;//取引先の銀行id
	double price,interest;//金額,金利
	int tau;//期間
	String state;

	int taucounter;
	double installment;//割賦
	double interestpayment;//割賦のうち金利分の支払い
	double grosspayment;//残りの支払総額
	double remainingprincipal;//残りの支払い元本
	double principal;//一回の支払い元本

	public BorrowingBank(int bankid, double price,double interest,int tau) {
		// TODO Auto-generated constructor stub
		this.bankid=bankid;
		this.price=price;
		this.interest=interest;
		this.tau=tau;
		this.taucounter=0;
		state="Alive";

		setCalcInst();//割賦と支払い元本の計算
	}

	private void setCalcInst() {
		// TODO 自動生成されたメソッド・スタブ
		installment=((1+interest)*price)/tau;
		interestpayment=installment-price/tau;
		principal=price/tau;
		grosspayment=(1+interest)*price;
		remainingprincipal=price;

	}


	public double getPayment() {
		// TODO 自動生成されたメソッド・スタブ
		grosspayment-=installment;
		remainingprincipal-=principal;
		taucounter++;
		return installment;
	}

	public double getInterestPayment() {
		// TODO 自動生成されたメソッド・スタブ
		return interestpayment;
	}

	public double getPrincipal() {//元本
		// TODO 自動生成されたメソッド・スタブ
		return principal;
	}

	public int getTaucounter() {
		// TODO 自動生成されたメソッド・スタブ
		return taucounter;
	}

	public int getTau(){
		return tau;
	}

	public double getRemainingprincipal() {
		// TODO 自動生成されたメソッド・スタブ
		return remainingprincipal;
	}







}
