import java.util.ArrayList;

public class FinalOutPut {

	/*
	 * タームごと各試行の平均、標準偏差の格納リスト
	 * list.get(x)⇒各試行ごとの平均・・・
	 */

	private int atp,term;
	private int banknode,firmnode;

	public FinalOutPut(int i, int term) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.atp=i;
		this.term=term;
	}
	
	/*
	 * ToDo
	 * それぞれのmax,minの値を出力したい
	 * 標準偏差も
	 */
	
	//最大値格納
	ArrayList<Integer> MaxMainbankNum=new ArrayList<Integer>();

	//タームごと
	//平均格納
	ArrayList<Double> Grossproduction=new ArrayList<Double>();
	ArrayList<Double> Productgrowthrate=new ArrayList<Double>();
	ArrayList<Double> FFnum=new ArrayList<Double>();
	ArrayList<Double> Avginterest=new ArrayList<Double>();
	ArrayList<Double> Rejectfinancing=new ArrayList<Double>();

	//企業
	ArrayList<Double> Avgfirmlev=new ArrayList<Double>();
	

	//銀行
	ArrayList<Double> Avgbankclientnum=new ArrayList<Double>();

	//エージェントごと
	ArrayList<Double> Avgbanktotalassets=new ArrayList<Double>();
	ArrayList<Double> Avgbanklf=new ArrayList<Double>();
	ArrayList<Double> Avgbankcnum=new ArrayList<Double>();
	ArrayList<Double> Avgbankresidual=new ArrayList<Double>();
	
	ArrayList<Double> AvgbankinitK=new ArrayList<Double>();

	ArrayList<Double> AvgfirmK=new ArrayList<Double>();
	ArrayList<Double> Avgfirmmainbank=new ArrayList<Double>();
	ArrayList<Double> Avgfirmleverage=new ArrayList<Double>();
	ArrayList<Double> Avgfirmgrowthrate=new ArrayList<Double>();
	
	
	ArrayList<Double> SDfirmmainbank=new ArrayList<Double>();
 
	ArrayList<Institution> Finalbank=new ArrayList<Institution>();
	ArrayList<Enterprise> Finalfirm=new ArrayList<Enterprise>();

	public void Calc(ArrayList<OutPut> output,int firmnode) {
		// TODO 自動生成されたメソッド・スタブ


		ArrayList<Double> Calc=new ArrayList<Double>();
		double a=0,b=0,c=0,d=0,e=0,f=0,g=0;

		double aa=0,bb=0,cc=0,dd=0,ee=0,ff=0;
		double fa=0,fb=0,fc=0,fd=0,fe=0;
		
	


		//タームごと
		for(int i=0;i<term;i++) {
			for(OutPut op:output) {//各試行

			a+=op.Grossproduction.get(i);
			b+=op.Productgrowthrate.get(i);
			c+=op.FFnum.get(i);
			d+=op.Avginterest.get(i);
			e+=op.Rejectfinancing.get(i);
			f+=op.Avgfirmlev.get(i);
			g+=op.Avgbankclientnum.get(i);
		}
			Grossproduction.add(a/atp);
			Productgrowthrate.add(b/atp);
			FFnum.add(c/atp);
			Avginterest.add(d/atp);
			Rejectfinancing.add(e/atp);
			Avgfirmlev.add(f/atp);
			Avgbankclientnum.add(g/atp);
			a=0;b=0;c=0;d=0;e=0;f=0;g=0;
		}

		//エージェントごと
		int count=0;
		for(OutPut op:output) {
			int maxmb=0;
			int mb=0;
			if(count==0) {
			for(Institution bank:op.Finalbank) {
				Avgbanktotalassets.add(bank.totalassets/atp);
				Avgbanklf.add(bank.lf/atp);
				Avgbankcnum.add((double) bank.clientnum/atp);
				Avgbankresidual.add( bank.residual/atp);
			}
			
			for(Institution initbank:op.Initbank) {
				AvgbankinitK.add(initbank.totalassets/atp);
			}
			for(Enterprise firm:op.Finalfirm) {
				AvgfirmK.add(firm.K/atp);
				Avgfirmmainbank.add((double)firm.mainbanknum/atp);
				Avgfirmleverage.add(firm.lev/atp);
				Avgfirmgrowthrate.add(firm.gr/atp);
				
				
				
				//maxの取得
				mb=firm.mainbanknum;
				if(mb>maxmb) maxmb=mb;
			}
			MaxMainbankNum.add(maxmb);
			}else {
				for(int i=0;i<op.Finalbank.size();i++) {
					double num=Avgbanktotalassets.get(i);
					double num1=Avgbanklf.get(i);
					double num2=Avgbankcnum.get(i);
					double num3=Avgbankresidual.get(i);
					num+=op.Finalbank.get(i).totalassets/atp;
					num1+=op.Finalbank.get(i).lf/atp;
					num2+=(double)op.Finalbank.get(i).clientnum/atp;
					num3+=op.Finalbank.get(i).residual/atp;
					Avgbanktotalassets.set(i,num);
					Avgbanklf.set(i,num1);
					Avgbankcnum.set(i, num2);                                   
					Avgbankresidual.set(i, num3);                                   
				}
				for(int i=0;i<op.Initbank.size();i++) {
					double num=AvgbankinitK.get(i);
					num+=op.Initbank.get(i).totalassets/atp;
					AvgbankinitK.set(i, num);
				}
				for(int i=0;i<op.Finalfirm.size();i++) {
					double num=AvgfirmK.get(i);
					double num1=Avgfirmmainbank.get(i);
					double num2=Avgfirmleverage.get(i);
					double num3=Avgfirmgrowthrate.get(i);
					num+=op.Finalfirm.get(i).K/atp;
					num1+=(double)op.Finalfirm.get(i).mainbanknum/atp;
					num2+=op.Finalfirm.get(i).lev/atp;
					num3+=op.Finalfirm.get(i).gr/atp;
					AvgfirmK.set(i,num);
					Avgfirmmainbank.set(i,num1);
					Avgfirmleverage.set(i,num2);
					Avgfirmgrowthrate.set(i,num3);
					//maxの取得
					mb=op.Finalfirm.get(i).mainbanknum;
					if(mb>maxmb) maxmb=mb;
				}
				MaxMainbankNum.add(maxmb);//一試行ごとのmainbank最大値の格納
				}
			count++;
			}
			
		//標準偏差
		for(int i=0;i<firmnode;i++) {
			double x=0;
			for(int j=0;j<atp;j++) {
				
				x+=Math.pow(output.get(j).Finalfirm.get(i).mainbanknum-Avgfirmmainbank.get(i),2);
			}
			SDfirmmainbank.add(Math.sqrt((double)x/atp));
		}
		
		
		

		/*
		for(int i=0;i<Avgbanktotalassets.size();i++) {
			double num=Avgbanktotalassets.get(i);
			
			Avgbanktotalassets.set(i,num/atp);
			
		}

		for(int i=0;i<AvgfirmK.size();i++) {
			double num=AvgfirmK.get(i);
			AvgfirmK.set(i, num/atp);
		}
		*/


		}






}
