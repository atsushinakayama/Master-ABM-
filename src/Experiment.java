import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;





public class Experiment {



	ArrayList<Institution> Bank=new ArrayList<Institution>();
	ArrayList<Enterprise> Firm=new ArrayList<Enterprise>();
	ArrayList<BrProcess> Vand=new ArrayList<BrProcess>();


	private int banknode,firmnode;
	private double k0;
	private double r;//市場金利
	private double rr;//金利係数
	private double dr;//預金金利
	private double liqAr;//市場性資産収益率
	private double div;//配当
	private int tau;//返却期限
	private int term;//ターム数
	private int creditterm;//クレジット市場のターム数
	private int startterm;
	private double z;//自己資本比率破綻条件
	private double rz;//自己資本維持率
	private int d;
	private double e;
	private double h;//forgetting process
	private double sm;//strength memory
	private double fii,b;//企業限界生産型
	private double pi;


	//お試し
	//金利
	double gatti=0.01;
	//固定費
	double kt=0.02;


	//金融機関
	double xx,psy,alpha,myu,sita,bscallrate;//riskaversion係数、乗数、riskcoefficient係数、貸出金利係数、乗数
	//企業
	double fi,g,c,lamda,initialbanknumber,beta;//生産能力、変動費率、破産コスト係数、partnerchoice,初期取引銀行候補先,生産能力係数

	ArrayList<Double> Prod=new ArrayList<Double>();//生産量
	ArrayList<Double> ProdGrowthRate=new ArrayList<Double>();//生産量成長率
	ArrayList<Integer> NumberOfFailedFirm=new ArrayList<Integer>();//企業破綻数
	ArrayList<Enterprise> FailedFirm=new ArrayList<Enterprise>();
	ArrayList<Institution> FailedBank=new ArrayList<Institution>();




	int newfirmid;
	int initfirmnode;
	int noobfirm;

	double lfmin,lfmax,val,vi;//企業レバレッジ
	int Candmax;//企業最大取引先

	int brfnum;//総破綻企業


	//OutPutリスト

	int ffnum;//タームごと企業破綻数カウンタ
	ArrayList<Integer> FFnum=new ArrayList<Integer>();//タームごと企業破綻数
	ArrayList<Double> FFrate=new ArrayList<Double>();//タームごと企業破綻割合

	double grossproduction;//生産量カウンタ
	ArrayList<Double> GrossProduction=new ArrayList<Double>();//タームごと総生産

	double grossbadloan;
	ArrayList<Double> GrossBadLoan=new ArrayList<Double>();//タームごとbadloan

	double grossr;
	int rcounter;
	ArrayList<Double> AvgInterest=new ArrayList<Double>();

	double moneystock;
	ArrayList<Double> MoneyStock=new ArrayList<Double>();

	double creditmoney;
	ArrayList<Double> CreditMoney=new ArrayList<Double>();

	double residual;
	ArrayList<Double> Residual=new ArrayList<Double>();

	double grossdemand;
	ArrayList<Double> Grossdemand=new ArrayList<Double>();

	double grosssupply;
	ArrayList<Double> Grosssupply=new ArrayList<Double>();

	int rf=0;//Rejectfinancing(融資拒否数)
	ArrayList<Integer> RejectFinancing=new ArrayList<Integer>();

	double grossfirmlev;
	ArrayList<Double> Avgfirmlev=new ArrayList<Double>();

	double grossbankclientnum;
	ArrayList<Double> Avgbankclientnum=new ArrayList<Double>();



	int brcount=0;


	public Experiment(ArrayList<Institution> Bank, ArrayList<Enterprise> Firm, ArrayList<BrProcess> vand,int banknode,int firmnode,double r, int tau,  int term, double z, double xx,
			double psy, double alpha, double myu, double sita, double fi, double g, double c, double lamda, int initialbanknumber, int d, double e, double k0 ,double dr
			,int startterm,double div,double liqAr,int creditterm,double bscallrate,double rz,double beta,double h,double sm
			,double lfmin,double lfmax,double val,double pi,double vi,int Candmax,double rr) {
		// TODO Auto-generated constructor stub
		this.Bank.addAll(Bank);
		this.Firm.addAll(Firm);
		this.Vand.addAll(vand);
		this.banknode=banknode;this.firmnode=firmnode;
		this.r=r;this.tau=tau;this.term=term;this.z=z;
		this.xx=xx;this.psy=psy;this.alpha=alpha;this.myu=myu;this.sita=sita;
		this.fi=fi;this.g=g;this.c=c;this.lamda=lamda;this.initialbanknumber=initialbanknumber;
		this.d=d;this.e=e;this.k0=k0;
		this.dr=dr;this.startterm=startterm;this.div=div;this.liqAr=liqAr;
		this.creditterm=creditterm;this.bscallrate=bscallrate;this.rz=rz;this.beta=beta;
		this.h=h;this.sm=sm;this.lfmin=lfmin;this.lfmax=lfmax;this.val=val;
		this.pi=pi;this.vi=vi;this.Candmax=Candmax;
		this.rr=rr;

		this.newfirmid=firmnode+1;
		this.initfirmnode=firmnode;
	}



	public void isStart() {
		// TODO Auto-generated method stubdd

		for(int x=0;x<term;x++){
			//System.out.println(x+"ターム目です");
			if(banknode==0) break;
			if(FailedBank.size()>0) {brcount++;break;}
			
			

			InitOPVariables();

			//StartEx(Bank,x,startterm);

		
			InitVariable(Bank,Firm);

			//BankIBLendAble(Bank,x);


			//市場金利の変動

			/* TODO
			 * output項目（各タームごと）
			 * →総生産量(Y(it)),成長率(Y(it)/Y(it-1)),企業破綻数,I/B取引約定額（金利以外)
			 * 最終output
			 * →企業銀行規模分布（累積）
			 *
			 *newBBに分ける意味
			 *返済期間tauをランダムに
			 *buffer<0のときの処理
			 *割賦or一括？
			 *保有市場性資産割合は毎度ランダムでもいいかも（固定でなくてもよい）
			 */

			//返却金利等の計算（この時点で計算しておく）
			Debt(Firm,Bank,x);

			AdjustBS(Firm,Bank,x);

			setForceLeverage(Bank,x);//金融機関目標レバレッジへの調整
			//setBankForceLeverage(Bank,x);//自己組織型

			AdjustBS(Firm,Bank,x);

			//企業最適資本の設定(期待利益の計算＋借入金の設定）
			//FirmOptimalStock(Firm,x);
			//企業今期レバレッジの選択
			setFirmForceLeverage(Firm,x);//自己組織型



			setEquiblium(Bank,Firm,x);

			for(int y=0;y<creditterm;y++) {
			//1取引銀行貸出可能額の設定(流動性規制)
			BankSupplyLiq(Bank,x);

			//銀行貸出金利の設定（全ての企業に対して）
			BankDecideInterest(Bank,Firm,banknode,firmnode,r);


			//企業取引先の決定(partnerchoice) //??partnerchoiceで取引候補先の追加はするのか
			//FirmDecideBank(Firm,Bank,lamda,x);
			FirmDecideBank1(Firm,Bank,x);

			BankDecideFirm(Firm,Bank,x);

			//銀行貸し出しリストの作成、I/B市場での取引（InterBankMarket）
			//InterbankMarket(Firm,Bank,x);

			//企業への貸付(Credit市場)
			CreditMarket(Firm,Bank,x);

			//企業Candidatelistの更新
			//FirmSetCandidate(Firm,x);

			//secondCreditMarket(余剰金の貸し付け)
			SecondCreditMarket(Firm,Bank,x);
			//ThirdCreditMarket(Firm,Bank,x);

			AdjustBS(Firm,Bank,x);


			}

			//銀行credit市場後のbuffer格納リスト
			BankinputBuffer(Bank,x);

			//銀行企業CMリストの更新//企業realLeverageの格納
			UpdateCMList(Bank,Firm,x);

			//企業生産高
			//FirmProductionProfit(Firm,x);
			//企業生産高（限界生産）
			FirmProductionProfit1(Firm,x);

			//企業割賦（分割払い＋金利）支払い
			FirmPayInstallments(Firm,Bank,Vand,x);



			//企業ターム始め利益(生産活動から）の決定
			//FirmRealProfit(Firm,x);
			FirmRealProfit1(Firm,x);



			//企業破綻処理
			BankruptFirm(Firm,x);


			//銀行金利（貸出金)の返却
			BankPayInstallments(Bank,Vand);
			//銀行利益の決定（+ショック）(CreditMarket)
			BankProfit(Bank,Firm,dr,x,div);
			//実利益から選択したレバレッジの評価
			EvaLev(Firm,Bank,x);
			//バランスシート調整
			AdjustBS(Firm,Bank,x);
			//銀行破綻処理+連鎖破綻処理(I/BMarket)
			ChainOfBankruptBank(Bank,x,r);
			//銀行最終利益
			BankRealProfit(Bank,x,r);
			AdjustBS(Firm,Bank,x);
			//破綻行をリストから消去
			BankruptProcessing(Bank,Firm,x);

			//Buffer<0の処理
			BankBufferProcessing(Bank,x);
			AdjustBS(Firm,Bank,x);

			//OPリストに値を代入
			InputOPList(x);
			
			//各エージェントリストの更新,消去
			updateList(Bank,Firm,FailedBank,FailedFirm,x);
			
			//企業の参入
			NewEntry(Firm,Bank,x);
			
			
			
			Firm.stream().forEach(s->s.live++);

		}
	




	}

	public int getBrCount() {
		// TODO 自動生成されたメソッド・スタブ
		return brcount;
	}







	private void setEquiblium(ArrayList<Institution> bank, ArrayList<Enterprise> firm, int x) {
		// TODO Auto-generated method stub

		/*
		for(int i=0;i<banknode;i++){
			grosssupply+=bank.get(i).buffer;
		}
		for(int i=0;i<firmnode;i++){
			grossdemand+=firm.get(i).L;
		}
		*/
		bank.stream().forEach(s->grosssupply+=s.buffer);
		firm.stream().forEach(s->grossdemand+=s.L);

		Grosssupply.add(grosssupply);
		Grossdemand.add(grossdemand);

	}



	private void BankIBLendAble(ArrayList<Institution> bank, int x) {
		// TODO 自動生成されたメソッド・スタブ

		for(int i=0;i<banknode;i++) {
			bank.get(i).setIBable(bscallrate);
		}

	}


	private void StartEx(ArrayList<Institution> bank, int x, int startterm) {
		// TODO 自動生成されたメソッド・スタブ

		if(x<startterm) {
			for(int i=0;i<banknode;i++) {
				bank.get(i).n-=bank.get(i).pai;
				bank.get(i).buffer-=bank.get(i).pai;
			}
		}



		}



/*
	private void TauCounter(ArrayList<Institution> bank, ArrayList<Enterprise> firm) {
		// TODO 自動生成されたメソッド・スタブ

		for(int i=0;i<firmnode;i++){
			for(int j=0;j<firm.get(i).BB.size();j++){
				firm.get(i).BB.get(j).taucounter++;
			}
		}

		for(int i=0;i<banknode;i++){
			for(int j=0;j<bank.get(i).LFlist.size();j++){
				bank.get(i).LFlist.get(j).taucounter++;
			}
			for(int j=0;j<bank.get(i).BBlist.size();j++){
				bank.get(i).BBlist.get(j).taucounter++;
			}
			for(int j=0;j<bank.get(i).LBlist.size();j++){
				bank.get(i).LBlist.get(j).taucounter++;
			}
		}

	}*/


	private void updateList(ArrayList<Institution> bank, ArrayList<Enterprise> firm,ArrayList<Institution> failedbank, ArrayList<Enterprise> failedfirm, int x) {
		// TODO 自動生成されたメソッド・スタブ


		//パラメタリストの更新
		for(int i=0;i<firmnode;i++){
			if(x==0){
				firm.get(i).Alist.add(firm.get(i).A);
			}else{
			firm.get(i).updatePList();
			}
		}



		//firmレバレッジリストの更新
		firm.stream().forEach(s->s.updateLevlist());

		//bankレバレッジリストの更新
		bank.stream().forEach(s->s.updateLevlist());



		for(int i=0;i<banknode;i++) {
			if(x!=0) {bank.get(i).updateList();}else {bank.get(i).CARlist.add(bank.get(i).CAR);}
		}


		//borrowingの相手の企業の状態の更新（破綻して入ればVandへ返済するため)
		for(int i=0;i<firmnode;i++){
			for(int j=0;j<firm.get(i).BB.size();j++) {
				for(int k=0;k<failedbank.size();k++) {
					if(firm.get(i).BB.get(j).bankid==failedbank.get(k).id) {
						firm.get(i).BB.get(j).state="Dead";
					}
				}
			}
			for(int j=0;j<firm.get(i).newBB.size();j++) {
				for(int k=0;k<failedbank.size();k++) {
					if(firm.get(i).newBB.get(j).bankid==failedbank.get(k).id) {
						firm.get(i).newBB.get(j).state="Dead";
					}
				}
			}
		}

		for(int i=0;i<banknode;i++) {
			for(int j=0;j<bank.get(i).BBlist.size();j++) {
				for(int k=0;k<failedbank.size();k++) {
					if(bank.get(i).BBlist.get(j).bankid==failedbank.get(k).id) {
						bank.get(i).BBlist.get(j).state="Dead";
					}
				}
			}
			for(int j=0;j<bank.get(i).newBBlist.size();j++) {
				for(int k=0;k<failedbank.size();k++) {
					if(bank.get(i).newBBlist.get(j).bankid==failedbank.get(k).id) {
						bank.get(i).newBBlist.get(j).state="Dead";
					}
}
			}
		}

	}


	private void InitVariable(ArrayList<Institution> bank, ArrayList<Enterprise> firm) {
		// TODO 自動生成されたメソッド・スタブ

		for(int i=0;i<banknode;i++){
			bank.get(i).Init();

		}

		for(int j=0;j<firmnode;j++){
			firm.get(j).Init();
		}

	}



	private void Debt(ArrayList<Enterprise> firm, ArrayList<Institution> bank, int x) {
		// TODO 自動生成されたメソッド・スタブ


		//newリストを通常リストに加える
		for(int i=0;i<firmnode;i++){
			for(int j=0;j<firm.get(i).newBB.size();j++){
				firm.get(i).addBB(firm.get(i).newBB.get(j).bankid,firm.get(i).newBB.get(j).price,firm.get(i).newBB.get(j).interest,tau);
			}
			firm.get(i).newBB.clear();
			}


		for(int i=0;i<banknode;i++){
			for(int j=0;j<bank.get(i).newBBlist.size();j++){
				bank.get(i).addBBList(bank.get(i).newBBlist.get(j).bankid,bank.get(i).newBBlist.get(j).ibborrowmoney,bank.get(i).newBBlist.get(j).r,tau);
		}
			bank.get(i).newBBlist.clear();
		}

		for(int i=0;i<banknode;i++) {
			for(int j=0;j<bank.get(i).newLBlist.size();j++) {
				bank.get(i).addLBList(bank.get(i).newLBlist.get(j).bankid, bank.get(i).newLBlist.get(j).ibloanmoney, bank.get(i).newLBlist.get(j).r, tau);
			}
			bank.get(i).newLBlist.clear();
		}

		for(int i=0;i<banknode;i++){
			for(int j=0;j<bank.get(i).newLFlist.size();j++){
				bank.get(i).addLF(bank.get(i).newLFlist.get(j).firmid,bank.get(i).newLFlist.get(j).lfprice,bank.get(i).newLFlist.get(j).interest,tau);
		}
			bank.get(i).newLFlist.clear();
		}




		for(int i=0;i<firmnode;i++){
			for(int j=0;j<firm.get(i).BB.size();j++){//
				for(int k=0;k<banknode;k++){

					if(firm.get(i).BB.get(j).bankid==bank.get(k).id){

						firm.get(i).rit+=firm.get(i).getInterestpayment(j);//ステップtにおける金利の支払い
					}
				}
			}
		}


	}



	private void setForceLeverage(ArrayList<Institution> bank, int x) {
		// TODO 自動生成されたメソッド・スタブ

		/*
		 * 目標レバレッジを初期CARと想定
		 * 目標レバレッジになるまで他人資本dを追加
		 * 総投資側は初期LiqAsset比率になるまでliqAに追加し、残りをbufferに
		 *
		 * 他人資本についてはインターバンク市場での借り入れも想定できる
		 *
		 * 自己資本比率の規定値を維持できていなければ，dおよびbufferを減らす．
		 *
		 */

	    //目標レバレッジを初期自己資本比率に設定
		bank.stream().filter(s->x==0)
					 .forEach(s->s.setInitForceLev());


		bank.stream().forEach(s->{
			s.setForceLev();
			s.setRemainCAR();
		});



	}

	private void setBankForceLeverage(ArrayList<Institution> bank,int x) {

		if(x==0) {bank.stream().forEach(s->s.FirstDecideLev());}else {

		}
			bank.stream().forEach(s->{

			s.calcEValue(h);//評価値，強度の計算
			s.setAdjacent();
			s.setAvaRange();
			s.choiceLev();
			s.setBS();//バランスシート調整
			});//
	}



	private void FirmOptimalStock(ArrayList<Enterprise> firm,int x) {
		// TODO Auto-generated method stub




		//期待利益の計算(一応）
		for(int i=0;i<firmnode;i++){
			firm.get(i).setKY(fi,beta);
			firm.get(i).setExpectedProfit(g);
		}

		//最適資本K*の計算　四捨五入
		for(int i=0;i<firmnode;i++){
			firm.get(i).setKK(g,c,fi,x);
		}

		//借入金の決定（K*とK(it)）
		for(int i=0;i<firmnode;i++){
			if(firm.get(i).KK-firm.get(i).K>1){
				firm.get(i).L=firm.get(i).KK-firm.get(i).K;
			}else{
				firm.get(i).L=0;//最適資本が現在の資本より小さければ借入を行わない(L=0)に設定，後の条件分岐で
			}
		}



	}

	private void setFirmForceLeverage(ArrayList<Enterprise> firm,int x) {

		if(x==0) {firm.stream().forEach(s->{s.DecideFirstLev();});}else {



			//評価値,強度の計算
			firm.stream().forEach(s->{
				s.calcEValue(h);
			});

			firm.stream().forEach(s->s.setAdjacent());//選択可能レバレッジ範囲

			firm.stream().forEach(s->s.setAvaRange());//近隣のレバレッジの選択

			//その中からレバレッジを選択する
			firm.stream().forEach(s->s.choiceLev());

			//借入金の計算
			firm.stream().forEach(s->s.setDemand());


		}



	}


	private void BankSupplyLiq(ArrayList<Institution> bank, int x) {
		// TODO 自動生成されたメソッド・スタブ

		//銀行貸出可能額S(it)の設定
		for(Institution b:bank) {
			b.S=b.buffer;
			if(b.S<0.01) b.S=0;
			b.Slist.add(b.S);
		}

	}



	private void BankDecideInterest(ArrayList<Institution> bank,ArrayList<Enterprise> firm,int banknode,int firmnode,double r) {
		// TODO Auto-generated method stub

		//金利は少数第四位まで四捨五入

		for(int i=0;i<banknode;i++){
			for(int j=0;j<firmnode;j++){
				//bank.get(i).addInterest(firm.get(j).id,r+myu*Math.pow((firm.get(j).lev)/bank.get(i).S,sita));
				bank.get(i).addInterest(firm.get(j).id,r+firm.get(j).lev*rr);
				//bank.get(i).addInterest(firm.get(j).id,r+myu*(firm.get(j).K/firm.get(j).A)/bank.get(i).S);
				//bank.get(i).addInterest(firm.get(j).id,r+myu*(firm.get(j).K/firm.get(j).A));
				//bank.get(i).addInterest(firm.get(j).id,gatti*bank.get(i).CAR+gatti*Math.pow(firm.get(j).lev,gatti));
				//bank.get(i).addInterest(firm.get(j).id,r+0.001*firm.get(j).lev);
			}
		}


	}


	private void FirmDecideBank(ArrayList<Enterprise> firm, ArrayList<Institution> bank, double lamda,int x) {
		// TODO Auto-generated method stub

		//接続先の金利
		for(int i=0;i<firmnode;i++){
			for(int j=0;j<firm.get(i).Candidatelist.size();j++){//firmの銀行接続先
				for(int k=0;k<banknode;k++){
					if(firm.get(i).Candidatelist.get(j).id==bank.get(k).id){

						for(int l=0;l<bank.get(k).rf.size();l++){//bankのrf(貸出金利)list
							if(firm.get(i).id==bank.get(k).rf.get(l).id){
								firm.get(i).Candidatelist.get(j).interest=bank.get(k).rf.get(l).r;
							}
						}
					}
				}
			}
		}

		//金利の低い銀行順に並び変え
		for(int i=0;i<firmnode;i++) Collections.sort(firm.get(i).Candidatelist,new FirmCandidatecomparator());
		//オーダーの作成
		for(int i=0;i<firmnode;i++) {
			for(int j=0;j<firm.get(i).Candidatelist.size();j++) {
			if(firm.get(i).L!=0){
				if(firm.get(i).Candidatelist.get(j).tradestate=="No") {
					firm.get(i).addOrderList(firm.get(i).Candidatelist.get(j).id,firm.get(i).L, firm.get(i).Candidatelist.get(j).interest,tau);
					}
				}
			}
		}
		/*
		//partnerchoice
		String New="New";
		String tradestate="No";

		for(int i=0;i<firmnode;i++){
			if(firm.get(i).L!=0){
				if(firm.get(i).Order.size()!=0) {
				SecureRandom rnd=new SecureRandom();
			int newbank=rnd.nextInt(banknode);//銀行idをランダムで取得

			if(!firm.get(i).Candidatelistid.contains(bank.get(newbank).id)){//Candidateリストに含まれていない銀行であれば
				for(int j=0;j<bank.get(newbank).rf.size();j++){
					if(firm.get(i).id==bank.get(newbank).rf.get(j).id){
						//企業のオーダーリストの金利がランダム銀行の金利より高い場合
						if(firm.get(i).Order.get(0).interest>bank.get(newbank).rf.get(j).r){
							//確率pでオーダーの変更
							double rold=firm.get(i).Order.get(0).interest;
							double rnew=bank.get(newbank).rf.get(j).r;
							double p=1-Math.exp((lamda*(rnew-rold))/rnew);
							double pro=Math.random();
							if(p>pro){
								firm.get(i).Order.remove(0);//オーダーの消去
								firm.get(i).addOrderList(bank.get(newbank).id,firm.get(i).L,rnew,tau);//新しいオーダー
								//Candidatelistの更新（追加）
								firm.get(i).addCandidate(bank.get(newbank).id,rnew,New,tradestate);
								firm.get(i).Candidatelistid.add(bank.get(newbank).id);
								//System.out.println(bank.get(newbank).id);
							}
						}
					}
				}
			}
		}
		}
		}*/



	}

	private void FirmDecideBank1(ArrayList<Enterprise> firm, ArrayList<Institution> bank,int x) {

		//接続先の金利
				for(int i=0;i<firmnode;i++){
					for(int j=0;j<firm.get(i).Candidatelist.size();j++){//firmの銀行接続先
						for(int k=0;k<banknode;k++){
							if(firm.get(i).Candidatelist.get(j).id==bank.get(k).id){

								for(int l=0;l<bank.get(k).rf.size();l++){//bankのrf(貸出金利)list
									if(firm.get(i).id==bank.get(k).rf.get(l).id){
										firm.get(i).Candidatelist.get(j).interest=bank.get(k).rf.get(l).r;
									}
								}
							}
						}
					}
				}

		//企業借入元本の作成
				for(Enterprise f:firm) {
					for(CandidateBank cb:f.Candidatelist) 	f.lp+=cb.price;
				}

		//オーダーの作成(接続ネットワーク先に分割オーダーの作成)
		//借入金(返済前の借入金：price)に応じて要求金の作成
		//企業候補先がない場合はランダムにオーダーを作成
				for(Enterprise f:firm) {
					if(f.L>0.001) {
						if(f.Candidatelist.size()!=0) {
						for(CandidateBank cb:f.Candidatelist) {
							//f.addOrderList(cb.id, (cb.grossl/f.l)*f.L, cb.interest, tau);
							if(f.lp!=0) {f.addOrderList(cb.id, (cb.price/f.lp)*f.L, cb.interest, tau);}else {
							f.addOrderList(cb.id, f.L, cb.interest, x);}
							f.NGlist.add(cb.id);//交渉を行った銀行idリスト
						}
					}else {
						int newbankid=(int) (banknode*Math.random())+1;
						double newr=0;
						for(Institution b:bank) {
							for(LInterest r:b.rf) {
								if(f.id==r.id) newr=r.r;
							}
						}
						f.addCandidate(newbankid, newr, "Old", "No");
						f.addOrderList(newbankid, f.L, newr, x);
						f.NGlist.add(newbankid);
					}
				}
				}

	}

	private void BankDecideFirm(ArrayList<Enterprise> firm,ArrayList<Institution> bank,int x){

		/*TODO
		 * Candidatelist 取引を行ったかいないかを更新すればok
		 *
		 */
		//銀行demandlistの格納
		for(Institution b:bank){
			if(b.S>1){
				for(Enterprise f:firm){
					for(OrderList ol:f.Order){
						if(b.id==ol.bankid){
							b.addDOL(f.id,ol.interest,ol.ordermoney,1-0.001*f.lev,tau);
							//b.addDOL(f.id,ol.interest,f.L,1-xx*Math.pow((f.lev/b.buffer),psy),tau);
						}
					}
				}
			}
		}

		//銀行demandlistからどの企業に貸し出すかを決定
		for(Institution b:bank){
			Iterator<DemandOrderList> itDOL=b.DOL.iterator();
			while(itDOL.hasNext()){
				double pro=Math.random();
				DemandOrderList dol=itDOL.next();
				if(dol.ps<pro){
					  itDOL.remove();
					  //企業オーダー拒否カウンタ
					  firm.stream().filter(s->dol.firmid==s.id)
					  			   .forEach(s->s.rejectordernum++);
					  rf++;
				}else{
					if(b.S>0.001){
						if(dol.l-b.S>0.001){
							dol.l=b.S;
							b.lft+=b.S;
							b.S=0;
							b.buffer=0;//便宜上ここで0としておく

						}else{
							b.S-=dol.l;
							b.buffer-=dol.l;
							b.lft+=dol.l;

						}
					}else{
						itDOL.remove();
					}
				}
			}
		}
		bank.stream().forEach(s->s.lftlist.add(s.lft));



		/*
				for(int i=0;i<banknode;i++){
					for (int j=0;j<bank.get(i).DOL.size();j++){

						double pro=Math.random();
						if(bank.get(i).DOL.get(j).ps<pro){ //貸出確率が閾値（ある確率）より小さいならdemandlistから消去
							//System.out.println(bank.get(i).id+" "+bank.get(i).DOL.get(j).ps+ " "+pro);
							bank.get(i).DOL.remove(j);
							j--;
							}else{
							if(bank.get(i).S>1){

								if(bank.get(i).DOL.get(j).l-bank.get(i).S>1) {

									bank.get(i).DOL.get(j).l=bank.get(i).S;
									bank.get(i).S=0;
									for(int k=j+1;k<bank.get(i).DOL.size();k++) {
										bank.get(i).DOL.remove(k);
										k--;
									}
									break;
								}else {
									bank.get(i).S-=bank.get(i).DOL.get(j).l;
								}
							}else{
								bank.get(i).DOL.remove(j);
								j--;
							}
							}
							}
					}*/


	}


	private void InterbankMarket(ArrayList<Enterprise> firm, ArrayList<Institution> bank,int x) {
		// TODO Auto-generated method stub
		/*
		//銀行demandorderlistの格納(id,interest,希望資金,貸出確率,期限)
		for(int i=0;i<banknode;i++){
			if(bank.get(i).S>1) {
			for(int j=0;j<firmnode;j++){
				if(firm.get(j).Order.size()!=0){
				if(bank.get(i).id==firm.get(j).Order.get(0).bankid){
					//bank.get(i).addDOL(firm.get(j).id,firm.get(j).Order.get(0).interest,firm.get(j).L,1-xx*Math.pow((firm.get(j).l/bank.get(i).S),psy),tau);
					bank.get(i).addDOL(firm.get(j).id,firm.get(j).Order.get(0).interest,firm.get(j).L,1-0.001*firm.get(j).lev,tau);
					//System.out.println(bank.get(i).DOL.get(0).ps);
					//交渉したかの更新（企業candidatelist)
					for(int k=0;k<firm.get(j).Candidatelist.size();k++) {
						if(firm.get(j).Candidatelist.get(k).id==bank.get(i).id) {
						firm.get(j).Candidatelist.get(k).tradestate="Yes";

						}
						}


				}
			}
		}
		}
		}*/
		//貸出リストの合計額が供給可能額より大きい場合,IB借入リスト（IBlist)の作成
		for(int i=0;i<banknode;i++){
			double lendsum=0;
			for(int j=0;j<bank.get(i).DOL.size();j++){
				lendsum+=bank.get(i).DOL.get(j).l;
			}
			if(lendsum-bank.get(i).buffer>1){
				//InterBank市場貸出リスト作成
				bank.get(i).addIBList(lendsum-bank.get(i).buffer,r,tau);
				//余剰金（IB市場での貸し出し)の計算
				bank.get(i).lft=bank.get(i).buffer;
				bank.get(i).buffer=0;//便宜上ここでBufferを0としておくことBufferplusリストに入らないようにしておく
			}else{
				bank.get(i).buffer=bank.get(i).buffer-lendsum;
				bank.get(i).lft=lendsum;
			}


		}
		/*
		if(lendsum>bank.get(i).buffer&&bank.get(i).S>lendsum){//Sのなかに要求金額が収まるのであれば
			bank.get(i).addIBlist(lendsum-bank.get(i).buffer,r,tau);
			bank.get(i).lft=bank.get(i).buffer;//この時点で足しておく
		}
		id(lendsum>bank.get(i).buffer&&bank.get(i).S<lendsum){//Sのなかに要求金額が収まらない場合
			double l=bank.get(i).S;double count=0;
			while(l==0){
				j=0;
				count+=bank.get(i).DOL.get(j).l;
				if(l<count){

				}

			}
		}*/
		//インターバンク市場
		//buffer>0,IBListがあるarraylistの作成

		ArrayList<Institution> Bufferplus=new ArrayList<Institution>();//bufferが余っている銀行（貸し手）
		ArrayList<Institution> IBdemand=new ArrayList<Institution>();//IB市場での資金需要がある銀行（借り手）

		for(int i=0;i<banknode;i++){
			if(bank.get(i).buffer>1) Bufferplus.add(bank.get(i));
			if(!bank.get(i).IBlist.isEmpty()) IBdemand.add(bank.get(i));
		}
		/*
		 * 現時点ではIBdemandをCARが高い順にソートしてその順に貸し手は貸出を決定（自己資本が低い銀行はIBではなかなか借りれない)
		 * 変更可能点➾貸出可能性（Credit市場と一緒)を考慮し，リストを作成．閾値より高ければ貸出を行う（順番は昇順、足りなくなる場合もあり）
		 */

		//銀行は全ての他の銀行のレバレッジを保有している（便宜上）
		/*
		for(int i=0;i<banknode;i++){
			for(int j=0;j<banknode;j++){
				bank.get(i).
		}
		}
		*/

		//IB市場での貸し出し制限（自己資本のbscallrateまで)
		for(int i=0;i<Bufferplus.size();i++) {
			Bufferplus.get(i).setIBable(bscallrate);
			if(Bufferplus.get(i).ibable<Bufferplus.get(i).buffer) {
				Bufferplus.get(i).ibable=Bufferplus.get(i).ibable;
			}else {
				Bufferplus.get(i).ibable=Bufferplus.get(i).buffer;
			}
		}



		for(int i=0;i<IBdemand.size();i++){
			//Collections.shuffle(IBdemand.get(i).Inid);//Inidのシャッフル（ランダム）
			//for(int j=0;j<IBdemand.get(i).Inid.size();j++){
				//接続先からランダムに選択し，buffer>0であれば申請を行う。
				Collections.shuffle(Bufferplus);//bufferplusリストのシャッフル
				Collections.shuffle(IBdemand);
					for(int k=0;k<Bufferplus.size();k++){
						if(IBdemand.get(i).IBlist.size()!=0&&Bufferplus.get(k).ibable>1){
						if(IBdemand.get(i).Inid.contains(Bufferplus.get(k).id)){
							/*
							 * Bufferplusはdemand側の貸し出し確率を計算、閾値より高ければ貸し出しを行う
							 * 現時点では貸し出し確率は考慮しない
							 * 政策（自己資本比率規制）を考えるときにここを書く
							 */
							if(IBdemand.get(i).IBlist.get(0).ibdemand<Bufferplus.get(k).ibable){
								//一回取引で完了する場合
							IBdemand.get(i).addnewBBList(Bufferplus.get(k).id,IBdemand.get(i).IBlist.get(0).ibdemand,r,tau);
							Bufferplus.get(k).addnewLBList(IBdemand.get(i).id,IBdemand.get(i).IBlist.get(0).ibdemand,r,tau);

							moneystock+=IBdemand.get(i).IBlist.get(0).ibdemand;
							//IBdemand.get(i).lft+=IBdemand.get(i).IBlist.get(0).ibdemand;
							//Bufferplus.get(k).lb+=IBdemand.get(i).IBlist.get(0).ibdemand;
							Bufferplus.get(k).ibable-=IBdemand.get(i).IBlist.get(0).ibdemand;
							Bufferplus.get(k).buffer-=IBdemand.get(i).IBlist.get(0).ibdemand;
							IBdemand.get(i).lft+=IBdemand.get(i).IBlist.get(0).ibdemand;
							//System.out.println(Bufferplus.get(k).id+"  "+Bufferplus.get(k).buffer);
							IBdemand.get(i).IBlist.remove(0);//IBリストの消去

						}else{

							IBdemand.get(i).addnewBBList(Bufferplus.get(k).id,Bufferplus.get(k).ibable,r,tau);
							Bufferplus.get(k).addnewLBList(IBdemand.get(i).id,Bufferplus.get(k).ibable,r,tau);
							moneystock+=Bufferplus.get(k).ibable;
							//IBdemand.get(k).bb+=Bufferplus.get(k).buffer;
							IBdemand.get(i).lft+=Bufferplus.get(k).ibable;
							IBdemand.get(i).IBlist.get(0).ibdemand-=Bufferplus.get(k).ibable;
							//Bufferplus.get(k).lb+=Bufferplus.get(k).buffer;
							Bufferplus.get(k).ibable=0;
							Bufferplus.get(k).buffer-=Bufferplus.get(k).ibable;


						}
			}
		}
		}
		}


		for(int i=0;i<banknode;i++) {
			bank.get(i).lftlist.add(bank.get(i).lft);
		}
	}


	private void CreditMarket(ArrayList<Enterprise> firm, ArrayList<Institution> bank, int x) {
		// TODO Auto-generated method stub

		//銀行は企業からの要望リストDOLに貸付

		//firstCreditMarket(Relation)
		/*
		for(int i=0;i<banknode;i++){
			Collections.shuffle(bank.get(i).DOL);
			//for(DemandOrderList j:bank.get(i).DOL)
			for(int j=0;j<bank.get(i).DOL.size();j++){
				for(int k=0;k<firmnode;k++){
					if(bank.get(i).lft!=0){
					if(bank.get(i).DOL.get(j).firmid==firm.get(k).id){
						if(bank.get(i).lft-firm.get(k).L>1){
							bank.get(i).addnewLFlist(firm.get(k).id,firm.get(k).L,bank.get(i).DOL.get(j).interest,tau);
							bank.get(i).lft-=firm.get(k).L;
							firm.get(k).addnewBB(bank.get(i).id,firm.get(k).L,bank.get(i).DOL.get(j).interest,tau);

							for(CandidateBank cb:firm.get(k).Candidatelist){
								if(cb.id==bank.get(i).id){
									cb.addPrincipal(firm.get(i).L);
								}
							}

							//企業Candidatelistのチェック(後のことを考えてこの書き方）
							for(int l=0;l<firm.get(k).Candidatelist.size();l++) {
								if(bank.get(i).id==firm.get(k).Candidatelist.get(l).id) {
									if(firm.get(k).Candidatelist.get(l).state=="New") {
										firm.get(k).Candidatelist.get(l).state="Old";
									}
								}
								if(firm.get(k).Candidatelist.get(l).tradestate=="No") {
									firm.get(k).Candidatelist.get(l).tradestate="Yes";
								}
							}

							creditmoney+=firm.get(k).L;
							firm.get(k).L=0;
							grossr+=bank.get(i).DOL.get(j).interest;
							rcounter++;
							//firm.get(k).Order.remove(0);//firm orderの消去
					}else{
						bank.get(i).addnewLFlist(firm.get(k).id,bank.get(i).lft,bank.get(i).DOL.get(j).interest,tau);
						firm.get(k).addnewBB(bank.get(i).id,bank.get(i).lft,bank.get(i).DOL.get(j).interest,tau);

						for(CandidateBank cb:firm.get(k).Candidatelist){
							if(cb.id==bank.get(i).id){
								cb.addPrincipal(bank.get(i).lft);
							}
						}

						//企業candidatelistの更新
						for(int l=0;l<firm.get(k).Candidatelist.size();l++) {
							if(bank.get(i).id==firm.get(k).Candidatelist.get(l).id) {
								if(firm.get(k).Candidatelist.get(l).state=="New") {
									firm.get(k).Candidatelist.get(l).state="Old";
									//System.out.println("新しい取引先だよ");
								}
								if(firm.get(k).Candidatelist.get(l).tradestate=="No") {
									firm.get(k).Candidatelist.get(l).tradestate="Yes";
								}
							}
						}
						creditmoney+=bank.get(i).lft;
						firm.get(k).L-=bank.get(i).lft;
						grossr+=bank.get(i).DOL.get(j).interest;
						rcounter++;
						bank.get(i).lft=0;
						//firm.get(k).Order.remove(0);//企業は所望の金額を借り入れることができない場合でも、リストを消去する
					}
					}
				}

			}
		}
		}*/

		//各銀行のlftは0になるはずである．

		for(Institution b:bank) {
			Collections.shuffle(b.DOL);
			for(DemandOrderList dol:b.DOL) {
				for(Enterprise f:firm) {
					if(dol.firmid==f.id) {
						for(OrderList ol:f.Order) {
							if(ol.bankid==b.id) {
								if(b.lft>0.01) {
								if(b.lft-ol.ordermoney>0.01) {

									b.addnewLFlist(f.id, ol.ordermoney, dol.interest, tau);
									f.addnewBB(b.id, ol.ordermoney, dol.interest,tau);
									b.lft-=ol.ordermoney;


									//企業Candidatelistの更新
									for(CandidateBank cb:f.Candidatelist) {
										if(b.id==cb.id) {
										cb.tradestate="Yes";
										cb.addPrincipal(ol.ordermoney);
									}
								}
									creditmoney+=ol.ordermoney;
									f.L-=ol.ordermoney;
									grossr+=dol.interest;
									rcounter++;
								}else {

									b.addnewLFlist(f.id, b.lft, dol.interest, tau);
									f.addnewBB(b.id,b.lft,dol.interest,tau);

									for(CandidateBank cb:f.Candidatelist) {
										if(b.id==cb.id) {
											cb.tradestate="Yes";
											cb.addPrincipal(b.lft);
										}
									}

									creditmoney+=b.lft;
									f.L-=b.lft;
									grossr+=dol.interest;
									rcounter++;
									b.lft=0;

								}
							}
						}
					}
				}
			}

		}
		}
		//企業buffer<0より融資を受けれなかった場合
		firm.stream().filter(s->s.L>0)
					.forEach(s->s.nonbuffernum++);




	}

	private void FirmSetCandidate(ArrayList<Enterprise> firm, int x) {
		// TODO 自動生成されたメソッド・スタブ

		for(int i=0;i<firmnode;i++) {
			for(int j=0;j<firm.get(i).Candidatelist.size();j++) {
				if(firm.get(i).Candidatelist.get(j).state=="New") {
					firm.get(i).Candidatelist.remove(j);
					firm.get(i).Candidatelistid.remove(j);
					j--;
				}
			}
		}

	}




	private void SecondCreditMarket(ArrayList<Enterprise> firm, ArrayList<Institution> bank, int x) {
		/*TODO
		 * まず企業側がdemandの残りを他銀行にオーダー
		 * 銀行側が利益獲得のためランダムで企業に接触
		 * buferがのこっているかつ供給可能流動性が残っている場合にsecondcredit市場へ参入
		 * 供給可能流動性がなくなるまでor企業の資金需要がなくなるまで貸出を行う
		 * 企業BBリストstateに新たな状態を追加したい
		 *
		 * regionに分けたいが、、、
		 */


		ArrayList<Institution> pbank=new ArrayList<Institution>();
		ArrayList<Enterprise> pfirm=new ArrayList<Enterprise>();

		bank.stream().filter(s->s.buffer>0.0001)
					 .forEach(s->pbank.add(s));
		firm.stream().filter(s->s.L>0.00001)
					 .forEach(s->pfirm.add(s));


		//企業-銀行ランダムマッチング
		int iterator=0;

		if(pfirm.size()>0&&pbank.size()>0){
			//どちらかのリストサイズが0になるかorイテレータ分
			//System.out.println("SecondCM:"+" firm;"+pfirm.size()+" pbank:"+pbank.size());
			do {

			Collections.shuffle(pfirm);Collections.shuffle(pbank);
			//0番目同士を取引
			//貸し出すかの判定
			if(!pfirm.get(0).NGlist.contains(pbank.get(0).id)) {
			if(1-0.001*pfirm.get(0).lev>Math.random()){
				pfirm.get(0).NGlist.add(pbank.get(0).id);
				//System.out.println(pbank.get(0).id+"銀行");
				if(pfirm.get(0).L<pbank.get(0).buffer){
					//bufferのほうが大きい場合
					pbank.get(0).addnewLFlist(pfirm.get(0).id,pfirm.get(0).L,rr*pfirm.get(0).lev+r,tau);
					pfirm.get(0).addnewBB(pbank.get(0).id, pfirm.get(0).L, rr*pfirm.get(0).lev+r,tau);
					pfirm.get(0).addCandidate(pbank.get(0).id,0.001*pfirm.get(0).lev+r,"Old","Yes");

					for(CandidateBank cb:pfirm.get(0).Candidatelist){
						if(cb.id==pbank.get(0).id){
							cb.addPrincipal(pfirm.get(0).L);


							if(cb.tradestate=="No") cb.tradestate="Yes";
						}


					}


					pbank.get(0).buffer-=pfirm.get(0).L;
					creditmoney+=pfirm.get(0).L;
					pfirm.get(0).L=0;
					grossr+=pfirm.get(0).lev*0.001+r;
					rcounter++;
					pfirm.remove(0);
				}else{

					pbank.get(0).addnewLFlist(pfirm.get(0).id, pbank.get(0).buffer,rr*pfirm.get(0).lev+r, tau);
					pfirm.get(0).addnewBB(pbank.get(0).id, pbank.get(0).buffer, rr*pfirm.get(0).lev+r,tau);
					pfirm.get(0).addCandidate(pbank.get(0).id,rr*pfirm.get(0).lev+r,"Old","Yes");

					for(CandidateBank cb:pfirm.get(0).Candidatelist){
						if(cb.id==pbank.get(0).id){
							cb.addPrincipal(pbank.get(0).buffer);
							if(cb.tradestate=="No") cb.tradestate="Yes";
						}
					}
					creditmoney+=pbank.get(0).buffer;
					pfirm.get(0).L-=pbank.get(0).buffer;
					grossr+=pfirm.get(0).lev*0.001+r;
					rcounter++;
					pbank.get(0).buffer=0;
					pbank.remove(0);

					}


			}else {
				pfirm.get(0).rejectordernum++;
				rf++;
			}
			/*else{
				pfirm.remove(0);
			}*/
			}

			iterator++;
			if(iterator>10000) break;
			if(pfirm.size()==0||pbank.size()==0) break;
			}while(iterator<10000);
		}

		pfirm.clear();
		pbank.clear();



	}

	public void ThirdCreditMarket(ArrayList<Enterprise> firm, ArrayList<Institution> bank, int x){

		/*TODO
		 *銀行が企業と接触し、貸し出し申請をする
		 * 一回の貸し出しに上限を設ける(自己比率0.1%分の貸し出し金まで)
		 * 貸出しは一回まで
		 *
		 * 需要過多を想定（現状はそうなのか？）
		 *
		 *
		 */

		ArrayList<Institution> pbank=new ArrayList<Institution>();
		ArrayList<Enterprise> pfirm=new ArrayList<Enterprise>();

		bank.stream().filter(s->s.buffer>0.0001)
		 .forEach(s->pbank.add(s));
		firm.stream().filter(s->s.L>0.00001)
		 .forEach(s->pfirm.add(s));

		//一回の貸し出し可能額の設定

		for(Institution pb:bank) {
			if(pb.buffer>pb.totalassets*0.001) {
				pb.SS=pb.totalassets*0.001;
			}else {
				pb.SS=pb.buffer;
			}
		}


		System.out.println("ThirdCM:"+" firm;"+pfirm.size()+" pbank:"+pbank.size());




		//企業-銀行ランダムマッチング

		if(pfirm.size()>0&&pbank.size()>0){
			while(pfirm.size()==0||pbank.size()==0){
			Collections.shuffle(pfirm);Collections.shuffle(pbank);
			//0番目同士を取引
			//貸し出すかの判定
			if(!pfirm.get(0).NGlist.contains(pbank.get(0).id)) {
				if(pfirm.get(0).L<pbank.get(0).SS){
					pbank.get(0).addnewLFlist(pfirm.get(0).id,pfirm.get(0).L,0.001*pfirm.get(0).lev+r,tau);
					pfirm.get(0).addnewBB(pbank.get(0).id, pfirm.get(0).L, 0.001*pfirm.get(0).lev+r,tau);
					pfirm.get(0).addCandidate(pbank.get(0).id,0.001*pfirm.get(0).lev+r,"Old","Yes");

					for(CandidateBank cb:pfirm.get(0).Candidatelist){
						if(cb.id==pbank.get(0).id){
							cb.addPrincipal(pfirm.get(0).L);
							if(cb.tradestate=="No") cb.tradestate="Yes";
						}
					}
					pbank.get(0).SS-=pfirm.get(0).L;
					pbank.get(0).buffer-=pfirm.get(0).L;
					creditmoney+=pfirm.get(0).L;
					pfirm.get(0).L=0;
					grossr+=pfirm.get(0).lev*0.001+r;
					rcounter++;
					pfirm.remove(0);
					pbank.remove(0);
				}else{

					pbank.get(0).addnewLFlist(pfirm.get(0).id, pbank.get(0).SS,rr*pfirm.get(0).lev+r, tau);
					pfirm.get(0).addnewBB(pbank.get(0).id, pbank.get(0).SS, rr*pfirm.get(0).lev+r,tau);
					pfirm.get(0).addCandidate(pbank.get(0).id,rr*pfirm.get(0).lev+r,"Old","Yes");

					for(CandidateBank cb:pfirm.get(0).Candidatelist){
						if(cb.id==pbank.get(0).id){
							cb.addPrincipal(pbank.get(0).SS);
							if(cb.tradestate=="No") cb.tradestate="Yes";
						}
					}
					creditmoney+=pbank.get(0).SS;
					pfirm.get(0).L-=pbank.get(0).SS;
					grossr+=pbank.get(0).DOL.get(0).interest;
					rcounter++;
					pbank.get(0).buffer=0;
					pbank.get(0).SS=0;
					pbank.remove(0);
					pfirm.remove(0);
					}


			}else{
				pfirm.remove(0);
			}
			}
		}

		pfirm.clear();
		pbank.clear();




	}

	private void AdjustBS(ArrayList<Enterprise> firm, ArrayList<Institution> bank, int x) {
		// TODO Auto-generated method stub

		for(int i=0;i<firmnode;i++){
			firm.get(i).l=0;
			for(int j=0;j<firm.get(i).BB.size();j++){
				firm.get(i).l+=firm.get(i).BB.get(j).getRemainingprincipal();
			}
			for(int j=0;j<firm.get(i).newBB.size();j++){

				firm.get(i).l+=firm.get(i).newBB.get(j).price;
			}
			firm.get(i).setKY(fi,beta);
			//System.out.println(firm.get(i).K);
		}

		for(int i=0;i<banknode;i++){
			bank.get(i).lb=0;
			bank.get(i).bb=0;
			bank.get(i).lf=0;
			for(int j=0;j<bank.get(i).LFlist.size();j++){
				bank.get(i).lf+=bank.get(i).LFlist.get(j).getRemainingPrincipal();
			}
			for(int j=0;j<bank.get(i).newLFlist.size();j++){
				bank.get(i).lf+=bank.get(i).newLFlist.get(j).getRemainingPrincipal();
			}
			for(int j=0;j<bank.get(i).BBlist.size();j++){
				bank.get(i).bb+=bank.get(i).BBlist.get(j).getIBBRemainingprincipal();
			}
			for(int j=0;j<bank.get(i).newBBlist.size();j++){
				bank.get(i).bb+=bank.get(i).newBBlist.get(j).getIBBRemainingprincipal();
			}
			for(int j=0;j<bank.get(i).LBlist.size();j++){
				bank.get(i).lb+=bank.get(i).LBlist.get(j).getIBLRemainingPrincipal();
			}
			for(int j=0;j<bank.get(i).newLBlist.size();j++){
				bank.get(i).lb+=bank.get(i).newLBlist.get(j).getIBLRemainingPrincipal();
			}

			bank.get(i).totalassets=bank.get(i).getTotalAssets();
			bank.get(i).totalinvest=bank.get(i).getTotalInvest();
			bank.get(i).setCAR();


		}


	}


	private void BankinputBuffer(ArrayList<Institution> bank, int x) {
		// TODO 自動生成されたメソッド・スタブ
		
		bank.stream().forEach(s->{
			residual+=s.buffer;
			s.residual=s.buffer;
			s.Residual.add(s.buffer);
		});
	}


	private void UpdateCMList(ArrayList<Institution> bank, ArrayList<Enterprise> firm, int x) {
		// TODO 自動生成されたメソッド・スタブ

		//CreditMarketでの使用リスト及び情報の整理
		firm.stream().forEach(s->s.updateCMlist(x));
		bank.stream().forEach(s->s.updateCMList());


	}


	private void FirmProductionProfit(ArrayList<Enterprise> firm, int x) {
		// TODO 自動生成されたメソッド・スタブ
		//生産活動
		for(int i=0;i<firmnode;i++){
			double p=2*Math.random();

			//firm.get(i).Y=firm.get(i).u*firm.get(i).Ylist.get(firm.get(i).Ylist.size()-1);
			firm.get(i).YY=firm.get(i).Y*p;
			//firm.get(i).Ylist.add(firm.get(i).Y);
			firm.get(i).pai=firm.get(i).YY;//ここで金利支払いのため実利益に生産高をあらかじめ足しておく
			grossproduction+=firm.get(i).YY;
		}
		System.out.println(grossproduction);
		GrossProduction.add(grossproduction);
	}


	private void FirmProductionProfit1(ArrayList<Enterprise> firm,int x) {

		//生産量の決定
		firm.stream().forEach(s->s.setKY1(fi,beta));


		firm.stream().forEach(s->{
			double u=0.04+0.21*Math.random();
			s.YY=u*s.Y;
			s.pai=s.YY;
			grossproduction+=s.YY;
		});
		//System.out.println(grossproduction);
		GrossProduction.add(grossproduction);

	}

	private void FirmPayInstallments(ArrayList<Enterprise> firm, ArrayList<Institution> bank,ArrayList<BrProcess> vand, int x) {
		// TODO Auto-generated method stub

		for(int i=0;i<firmnode;i++){
			for(int j=0;j<firm.get(i).BB.size();j++){//
				for(int k=0;k<banknode;k++){
					if(firm.get(i).BB.get(j).bankid==bank.get(k).id){
						double inst=firm.get(i).getInst(j);//+taucounterおよび支払い残高の計算
						double interestpayment=firm.get(i).getInterestpayment(j);
						double principal=firm.get(i).getPrincipal(j);
						firm.get(i).l-=principal;//もともと借りていた分の元本を返却
						firm.get(i).rit+=interestpayment;//ステップtにおける金利の支払い
						creditmoney-=principal;
						for(CandidateBank cb:firm.get(i).Candidatelist){
							if(cb.id==bank.get(k).id){
								cb.minusPrincipal(principal);
							}
						}

					}
				}
				if(firm.get(i).BB.get(j).state=="Dead") {
					firm.get(i).l-=firm.get(i).getPrincipal(j);
					firm.get(i).rit+=firm.get(i).getInterestpayment(j);
					vand.get(0).L+=firm.get(i).getInst(j);//破綻処理機関に格納
					creditmoney-=firm.get(i).getPrincipal(j);
				}
			}
		}


		for(int i=0;i<banknode;i++) {
			for(int j=0;j<bank.get(i).LFlist.size();j++) {
						bank.get(i).buffer+=bank.get(i).LFlist.get(j).installment;
						bank.get(i).n+=bank.get(i).LFlist.get(j).interestpayment;
						bank.get(i).pailf+=bank.get(i).LFlist.get(j).interestpayment;
						bank.get(i).updateLFlist(j);
					}
				}



		//返済完了の場合、リストを消去
		for(int i=0;i<firmnode;i++){
			for(int j=0;j<firm.get(i).BB.size();j++){
				if(firm.get(i).getTaucounter(j)==firm.get(i).getTau(j)){
					firm.get(i).BB.remove(j);
					j--;
			}
		}
		}


		for(int i=0;i<banknode;i++){
			for(int j=0;j<bank.get(i).LFlist.size();j++){
				if(bank.get(i).getTaucounter(j)==bank.get(i).getTau(j)){
					bank.get(i).LFlist.remove(j);
					j--;
				}

		}
		}


	}


	private void FirmRealProfit(ArrayList<Enterprise> firm,int x) {
		// TODO Auto-generated method stub

		for(int i=0;i<firmnode;i++){
			firm.get(i).pai=firm.get(i).YY-firm.get(i).rit-g*firm.get(i).YY-firm.get(i).f;
			//firm.get(i).pailist.add(firm.get(i).pai);

		}

	}

	private void FirmRealProfit1(ArrayList<Enterprise> firm,int x) {

		//自己資本金に応じて現象
		
		firm.stream().forEach(s->{
			s.pai=s.YY-s.rit-s.f*s.A;}
		);
		

		//固定費
		/*
		firm.stream().forEach(s->{
			s.pai=s.YY-s.rit-0.5;}
		);
		*/


	}

	private void EvaLev(ArrayList<Enterprise> firm,ArrayList<Institution> bank,int x) {

		firm.stream().forEach(s->s.EvaLeverage(sm));
		bank.stream().forEach(s->s.EvaLeverage(sm));
		

	}

	private void BankruptFirm(ArrayList<Enterprise> firm,int x) {
		// TODO Auto-generated method stub
		for(int i=0;i<firmnode;i++){
			if(firm.get(i).pai<0) {
			firm.get(i).A=firm.get(i).A+firm.get(i).pai;}else {
				firm.get(i).A+=(1-pi)*firm.get(i).pai;
			}
			if(firm.get(i).A<0){
				firm.get(i).state="Dead";
				//System.out.println("企業破綻したお");

			}
		}
		NumberOfFailedFirm.add(ffnum);//タームごと企業破綻数の格納
	}



	private void BankPayInstallments(ArrayList<Institution> bank,ArrayList<BrProcess> vand) {
		// TODO Auto-generated method stub


		for(int i=0;i<banknode;i++){
			for(int j=0;j<bank.get(i).BBlist.size();j++){
				for(int k=0;k<banknode;k++){
					if(bank.get(i).BBlist.get(j).bankid==bank.get(k).id){
						double inst=bank.get(i).getInst(j);
						double principal=bank.get(i).getPrincipal(j);

						moneystock-=principal;
						bank.get(i).bb-=principal;
						bank.get(i).buffer-=inst;
						bank.get(i).n-=inst-principal;
						bank.get(i).paibb+=inst-principal;
						bank.get(i).updateBBlist(j);
					}
				}
					if(bank.get(i).BBlist.get(j).state=="Dead") {
						bank.get(i).bb-=bank.get(i).getPrincipal(j);
						bank.get(i).buffer-=bank.get(i).getInst(j);
						bank.get(i).n-=bank.get(i).getInst(j)-bank.get(i).getPrincipal(j);
						vand.get(0).L+=bank.get(i).getInst(j);//破綻処理機関に格納
						bank.get(i).updateBBlist(j);
					}
				}
			}


		for(int i=0;i<banknode;i++) {
			for(int j=0;j<bank.get(i).LBlist.size();j++){
				for(int k=0;k<banknode;k++) {
				if(bank.get(i).LBlist.get(j).bankid==bank.get(k).id){
					bank.get(i).buffer+=bank.get(i).LBlist.get(j).installment;
					bank.get(i).n+=bank.get(i).LBlist.get(j).interestpayment;
					bank.get(i).pailb+=bank.get(i).LBlist.get(j).interestpayment;
					bank.get(i).updateLBlist(j);


				}
			}
		}
		}

		//返済完了の場合、リストを消去，
		for(int i=0;i<banknode;i++){
			for(int j=0;j<bank.get(i).BBlist.size();j++){
				if(bank.get(i).getIBBTaucounter(j)==bank.get(i).getIBBTau(j)){
					bank.get(i).BBlist.remove(j);
					j--;
				}
			}
		}



		for(int i=0;i<banknode;i++) {
			for(int j=0;j<bank.get(i).LBlist.size();j++){
				if(bank.get(i).getIBLTaucounter(j)==bank.get(i).getIBLTau(j)){
					bank.get(i).LBlist.remove(j);
					j--;
				}
			}
		}
	}




	private void BankProfit(ArrayList<Institution> bank, ArrayList<Enterprise> firm,double r,int x,double div) {
		// TODO Auto-generated method stub

		//破綻行の影響（企業)
		for(int i=0;i<banknode;i++){
			for(int k=0;k<firmnode;k++){
				for(int j=0;j<bank.get(i).LFlist.size();j++){
					if(bank.get(i).LFlist.get(j).firmid==firm.get(k).id){
						if(firm.get(k).state=="Dead"){
							bank.get(i).badfloan+=bank.get(i).getRemainingPrincipal(j);
							bank.get(i).grossbadloan+=bank.get(i).getRemainingPrincipal(j);
							grossbadloan+=bank.get(i).getRemainingPrincipal(j);
							creditmoney-=bank.get(i).getRemainingPrincipal(j);
							bank.get(i).lf-=bank.get(i).getRemainingPrincipal(j);
							bank.get(i).n-=bank.get(i).getRemainingPrincipal(j);
							bank.get(i).LFlist.remove(j);
							j--;
						}
					}
				}
			}
		}
		if(x>2) {
		//市場性資産評価損益，預金金利支払い
		bank.stream().forEach(s->{
			s.n-=s.getExpense(dr);
			s.buffer-=s.getExpense(dr);
			s.n+=s.getLiqAssetProfit(liqAr);
			s.buffer+=s.getLiqAssetProfit(liqAr);
		});

		bank.stream().forEach(s->s.pai=s.getPai(liqAr));
		}

		if(x>2) {

		//配当の支払い
		bank.stream().filter(s->s.pai>0)
					 .forEach(s->{
						 s.n-=s.getDiv(div);
						 s.buffer-=s.getDiv(div);
					 });

		bank.stream().forEach(s->s.pai=s.getPai(liqAr));

		}
	}






	private void BankruptProcessing(ArrayList<Institution> bank, ArrayList<Enterprise> firm,int x) {
		// TODO 自動生成されたメソッド・スタブ


		for(int i=0;i<firmnode;i++){
			if(firm.get(i).state=="Dead"){
				FailedFirm.add(firm.get(i));
				ffnum++;
				brfnum++;
				//System.out.println("企業破綻"+firm.get(i).id+" 借入金;"+firm.get(i).l+" 初期Candidate;"+firm.get(i).Candidatelistid);
				firm.remove(i);
				firmnode--;
				i--;
			}
		}


	}



	private void NewEntry(ArrayList<Enterprise> firm, ArrayList<Institution> bank, int x) {
		// TODO 自動生成されたメソッド・スタ

		//企業
		int Nentry=0;
		double abs=Math.abs(d*(r-e));//市場金利は0.1を超えないことを想定

		//Nentry=(int) (200*0.2/(1+abs));
		Nentry=ffnum;
	    //System.out.println("noob;"+ffnum);




		//企業リストへ追加
		//ランダム・パートナーチョイス型
		/*
		for(int i=0;i<Nentry;i++) {
			//取引先リストの作成
			ArrayList<Integer> list=new ArrayList<Integer>();//銀行idリストの作成
			for(int j=0;j<banknode;j++) list.add(bank.get(j).id);
			Collections.shuffle(list);
			ArrayList<Integer> Initbanklist=new ArrayList<Integer>();
			for(int k=0;k<initialbanknumber;k++) {
				Initbanklist.add(list.get(k));
			}
			Enterprise F=new Enterprise(newfirmid,k0,Initbanklist);
			firm.add(F);
			noobfirm++;
			firmnode++;
			newfirmid++;
		}
		*/

		//銀行企業固定
		ArrayList<Integer> list1=new ArrayList<Integer>();
		for(int i=0;i<Nentry;i++) {
			Enterprise F1=new Enterprise(newfirmid,k0,FailedFirm.get(brfnum-Nentry).Candidatelistid,lfmin,lfmax,val,vi);
			firm.add(F1);
			noobfirm++;
			firmnode++;
			newfirmid++;

		}






	}

	private void ChainOfBankruptBank(ArrayList<Institution> bank,int x,double r){


			int newdeadcount=0;
			do{

				for(int i=0;i<banknode;i++){
					bank.get(i).pai=bank.get(i).getPai(liqAr);
					bank.get(i).setCAR();
					if(bank.get(i).getCAR()<z||bank.get(i).n<0) {
						bank.get(i).state="Dead";
						newdeadcount=0;
						System.out.println("銀行破綻したお"+" CAR"+bank.get(i).CAR+"　総資産"+bank.get(i).totalassets+"  ターム"+x+"  badloan"+bank.get(i).badfloan);
					}else{
						newdeadcount=1;}
					}

				//プログラム上、badloanの加算とリストの消去は別で行なっている
				for(int i=0;i<banknode;i++){
					for(int k=0;k<banknode;k++){
						for(int j=0;j<bank.get(i).LBlist.size();j++){
							if(bank.get(i).LBlist.get(j).bankid==bank.get(k).id){
								if(bank.get(k).state=="Dead"){
									bank.get(i).badbloan+=bank.get(i).getIBRemainingPrincipal(j);
									bank.get(i).grossbadloan+=bank.get(i).getIBRemainingPrincipal(j);
									moneystock-=bank.get(i).getIBRemainingPrincipal(j);
									bank.get(i).lb-=bank.get(i).getIBRemainingPrincipal(j);
									bank.get(i).n-=bank.get(i).getIBRemainingPrincipal(j);
									bank.get(i).LBlist.remove(j);
									j--;
								}
							}
						}
					}
				}




				for(int i=0;i<banknode;i++){
					if(bank.get(i).state=="Dead"){
						FailedBank.add(bank.get(i));
						bank.remove(i);
						banknode--;
						i--;
					}
				}
				if(banknode==0)break;
			}while(newdeadcount==0);

		}


	private void BankRealProfit(ArrayList<Institution> bank, int x,double r) {
		// TODO 自動生成されたメソッド・スタブ
		for(int i=0;i<banknode;i++){
			bank.get(i).pai=bank.get(i).getPai(liqAr);
			if(bank.get(i).buffer<0) System.out.println("buffer0だよ"+Bank.get(i).id+"銀行"+"  buffer;"+bank.get(i).buffer);
		}


	}

	private void BankBufferProcessing(ArrayList<Institution> bank, int x) {

		/*buffer<0の銀行は預金部分の減少，市場性資産の保有割合分まで減少
		 *
		 */

		bank.stream().filter(s->s.buffer<0)
					 .forEach(s->s.BufferProcessing());

		bank.stream().filter(s->s.totalassets-s.totalinvest>0.01||s.totalinvest-s.totalassets>0.01)
					 .forEach(s->System.out.println(s.id+"銀行釣り合ってないよ"));
	}


	private void InputOPList(int x) {
		// TODO 自動生成されたメソッド・スタブ
		GrossBadLoan.add(grossbadloan);
		FFnum.add(ffnum);
		FFrate.add((double) ffnum/(firmnode-noobfirm));
		AvgInterest.add(grossr/rcounter);
		MoneyStock.add(moneystock);
		CreditMoney.add(creditmoney);
		Residual.add(residual);
		RejectFinancing.add(rf);

		if(x==0) {ProdGrowthRate.add(0.0);}else {
			ProdGrowthRate.add((GrossProduction.get(x)-GrossProduction.get(x-1))/GrossProduction.get(x-1));
		}

		//企業
		Firm.stream().forEach(s->grossfirmlev+=s.lev);
		Avgfirmlev.add(grossfirmlev/Firm.size());

		//銀行
		Bank.stream().forEach(s->grossbankclientnum+=s.clientnum);
		Avgbankclientnum.add(grossbankclientnum/Bank.size());
	}



	private void InitOPVariables() {
		// TODO 自動生成されたメソッド・スタブ
		ffnum=0;
		grossproduction=0;
		grossbadloan=0;
		noobfirm=0;
		grossr=0;
		rcounter=0;
		residual=0;
		grosssupply=0;
		grossdemand=0;
		rf=0;
		grossfirmlev=0;
		grossbankclientnum=0;
	}





	//一試行終了，結果の格納

	public void OutPutResult(ArrayList<OutPut> output,int atp) {
		// TODO 自動生成されたメソッド・スタブ

		output.get(atp).Grossproduction.addAll(GrossProduction);
		output.get(atp).Productgrowthrate.addAll(ProdGrowthRate);
		output.get(atp).FFnum.addAll(FFnum);
		output.get(atp).Avginterest.addAll(AvgInterest);
		output.get(atp).Rejectfinancing.addAll(RejectFinancing);

		output.get(atp).Avgfirmlev.addAll(Avgfirmlev);
		output.get(atp).Avgbankclientnum.addAll(Avgbankclientnum);

		//エージェント規模ごとにソート

		setSortFirmK();
		//setSortBankK();

		//Bank.stream().sorted(Comparator.comparing(Institution::getTotalassets));
		//Firm.stream().sorted(Comparator.comparingDouble(Enterprise::getK));

		output.get(atp).Finalbank.addAll(Bank);
		output.get(atp).Finalfirm.addAll(Firm);
		System.out.println(Firm.size());

	}


	//Expリストの初期化





	public void clearList() {
		// TODO 自動生成されたメソッド・スタブ
		GrossBadLoan.clear();FFnum.clear();FFrate.clear();AvgInterest.clear();MoneyStock.clear();CreditMoney.clear();
		Residual.clear();RejectFinancing.clear();Avgfirmlev.clear();Avgbankclientnum.clear();
		Grossdemand.clear();Grosssupply.clear();GrossProduction.clear();

		Prod.clear();ProdGrowthRate.clear();NumberOfFailedFirm.clear();FailedFirm.clear();FailedBank.clear();
	}





















	public ArrayList<Double> getGrossProduction() {
		// TODO 自動生成されたメソッド・スタブ
		return GrossProduction;
	}



	public int getalivefirm() {
		// TODO Auto-generated method stub
		return firmnode;
	}

	public int getAliveBank() {
		return banknode;
	}




	public void show() {
		// TODO 自動生成されたメソッド・スタブ
		/*
		for(int i=0;i<banknode;i++) {
			System.out.println("銀行"+Bank.get(i).id+"  企業取引数："+Bank.get(i).LFlist.size()+"  銀行取引数："+(Bank.get(i).LBlist.size()+Bank.get(i).BBlist.size())+"  自己資本比率:"+Bank.get(i).CAR);
			System.out.println("lftlist:"+Bank.get(i).lftlist);
		}*/
		System.out.println(Vand.get(0).L);
	}



	public int getTerm() {
		// TODO Auto-generated method stub
		return term;
	}



	public Double getAgrregateOP(int i) {
		// TODO Auto-generated method stub
		return GrossProduction.get(i);
	}



	public double getGrowthRateOP(int i) {
		// TODO 自動生成されたメソッド・スタブ
		if(i==0) return 0;
		else {
		return (GrossProduction.get(i)-GrossProduction.get(i-1))/GrossProduction.get(i-1);
		}
	}



	public int getFailedFirm(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return FFnum.get(i);
	}

	public double getBadLoan(int i) {

		return GrossBadLoan.get(i);
	}



	public double getFailedFirmrate(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return FFrate.get(i);
	}



	public	double getBankCAR(int j) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(j).CAR;
	}



	public double getFirmK(int j) {
		// TODO Auto-generated method stub



		return Firm.get(j).K;
	}



	public Double getAVGInterest(int j) {
		// TODO 自動生成されたメソッド・スタブ
		return AvgInterest.get(j);
	}



	public double getMoneyStock(int j) {
		// TODO Auto-generated method stub
		return MoneyStock.get(j);
	}

	public double getCreditMoney(int j){

		return CreditMoney.get(j);
	}



	public void setSortFirmK() {
		// TODO 自動生成されたメソッド・スタブ
		//総資産順に並び変え
		Collections.sort(Firm,new GetFirmKComparator());
	}

	private void setSortBankK() {
		// TODO 自動生成されたメソッド・スタブ
	//	Collections.sort(Firm,new GetBankKComparator());
	}



	public int getBankId(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).id;
	}



	public double getBankLf(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).lf;
	}



	public double getBankLb(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).lb;
	}



	public double getBankBb(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).bb;
	}



	public double getBankK(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).totalassets;
	}



	public double getBankBuffer(int i) {
		// TODO Auto-generated method stub
		return Bank.get(i).buffer;
	}



	public int getFirmCandidateNum(int j) {
		// TODO 自動生成されたメソッド・スタブ
		return Firm.get(j).Candidatelist.size();
	}



	public double getFirmLeverage(int j) {
		// TODO 自動生成されたメソッド・スタブ
		return Firm.get(j).lev;
	}



	public double getResidual(int j) {
		// TODO 自動生成されたメソッド・スタブ
		return Residual.get(j);
	}



	public double getBankGrossBadLoan(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).grossbadloan;
	}



	public double getBankResidual(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).residual;
	}



	public double getGrossDemand(int j) {
		// TODO Auto-generated method stub
		return Grossdemand.get(j);
	}



	public double getGrossSupply(int j) {
		// TODO Auto-generated method stub
		return Grosssupply.get(j);
	}



	public int getBankLFSize(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).LFlist.size();
	}



	public double getBankgoalLev(int j) {
		// TODO Auto-generated method stub
		return Bank.get(0).goallevt.get(j);
	}



	public Double getBankactLev2(int j) {
		// TODO Auto-generated method stub
		return Bank.get(0).CARlist.get(j);
	}



	public double getFirmnextLeverage(int j) {
		// TODO 自動生成されたメソッド・スタブ
		return Firm.get(j).nextlev;
	}



	public double getFirmLevNowEvalue(int i) {
		// TODO 自動生成されたメソッド・スタブ
		double levnowevalue=0;
		for(int j=0;j<Firm.get(i).levlist.size();j++) {
			if(Firm.get(i).levlist.get(j).state=="Yes") levnowevalue=Firm.get(i).levlist.get(j).X;
		}
		return levnowevalue;
	}



	public double getBankLevlist(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(0).levlevellist.get(i);
	}



	public double getBanklevlevelvalue(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(0).levlist.get(i).evalue;
	}



	public double getBankTimeCAR(int j) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(0).CARlist.get(j);
	}



	public double getBankLiqAsset(int i) {
		// TODO 自動生成されたメソッド・スタブ
		return Bank.get(i).liqA;
	}



	public int getRejectFinancing(int j) {
		// TODO 自動生成されたメソッド・スタブ
		return RejectFinancing.get(j);
	}

	public int getBankClientNum(int j) {

		return Bank.get(j).clientnum;
	}



	public double getOneFirmLev(int ag, int j) {
		// TODO 自動生成されたメソッド・スタブ
	
		return Firm.get(ag).Levlist.get(j);
	}
	public double getOneFirmK(int ag,int j) {
		return Firm.get(ag).Klist.get(j);
	}
	
	public double getOneFirmGoalLev(int ag,int j) {
		return Firm.get(ag).goallevt.get(j);
	}
	
	public double getOneFirmCandNum(int ag,int j) {
		return Firm.get(ag).Candlist.get(j);
	}
	
	public double getOneFirmPai(int ag,int j) {
		return Firm.get(ag).pailist.get(j);
	}
	
	public double getOneFirmLevChoice(int ag,int j) {
		return Firm.get(ag).Choiceratelist.get(j);
	}



	public int getOneFirmLived(int ag) {
		// TODO 自動生成されたメソッド・スタブ
		return Firm.get(ag).live;
	}



	public int getOneFirmId(int ag) {
		// TODO 自動生成されたメソッド・スタブ
		return Firm.get(ag).id;
	}



	public double getOneFirmRealLev(int ag, int j) {
		// TODO 自動生成されたメソッド・スタブ
		return Firm.get(ag).RealLevlist.get(j);
	}



	public int getFirmRejectOrderNum(int j) {
		// TODO Auto-generated method stub
		return Firm.get(j).rejectordernum;
	}



	public int getFirmNonBufferNum(int j) {
		// TODO Auto-generated method stub
		return Firm.get(j).nonbuffernum;
	}





















}