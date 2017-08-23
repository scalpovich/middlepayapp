/*
j8583 A Java implementation of the ISO8583 protocol
Copyright (C) 2007 Enrique Zamudio Lopez

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
*/
package com.rhjf.appserver.util.solab.iso8583;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;

import com.rhjf.appserver.util.BytesTool;

/** Represents a value that is stored in a field inside an ISO8583 message.
 * It can format the value when the message is generated.
 * Some values have a fixed length, other values require a length to be specified
 * so that the value can be padded to the specified length. LLVAR and LLLVAR
 * values do not need a length specification because the length is calculated
 * from the stored value.
 * 
 * @author Enrique Zamudio
 */
public class IsoValue<T> implements Cloneable {

	private IsoType type;
	private int oriLen;
	private int length;
	private T value;

	/** Creates a new instance that stores the specified value as the specified type.
	 * Useful for storing LLVAR or LLLVAR types, as well as fixed-length value types
	 * like DATE10, DATE4, AMOUNT, etc.
	 * 存储不定长的，lllvar
	 * @param t the ISO type.
	 * @param value The value to be stored. */
	public IsoValue(IsoType t, T value) 
	{
		if (t.needsLength()) 
		{
			throw new IllegalArgumentException("Fixed-value types must use constructor that specifies length");
		}
		type = t;
		this.value = value;
		if (type == IsoType.LLVAR || type == IsoType.LLLVAR ||type==IsoType.LLLHEX || type==IsoType.LLHEX) 
		{
			length = value.toString().getBytes().length;//即使是不定长的，这里也是可以得到他的长度的
			
			//下面是判断是否超过不定长的一般规定长度
			if (t == IsoType.LLVAR && length > 99) 
			{
				throw new IllegalArgumentException("LLVAR can only hold values up to 99 chars");
			} 
			else if (t == IsoType.LLLVAR && length > 999) 
			{
				throw new IllegalArgumentException("LLLVAR can only hold values up to 999 chars");
			}
		} 
		else 
		{
			length = type.getLength();
		}
		
		this.oriLen = length;
	}

	/** Creates a new instance that stores the specified value as the specified type.
	 * Useful for storing fixed-length value types. 
	 * 存储定长的
	 * */
	public IsoValue(IsoType t, T val, int len) 
	{
		type = t;
		value = val;
		length = len;//这里有2个地方设置length的，这个是其中之一，但是后面还会改变
		if (length == 0 && t.needsLength()) 
		{
			throw new IllegalArgumentException("Length must be greater than zero");
		} 
		else if(t == IsoType.LLLHEXLEN){
			length = val.toString().getBytes().length;
		}
		else if (t == IsoType.LLVAR || t == IsoType.LLLVAR||type==IsoType.LLLHEX || type==IsoType.LLHEX)
		{//下面是判断是否超过不定长的一般规定长度
			length = val.toString().getBytes().length;//这是其2，如果是llvar或者lllvar
			
			if ((t == IsoType.LLVAR || type==IsoType.LLHEX)&& length > 99) 
			{
				throw new IllegalArgumentException("can only hold values up to 99 chars");
			} 
			else if ((t == IsoType.LLLVAR|| type==IsoType.LLLHEX) && length > 999) 
			{
				throw new IllegalArgumentException("can only hold values up to 999 chars");
			}
		}
		
		this.oriLen = this.length;
	}

	public IsoValue(IsoType t, T val, int len, int oriLen)
	{
		this(t, val, len);
		this.oriLen = oriLen;
	}
	
	/** Returns the ISO type to which the value must be formatted. */
	public IsoType getType() {
		return type;
	}

	/** Returns the length of the stored value, of the length of the formatted value
	 * in case of NUMERIC or ALPHA. It doesn't include the field length header in case
	 * of LLVAR or LLLVAR. */
	public int getLength() {
		return length;
	}

	public int getOriLen()
	{
		return oriLen;
	}

	/** Returns the stored value without any conversion or formatting. */
	public T getValue() {
		return value;
	}

	/** Returns the formatted value as a String. The formatting depends on the type of the
	 * receiver. */
	public String toString()
	{
		if (value == null) 
		{
			return "ISOValue<null>";
		}
		if (type == IsoType.NUMERIC || type == IsoType.AMOUNT) 
		{
			if (type == IsoType.AMOUNT)
			{
				return type.format((BigDecimal)value, 12);
			} 
			else if (value instanceof Number) 
			{
				return type.format(((Number)value).longValue(), length);
			} 
			else 
			{
				return type.format(value.toString(), length);
			}
		}
		else if(type == IsoType.LLLNUMERIC ||type == IsoType.LLNUMERIC )
		{
			return type.format(value.toString(), length);//拿过去右边补0
		}
		else if(type == IsoType.LLLHEX ||type == IsoType.LLHEX )
		{
			return type.format(value.toString(), length);//拿过去右边补0
		}
		
		else if (type == IsoType.ALPHA || type == IsoType.ALPHAHEX) 
		{
			return type.format(value.toString(), length);
		} 
		else if (type == IsoType.LLLVAR || type == IsoType.LLLVAR)
		{
			return value.toString();
		}
		else if (value instanceof Date)
		{
			return type.format((Date)value);
		}
		return value.toString();
	}

	/** Returns a copy of the receiver that references the same value object. */
	@SuppressWarnings("unchecked")
	public IsoValue<T> clone() {
		try {
			return (IsoValue<T>)super.clone();
		} catch (CloneNotSupportedException ex) {
			return null;
		}
	}

	/** Returns true of the other object is also an IsoValue and has the same type and length,
	 * and if other.getValue().equals(getValue()) returns true. */
	public boolean equals(Object other) {
		if (other == null || !(other instanceof IsoValue)) {
			return false;
		}
		IsoValue comp = (IsoValue)other;
		return (comp.getType() == getType() && comp.getValue().equals(getValue()) && comp.getLength() == getLength());
	}

	/** Writes the formatted value to a stream, with the length header
	 * if it's a variable length type. */
	public void write(OutputStream outs, boolean binary) throws IOException 
	{
		if (type == IsoType.LLLVAR || type == IsoType.LLVAR ) 
		{

			if (binary) 
			{
				if (type == IsoType.LLLVAR) 
				{
					outs.write(length / 100); //00 to 09 automatically in BCD
				}
				//BCD encode the rest of the length
				outs.write((((length % 100) / 10) << 4) | (length % 10));
			} 
			else 
			{
				//write the length in ASCII
				if (type == IsoType.LLLVAR) {
					outs.write((length / 100) + 48);
				}
				if (length >= 10) {
					outs.write(((length % 100) / 10) + 48);
				} else {
					outs.write(48);
				}
				outs.write((length % 10) + 48);
			}
		}
		else if (binary)
		{
			//numeric types in binary are coded like this
			byte[] buf = null;
			if (type == IsoType.NUMERIC) {
				buf = new byte[(length / 2) + (length % 2)];
			} 
			if(type == IsoType.LLLHEXLEN){
				buf = new byte[(length / 2) + (length % 2)];
			}
			
			if (type == IsoType.ALPHAHEX ||type == IsoType.LLHEX ||type == IsoType.LLLHEX) {
				//因为存储的是十六进制字符串
				//例如：ab12(ASCII:41423132),变为byte数组后为：0xab,0x12
				//转换后，长度变为原来的一半
				buf = new byte[(length / 2) + (length % 2)];
			} 
			
			else if (type == IsoType.LLLNUMERIC) {
				buf = new byte[(length / 2) + (length % 2)];
			}
			else if (type == IsoType.LLNUMERIC) {
				buf = new byte[(length / 2) + (length % 2)];
			}
			else if (type == IsoType.AMOUNT) {
				buf = new byte[6];
			} else if (type == IsoType.DATE10 || type == IsoType.DATE4 || type == IsoType.DATE_EXP || type == IsoType.TIME) {
				buf = new byte[length / 2];
			}
			else if(type == IsoType.LLLBINARY)
			{
				buf = new byte[length];
			}
			//Encode in BCD if it's one of these types
			if (buf != null) 
			{
				//因为存储的是十六进制字符串
				//例如：ab12(ASCII:41423132),变为byte数组后为：0xab,0x12
				//转换后，长度变为原来的一半
	
				String strTmp=toString();
				if(type == IsoType.LLLNUMERIC ||type == IsoType.LLLHEX)
				{
					String strLen=String.format("%1$,04d", oriLen);
					byte[] buf1=new byte[2];
					toBcd(strLen, buf1);
					outs.write(buf1);
				}
				else if(type == IsoType.LLLHEXLEN){
					String strLen=String.format("%1$,04d",length/2);
					byte[] buf1=new byte[2];
					toBcd(strLen, buf1);
					outs.write(buf1);
				}
				else if(type == IsoType.LLNUMERIC ||type == IsoType.LLHEX)
				{
					String strLen="";
						
					//if(type == IsoType.LLHEX)
					//{
					//	strLen=String.format("%1$,02d",length-1);
					//}
					//else
					//{
						
						strLen=String.format("%1$,02d",oriLen);
						
					//}
					byte[] buf1=new byte[1];
					toBcd(strLen, buf1);
					outs.write(buf1);
				}
				else if(type == IsoType.LLLBINARY)
				{
					String strLen=String.format("%1$,04d",length);
					
					byte[] buf1=new byte[2];
					toBcd(strLen, buf1);
					outs.write(buf1);
				}
				if(type == IsoType.ALPHAHEX || type == IsoType.LLLHEX || type == IsoType.LLHEX || type == IsoType.LLLHEXLEN){
					toHex(strTmp,buf);
				}else{
					toBcd(strTmp, buf);
				}
				outs.write(buf);
				return;
			}
		}
		//Just write the value as text
		outs.write(toString().getBytes());
	}

	/** Encode the value as BCD and put it in the buffer. The buffer must be big enough
	 * to store the digits in the original value (half the length of the string). */
	private void toBcd(String value, byte[] buf) {
		
		/*int charpos = 0; //char where we start
		int bufpos = 0;
		if (value.length() % 2 == 1) {
			//for odd lengths we encode just the first digit in the first byte
			buf[0] = (byte)(value.charAt(0) - 48);
			charpos = 1;
			bufpos = 1;
		}
		//encode the rest of the string
		while (charpos < value.length()) {
			buf[bufpos] = (byte)(((value.charAt(charpos) - 48) << 4)
					| (value.charAt(charpos + 1) - 48));
			charpos += 2;
			bufpos++;
		}*/
		System.arraycopy(BytesTool.HexStr2Bytes(value), 0, buf, 0, value.length()/2) ;
		//buf=BytesTool.HexStr2Bytes(value);
	}

	private int hex2Int(char ccSrc){
		char cc=ccSrc;
		if(Character.isLowerCase(ccSrc))
			cc=Character.toUpperCase(ccSrc);
		
		int iRtn=0;
		switch(cc){
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			iRtn=(int)(cc - 48);
			break;
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
			iRtn=(int)(cc - 55);
			break;
		}
		return iRtn;
	}
	
	private void toHex(String value, byte[] buf) {
		int charpos = 0; //char where we start
		int bufpos = 0;

		//encode the rest of the string
		while (charpos < value.length()) {
			char c1=(char)value.charAt(charpos);
			char c2=(char)value.charAt(charpos+1);
			
			buf[bufpos] = (byte)((hex2Int(c1) << 4)	| hex2Int(c2));
			
			charpos += 2;
			bufpos++;
		}
	}

}
