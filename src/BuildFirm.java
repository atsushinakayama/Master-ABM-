import java.util.ArrayList;

public class BuildFirm {

	private int firmnode;
	private int banknode;
	private double k0;
	private double fCAR;
	private int initbank;
	private double f;//固定費率

	private double lfmin,lfmax,val,vi;

	ArrayList<Enterprise> Firm=new ArrayList<Enterprise>();

	//銀行接続性規模ごと
	private double sumK;


	public BuildFirm(int firmnode,double k02,int initbank,int banknode,double f,double lfmin,double lfmax,double val,double vi){
		this.firmnode=firmnode;
		this.k0=k02;
		this.initbank=initbank;
		this.banknode=banknode;
		this.f=f;
		this.lfmin=lfmin;this.lfmax=lfmax;this.val=val;this.vi=vi;
	}

	public BuildFirm(int firmnode, double k0, double fCAR,int initbank,int banknode) {
		// TODO Auto-generated constructor stub
		this.firmnode=firmnode;
		this.fCAR=fCAR;
		this.k0=k0;
		this.initbank=initbank;
		this.banknode=banknode;
	}



	public void isBuild() {
		// TODO Auto-generated method stub

		for(int i=0;i<firmnode;i++){
			Enterprise F=new Enterprise(f,i,k0);
			Firm.add(F);
		}

		for(int i=0;i<firmnode;i++){
			Firm.get(i).state="Alive";
		}

		//leverageListの作成
		LevListCreate();

		//初期金融機関取引先
		InitCandidateBank();


	}



	private void LevListCreate() {
		// TODO 自動生成されたメソッド・スタブ
		String state="No";
		String range="No";
		Firm.stream().forEach(s->s.setLevList(lfmin,lfmax,val,state,range,vi));

	}

	public void isBuild1() {
		// TODO Auto-generated method stub

		for(int i=0;i<firmnode;i++){
			Enterprise F=new Enterprise(i,k0,fCAR);
			Firm.add(F);
			Firm.get(i).state="Alive";
		}

		InitCandidateBank();
	}


	private void InitCandidateBank() {
		// TODO 自動生成されたメソッド・スタブ

		String state="Old";//リレーション先
		String tradestate="No";//取引を行っているか否か


		//1金融機関につきxの企業
		int x=firmnode/banknode;
		int y=x;
		int num=0;
		int j=0;
		for(int t=0;t<banknode;t++) {
		for(int i=j;i<y;i++) {
			Firm.get(i).addCandidate(t+1,0,state,tradestate);
			Firm.get(i).Candidatelistid.add(t+1);
			//System.out.println("firm"+Firm.get(i).id+" "+Firm.get(i).Candidatelist.get(0).id);
		}j=y;y+=x;if(j==firmnode) break;
		}

		//initialcandiddate
		/*
		ArrayList<Integer> list=new ArrayList<Integer>();//銀行idリストの作成
		for(int i=0;i<banknode;i++) list.add(i+1);
		for(int i=0;i<firmnode;i++){
			Collections.shuffle(list);
			for(int j=0;j<initbank;j++){
				Firm.get(i).addCandidate(list.get(j),0,state,tradestate);
				Firm.get(i).Candidatelistid.add(list.get(j));
			}
		}
*/
	}

	public void BankConnectDiversity(ArrayList<Institution> bank) {
		// TODO 自動生成されたメソッド・スタブ
		/*
		 * 銀行の規模ごとに企業初期Candidateの割り当て
		 *
		 */

		Firm.stream().forEach(s->{s.Candidatelist.clear();s.Candidatelistid.clear();});
		

		bank.stream().forEach(s->sumK+=s.totalassets);
		bank.stream().forEach(s->s.initclientnum=(int)((s.totalassets/sumK)*firmnode));

		int j=0;
		int yy=0;
		for(Institution b:bank) {
		
			int y=b.initclientnum;
			yy+=y;
			for(int i=j;i<yy;i++) {
				if(i==firmnode) break;
				Firm.get(i).addCandidate(b.id,0,"Old","No");
				Firm.get(i).Candidatelistid.add(b.id);
				Firm.get(i).initbank=b.id;
			}j=yy;
		}

		for(Enterprise f:Firm) {
			if(f.Candidatelist.size()==0) {
				int initbank=(int)(bank.size()*Math.random());
				f.addCandidate( initbank, 0, "Old", "No");
				f.Candidatelistid.add(initbank);
			}
		}


	}




	public ArrayList<Enterprise> getFinalFirm() {
		// TODO Auto-generated method stub
		return Firm;
	}






}
