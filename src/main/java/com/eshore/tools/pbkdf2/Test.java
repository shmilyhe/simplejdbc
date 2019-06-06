package com.eshore.tools.pbkdf2;

import com.eshore.tools.Bytes;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long bt =System.currentTimeMillis();
		int loop=100;
		for(int i=0;i<loop;i++){
			PBKDF2.pbkdf2("Admin","4wcsVGKRvi",100,50,new Sha256());
		}
		System.out.println(""+(loop*1000/(System.currentTimeMillis()-bt))+" ps");
		Sha256 a = new Sha256();
		//a.write("123445".getBytes());
		System.out.println(Bytes.toHexString(a.sum("1234567890".getBytes())));
	}

}
