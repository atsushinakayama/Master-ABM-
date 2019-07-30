

import java.util.ArrayList;

public class Network{
	
	private Matrix matrix;
	private int order;

	//コンストラクタ
	public Network(int order){
		matrix=new Matrix(order,order,false);//マトリクス作成，行列(i,j)
		this.order=order;
		}


	public Network() {
		// TODO 自動生成されたコンストラクター・スタブ
	}


	public void createBA(){//BAグラフの作成（完全グラフ⇒BAグラフ)

		Network complete = new Network(order);//ノード数m(order=150)の完全グラフ
		complete.createComplete();//Order×Order行列作成(BitSety⇒true)
		
		//完全グラフの作成
		for(int i=0;i<complete.order;i++)
			for(int j=0;j<complete.order;j++)
				matrix.set(i, j, (boolean)complete.matrix.get(i,j));
			
		
		
		//重み付きグラフの作成(why Institution class?)
		ArrayList<Institution> FWL=new ArrayList<Institution>();
		double tau1[]=new double[order];
		double tau2[]=new double[order];
		double lambda1=0.8;
		double lambda2=0.8;
		
		
		for(int i=0;i<order;i++){
			tau2[i]=-Math.log(1-Math.random())/lambda1;
			tau1[i]=-Math.log(1-Math.random())/lambda2;;
		}
		java.util.Arrays.sort(tau1);	//昇順で重みを作成
		java.util.Arrays.sort(tau2);
		
		for(int i=0;i<order;i++){
			Institution FW=new Institution(i,1,1);//便宜的にindeg,outdegは１としている
			FW.w1=tau1[order-1-i];
			FW.w2=tau2[order-1-i];
			FWL.add(FW);
		}
		
		double theta=4;//閾値
		//重みの和が閾値より小さいなら線を消す
		for(int i=0;i<FWL.size();i++){
			for(int j=0;j<FWL.size();j++){
				if(i!=j&&FWL.get(i).w1+FWL.get(j).w2<theta){
					matrix.set(i, j,false);
				}
			}
		}
		
		//ランダムに枝を追加
		for(int i=0;i<order;i++){
			if(getIndegree(i)==0)
				randomInset(i);
			if(getOutdegree(i)==0)
				randomOutset(i);
			}
		}
		
		
	public void createCM() {
		// TODO Auto-generated method stub
		Network complete = new Network(order);//ノード数m(order=150)の完全グラフ
		complete.createComplete();
		for(int i=0;i<complete.order;i++)
			for(int j=0;j<complete.order;j++)
				matrix.set(i, j, (boolean)complete.matrix.get(i,j));
		show();
	}
	
		
	


	public void createComplete() {
		// TODO 自動生成されたメソッド・スタブ
		createregular(order/2);

	}


	public void createregular(int k) {//
		// TODO 自動生成されたメソッド・スタブ
		createCircle();

		for(int i=2;i<=k;i++){
			for(int j=0;j<order;j++){
				this.matrix.set(j,(j+i)%order);
				this.matrix.set((j+i)%order, j);
			}
		}
	}


	public void createCircle() {
		// TODO 自動生成されたメソッド・スタブ
		clear();//すべての要素の解除→Matrix.set

		int i=0;
		for(;i<order-1;i++){
			this.matrix.set(i, i+1);//
			this.matrix.set(i+1, i);
		}
		this.matrix.set(i, 0);       
		this.matrix.set(0, i);     

	}


	public void randomInset(int i) {//入字数をランダムな所に接続
		// TODO Auto-generated method stub
		int j=0;
		do{
			j=(int)(Math.random()*order);
		}while(i!=j);
		matrix.set(j,i,true);		
		}
	

	public void randomOutset(int i) {
		// TODO Auto-generated method stub
		int j=0;
		do{
			j=(int)(Math.random()*order);
		}while(i!=j);
		matrix.set(i,j,true);		
		}

	public int getIndegree(int node) {//ノードの出自数を返す
		// TODO Auto-generated method stub
		return matrix.cardinality2(node);
	}

	public int getOutdegree(int node) {//nodeの出次数を返す（行）
		// TODO Auto-generated method stub
		return matrix.cardinality(node);
	}
//////////////////////////////////////////////////////////////////////

///////////////////金融機関バランスシートの構成////////////////////////////
	
	public ArrayList<Integer> getInid(int node) {//入ってきている金融機関のid配列
		// TODO Auto-generated method stub
		return matrix.setInid(node);
	}

	public ArrayList<Integer> getOutid(int node) {
		// TODO Auto-generated method stub
		return matrix.setOutid(node);
	}
	
	
	public void clear() {
		// TODO 自動生成されたメソッド・スタブ
		matrix.clear();
}

	public void show() {
		// TODO Auto-generated method stub
		matrix.show();
	}


	

	

}
