import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.xmlbeans.impl.soap.Node;

public class Institution implements Cloneable{


	public Institution(int num,int p, int q){
		id=num;
		indeg=p;
		outdeg=q;
		deg=p+q;
	}

	//対称型コンストラクタ
	public Institution(int i, double a0) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.id=i;
		this.A0=a0;
		n=A0;
		buffer=A0;
		d=0;
	}


	int id;//金融機関のID
	int indeg,outdeg,deg;

	double A0;//

	String state;//現存か破綻

	public double w1,w2;//ネットワーク重み

	double initCAR;//初期設定自己資本比率
	double CAR;//自己資本比率
	double totalassets;//総資産
	double totalinvest;//総投資
	double n;//自己資本金
	double d;//預金等他人資本
	double lf;//企業貸付額
	double lft;//ステップtに企業に貸付をする予定である金額
	double lb;//インターバンク貸付金
	double bb;//インターバンク借入金
	double liqA;//市場性資産
	double buffer;//余剰金

	double pailf;//企業からの金利収入
	double pailb;//銀行からの金利収入
	double paibb;//銀行に支払う金利
	double pai;//ステップtでの利益
	double badfloan;//企業貸付の焦げ付き
	double badbloan;//銀行貸し付けの焦げ付き
	double grossbadloan;//総貸出損失
	double expense;//経費
	double divident;//配当
	double residual;//残余

	double S;//1期の取引貸出可能額
	double SS;//thirdcredit市場貸出し制限額
	double forcelev;//目標レバレッジ
	double liqArate;//市場性資産保持率
	double ddt;//t期における受け入れるべき他人資本
	double liqAplust;//t期における追加投資LiqAsset
	double liqAprofit;//市場性資産収益

	double rddt;//t期における売却（減少）すべき資本
	double rbuf;//売却留保金

	double ibrate;//ib貸出比率
	double ibable;//ib貸出可能額

	//自己組織型
	int levnownum;//レバレッジレベルナンバー
	double nextlev;
	double sigmat;
	double counter;

	int clientnum;//顧客数
	int initclientnum;//初期顧客数
	ArrayList<Integer> ClientNum=new ArrayList<Integer>();

	//取引可能インターバンクネットワーク
	ArrayList<Integer> Inid=new ArrayList<Integer>();
	ArrayList<Integer> Outid=new ArrayList<Integer>();

	ArrayList<Leverage> levlist=new ArrayList<Leverage>();
	ArrayList<LevProbably> list=new ArrayList<LevProbably>();
	ArrayList<Double> levlevellist=new ArrayList<Double>();


	ArrayList<Double> pailist=new ArrayList<Double>();
	ArrayList<Double> lftlist=new ArrayList<Double>();
	ArrayList<Double> nlist=new ArrayList<Double>();
	ArrayList<Double> CARlist=new ArrayList<Double>();
	ArrayList<Double> bufferlist=new ArrayList<Double>();
	ArrayList<Double> Residual=new ArrayList<Double>();


	ArrayList<LInterest> rf=new ArrayList<LInterest>();//全Firm貸出金利
	ArrayList<DemandOrderList> DOL=new ArrayList<DemandOrderList>();//企業からの要望リスト
	ArrayList<LendingFirm> LFlist=new ArrayList<LendingFirm>();//企業貸出リスト
	ArrayList<LendingFirm> newLFlist=new ArrayList<LendingFirm>();
	ArrayList<IBLoan> LBlist=new ArrayList<IBLoan>();//IB貸出リスト
	ArrayList<IBBorrow> BBlist=new ArrayList<IBBorrow>();//IB借入リスト
	ArrayList<IBBorrow> newBBlist=new ArrayList<IBBorrow>();//t期のIB借入リスト
	ArrayList<IBLoan> newLBlist=new ArrayList<IBLoan>();//t期のIB貸出リスト
	ArrayList<IBList> IBlist=new ArrayList<IBList>();//IB需要リスト
	ArrayList<Double> Slist=new ArrayList<Double>();//タームごと貸し出し可能額
	ArrayList<Double> goallevt=new ArrayList<Double>();//タイムステップごと目標レバレッジ
	ArrayList<Double> actlevt=new ArrayList<Double>();//タイムステップごと実際のレバレッジ


	public double getTotalassets() {
		return totalassets;
	}


	public void FirstDecideLev() {
		/*
		BigDecimal c=new BigDecimal(String.valueOf(CAR));
		int car=(int)c.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
		*/



		nextlev=CAR;
		levlist.stream().filter(s->s.level==nextlev)
						.forEach(s->s.state="Yes");
		levlevellist.add(nextlev);
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

	public void choiceLev() {

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
		for(LevProbably l:list) {
		if(r<=l.getCount()&&a<=r) {
		nextlev=l.getLevel();
		break;
		}else {
		a=l.getCount();
		}
		}

		goallevt.add(nextlev);

		levlist.stream().filter(s->s.level==nextlev)
					.forEach(s->s.state="Yes");
		levlist.stream().filter(s->s.level!=nextlev)
				.forEach(s->s.state="No");
		for(Leverage l:levlist) {
		if(l.level-0.005==nextlev||l.level+0.005==nextlev) {l.adjacent="Yes";}else {l.adjacent="No";}
		}

		levlevellist.add(nextlev);

	}

	public void setBS() {



		//外部資金(d)の調達
		ddt=(n/nextlev)-totalassets;
		if(ddt>0.1) {
			d+=ddt;
			totalassets+=ddt;
			liqAplust=totalassets*liqArate-liqA;
			if(liqAplust>ddt) {
				liqAplust=ddt;
				liqA+=liqAplust;
				}else {
					liqA+=liqAplust;
			}
			if(ddt-liqAplust>0) {
				buffer+=ddt-liqAplust;
			}
		}else {

		//資産の縮小(buffer分だけ)
		if(ddt<-0.1) {
			if(-ddt<buffer) {
				totalassets+=ddt;
				d+=ddt;
				buffer+=ddt;

			}else {
				totalassets-=buffer;
				d+=buffer;
				buffer=0;
			}
		}
		/*
		//資産の縮小(流動性資産も割合を保つように)
		if(ddt<-0.1) {
			if(-ddt<buffer) {
				totalassets+=ddt;
				d+=ddt;
				buffer+=ddt;

			}else {
				totalassets-=buffer;

				d+=buffer;
				buffer=0;
			}
		}*/

		}







	}

	public void EvaLeverage(double sm) {

		levlist.stream().filter(s->s.state=="Yes")
			.forEach(s->s.evalue=(1-sm)*s.evalue+sm*pai);

		levlist.stream().filter(s->s.evalue<0)
			    		.forEach(s->s.evalue=0);

	}

	public void setLev(double lbmin,double lbmax,double val,String state,String range,double vi) {

		int size=(int) (((lbmax-lbmin))/val)+1;
		double value=lbmin;
		BigDecimal b1=new BigDecimal(value);
		BigDecimal b2=new BigDecimal(val);

		for(int i=0;i<size;i++) {
			Leverage newlev=new Leverage(value,0,0,state,range,i,vi);
			levlist.add(newlev);

			BigDecimal result=b1.add(b2);
			b1=result;
			value=result.doubleValue();
		}



	}

	public void setActualLeverage(){

		actlevt.add(CAR);
	}

	public void addInterest(int firmid,double r){


		LInterest firminterest=new LInterest(firmid,r);
		rf.add(firminterest);
	}


	public void addDOL(int firmid, double interest, double l,double ps, int tau) {
		// TODO 自動生成されたメソッド・スタブ
		DemandOrderList dol=new DemandOrderList(firmid,interest,l,ps,tau);
		DOL.add(dol);

	}


	public void addIBList(double ibdemand, double r, int tau) {
		// TODO 自動生成されたメソッド・スタブ
		IBList iblist=new IBList(ibdemand,r,tau);//(IB需要金、金利、期限）
		IBlist.add(iblist);


	}

	public void addLBList(int ibloanid, double ibloanmoney, double r, int tau){

		IBLoan ibloan=new IBLoan(ibloanid,ibloanmoney,r,tau);
		LBlist.add(ibloan);
	}

	public void addnewLBList(int ibloanid, double ibloanmoney, double r, int tau){

		IBLoan ibloan=new IBLoan(ibloanid,ibloanmoney,r,tau);
		newLBlist.add(ibloan);
	}


	public void addBBList(int IBborrowid, double ibborrowmoney, double r, int tau) {
		// TODO 自動生成されたメソッド・スタブ
		 IBBorrow ibborrow=new IBBorrow(IBborrowid,ibborrowmoney,r,tau);
		 BBlist.add(ibborrow);
	}


	public void addnewBBList(int IBborrowid, double ibborrowmoney, double r, int tau) {
		// TODO 自動生成されたメソッド・スタブ
		 IBBorrow ibborrow=new IBBorrow(IBborrowid,ibborrowmoney,r,tau);
		 newBBlist.add(ibborrow);
	}


	public void addLF(int firmid, double lfprice, double interest, int tau) {
		// TODO Auto-generated method stub
		LendingFirm lf=new LendingFirm(firmid,lfprice,interest,tau);
		LFlist.add(lf);
	}

	public void addnewLFlist(int firmid, double lfprice, double interest, int tau) {
		// TODO 自動生成されたメソッド・スタブ
		LendingFirm lf=new LendingFirm(firmid,lfprice,interest,tau);
		newLFlist.add(lf);
	}





	public double getTotalAssets() {
		// TODO 自動生成されたメソッド・スタブ
		totalassets=bb+d+n;
		return totalassets;
	}


	public double getTotalInvest() {
		// TODO 自動生成されたメソッド・スタブ
		totalinvest=lb+lf+buffer+liqA;
		return totalinvest;
	}

	public void setCAR() {
		// TODO Auto-generated method stub
		CAR=n/totalassets;
	}

	public void updateLFlist(int LFlistnumber) {
		// TODO Auto-generated method stub
		LFlist.get(LFlistnumber).updateList();

	}


	public int getTaucounter(int LFlistnumber) {
		// TODO 自動生成されたメソッド・スタブ
		return LFlist.get(LFlistnumber).getTaucoungter();
	}


	public int getTau(int LFlistnumber) {
		// TODO 自動生成されたメソッド・スタブ
		return LFlist.get(LFlistnumber).getTau();
	}


	public double getInst(int bankj) {
		// TODO Auto-generated method stub
		return BBlist.get(bankj).getInstallment();
	}


	public double getPrincipal(int bankj) {
		// TODO Auto-generated method stub
		return BBlist.get(bankj).getPrincipal();
	}


	public int getIBBTaucounter(int bankj) {
		// TODO Auto-generated method stub
		return BBlist.get(bankj).getTaucounter();
	}


	public int getIBBTau(int bankj) {
		// TODO Auto-generated method stub
		return BBlist.get(bankj).getTau();
	}

	public int getIBLTaucounter(int bankj) {
		// TODO Auto-generated method stub
		return LBlist.get(bankj).getTaucounter();
	}


	public int getIBLTau(int bankj) {
		// TODO Auto-generated method stub
		return LBlist.get(bankj).getTau();
	}


	public void updateLBlist(int bankj) {
		// TODO 自動生成されたメソッド・スタブ
		LBlist.get(bankj).updatelist();
	}

	public void updateBBlist(int bankj) {
		// TODO 自動生成されたメソッド・スタブ
		BBlist.get(bankj).updatelist();
	}

	public double getPai(double liqAr) {
		// TODO 自動生成されたメソッド・スタブ
		pai=pailf+pailb+liqAr*liqA-badfloan-badfloan-paibb-expense-divident;
		return pai;
	}


	public double getRemainingPrincipal(int firmj) {
		// TODO 自動生成されたメソッド・スタブ
		return LFlist.get(firmj).getRemainingPrincipal();
	}


	public double getIBRemainingPrincipal(int bankj) {
		// TODO 自動生成されたメソッド・スタブ
		return LBlist.get(bankj).getIBLRemainingPrincipal();
	}


	public double getCAR() {
		// TODO 自動生成されたメソッド・スタブ
		return CAR;
	}


	public void Init() {
		// TODO 自動生成されたメソッド・スタブ
		lft=0;
		pai=0;pailf=0;pailb=0;paibb=0;
		badfloan=0;
		badbloan=0;
		expense=0;
		divident=0;
		ddt=0;
		liqAplust=0;
		liqAprofit=0;
		residual=0;
		rddt=0;
		sigmat=0;
		counter=0;
		clientnum=0;
	}


	public void updateList() {
		// TODO 自動生成されたメソッド・スタブ
		pailist.add(pai);
		nlist.add(n);
		CARlist.add(CAR);
		bufferlist.add(buffer);
	}

	public void updateLevlist() {

		levlist.stream().forEach(s->{s.range="No";
		s.finalrange="No";
		s.adjacent="No";
		s.pl=0;
		});
		list.clear();

	}

	public void updateCMList() {
		// TODO 自動生成されたメソッド・スタブ


		//顧客数の計算(newBB,BB)
		ArrayList<Integer> client=new ArrayList<Integer>();
		newLFlist.stream().filter(s->!client.contains(s.firmid))
				          .forEach(s->client.add(s.firmid));
		LFlist.stream().filter(s->!client.contains(s.firmid))
		  			   .forEach(s->client.add(s.firmid));
		clientnum=client.size();
		ClientNum.add(clientnum);


		//CMリストの消去
		rf.clear();
		DOL.clear();
		IBlist.clear();
	}



	public double getExpense(double dr) {
		// TODO Auto-generated method stub
		expense=d*dr;
		return expense;
	}

	public double getDiv(double div) {
		// TODO 自動生成されたメソッド・スタブ
		if(pai>0) {divident=pai*div;}else {div=0;}
		return divident;
	}

	public double getBuffer() {
		// TODO 自動生成されたメソッド・スタブ
		return bb+n+d-(lf+lb);
	}

	public void setInitForceLev() {

		initCAR=CAR;
	}



	public void setForceLev() {
		// TODO 自動生成されたメソッド・スタブ
		ddt=(n/forcelev)-totalassets;
		if(ddt>0.1) {
			d+=ddt;
			totalassets+=ddt;
			liqAplust=totalassets*liqArate-liqA;
			if(liqAplust>ddt) {
				liqAplust=ddt;
				liqA+=liqAplust;
				}else {
					liqA+=liqAplust;
			}
			if(ddt-liqAplust>0) {
				buffer+=ddt-liqAplust;
			}
		}
	}


	public void setRemainCAR() {
		// TODO 自動生成されたメソッド・スタブ
		rddt=totalassets-(n/initCAR);
		/*
		if(rddt<-0.1) {
			if(-rddt<buffer) {
				totalassets+=rddt;
				d+=rddt;
				buffer+=rddt;

			}else

			{
				//資産縮小(bufferｍのみ)
				totalassets-=buffer;
				d+=buffer;
				buffer=0;
			}

		}
		*/

		//LiqA割合を維持しつつ縮小
		if(rddt>0.001) {
				double sellliqA=0;

				totalassets-=rddt;
				sellliqA=liqA-totalassets*liqArate;
				d-=rddt;
				if(buffer-(rddt-sellliqA)>0) {
					buffer-=(rddt-sellliqA);
					liqA=totalassets*liqArate;
				}else {
					liqA=totalassets*liqArate-(rddt-sellliqA-buffer);
					buffer=0;
				}
		}









		//liqArateを維持できるバッファーになった場合；
		double liqAp=totalassets*liqArate-liqA;
		if(buffer>0.001) {
			if(liqAp>0.001) {
				if(buffer>liqAp) {
					liqA+=liqAp;
					buffer-=liqAp;
				}else {
					liqAp+=buffer;
					buffer=0;
				}
			}
		}





	}

	public void setIBable(double bscallrate) {
		// TODO 自動生成されたメソッド・スタブ

		ibrate=bscallrate;
		ibable=totalassets*ibrate;
	}

	public double getLiqAssetProfit(double liqAr) {
		// TODO Auto-generated method stub
		liqAprofit=liqA*liqAr;
		return liqAprofit;
	}

	public void BufferProcessing() {
		// TODO 自動生成されたメソッド・スタブ

		d-=buffer;
		buffer=0;
		totalassets=getTotalAssets();
		liqA=totalassets*liqArate;
		totalinvest=getTotalInvest();
	}


	
/*
	public Institution clone() {
		Institution b=null;
		try {
			b=(Institution)super.clone();
			b=this.clone();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	*/

	public Institution clone(){
		try{
			return (Institution)super.clone();
		}catch(CloneNotSupportedException e){
			throw new InternalError(e.toString());

		}
	}











}
