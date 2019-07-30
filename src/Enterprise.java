import java.util.ArrayList;
import java.util.Iterator;

public class Enterprise {



	int id;//id
	double K0;//初期自己資本

	//初期自己資本あり
	double fCAR;
	double a0;//初期自己資本金
	double l0;//初期他人資本
	
	int live;


	public Enterprise(double f,int i,double k0) {
		// TODO Auto-generated constructor stub
		this.id=i;
		this.K0=k0;
		this.f=f;
		setBS();
		live=0;
	}

	private void setBS() {
		// TODO 自動生成されたメソッド・スタブ
		this.A=K0;
		this.K=K0;
		lev=K/A;

	}

	//初期自己資本比率ありコンストラクタ
	public Enterprise(int i, double k0, double fCAR) {
		// TODO Auto-generated constructor stub
		this.id=i;
		this.K0=k0;
		this.fCAR=fCAR;

		setBS1(k0,fCAR);
	}




	private void setBS1(double k0, double fCAR) {
		// TODO Auto-generated method stub
		A=k0*fCAR;
		L0=k0-A;
		K=k0;
	}


	//newentryコンストラクタ
	public Enterprise(int newfirmid, double k0, ArrayList<Integer> initbanklist,double lfmin,double lfmax,double val,double vi) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.id=newfirmid;
		this.K0=k0;
		this.Candidatelistid.addAll(initbanklist);
		setBS();
		String state="old";
		String tradestate="no";

		String nstate="No";
		String range="No";

		for(int i=0;i<initbanklist.size();i++) {
			addCandidate(initbanklist.get(i),0,state,tradestate);
		}

		setLevList(lfmin,lfmax,val,nstate,range,vi);
		DecideFirstLev();
		live=0;
	}













	String state;//現存か破綻


	double l;//総借入金
	double lp;//candidatelistの借入元本総和

	int levnownum;//現在のレバレッジナンバー
	double nextlev;//次期選択レバレッジ
	double reallev;//資金供給後のレバレッジ
	double sigmat;
	double counter;//レバレッジ選択確率カウンター
	double nearlev;
	double minlevel=0;//Reallev用nearlevlevel


	double upai;//期待利益
	double pai;//実利益
	double Y;//予想生産高
	double YY;//実生産高
	double rit;//ステップtでの金利の支払い分
	double A;//自己資本
	double K;//総資本
	double KK;//最適資本K*
	double ps;//新規partnerchoice確率
	double L;//希望借入金
	double lev;//レバレッジA
	double L0;//他人資本
	double f;//固定費
	double gr;//成長率

	int initbank;//初期取引銀行
	int mainbanknum;//取引先銀行数（最終)
	
	int rejectordernum;//オーダーを拒否された回数
	int nonbuffernum;//buffer不足に資金需要が余った回数
	

	ArrayList<Leverage> levlist=new ArrayList<Leverage>();//レバレッジ候補リスト
	ArrayList<LevProbably> list=new ArrayList<LevProbably>();//レバレッジ最終候補リスト
	ArrayList<Double> goallevt=new ArrayList<Double>();//タイムステップごと目標レバレッジ
	ArrayList<Double> actlevt=new ArrayList<Double>();//タイムステップごと実際のレバレッジ(生産活動前)


	ArrayList<Double> pailist=new ArrayList<Double>();//実利益リスト
	ArrayList<Double> Ylist=new ArrayList<Double>();//生産高リスト
	ArrayList<Double> Alist=new ArrayList<Double>();//自己資本高リスト

	ArrayList<CandidateBank> Candidatelist=new ArrayList<CandidateBank>();//取引先の候補リスト(id,金利)
	ArrayList<Integer> Candidatelistid=new ArrayList<Integer>();//取引先の候補リスト（idのみ)
	ArrayList<Integer> Tradeable=new ArrayList<Integer>();//取引可能な金融機関のリスト(idのみ)


	ArrayList<OrderList> Order=new ArrayList<OrderList>();//marketに出すオーダー（一つのみ）


	ArrayList<BorrowingBank> BB=new ArrayList<BorrowingBank>();//銀行からの借り入れリスト
	ArrayList<BorrowingBank> newBB=new ArrayList<BorrowingBank>();//t期に銀行から借り入れたリスト
	BorrowingBank brbank;
	ArrayList<Integer> NGlist=new ArrayList<Integer>();//交渉を行った銀行リスト(次cd市場の交渉ができない)

	//時系列格納リスト
	ArrayList<Double> Levlist=new ArrayList<Double>();
	ArrayList<Double> RealLevlist=new ArrayList<Double>();
	ArrayList<Double> Klist=new ArrayList<Double>();
	ArrayList<Integer> Candlist=new ArrayList<Integer>();
	ArrayList<Double> ROAlist=new ArrayList<Double>();
	ArrayList<Double> Choiceratelist=new ArrayList<Double>();
	ArrayList<Integer> RejectOrderNumlist=new ArrayList<Integer>();//オーダー拒否数
	ArrayList<Integer> NonBufferNumlist=new ArrayList<Integer>();	//buuferが足りないから融資を受けれない数(全額受けれなかった場合)
	
	
	public double getK() {
		return K;
	}


	public void DecideFirstLev() {

		levlist.get(0).state="Yes";
	}


	public void calcEValue(double h) {

		levlist.stream().forEach(s->{
			s.evalue-=h*s.evalue;
			s.setStrength();
	});
	}

	public void setAdjacent() {

		for(int i=0;i<levlist.size();i++){
			if(levlist.get(i).state=="Yes"){
				levlist.get(i).adjacent="Yes";
				if(i>0)levlist.get(i-1).adjacent="Yes";
				if(i<levlist.size()-1)levlist.get(i+1).adjacent="Yes";

			}
		}


	}

	public void setAvaRange() {

		/*
		//現在のレバレッジに近い値をstate=Yesに
		double a=100;
		for(Leverage l:levlist) {
			if(Math.abs(a)>Math.abs(l.level-lev)) {
				a=Math.abs(l.level-lev);
				nearlev=l.level;
			}
		}
		levlist.stream().filter(s->s.level==nearlev)
						.forEach(s->{levnownum=s.levnum;
						s.state="Yes";
								});
	*/

		levlist.stream().filter(s->s.state=="Yes")
						.forEach(s->levnownum=s.levnum);

		int i;
		i=levnownum-3;if(i<0)i=0;

		for(int j=i;j<=levnownum+3;j++) {
			if(j>=levlist.size()) {break;}else {
			levlist.get(j).range="Yes";}
			if(levlist.get(j).adjacent=="Yes")levlist.get(j).finalrange="Yes";

		}

		int count=0;
		int maxrangenum=0;
		for(int j=0;i<levlist.size();i++){
			if(levlist.get(i).range=="Yes") maxrangenum=levlist.get(i).levnum;
			if(levlist.get(i).finalrange=="Yes") count++;
		}

		if(count==0){
			nextlev=levlist.get(maxrangenum).levnum;
			levlist.get(maxrangenum).state="Yes";
		}

		}

	public void choiceLev(){



		levlist.stream().filter(s->s.finalrange=="Yes")
		                .forEach(s->sigmat+=Math.exp(s.X));

		levlist.stream().filter(s->s.finalrange=="Yes")
					    .forEach(s->{s.pl=Math.exp(s.X)/sigmat;
					    counter+=s.pl;
					    LevProbably l=new LevProbably(s.levnum,counter,s.level);
					    list.add(l);
					    });

		double r=Math.random();//乱数生成
		double a=0;
		
		//前期に選択したレバレッジを現在のレバレッジとしている
		/*
		for(LevProbably l:list) {
			if(r<=l.getCount()&&a<=r) {
				nextlev=l.getLevel();
				Choiceratelist.add(l.getCount()-a);
				break;
			}else {
				a=l.getCount();
			}

		}*/
		
		//前期CM後に一番近かったレバレッジレベルを前期レバレッジとする
		
		for(int i=0;i<list.size();i++) {
			if(r<=list.get(i).getCount()&&a<=r) {
				nextlev=list.get(i).getLevel();
				if(i==0) {Choiceratelist.add(list.get(i).getCount());}else {
				Choiceratelist.add(list.get(i).getCount()-list.get(i-i).getCount());}
				break;
			}else {
				a=list.get(i).getCount();
			}
		}
		
		

		goallevt.add(nextlev);

		levlist.stream().filter(s->s.level==nextlev)
			 			.forEach(s->s.state="Yes");
		levlist.stream().filter(s->s.level!=nextlev)
						.forEach(s->s.state="No");
		for(Leverage l:levlist) {
			if(l.level-0.5==nextlev||l.level+0.5==nextlev) {l.adjacent="Yes";}else {l.adjacent="No";}
		}


	}

	public void setDemand() {

			L=nextlev*A-K;
			if(L<0.01) L=0;
	}


	public void EvaLeverage(double sm) {

			levlist.stream().filter(s->s.state=="Yes")
			 				.forEach(s->s.evalue=(1-sm)*s.evalue+sm*pai);
			
		
	}
	


	public void addBB(int Bankid,double price,double interest,int tau){
		BorrowingBank bb=new BorrowingBank(Bankid,price,interest,tau);
		BB.add(bb);

	}

	public void addCandidate(int bankid,double interest,String Old,String tradestate){
		CandidateBank cb=new CandidateBank(bankid,interest,Old,tradestate);
		Candidatelist.add(cb);
	}

	public void addOrderList(int bankid,double l,double interest,int tau){
		OrderList order=new OrderList(bankid,l,interest,tau);
		Order.add(order);
	}

	public double getInst(int bankj) {
		// TODO 自動生成されたメソッド・スタブ
		double payment=BB.get(bankj).getPayment();
		return payment;
	}

	public double getInterestpayment(int bankj) {
		// TODO Auto-generated method stub
		double interestpayment=BB.get(bankj).getInterestPayment();
		return interestpayment;
	}

	public double getPrincipal(int bankj) {
		// TODO Auto-generated method stub

		return BB.get(bankj).getPrincipal();
	}

	public int getTaucounter(int bankj) {
		// TODO 自動生成されたメソッド・スタブ
		return BB.get(bankj).getTaucounter();
	}

	public int getTau(int bankj) {
		// TODO 自動生成されたメソッド・スタブ
		return BB.get(bankj).getTau();
	}

	public void addnewBB(int bankid, double price, double interest, int tau) {
		// TODO 自動生成されたメソッド・スタブ
		BorrowingBank bb=new BorrowingBank(bankid,price,interest,tau);
		newBB.add(bb);
	}

	public void Init() {
		// TODO 自動生成されたメソッド・スタブ
		upai=0;
		pai=0;
		rit=0;
		Y=0;
		L=0;
		sigmat=0;
		counter=0;
		lp=0;
		rejectordernum=0;
		nonbuffernum=0;
	}

	public void setKY(double fi,double beta) {
		// TODO 自動生成されたメソッド・スタブ
		K=A+l+L0;
		Y=fi*K;
		lev=K/A;
	}

	public void setKY1(double fi,double beta) {
		K=A+l+L0;
		Y=fi*Math.pow(K, beta);
		lev=K/A;

		//生産活動前の実際のレバレッジを格納
		actlevt.add(lev);
	}

	public void setExpectedProfit(double g) {
		// TODO 自動生成されたメソッド・スタブ
		upai=(1-g)*Y-rit;
	}

	public void setKK(double g, double c, double fi, int x) {
		// TODO 自動生成されたメソッド・スタブ
		KK=(((1-g)/c)-((rit-Alist.get(Alist.size()-1))/2))/(g*fi);
		/*
		BigDecimal bd=new BigDecimal(KK);
		BigDecimal bd1=bd.setScale(1, BigDecimal.ROUND_HALF_UP);
		KK=bd1.doubleValue();
		*/
	}

	public void updatePList() {
		// TODO 自動生成されたメソッド・スタブ
		Ylist.add(Y);
		Alist.add(A);
		pailist.add(pai);
		Levlist.add(lev);
		Klist.add(K);
		if(Klist.size()>1) gr=Klist.get(Klist.size()-1)/Klist.get(Klist.size()-2);
		
		Candlist.add(mainbanknum);
		
		if(Order.size()!=0) Order.clear();

	}

	public void updateCMlist(int x) {
		// TODO 自動生成されたメソッド・スタブ

		//CMリストの消去
		Order.clear();


		//取引を行っている状態ならtradestateをYesに

		if(x>3) Candidatelist.stream().forEach(s->s.tradestate="No");

		for(CandidateBank cb:Candidatelist) {
			for(BorrowingBank bb:newBB) {
				if(cb.id==bb.bankid) cb.tradestate="Yes";
			}
			for(BorrowingBank bbb:BB) {
				if(cb.id==bbb.bankid) cb.tradestate="Yes";
			}
		}

		//candidatelistの中で取引を行っていない銀行の消去
		Iterator<CandidateBank> itcd=Candidatelist.iterator();
		while(itcd.hasNext()) {

			String tradestate=itcd.next().tradestate;
			if(tradestate=="No") {
				itcd.remove();
			}
		 }

		//candidatelistのpriceの更新(credit市場でいちいち足さなくてよい)
		Candidatelist.stream().forEach(s->s.price=0);

		for(CandidateBank cb:Candidatelist) {
			for(BorrowingBank bb:BB) {
				if(cb.id==bb.bankid) {
					cb.price+=bb.price;
				}
			}
			for(BorrowingBank bbb:newBB) {
				if(cb.id==bbb.bankid) {
					cb.price+=bbb.price;
				}
			}
		}

		//mainbank数の計算
		ArrayList<Integer> mainbank=new ArrayList<Integer>();
		BB.stream().filter(s->!mainbank.contains(s.bankid))
		           .forEach(s->mainbank.add(s.bankid));
		newBB.stream().filter(s->!mainbank.contains(s.bankid))
					  .forEach(s->mainbank.add(s.bankid));
		mainbanknum=mainbank.size();
		

		//NGlist初期化
		NGlist.clear();
		
		//RealLeverageの格納
		SetRealLev();

		//オーダー拒否の格納
		RejectOrderNumlist.add(rejectordernum);
		NonBufferNumlist.add(nonbuffernum);
	}
	
	public void SetRealLev() {
		
		reallev=lev;
		RealLevlist.add(reallev);
		//現在のレバレッジに近い値のレベルのstateをyesに
		levlist.stream().forEach(s->s.state="No");
		double min=0,now=0;
		for(Leverage l:levlist) {
			now=Math.abs(reallev-l.level);
			if(min-now>=0) {min=now;minlevel=l.level;}
		}
		levlist.stream().filter(s->s.level==minlevel)
						.forEach(s->s.state="Yes");
		
	}

	public void updateLevlist() {

		levlist.stream().forEach(s->{s.range="No";
									s.finalrange="No";
									s.adjacent="No";
									s.pl=0;
		});
		list.clear();

	}



	public void setLevList(double lfmin, double lfmax, double val,String state,String range,double vi) {
		// TODO 自動生成されたメソッド・スタブ

		int size=(int)((lfmax-lfmin)/val)+1;
		double value=lfmin;
		for(int i=0;i<size;i++) {
			Leverage newlev=new Leverage(value,0,0,state,range,i,vi);
			levlist.add(newlev);
			value+=val;
		}


	}












}
