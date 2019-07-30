import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

public class Main {

	private static FinalOutPut fop;



	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//初期状態
		/**金融機関 */
		int banknode=50;//書き方：BANK_NODE
		double E0=50000;//初期asset金額
		double MegaCAR=0.15,MediumCAR=0.11,SmallCAR=0.09;
		double a1=0.2,a2=0.5;//自己資本比率の決定(上位a%)
		double la1=0.5,la2=0.7;//市場性資産保有割合
		double bscallrate=0.1;
		double div=0.75;//divident（配当など0.75）
		String NW="BA";//CM,BA
		String BankType="Sym";

		//対称型金融機関
		double A0=150;
		double asyCAR=0.10;

		//企業
		int firmnode=1000;//初期企業数
		double k0=50;//初期asset金額


		//企業（初期自己資本比率あり)
		double fCAR=0.35;

		//市場状況
		double r=0.01;//市場金利
		double rr=0.001;//金利係数
		double dr=0.001;//預金金利
		double liqAr=0.001;//市場性資産収益
		int tau=4;


		//実験条件
		int atp=1;//試行回数
		int term=400;//ターム
		int creditterm=1;//クレジット市場でのターム
		int startterm=50;//
		double z=0.04;//自己資本比率破綻条件
		double rz=0.10;//自己資本維持比率⇒初期自己資本比率を維持
		int d=100;//参入location係数
		double e=0.1;//参入sensibity係数


		//レバレッジの設定
		/*
		double[] levf= {1,1.5,2,2.5,3,3.5,4,4.5,5,5.5,6,6.5,7,7.5,8,8.5,9,9.5,10,10.5,
				11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16,16.5,17,17.5,18,18.5,19,19.5,20};
		double[] levb= {8,8.5,9,9.5,10,10.5,11,11.5,12,12.5,13,13.5,14,14.5,15,15.5,16};*/
		//ArrayList<Leverage> Levf=new ArrayList<Leverage>();
		//ArrayList<Leverage> Levb=new ArrayList<Leverage>();
		double lfmin=1.5,lfmax=20.0,lbmin=0.080,lbmax=0.160;
		double val=0.5,valb=0.005;//間隔
		double h=0.03;//forgetting processs
		double sm=0.3;//割引率（0.3)
		double vi=0.45;//strength係数


		//金融機関
		double xx=0.2;//riskaversion係数
		double psy=0.1;//riskaversion乗数
		double alpha=0.08;//riskcoefficient係数（バーゼル規制による供給可能流動性）
		double myu=0.05;//貸出金利係数
		double sita=0.1;//貸出金利条数


		//企業
		//double fi=0.1,beta=0.75;//生産能力(0.1),生産能力係数
		int Candmax=10;//取引先限界数

		//企業（限界生産型）
		double fi=0.5,beta=0.83;
		double g=0.75;//変動費率
		double c=1;//破産コスト係数
		double lamda=1;//partnerchoice(intensityofchoice)
		int initialbanknumber=1;//初期状態取引候補先

		double f=0.1;//固定費 自己資本割合分(0.03)
		double pi=0.3;//実利益減少割合	(0.3)




		ArrayList<Institution> Bank=new ArrayList<Institution>();
		ArrayList<Enterprise> Firm=new ArrayList<Enterprise>();

		ArrayList<Institution> InitBank=(ArrayList<Institution>) Bank.clone();


		//OutPutリスト
		ArrayList<OutPut> Output=new ArrayList<OutPut>();
		for(int i=1;i<=atp;i++) {
			OutPut op=new OutPut(i,term);
			Output.add(op);
		}
		ArrayList<FinalOutPut> FinalOutput=new ArrayList<FinalOutPut>();
			FinalOutPut op=new FinalOutPut(atp,term);
			FinalOutput.add(op);



		//start
		for(int y=0;y<atp;y++) {
			System.out.println(y+"試行目");

		//各エージェントバランスシート作成

		//bankバランスシート、初期ネットワーク構築
		Build BankBS=new Build(banknode,E0,MegaCAR,MediumCAR,SmallCAR,a1,a2,NW,BankType,A0,asyCAR,lbmin,lbmax,valb,vi);
		BankBS.isBuild();
		BankBS.addLiqAsset(la1,la2);

		//bankバランスシート（対称型)
		//BuildBankSym BankBSSym=new BuildBankSym(banknode,A0);
		//BankBSSym.isBuild();


		Bank.addAll(BankBS.getFinalBank());
		//InitBank.add(BankBS.getFinalBank().clone());
		//Bank.addAll(BankBSSym.getFinalBank());

		for(Institution b:Bank) {
			InitBank.add(b.clone());
		}

		//firmバランスシート作成（A(i0))
		BuildFirm FirmBS=new BuildFirm(firmnode,k0,initialbanknumber,banknode,f,lfmin,lfmax,val,vi);
		FirmBS.isBuild();
		FirmBS.BankConnectDiversity(Bank);

		//firmバランスシート作成(自己資本比率あり)
		//BuildFirm FirmBS1=new BuildFirm(firmnode,k0,fCAR,initialbanknumber,banknode);
		//FirmBS1.isBuild1();



		Firm.addAll(FirmBS.getFinalFirm());
		//Firm.addAll(FirmBS1.getFinalFirm());

		//破綻処理機関リスト
		ArrayList<BrProcess> Vand=new ArrayList<BrProcess>();
		BrProcess vand=new BrProcess(1);
		Vand.add(vand);


		//初期状態出力(予備)
		//showInitExcel(Bank,Firm);

		//初期銀行状態の格納
		InitBankOutput(Output,InitBank,y);

		//実験用クラス

		Experiment Exp1=new Experiment(Bank,Firm,Vand,banknode,firmnode,r,tau,term,z,xx,psy,
				alpha,myu,sita,fi,g,c,lamda,initialbanknumber,d,e,k0,dr,startterm,div,liqAr,creditterm
				,bscallrate,rz,beta,h,sm,lfmin,lfmax,val,pi,vi,Candmax,rr);
		Exp1.isStart();
		if(Exp1.getBrCount()>0) {
			System.out.println("やりなおし");
			y--;
		}else {
		Exp1.OutPutResult(Output,y);

		}
		Exp1.clearList();




		//showExcel(Exp1,Bank);
		//showAgent(Exp1,Firm);




		AgentClear(Bank,Firm,Vand,InitBank);

	}
		ResultAdjust(FinalOutput,Output,term,atp,firmnode);//平均，(標準偏差)

		//実験条件の出力
		OutPutConditions(banknode,firmnode,r,tau,term,z,xx,psy,
				alpha,myu,sita,fi,g,c,lamda,initialbanknumber,d,e,k0,dr,startterm,div,liqAr,creditterm
				,bscallrate,rz,beta,h,sm,lfmin,lfmax,val,pi,vi,Candmax,rr,E0,f);

		OutPutResult(FinalOutput,term,atp,firmnode,banknode);

		System.out.println("--------------------------------終了------------------------------------");




	}












	private static void OutPutConditions(int banknode, int firmnode, double r, int tau, int term, double z, double xx,
			double psy, double alpha, double myu, double sita, double fi, double g, double c, double lamda,
			int initialbanknumber, int d, double e, double k0, double dr, int startterm, double div, double liqAr,
			int creditterm, double bscallrate, double rz, double beta, double h, double sm, double lfmin, double lfmax,
			double val, double pi, double vi, int candmax, double rr,double E0,double f) {
		// TODO 自動生成されたメソッド・スタブ

		Workbook book=null;

		try {

			book = new HSSFWorkbook();
			FileOutputStream fileOut=new FileOutputStream("workbookConditions.xls");
			String safename=WorkbookUtil.createSafeSheetName("[Conditions]");
			Sheet sheet1=book.createSheet(safename);
			CreationHelper createHelper=book.getCreationHelper();

			int x=0;
			Row row0=sheet1.createRow((short)x);
			row0.createCell(0).setCellValue("実験条件");
			Row row1=sheet1.createRow((short)x+1);
			row1.createCell(0).setCellValue("Bank");
			Row row2=sheet1.createRow((short)x+2);
			Row row3=sheet1.createRow((short)x+3);
			row2.createCell(0).setCellValue("node");row3.createCell(0).setCellValue(banknode);
			row2.createCell(1).setCellValue("初期合計Asset");row3.createCell(1).setCellValue(E0);
			row2.createCell(2).setCellValue("初期CAR");row3.createCell(2).setCellValue("0.15,0.11,0.09");
			row2.createCell(3).setCellValue("LiqA保持率");row3.createCell(3).setCellValue("50-70%");
			row2.createCell(4).setCellValue("μ");row3.createCell(4).setCellValue(myu);
			row2.createCell(5).setCellValue("θ");row3.createCell(5).setCellValue(sita);
			row2.createCell(6).setCellValue("ρ");row3.createCell(6).setCellValue(xx);
			row2.createCell(7).setCellValue("о");row3.createCell(7).setCellValue(psy);
			row2.createCell(8).setCellValue("配当率");row3.createCell(8).setCellValue(div);

			Row row4=sheet1.createRow((short)x+5);
			row4.createCell(0).setCellValue("Firm");
			Row row5=sheet1.createRow((short)x+6);
			Row row6=sheet1.createRow((short)x+7);
			row5.createCell(0).setCellValue("node");row6.createCell(0).setCellValue(firmnode);
			row5.createCell(1).setCellValue("初期Asset");row6.createCell(1).setCellValue(k0);
			row5.createCell(2).setCellValue("LevSet");row6.createCell(2).setCellValue(lfmin+" "+lfmax);
			row5.createCell(3).setCellValue("生産関数係数");row6.createCell(3).setCellValue(fi);
			row5.createCell(4).setCellValue("生産関数乗数");row6.createCell(4).setCellValue(beta);
			row5.createCell(5).setCellValue("m");row6.createCell(5).setCellValue(0.21);
			row5.createCell(6).setCellValue("ε");row6.createCell(6).setCellValue(0.04);
			row5.createCell(7).setCellValue("生産コスト係数(自己資本比例型)");row6.createCell(7).setCellValue(f);
			row5.createCell(8).setCellValue("σ");row6.createCell(8).setCellValue(1);
			row5.createCell(9).setCellValue("金利係数(lev比例型)");row6.createCell(9).setCellValue(rr);

			Row row7=sheet1.createRow((short)x+9);
			row7.createCell(0).setCellValue("Learning");
			Row row8=sheet1.createRow((short)x+10);
			Row row9=sheet1.createRow((short)x+11);
			row8.createCell(0).setCellValue("割引率");row9.createCell(0).setCellValue(sm);
			row8.createCell(1).setCellValue("forgatting");row9.createCell(1).setCellValue(h);
			row8.createCell(2).setCellValue("g");row9.createCell(2).setCellValue(0.1);
			row8.createCell(3).setCellValue("Strength");row9.createCell(3).setCellValue(vi);

			Row row10=sheet1.createRow((short)x+12);
			row10.createCell(0).setCellValue("市場環境");
			Row row11=sheet1.createRow((short)x+13);
			Row row12=sheet1.createRow((short)x+14);
			row11.createCell(0).setCellValue("市場金利");row12.createCell(0).setCellValue(sm);
			row11.createCell(1).setCellValue("返済期間");row12.createCell(1).setCellValue(tau);
			row11.createCell(2).setCellValue("ステップ");row12.createCell(2).setCellValue(term);
			row11.createCell(3).setCellValue("市場金利");row12.createCell(3).setCellValue(r);
			row11.createCell(4).setCellValue("預金金利");row12.createCell(4).setCellValue(dr);
			row11.createCell(5).setCellValue("市場性資産収益");row12.createCell(5).setCellValue(liqAr);

			book.write(fileOut);
			fileOut.close();

		}catch(Exception ee) {
			ee.printStackTrace();
		}finally {

		}

	}












	private static void InitBankOutput(ArrayList<OutPut> output, ArrayList<Institution> initBank, int y) {
		// TODO 自動生成されたメソッド・スタブ

		output.get(y).Initbank.addAll(initBank);

	}






	private static void ResultAdjust(ArrayList<FinalOutPut> finaloutput, ArrayList<OutPut> output,int term,int atp,int firmnode) {
		// TODO 自動生成されたメソッド・スタブ

		finaloutput.get(0).Calc(output,firmnode);


	}



	private static void AgentClear(ArrayList<Institution> bank, ArrayList<Enterprise> firm, ArrayList<BrProcess> vand, ArrayList<Institution> initBank) {
		// TODO 自動生成されたメソッド・スタブ
		bank.clear();firm.clear();vand.clear();initBank.clear();
	}

	private static void showAgent(Experiment e1, ArrayList<Enterprise> firm) {
		// TODO 自動生成されたメソッド・スタブ
		Workbook book=null;

		//ある企業エージェントの時系列推移
		int ag=0;//企業エージェントリストindex
		//資産規模順にソート
		e1.setSortFirmK();


		try {
			book = new HSSFWorkbook();
			FileOutputStream fileOut=new FileOutputStream("workbookAgent.xls");
			String safename=WorkbookUtil.createSafeSheetName("[One Firm]");
			Sheet sheet1=book.createSheet(safename);
			CreationHelper createHelper=book.getCreationHelper();
			int x=0;
			Row row0=sheet1.createRow((short)x);
			row0.createCell(0).setCellValue("企業"+e1.getOneFirmId(ag));
			Row row1=sheet1.createRow((short)x+2);
			row1.createCell(0).setCellValue("Leverage");
			Row row3=sheet1.createRow((short)x+8);
			row3.createCell(0).setCellValue("総資産");

			Row row5=sheet1.createRow((short)x+14);
			row5.createCell(0).setCellValue("選択レバレッジ");

			Row row7=sheet1.createRow((short)x+20);
			row7.createCell(0).setCellValue("取引先数");

			Row row9=sheet1.createRow((short)x+26);
			row9.createCell(0).setCellValue("収益");
			Row row11=sheet1.createRow((short)x+32);
			row11.createCell(0).setCellValue("選択レバレッジ値");

			Row row13=sheet1.createRow((short)x+38);
			row13.createCell(0).setCellValue("Reallev(afterCM)");


			int endterm=e1.getOneFirmLived(ag)-1;


			int rowterm=endterm/100+1;
			int term=endterm;
			int xterm=100;
			int a=0,b=xterm-1;

			for(;x<rowterm;x++){
				Row row2=sheet1.createRow((short)(x+3));
				Row row4=sheet1.createRow((short)(x+9));
				Row row6=sheet1.createRow((short)(x+15));
				Row row8=sheet1.createRow((short)(x+21));
				Row row10=sheet1.createRow((short)(x+27));
				Row row12=sheet1.createRow((short)(x+33));
				Row row14=sheet1.createRow((short)(x+39));

				int j=0;
				for(j=a;j<=b;j++){
				row2.createCell(j-(x*xterm)).setCellValue(e1.getOneFirmLev(ag,j));
				row4.createCell(j-(x*xterm)).setCellValue(e1.getOneFirmK(ag,j));
				row6.createCell(j-(x*xterm)).setCellValue(e1.getOneFirmGoalLev(ag,j));
				row8.createCell(j-(x*xterm)).setCellValue(e1.getOneFirmCandNum(ag,j));
				row10.createCell(j-(x*xterm)).setCellValue(e1.getOneFirmPai(ag,j));
				row12.createCell(j-(x*xterm)).setCellValue(e1.getOneFirmLevChoice(ag,j));
				row14.createCell(j-(x*xterm)).setCellValue(e1.getOneFirmRealLev(ag,j));



			    //cell.setCellValue(createHelper.createRichTextString("sample String"));
				}
				/*
				for(j=a;j<e1.getAliveBank();j++) {
					rowx.createCell(j-(x*e1.getAliveBank())).setCellValue(e1.getBankCAR(j));
				}*/
				if(term>100){
				if(term-1>xterm+j){
					if(term-j-1>xterm){
						a=j;b=j+xterm-1;
					}}else{
						a=j;
						b=term-1;
					}

				}
				}
			 book.write(fileOut);



			}catch(Exception e){
			e.printStackTrace();
		}finally{


		}

	}



	private static void OutPutResult(ArrayList<FinalOutPut> finaloutput,int term,int atp,int firmnode,int banknode ) {
		// TODO 自動生成されたメソッド・スタブ

		Workbook book=null;

		try{
			//c:\\User\tkfcb\Onedrive\OutPut\workbook.xls

			book = new HSSFWorkbook();
			String outputPath="C:\\\\User\\\\tkfcb\\\\OneDrive\\\\OutPut\\\\";
			String fileName="workbook.xls";
			FileOutputStream fileOut=new FileOutputStream(fileName);
			String safename=WorkbookUtil.createSafeSheetName("[OutPutResult1]");
			String safename1=WorkbookUtil.createSafeSheetName("[BankResult]");
			String safename2=WorkbookUtil.createSafeSheetName("[FirmResult]");
			String safename3=WorkbookUtil.createSafeSheetName("[InitBank]");
			String safename4=WorkbookUtil.createSafeSheetName("[SortFirmDeg]");
			Sheet sheet1=book.createSheet(safename);
			Sheet sheet2=book.createSheet(safename1);
			Sheet sheet3=book.createSheet(safename2);
			Sheet sheet4=book.createSheet(safename3);
			Sheet sheet5=book.createSheet(safename4);
			CreationHelper createHelper=book.getCreationHelper();

			//100タームごとに改行
			int rowterm=term/100+1;
			int xterm=100;
			int a=0,b=xterm-1;
			//if(e1.getTerm()>100) {xterm=100;a=0;b=xterm-1;}else {xterm=0;a=0;b=e1.getTerm()-1;}
			int t=0;
			Row row = sheet1.createRow((short)t);
			row.createCell(0).setCellValue("総生産");
			Row row2 = sheet1.createRow((short)t+8);
			row2.createCell(0).setCellValue("生産高成長率");
			Row row4 = sheet1.createRow((short)t+17);
			row4.createCell(0).setCellValue("企業破産数");
			Row row6 = sheet1.createRow((short)t+27);
			row6.createCell(0).setCellValue("平均約定金利");
			Row row8 = sheet1.createRow((short)t+36);
			row8.createCell(0).setCellValue("貸出拒否数");
			Row row10 = sheet1.createRow((short)t+45);
			row10.createCell(0).setCellValue("企業レバレッジ");
			Row row12 = sheet1.createRow((short)t+54);
			row12.createCell(0).setCellValue("銀行クライアント数");

			for(int x=0;x<rowterm;x++){

				Row row1 = sheet1.createRow((short)x+1);

				Row row3 = sheet1.createRow((short)x+9);

				Row row5 = sheet1.createRow((short)x+18);

				Row row7 = sheet1.createRow((short)x+28);

				Row row9 = sheet1.createRow((short)x+37);

				Row row11 = sheet1.createRow((short)x+46);

				Row row13 = sheet1.createRow((short)x+55);
				int j=0;
				for(j=a;j<=b;j++){
					row1.createCell(j-(x*xterm)).setCellValue(finaloutput.get(0).Grossproduction.get(j));
					row3.createCell(j-(x*xterm)).setCellValue(finaloutput.get(0).Productgrowthrate.get(j));
					row5.createCell(j-(x*xterm)).setCellValue(finaloutput.get(0).FFnum.get(j));
					row7.createCell(j-(x*xterm)).setCellValue(finaloutput.get(0).Avginterest.get(j));
					row9.createCell(j-(x*xterm)).setCellValue(finaloutput.get(0).Rejectfinancing.get(j));
					row11.createCell(j-(x*xterm)).setCellValue(finaloutput.get(0).Avgfirmlev.get(j));
					row13.createCell(j-(x*xterm)).setCellValue(finaloutput.get(0).Avgbankclientnum.get(j));

				}
				if(term>100){
					if(term-1>xterm+j){
						if(term-j-1>xterm){
							a=j;b=j+xterm-1;
						}}else{
							a=j;
							b=term-1;
						}

					}
			}

			//企業
			int rowfirm=firmnode/100;

			int fn=100;
			int c=0,d=fn-1;
			int u=0;

			//mainbank順に並べたときの標準偏差
			//新しいリストの作成
			ArrayList<FinalOutputFirm> FOF=new ArrayList<FinalOutputFirm>();
			for(int i=0;i<firmnode;i++) {
				FinalOutputFirm A=new FinalOutputFirm(i,finaloutput.get(0).Avgfirmmainbank.get(i),finaloutput.get(0).SDfirmmainbank.get(i));
				FOF.add(A);
			}


			//degree順にソート
			ArrayList<FinalOutputFirm> FOFdeg=new ArrayList<FinalOutputFirm>();
			FOF.stream().sorted((Comparator.comparingDouble(FinalOutputFirm::getAvgDegree)).reversed())
						.forEach(s->FOFdeg.add(s));


			Row row00= sheet3.createRow((short)u);
			row00.createCell(0).setCellValue("総資産");
			Row row02=sheet3.createRow((short)u+15);
			row02.createCell(0).setCellValue("mainbank数");
			Row row04=sheet3.createRow((short)u+30);
			row04.createCell(0).setCellValue("Leverage");
			Row row06=sheet3.createRow((short)u+45);
			row06.createCell(0).setCellValue("mainbank標準偏差");

			Row row08=sheet3.createRow((short)u+60);
			row08.createCell(0).setCellValue("成長率");
			Row rowx1=sheet3.createRow((short)u+75);
			rowx1.createCell(0).setCellValue("SDdeg");
				for(int x=0;x<rowfirm;x++){
				/*
					Row row0= sheet3.createRow((short)x);
					row0.createCell(0).setCellValue("総資産");
					*/
					Row row1f = sheet3.createRow((short)x+1);//行
					Row row3f = sheet3.createRow((short)x+16);
					Row row5f = sheet3.createRow((short)x+31);
					Row row7f = sheet3.createRow((short)x+46);
					Row row9f = sheet3.createRow((short)x+61);
					Row row1g = sheet3.createRow((short)x+76);

				int j=0;
				for(j=c;j<=d;j++){
					//System.out.print(d);
					row1f.createCell(j-(x*fn)).setCellValue(finaloutput.get(0).AvgfirmK.get(j));
					row3f.createCell(j-(x*fn)).setCellValue(finaloutput.get(0).Avgfirmmainbank.get(j));
					row5f.createCell(j-(x*fn)).setCellValue(finaloutput.get(0).Avgfirmleverage.get(j));
					row7f.createCell(j-(x*fn)).setCellValue(finaloutput.get(0).SDfirmmainbank.get(j));
					row9f.createCell(j-(x*fn)).setCellValue(finaloutput.get(0).Avgfirmgrowthrate.get(j));
					//row9f.createCell(j-(x*fn)).setCellValue(FOFdeg.get(j).getAvgDegree());
					//row1g.createCell(j-(x*fn)).setCellValue(FOFdeg.get(j).getSDDegree());
					//row7.createCell(j-(x*fn)).setCellValue(e1.getFirmnextLeverage(j));
				}

				if(firmnode-1>fn+j){
					if(firmnode-j-1>fn){
						c=j;d=j+fn-1;
					}}else{
						c=j;
						d=firmnode-1;
					}
			}






			//各試行ごとの最大値
				/*
				Row row0x= sheet3.createRow((short)u+60);
				row0x.createCell(0).setCellValue("max取引先");
				for(int i=0;i<atp;i++) {
					Row row0y = sheet3.createRow((short)u+61);//行
					System.out.println(finaloutput.get(0).MaxMainbankNum.get(i));
					row0y.createCell(i).setCellValue(finaloutput.get(0).MaxMainbankNum.get(i));
				}
				*/

				//銀行


				for(int x=0;x<1;x++) {
				Row row0b=sheet2.createRow((short)x);
				Row row1b=sheet2.createRow((short)x+2);
				row1b.createCell(0).setCellValue("CAR");

				Row row2b=sheet2.createRow((short)x+3);
				Row row3b=sheet2.createRow((short)x+4);
				row3b.createCell(0).setCellValue("企業への貸付:IBloan:IBborrow");
				Row row4b=sheet2.createRow((short)x+5);
				Row row5b=sheet2.createRow((short)x+6);
				Row row6b=sheet2.createRow((short)x+7);

				Row row7b=sheet2.createRow((short)x+8);
				row7b.createCell(0).setCellValue("銀行資産規模K");
				Row row8b=sheet2.createRow((short)x+9);
				Row row9b=sheet2.createRow((short)x+10);

				row9b.createCell(0).setCellValue("銀行buffer");
				Row row10b=sheet2.createRow((short)x+11);
				Row row11b=sheet2.createRow((short)x+12);
				row11b.createCell(0).setCellValue("銀行総badloan");
				Row row12b=sheet2.createRow((short)x+13);
				Row row13b=sheet2.createRow((short)x+14);
				row13b.createCell(0).setCellValue("銀行residual");
				Row row14b=sheet2.createRow((short)x+15);
				Row row15b=sheet2.createRow((short)x+16);
				row15b.createCell(0).setCellValue("銀行総LFlistsize");
				Row row16b=sheet2.createRow((short)x+17);
				Row row17b=sheet2.createRow((short)x+18);
				row17b.createCell(0).setCellValue("銀行市場性資産");
				Row row18b=sheet2.createRow((short)x+19);
				Row row19b=sheet2.createRow((short)x+20);
				row19b.createCell(0).setCellValue("ClientNumber");
				Row row20b=sheet2.createRow((short)x+21);


				for(int i=0;i<banknode;i++) {
				row0b.createCell(i).setCellValue(i+1);

				//row2.createCell(i).setCellValue(e1.getBankCAR(i));
				row4b.createCell(i).setCellValue(finaloutput.get(0).Avgbanklf.get(i));
				//row5.createCell(i).setCellValue(e1.getBankLb(i));
				//row6.createCell(i).setCellValue(e1.getBankBb(i));

				row8b.createCell(i).setCellValue(finaloutput.get(0).Avgbanktotalassets.get(i));

				//row10.createCell(i).setCellValue(e1.getBankBuffer(i));
				//row12.createCell(i).setCellValue(e1.getBankGrossBadLoan(i));
				row14b.createCell(i).setCellValue(finaloutput.get(0).Avgbankresidual.get(i));
				//row16.createCell(i).setCellValue(e1.getBankLFSize(i));
				//row18.createCell(i).setCellValue(e1.getBankLiqAsset(i));
				row20b.createCell(i).setCellValue(finaloutput.get(0).Avgbankcnum.get(i));

				}
				/*
				for(int i=0;i<bank.get(0).levlist.size();i++) {
					row18.createCell(i).setCellValue(e1.getBanklevlevelvalue(i));
				}
				*/
				}

				//初期銀行

				for(int x=0;x<1;x++) {
					Row row0bb=sheet4.createRow((short)x);
					Row row1bb=sheet4.createRow((short)x+8);
					row1bb.createCell(0).setCellValue("銀行資産規模K");
					Row row2bb=sheet4.createRow((short)x+9);


					for(int i=0;i<banknode;i++) {
						row0bb.createCell(i).setCellValue(i+1);
						row2bb.createCell(i).setCellValue(finaloutput.get(0).AvgbankinitK.get(i));
					}

				}


			book.write(fileOut);
			fileOut.close();

		}catch(Exception e){
			e.printStackTrace();
		}finally{


		}

	}






	private static void getValue(Experiment e1) {
		// TODO 自動生成されたメソッド・スタブ


		ArrayList<Double> K=new ArrayList<Double>();
		ArrayList<Double> Badloan=new ArrayList<Double>();
		ArrayList<Double> Residual=new ArrayList<Double>();
		ArrayList<Double> Buffer=new ArrayList<Double>();
		ArrayList<Double> L=new ArrayList<Double>();

	//	kk+=e1.getBankK();
	}

	private static void showInitExcel(ArrayList<Institution> bank, ArrayList<Enterprise> firm) {

		Workbook book=null;

		try{
			book = new HSSFWorkbook();
			FileOutputStream fileOut=new FileOutputStream("workbook1.xls");
			String safename=WorkbookUtil.createSafeSheetName("[InitBank]");
			String safename1=WorkbookUtil.createSafeSheetName("[InitFirm]");
			Sheet sheet1=book.createSheet(safename);
			Sheet sheet2=book.createSheet(safename1);
			CreationHelper createHelper=book.getCreationHelper();

			//銀行
			int x=0;

			Row row0=sheet1.createRow((short)x);
			Row row1=sheet1.createRow((short)x+2);
			row1.createCell(0).setCellValue("CAR");
			Row row2=sheet1.createRow((short)x+3);
			Row row3=sheet1.createRow((short)x+4);
			row3.createCell(0).setCellValue("総資産");
			Row row4=sheet1.createRow((short)x+5);
			Row row5=sheet1.createRow((short)x+6);
			row5.createCell(0).setCellValue("Buffer");
			Row row6=sheet1.createRow((short)x+7);
			Row row7=sheet1.createRow((short)x+8);
			row7.createCell(0).setCellValue("初期Candidate");
			Row row8=sheet1.createRow((short)x+9);

			for(Institution b:bank) {
				row0.createCell(b.id).setCellValue(b.id);
				row2.createCell(b.id).setCellValue(b.CAR);
				row4.createCell(b.id).setCellValue(b.totalassets);
				row6.createCell(b.id).setCellValue(b.buffer);
				row8.createCell(b.id).setCellValue(b.initclientnum);
			}

			//企業
			int rowfirm=firm.size()/100;
			int firmnode=firm.size();
			int fn=100;
			int c=0,d=fn-1;
			int t=0;


			Row row11=sheet2.createRow((short)t);
			row11.createCell(0).setCellValue("初期取引先");
			//Row row12=sheet2.createRow((short)t+15);
			//row12.createCell(0).setCellValue("CAR");

			for(int y=0;y<rowfirm;y++){
				/*
					Row row0= sheet3.createRow((short)x);
					row0.createCell(0).setCellValue("総資産");
					*/
					Row rowf = sheet2.createRow((short)y+1);//行
					//Row rowf1 = sheet2.createRow((short)y+16);
					//Row rowf2 = sheet2.createRow((short)y+31);
					//Row rowf3 = sheet2.createRow((short)y+46);
				int j=0;
				for(j=c;j<=d;j++){
					//System.out.print(d);
					rowf.createCell(j-(y*fn)).setCellValue(firm.get(j).initbank);
				}

				if(firmnode-1>fn+j){
					if(firmnode-j-1>fn){
						c=j;d=j+fn-1;
					}}else{
						c=j;
						d=firmnode-1;
					}
			}


			book.write(fileOut);

		}catch(Exception e){
			e.printStackTrace();
		}finally{


		}
	}



	private static void showMultiExcel(Experiment e1, double kk, double car, double badloan, double residual, double l, int atp) {
		// TODO 自動生成されたメソッド・スタブ


		final String INPUT_DIR="D:/Users/atsushi/Desktop/実験結果/";
		Workbook book=null;

		try{
			book = new HSSFWorkbook();
			FileOutputStream fileOut=new FileOutputStream("workbook1.xls");
			String safename=WorkbookUtil.createSafeSheetName("[OutPut+Firm]");
			String safename1=WorkbookUtil.createSafeSheetName("[Bank]");
			Sheet sheet1=book.createSheet(safename);
			Sheet sheet2=book.createSheet(safename1);
			CreationHelper createHelper=book.getCreationHelper();

			int x=1;

			Row row1=sheet2.createRow((short)x+2);
			row1.createCell(0).setCellValue("CAR");
			Row row2=sheet2.createRow((short)x+3);
			row2.createCell(0).setCellValue(car/atp);
			Row row3=sheet2.createRow((short)x+4);
			row3.createCell(0).setCellValue("企業への貸付");
			Row row4=sheet2.createRow((short)x+5);
			row4.createCell(0).setCellValue(l/atp);
			Row row5=sheet2.createRow((short)x+5);
			row3.createCell(0).setCellValue("badloan");
			//Row row5=sheet2.createRow((short)x+5);


		}catch(Exception e){
			e.printStackTrace();
		}finally{


		}

	}



	private static void showExcel(Experiment e1, ArrayList<Institution> bank) {
		// TODO Auto-generated method stub



		final String INPUT_DIR="D:/Users/atsushi/Desktop/実験結果/";
		Workbook book=null;

		try{
			book = new HSSFWorkbook();
			FileOutputStream fileOut=new FileOutputStream("workbook.xls");
			String safename=WorkbookUtil.createSafeSheetName("[OutPut]");
			String safename1=WorkbookUtil.createSafeSheetName("[Bank]");
			String safename2=WorkbookUtil.createSafeSheetName("[Firm]");
			Sheet sheet1=book.createSheet(safename);
			Sheet sheet2=book.createSheet(safename1);
			Sheet sheet3=book.createSheet(safename2);
			CreationHelper createHelper=book.getCreationHelper();

			//100タームごとに改行
			int rowterm=e1.getTerm()/100+1;
			int term=e1.getTerm();
			int xterm=100;
			int a=0,b=xterm-1;
			//if(e1.getTerm()>100) {xterm=100;a=0;b=xterm-1;}else {xterm=0;a=0;b=e1.getTerm()-1;}
			for(int x=0;x<rowterm;x++){
			Row row = sheet1.createRow((short)x);//行
			Row row1=sheet1.createRow((short)(x+rowterm+2));
			Row row2=sheet1.createRow((short)(x+rowterm*2+2));
			Row row3=sheet1.createRow((short)(x+rowterm*3+2));
			Row row4=sheet1.createRow((short)(x+rowterm*4+2));
			Row row5=sheet1.createRow((short)(x+rowterm*5+2));
			Row row6=sheet1.createRow((short)(x+rowterm*6+2));
			Row row7=sheet1.createRow((short)(x+rowterm*7+2));
			Row row8=sheet1.createRow((short)(x+rowterm*8+2));
			Row row9=sheet1.createRow((short)(x+rowterm*9+2));
			Row row10=sheet1.createRow((short)(x+rowterm*10+2));
			Row row11=sheet1.createRow((short)(x+rowterm*11+2));
			Row row12=sheet1.createRow((short)(x+rowterm*12+2));
			Row row13=sheet1.createRow((short)(x+rowterm*11+2));

			int j=0;
			for(j=a;j<=b;j++){
			row.createCell(j-(x*xterm)).setCellValue(e1.getAgrregateOP(j));
			row1.createCell(j-(x*xterm)).setCellValue(e1.getGrowthRateOP(j));
			row2.createCell(j-(x*xterm)).setCellValue(e1.getFailedFirm(j));
			row3.createCell(j-(x*xterm)).setCellValue(e1.getFailedFirmrate(j));
			row4.createCell(j-(x*xterm)).setCellValue(e1.getBadLoan(j));
			row5.createCell(j-(x*xterm)).setCellValue(e1.getAVGInterest(j));
			row6.createCell(j-(x*xterm)).setCellValue(e1.getMoneyStock(j));
			row7.createCell(j-(x*xterm)).setCellValue(e1.getCreditMoney(j));
			row8.createCell(j-(x*xterm)).setCellValue(e1.getResidual(j));
			row9.createCell(j-(x*xterm)).setCellValue(e1.getGrossDemand(j));
			row10.createCell(j-(x*xterm)).setCellValue(e1.getGrossSupply(j));
			row11.createCell(j-(x*xterm)).setCellValue(e1.getRejectFinancing(j));


		    //cell.setCellValue(createHelper.createRichTextString("sample String"));
			}
			/*
			for(j=a;j<e1.getAliveBank();j++) {
				rowx.createCell(j-(x*e1.getAliveBank())).setCellValue(e1.getBankCAR(j));
			}*/
			if(term>100){
			if(term-1>xterm+j){
				if(term-j-1>xterm){
					a=j;b=j+xterm-1;
				}}else{
					a=j;
					b=term-1;
				}

			}
			}

			//企業list
			e1.setSortFirmK();
			int rowfirm=e1.getalivefirm()/100;
			int firmnode=e1.getalivefirm();
			int fn=100;
			int c=0,d=fn-1;
			int t=0;

			Row row00= sheet3.createRow((short)t);
			row00.createCell(0).setCellValue("総資産");
			Row row02=sheet3.createRow((short)t+15);
			row02.createCell(0).setCellValue("取引先");
			Row row04=sheet3.createRow((short)t+30);
			row04.createCell(0).setCellValue("Leverage");
			Row row06=sheet3.createRow((short)t+45);
			row06.createCell(0).setCellValue("ChoiveLeverage");
			Row row08=sheet3.createRow((short)t+60);
			row08.createCell(0).setCellValue("RejectOrder");
			Row row010=sheet3.createRow((short)t+75);
			row010.createCell(0).setCellValue("NonBuffer");
				for(int x=0;x<rowfirm;x++){
				/*
					Row row0= sheet3.createRow((short)x);
					row0.createCell(0).setCellValue("総資産");
					*/
					Row row1 = sheet3.createRow((short)x+1);//行
					Row row3 = sheet3.createRow((short)x+16);
					Row row5 = sheet3.createRow((short)x+31);
					Row row7 = sheet3.createRow((short)x+46);
					Row row9 =sheet3.createRow((short)x+61);
					Row row11 =sheet3.createRow((short)x+76);
				int j=0;
				for(j=c;j<=d;j++){
					//System.out.print(d);
					row1.createCell(j-(x*fn)).setCellValue(e1.getFirmK(j));
					row3.createCell(j-(x*fn)).setCellValue(e1.getFirmCandidateNum(j));
					row5.createCell(j-(x*fn)).setCellValue(e1.getFirmLeverage(j));
					row7.createCell(j-(x*fn)).setCellValue(e1.getFirmnextLeverage(j));
					row9.createCell(j-(x*fn)).setCellValue(e1.getFirmRejectOrderNum(j));
					row11.createCell(j-(x*fn)).setCellValue(e1.getFirmNonBufferNum(j));
				}

				if(firmnode-1>fn+j){
					if(firmnode-j-1>fn){
						c=j;d=j+fn-1;
					}}else{
						c=j;
						d=firmnode-1;
					}
			}
				/*
				if(rowfirm==0) {
					int x=0;
					Row row0= sheet3.createRow((short)x);
					row0.createCell(0).setCellValue("総資産");
					Row row1 = sheet3.createRow((short)x);//行
					Row row2=sheet3.createRow((short)x+15);
					row2.createCell(0).setCellValue("取引先");
					Row row3 = sheet3.createRow((short)x);
					Row row4=sheet3.createRow((short)x+30);
					row4.createCell(0).setCellValue("Leverate");
					Row row5 = sheet3.createRow((short)x);
					Row row6=sheet3.createRow((short)x+45);
					row6.createCell(0).setCellValue("ChoiveLeverage");
					Row row7 = sheet3.createRow((short)x);
					//Row row8 = sheet3.createRow((short)x);
					//Row row9=sheet3.createRow((short)x+20);
				for(int i=0;i<firmnode;i++) {
					row1.createCell(i).setCellValue(e1.getFirmK(i));
					row3.createCell(i).setCellValue(e1.getFirmCandidateNum(i));
					row5.createCell(i).setCellValue(e1.getFirmLeverage(i));
					row7.createCell(i).setCellValue(e1.getFirmnextLeverage(i));
					//row4.createCell(i).setCellValue(e1.getFirmLevNowEvalue(i));
				}
				}
*/

			//銀行リスト

			int banknode=e1.getAliveBank();

			for(int x=0;x<1;x++) {
			Row row0=sheet2.createRow((short)x);
			Row row1=sheet2.createRow((short)x+2);
			row1.createCell(0).setCellValue("CAR");
			Row row2=sheet2.createRow((short)x+3);
			Row row3=sheet2.createRow((short)x+4);
			row3.createCell(0).setCellValue("企業への貸付:IBloan:IBborrow");
			Row row4=sheet2.createRow((short)x+5);
			Row row5=sheet2.createRow((short)x+6);
			Row row6=sheet2.createRow((short)x+7);
			Row row7=sheet2.createRow((short)x+8);
			row7.createCell(0).setCellValue("銀行資産規模K");
			Row row8=sheet2.createRow((short)x+9);
			Row row9=sheet2.createRow((short)x+10);
			row9.createCell(0).setCellValue("銀行buffer");
			Row row10=sheet2.createRow((short)x+11);
			Row row11=sheet2.createRow((short)x+12);
			row11.createCell(0).setCellValue("銀行総badloan");
			Row row12=sheet2.createRow((short)x+13);
			Row row13=sheet2.createRow((short)x+14);
			row13.createCell(0).setCellValue("銀行residual");
			Row row14=sheet2.createRow((short)x+15);
			Row row15=sheet2.createRow((short)x+16);
			row15.createCell(0).setCellValue("銀行総LFlistsize");
			Row row16=sheet2.createRow((short)x+17);
			Row row17=sheet2.createRow((short)x+18);
			row17.createCell(0).setCellValue("銀行市場性資産");
			Row row18=sheet2.createRow((short)x+19);
			Row row19=sheet2.createRow((short)x+20);
			row19.createCell(0).setCellValue("ClientNumber");
			Row row20=sheet2.createRow((short)x+21);

			for(int i=0;i<banknode;i++) {
			row0.createCell(i).setCellValue(e1.getBankId(i));
			row2.createCell(i).setCellValue(e1.getBankCAR(i));
			row4.createCell(i).setCellValue(e1.getBankLf(i));
			row5.createCell(i).setCellValue(e1.getBankLb(i));
			row6.createCell(i).setCellValue(e1.getBankBb(i));
			row8.createCell(i).setCellValue(e1.getBankK(i));
			row10.createCell(i).setCellValue(e1.getBankBuffer(i));
			row12.createCell(i).setCellValue(e1.getBankGrossBadLoan(i));
			row14.createCell(i).setCellValue(e1.getBankResidual(i));
			row16.createCell(i).setCellValue(e1.getBankLFSize(i));
			row18.createCell(i).setCellValue(e1.getBankLiqAsset(i));
			row20.createCell(i).setCellValue(e1.getBankClientNum(i));
			}
			/*
			for(int i=0;i<bank.get(0).levlist.size();i++) {
				row18.createCell(i).setCellValue(e1.getBanklevlevelvalue(i));
			}
			*/





			}
		    book.write(fileOut);
		    //fileOut.close();

		    // 1つ目のセルを作成 ※行と同じく、0からスタート
		    //Cell a1 = row.createCell(0);  // Excel上、「A1」の場所

		}catch(Exception e){
			e.printStackTrace();
		}finally{


		}


	}

}
