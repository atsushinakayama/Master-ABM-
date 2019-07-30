import java.util.ArrayList;

public class Build {

	private int banknode;
	private double megaCAR,mediumCAR,smallCAR,a1,a2,E,A0,asyCAR;
	String NW;
	String BankType;

	private double lbmin,lbmax;
	private double valb;
	private double vi;

	private int wdegsum=0;

	ArrayList<Institution> Bank=new ArrayList<Institution>();


	public Build(int banknode,double E,double megaCAR,double mediumCAR,double smallCAR,double a1,
			double a2,String NW,String BankType,double A0,double asyCAR,double lbmin,double lbmax,double valb,
			double vi){
		this.banknode=banknode;
		this.E=E;
		this.megaCAR=megaCAR;
		this.mediumCAR=mediumCAR;
		this.smallCAR=smallCAR;
		this.a1=a1;
		this.a2=a2;
		this.NW=NW;
		this.BankType=BankType;
		this.A0=A0;
		this.asyCAR=asyCAR;
		this.lbmin=lbmin;
		this.lbmax=lbmax;
		this.valb=valb;
		this.vi=vi;
	}


	public void isBuild() {
		// TODO Auto-generated method stub

		//初期ネットワーク構築
		if(NW=="BA"){
		Network BA=new Network(banknode);
		BA.createBA();
		for(int i=0;i<banknode;i++) {
			Institution FI=new Institution(i+1,BA.getIndegree(i),BA.getOutdegree(i));//金融機関の入次数、出次数
			Bank.add(FI);
		}
		for(int i=0;i<banknode;i++){
			Bank.get(i).Inid=BA.getInid(i);// <arraylist>i番目の銀行の入ってきている銀行のidリスト
			Bank.get(i).Outid=BA.getOutid(i);//出て行ってい金融機関のid
		}

		Deciderank();//自己資本比率の決定（総次数の大きさに応じて）
		}

		//完全ネットワーク
		if(NW=="CM"){
		Network CM=new Network(banknode);
		CM.createCM();
		for(int i=0;i<banknode;i++){
			Institution FI=new Institution(i+1,CM.getIndegree(i),CM.getOutdegree(i));//金融機関の入次数、出次数
			Bank.add(FI);
		}
		for(int i=0;i<banknode;i++){
			Bank.get(i).Inid=CM.getInid(i);// <arraylist>i番目の銀行の入ってきている銀行のidリスト
			Bank.get(i).Outid=CM.getOutid(i);//出て行ってい金融機関のid
		}
		}


		BScreate();//バランスシートの作成(e,n,d)

		LevListcreate();//leverageリストの作成



		for(int i=0;i<banknode;i++){
			Bank.get(i).state="Alive";
		}

	}

	public void LevListcreate() {
		String state="No";
		String range="No";
		Bank.stream().forEach(s->s.setLev(lbmin,lbmax,valb,state,range,vi));

	}

	public void addLiqAsset(double la1, double la2) {
		// TODO 自動生成されたメソッド・スタブ


		for(int i=0;i<banknode;i++) {
			Bank.get(i).liqArate=la1+Math.random()*(la2-la1);
			Bank.get(i).liqA=Bank.get(i).liqArate*Bank.get(i).totalassets;
			Bank.get(i).buffer=Bank.get(i).totalassets-Bank.get(i).liqA;
		}
	}




	private void BScreate() {
		// TODO Auto-generated method stub

		int sum=0;//全枝数のカウント
		for(int i=0;i<banknode;i++) sum+=Bank.get(i).deg;

		//e,d,nの決定
		if(NW=="BA"){
		for(int i=0;i<banknode;i++) {
			Bank.get(i).totalassets=E*((double)Bank.get(i).deg/(double)sum);
			Bank.get(i).n=Bank.get(i).totalassets*Bank.get(i).CAR;
			Bank.get(i).d=Bank.get(i).totalassets-Bank.get(i).n;
			Bank.get(i).buffer=Bank.get(i).totalassets;
		}
		}

		if(NW=="CM"){
			for(int i=0;i<banknode;i++){
				Bank.get(i).totalassets=E/banknode;
				Bank.get(i).n=Bank.get(i).totalassets*asyCAR;
				Bank.get(i).forcelev=megaCAR;
				Bank.get(i).d=Bank.get(i).totalassets-Bank.get(i).n;
				Bank.get(i).buffer=Bank.get(i).totalassets;
			}

		}

	}



	private void Deciderank() {
		// TODO Auto-generated method stub

		for(int i=0;i<banknode;i++){
			if(i<banknode*a1) {Bank.get(i).CAR=megaCAR;Bank.get(i).forcelev=megaCAR;}
			if(banknode*a1<=i&&i<=banknode*a2) {Bank.get(i).CAR=mediumCAR;Bank.get(i).forcelev=mediumCAR;}
			if(banknode*a2<i) {Bank.get(i).CAR=smallCAR;Bank.get(i).forcelev=smallCAR;}
			}
		}


	public ArrayList<Institution> getFinalBank() {
		// TODO Auto-generated method stub
		return Bank;
	}






	}







