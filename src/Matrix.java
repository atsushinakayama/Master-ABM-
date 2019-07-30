

import java.util.ArrayList;
import java.util.BitSet;

public class Matrix{

	private BitSet set;
	private double[] dblArray;
	private int rowSize;//行サイズ
	private int columnSize;//列サイズ
	private final String TYPE;

	public Matrix(int row,int column,boolean bl){
			if(bl){
				TYPE = "dblarray";
				dblArray = new double[row*column];
				set = null;
			}else{
				TYPE = "bitset";
				set = new BitSet();
				dblArray = null;
			}
			rowSize = row;
			columnSize = column;
		}



	public void clear(){
		set.clear();
	}

	public void set(int row,int column){
		set.set(row*columnSize + column);//指定されたインデックスのビットをtrueに設定
	}

	public void set(int row,int column,boolean bl){
		set.set(row*columnSize + column,bl);//指定されたインデクスのビットをfalseに
	}


	public Object get(){
		switch(TYPE){
		case"bitset":
		return set;
		case"dblarray":
			return dblArray;
			default:
			return null;
		}
	}



	public Object get(int row, int column) {
		// TODO Auto-generated method stub
		switch(TYPE){
		case "bitset":
		return set.get(row*columnSize+column);
		case "dblarray":
		return set.get(row*columnSize+column);
		default:
			return null;

		}
	}



	public int cardinality(int rowNumber) {//rowNumber>>0-150
		// TODO Auto-generated method stub
		return getRow(rowNumber).cardinality();//行で見る
	}




	public BitSet getRow(int rowNumber) {
		// TODO Auto-generated method stub

		return set.get(rowNumber*columnSize, (rowNumber+1)*columnSize);//第rowNumber列のbit全てをtrueに
	}



	public int cardinality2(int columnNumber) {
		// TODO Auto-generated method stub
		int indeg=0;
		int p=columnNumber;
		for(int i=0;i<columnSize;i++){
			if(set.get(p)){
				indeg++;
			}
			p+=columnSize;
		}
		return indeg;
	}


/////////////////////////////////////////////////////////////////


	public ArrayList<Integer> setInid(int columnNumber) {//列で見たとき(出次数) 出て行っている金融機関のidのリスト
		// TODO Auto-generated method stub
		//System.out.println(columnNumber+"番目の銀行の入次数:");
		int p=columnNumber;
		ArrayList<Integer> Inid=new ArrayList<Integer>();
		int k=0;
		for(int i=1;i<=columnSize;i++){
			if(set.get(p)){
				Inid.add(i);
				k++;

				//System.out.println(i+"銀行");
			}else{
				Inid.add(0);
				k++;
			}
			//System.out.println("");
			p+=columnSize;

		}
		return Inid;
	}

	public ArrayList<Integer> setOutid(int rowNumber) {
		// TODO Auto-generated method stub
		//System.out.println(rowNumber+"番目の銀行の出次数:");
		int k=1;
		ArrayList<Integer> Outid=new ArrayList<Integer>();
		for(int i=1;i<=rowSize;i++){
			if(set.get(rowSize*(rowNumber)+i-1)){
				Outid.add(i);
				k++;
				//System.out.print(i+"銀行");
			}else{
				Outid.add(0);
				k++;
			}
			//System.out.println("");

		}
		return Outid;
	}


	public void show(){
		switch (TYPE){
		case "bitset":
			for(int i=0;i<rowSize;i++){
				for(int j=0;j<columnSize;j++)

					System.out.print(((boolean)get(i,j)?1:0)+" ");
					System.out.println("");

			}
				break;
				case "dblarray":
					for(int i=0;i<rowSize;i++){
						for(int j=0;j<columnSize;j++){
							System.out.print(dblArray[i*columnSize+j]+"");
							System.out.println();
						}

				}
			}

		}








	}
