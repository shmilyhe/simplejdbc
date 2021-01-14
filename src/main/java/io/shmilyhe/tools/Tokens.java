package io.shmilyhe.tools;
/**
 * tokenization
 * @author eshore
 *
 */
public class Tokens {
	
	public static void main(String[] args){
		System.out.println(randomString(8));
		System.out.println(randomString(16));
		System.out.println(randomString(32));
		System.out.println(randomString(34));
	}
	
	/**
	 * 生成指定长度的随机数
	 * @param len 长度
	 * @return 随机数
	 */
	public static String randomString(int len){
		char[] cs = new char[len];
		for(int i=0;i<len;i+=16){
			char[] c=random16();
			if(i+c.length>=len){
				System.arraycopy(c, 0, cs, i, len-i);
			}else{
				System.arraycopy(c, 0, cs, i, c.length);
			}
		}
		return new String(cs);
	}
	
	/**
	 * 每次生成16个随机字符
	 * @return 随机字符
	 */
	private static char[] random16(){
		long v =Double.doubleToLongBits(Math.random());
		long v2 =Double.doubleToLongBits(Math.random());
		return fillchar(
				(byte)(v2 >>> 32),
				 (byte)(v2 >>> 24),
				 (byte)(v >>> 40),
				 (byte)(v >>> 32),
				 (byte)(v >>> 24),
				 (byte)(v >>> 16),
				 (byte)(v >>> 8),
				 (byte)(v >>> 0)
				);
	}
	
	/**
	 * 把byte 转换成hex char
	 * @param v byte
	 * @return char
	 */
	private static char[] fillchar(byte ...v){
		char[] cs = new char[v.length*2];
		int j=0;
		for(int i=0;i<v.length;i++,j+=2){
			//System.out.println("j:"+j+"i:"+i+":"+(toUnsigned(v[i])>>>4)+"-"+(toUnsigned(v[i])& 0xf));
			cs[j]=hexarra[toUnsigned(v[i])>>>4];
			cs[j+1]=hexarra[toUnsigned(v[i])& 0xf];
		}
		return cs;
	}
	/**
	 * 整数转为两byte
	 * @param v
	 * @return
	 */
	private static byte[] writeShort(int v)  {
	 	byte[] b = new byte[2];
        b[0]=(byte)((v >>> 8) & 0xFF);
        b[1]=(byte)((v >>> 0) & 0xFF);
        return b;
    }
	static char[] hexarra="0123456789abcdef".toCharArray();
	
	/**
	 * 长度转为hex char
	 * @param i 长度
	 * @return hex char
	 */
	private static char[] len(int i){
		if(i<15){
			return new char[]{hexarra[i]};
		}
		 byte[]b=writeShort(i);
		 return new char[]{
				 'e',
				 hexarra[toUnsigned(b[0])>>>4],
				 hexarra[toUnsigned(b[0])& 0xf],
				 hexarra[toUnsigned(b[1])>>>4],
				 hexarra[toUnsigned(b[1])& 0xf]
		 };

	}
	
	/**
	 * byte 转为 Unsigned
	 * @param b byte
	 * @return Unsigned
	 */
	private static int toUnsigned(byte b) {
        return b < 0 ? b + 256 : b;
    }
	
	/**
	 * 数组序列化为token
	 * @param str 输入
	 * @return token
	 */
	public static String serialization(String ...str){
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		sb.append(hexarra[str.length]);
		for(int i=0;i<str.length;i++){
			sb.append(len(str[i].length()));
			sb1.append(str[i]);
		}
		return sb.append(sb1).toString();
	}
	
	/**
	 * 反序列化
	 * @param str token
	 * @return token数组
	 */
	public static String[] deserialization(String str){
		int len=(int)str.charAt(0);
		if(len>96){len-=87;}else{len-=48;}
		int[] lens= new int[len];
		int i=1;
		String [] strs =new String[len];
		for(int j=0;j<lens.length;j++){
			char c=str.charAt(i);
			int l=0;
			if(c=='e'){
				l=Integer.parseUnsignedInt(new String(new char[]{str.charAt(i+1),str.charAt(i+2),str.charAt(i+3),str.charAt(i+4)}), 16);
				i+=5;
			}else{
				l=(int)c;
				l=l>96?l-87:l-48;
				i+=1;
			}
			lens[j]=l;
		}
		for(int j=0;j<strs.length;j++){
			strs[j]=str.substring(i, i+lens[j]);
			i+=lens[j];
		}
		return strs;
	}

}
