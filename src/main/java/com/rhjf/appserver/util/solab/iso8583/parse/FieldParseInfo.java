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
package com.rhjf.appserver.util.solab.iso8583.parse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.rhjf.appserver.util.solab.iso8583.IsoType;
import com.rhjf.appserver.util.solab.iso8583.IsoValue;

/** This class contains the information needed to parse a field from a message buffer.
 * 
 * @author Enrique Zamudio
 */
public class FieldParseInfo {

	private IsoType type;
	private int length;

	/** Creates a new instance that parses a value of the specified type, with the specified length.
	 * The length is only useful for ALPHA and NUMERIC types.
	 * @param t The ISO type to be parsed.
	 * @param len The length of the data to be read (useful only for ALPHA and NUMERIC types). */
	public FieldParseInfo(IsoType t, int len) {
		if (t == null) {
			throw new IllegalArgumentException("IsoType cannot be null");
		}
		type = t;
		length = len;
	}

	/** Returns the specified length for the data to be parsed. */
	public int getLength() {
		return length;
	}

	/** Returns the data type for the data to be parsed. */
	public IsoType getType() {
		return type;
	}

	/** Parses the character data from the buffer and returns the
	 * IsoValue with the correct data type in it. */
	public IsoValue<?> parse(byte[] buf, int pos) throws ParseException 
	{
		if (type == IsoType.NUMERIC || type == IsoType.ALPHA || type == IsoType.ALPHAHEX) 
		{
			return new IsoValue<String>(type, new String(buf, pos, length), length);
		}
		/*else if (type == IsoType.LLVAR || type == IsoType.LLNUMERIC) 
		{
			length = ((buf[pos] - 48) * 10) + (buf[pos + 1] - 48);
			return new IsoValue<String>(type, new String(buf, pos + 2, length));
		} 
		else if (type == IsoType.LLLVAR || type == IsoType.LLLNUMERIC) 
		{
			length = ((buf[pos] - 48) * 100) + ((buf[pos + 1] - 48) * 10) + (buf[pos + 2] - 48);
			return new IsoValue<String>(type, new String(buf, pos + 3, length));
		} */
		else if (type == IsoType.LLVAR ||type == IsoType.LLHEX) 
		{
			length = ((buf[pos] - 48) * 10) + (buf[pos + 1] - 48);
			return new IsoValue<String>(type, new String(buf, pos + 2, length));
		} 
		else if (type == IsoType.LLLVAR || type == IsoType.LLLHEX) 
		{
			length = ((buf[pos] - 48) * 100) + ((buf[pos + 1] - 48) * 10) + (buf[pos + 2] - 48);
			return new IsoValue<String>(type, new String(buf, pos + 3, length));
		} 
		
		//我们是用binary，所以用parsebinary函数，而不用parse函数
		else if (type == IsoType.AMOUNT) {
			byte[] c = new byte[13];
			System.arraycopy(buf, pos, c, 0, 10);
			System.arraycopy(buf, pos + 10, c, 11, 2);
			c[10] = '.';
			return new IsoValue<BigDecimal>(type, new BigDecimal(new String(c)));
		} else if (type == IsoType.DATE10) {
			//A SimpleDateFormat in the case of dates won't help because of the missing data
			//we have to use the current date for reference and change what comes in the buffer
			Calendar cal = Calendar.getInstance();
			//Set the month in the date
			cal.set(Calendar.MONTH, ((buf[pos] - 48) * 10) + buf[pos + 1] - 49);
			cal.set(Calendar.DATE, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48);
			cal.set(Calendar.HOUR_OF_DAY, ((buf[pos + 4] - 48) * 10) + buf[pos + 5] - 48);
			cal.set(Calendar.MINUTE, ((buf[pos + 6] - 48) * 10) + buf[pos + 7] - 48);
			cal.set(Calendar.SECOND, ((buf[pos + 8] - 48) * 10) + buf[pos + 9] - 48);
			if (cal.getTime().after(new Date())) {
				cal.add(Calendar.YEAR, -1);
			}
			return new IsoValue<Date>(type, cal.getTime());
		} else if (type == IsoType.DATE4) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			//Set the month in the date
			cal.set(Calendar.MONTH, ((buf[pos] - 48) * 10) + buf[pos + 1] - 49);
			cal.set(Calendar.DATE, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48);
			if (cal.getTime().after(new Date())) {
				cal.add(Calendar.YEAR, -1);
			}
			return new IsoValue<Date>(type, cal.getTime());
		} else if (type == IsoType.DATE_EXP) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.DATE, 1);
			//Set the month in the date
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - (cal.get(Calendar.YEAR) % 100)
					+ ((buf[pos] - 48) * 10) + buf[pos + 1] - 48);
			cal.set(Calendar.MONTH, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 49);
			return new IsoValue<Date>(type, cal.getTime());
		} else if (type == IsoType.TIME) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, ((buf[pos] - 48) * 10) + buf[pos + 1] - 48);
			cal.set(Calendar.MINUTE, ((buf[pos + 2] - 48) * 10) + buf[pos + 3] - 48);
			cal.set(Calendar.SECOND, ((buf[pos + 4] - 48) * 10) + buf[pos + 5] - 48);
			return new IsoValue<Date>(type, cal.getTime());
		}
		return null;
	}

	
	private char hex(int i) {
		switch (i) {
		case 10:
			return 'A';
		case 11:
			return 'B';
		case 12:
			return 'C';
		case 13:
			return 'D';
		case 14:
			return 'E';
		case 15:
			return 'F';
		default:
			return (char) (i + 48);
		}
	}
	
	private String bytes2HexStr(byte[] ba) {
		StringBuffer md = new StringBuffer(32);
		for (int i = 0; i < ba.length; i++) {
			int j = ba[i];
			if (j < 0)
				j += 256;
			md.append(hex(j / 16));
			md.append(hex(j % 16));
		}
		return md.toString();
	}	
	/** Parses binary data from the buffer, creating and returning an IsoValue of the configured
	 * type and length. */
	public IsoValue<?> parseBinary(byte[] buf, int pos) throws ParseException //我们使用这个函数而不是上面那个
	{
		int oriLen = 0;
		if (type == IsoType.ALPHA) 
		{
			return new IsoValue<String>(type, new String(buf, pos, length), length);
		} 
		if (type == IsoType.ALPHAHEX) 
		{
			byte[] baData= new byte[length/2];
			for(int i=0;i<baData.length;i++)
				baData[i]=buf[pos+i];
			
			String ss=bytes2HexStr(baData);
			return new IsoValue<String>(type, ss, length);
		} 
		else if (type == IsoType.LLLNUMERIC)
		{ 
			//前两个字节为长度
			//pos+=2;
			length = ((buf[pos] & 0x0f) * 100) + (((buf[pos + 1] & 0xf0) >> 4) * 10) + (buf[pos + 1] & 0x0f);
			oriLen = length;
			if(length%2==0)
			{
				length=(length/2);
			}
			else
			{
			  length=(length/2)+1;
			}
			
			byte[] baData= new byte[length];
			for(int i=0;i<length;i++)
				baData[i]=buf[pos+2+i];
			
			String ss=bytes2HexStr(baData);
			
			ss = ss.substring(0, oriLen) ;
			return new IsoValue<String>(type, ss, oriLen, oriLen);
			//return new IsoValue<String>(type, ss, length);
//			return new IsoValue<String>(type, new String(buf, pos+2, length), length);
		}else if(type == IsoType.LLLBINARY){
			 
			//前两个字节为长度
			//pos+=2;
			length = ((buf[pos] & 0x0f) * 100) + (((buf[pos + 1] & 0xf0) >> 4) * 10) + (buf[pos + 1] & 0x0f);
			oriLen = length;
						
			byte[] baData= new byte[length];
			for(int i=0;i<length;i++)
				baData[i]=buf[pos+2+i];
			
			String ss=bytes2HexStr(baData);
			return new IsoValue<String>(type, ss, length, oriLen);
			//return new IsoValue<String>(type, ss, length);
//			return new IsoValue<String>(type, new String(buf, pos+2, length), length);
		
		}
		else if (type == IsoType.LLNUMERIC)
		{ 
			length = (((buf[pos] & 0xf0) >> 4) * 10) + (buf[pos] & 0x0f);
			oriLen = length;
			if(length%2==0)
			{
				length=(length/2);
			}
			else
			{
			  length=(length/2)+1;
			}
			
			byte[] baData= new byte[length];
			for(int i=0;i<length;i++)
				baData[i]=buf[pos+1+i];
			
			String ss=bytes2HexStr(baData);
			if (ss.length() > oriLen)
			{
				ss = ss.substring(0, oriLen);
			}
			
			return new IsoValue<String>(type, ss, oriLen, oriLen) ;
			//return new IsoValue<String>(type, ss, length);
//			return new IsoValue<String>(type, new String(buf, pos+1, length), length);
		}
		else if (type == IsoType.NUMERIC) 
		{

			//A long covers up to 18 digits
			if (length < 19) {
				long l = 0;
				int power = 1;
				for (int i = pos + (length / 2) + (length % 2) - 1; i >= pos; i--) {
					l += (buf[i] & 0x0f) * power;
					power *= 10;
					l += ((buf[i] & 0xf0) >> 4) * power;
					power *= 10;
				}
				return new IsoValue<Number>(IsoType.NUMERIC, l, length);
			} 
			else 
			{
				//Use a BigInteger
				char[] digits = new char[length];
				int start = 0;
				for (int i = pos; i < pos + (length / 2) + (length % 2); i++) {
					digits[start++] = (char)(((buf[i] & 0xf0) >> 4) + 48);
					digits[start++] = (char)((buf[i] & 0x0f) + 48);
				}
				return new IsoValue<Number>(IsoType.NUMERIC, new BigInteger(new String(digits)), length);
			}

		}
		/*else if (type == IsoType.LLLNUMERIC)
		{ 
			//前两个字节为长度
			pos+=2;
			//A long covers up to 18 digits
			if (length < 19) 
			{
				long l = 0;
				int power = 1;
				for (int i = pos + (length / 2) + (length % 2) - 1; i >= pos; i--) 
				{
					l += (buf[i] & 0x0f) * power;
					power *= 10;
					l += ((buf[i] & 0xf0) >> 4) * power;
					power *= 10;
				}
				return new IsoValue<Number>(IsoType.LLLNUMERIC, l, length);
			}
		else if (type == IsoType.LLNUMERIC)
			{ 
				//前两个字节为长度
				pos+=1;
				//A long covers up to 18 digits
				if (length < 19) 
				{
					long l = 0;
					int power = 1;
					for (int i = pos + (length / 2) + (length % 2) - 1; i >= pos; i--) 
					{
						l += (buf[i] & 0x0f) * power;
						power *= 10;
						l += ((buf[i] & 0xf0) >> 4) * power;
						power *= 10;
					}
					return new IsoValue<Number>(IsoType.LLNUMERIC, l, length);
				}
			}
			else 
			{
				//Use a BigInteger
				char[] digits = new char[length];
				int start = 0;
				for (int i = pos; i < pos + (length / 2) + (length % 2); i++) 
				{
					digits[start++] = (char)(((buf[i] & 0xf0) >> 4) + 48);
					digits[start++] = (char)((buf[i] & 0x0f) + 48);
				}
				return new IsoValue<Number>(IsoType.NUMERIC, new BigInteger(new String(digits)), length);
			}
		}*/
		else if (type == IsoType.LLHEX) {
			length = (((buf[pos] & 0xf0) >> 4) * 10) + (buf[pos] & 0x0f);
			oriLen = length ;
			//35域，写是37个的，但实际是38个
			int newLen = 0;
			if(length%2!=0)
			{
				newLen=length+1;
			}
			else
			{
				newLen=length;
			}
			
			length=newLen/2;
			byte[] baData= new byte[length];
			for(int i=0;i<length;i++)
				baData[i]=buf[pos+1+i];
			
			String ss=bytes2HexStr(baData);
			//System.out.println(String.format("The string is %s, the length is %d", ss, oriLen));
			ss = ss.substring(0, oriLen) ;
			//return new IsoValue<String>(type, ss.substring(0, ss.length()-1));
			return new IsoValue<String>(type, ss, oriLen, oriLen) ;
		} 
		else if (type == IsoType.LLLHEX) 
		{
			int newLen = 0;			
			length = ((buf[pos] & 0x0f) * 100) + (((buf[pos + 1] & 0xf0) >> 4) * 10) + (buf[pos + 1] & 0x0f);
			oriLen = length ;
			if(length%2!=0)
			{
				newLen=length+1;
			}
			else
			{
				newLen=length;
			}
			
			length=newLen/2;
			byte[] baData= new byte[length];
			for(int i=0;i<length;i++)
				baData[i]=buf[pos+2+i];
			
			String ss=bytes2HexStr(baData);
			System.out.println(String.format("The string is %s, the length is %d", ss, ss.length()));
			ss = ss.substring(0, oriLen) ;
			return new IsoValue<String>(type, ss, oriLen, oriLen) ;
			//return new IsoValue<String>(type, ss);
		} 
		else if (type == IsoType.LLVAR) {

			length = (((buf[pos] & 0xf0) >> 4) * 10) + (buf[pos] & 0x0f);
			return new IsoValue<String>(type, new String(buf, pos + 1, length));
		} 
		else if (type == IsoType.LLLVAR) {

			length = ((buf[pos] & 0x0f) * 100) + (((buf[pos + 1] & 0xf0) >> 4) * 10) + (buf[pos + 1] & 0x0f);		
			return new IsoValue<String>(type, new String(buf, pos + 2, length));
		} 
		else if (type == IsoType.AMOUNT) {

			char[] digits = new char[13];
			digits[10] = '.';
			int start = 0;
			for (int i = pos; i < pos + 6; i++) {
				digits[start++] = (char)(((buf[i] & 0xf0) >> 4) + 48);
				digits[start++] = (char)((buf[i] & 0x0f) + 48);
				if (start == 10) {
					start++;
				}
			}
			return new IsoValue<BigDecimal>(IsoType.AMOUNT, new BigDecimal(new String(digits)));
		} 
		else if (type == IsoType.DATE10 || type == IsoType.DATE4 || type == IsoType.DATE_EXP
				|| type == IsoType.TIME) {

			int[] tens = new int[(type.getLength() / 2) + (type.getLength() % 2)];
			int start = 0;
			for (int i = pos; i < pos + tens.length; i++) {
				tens[start++] = (((buf[i] & 0xf0) >> 4) * 10) + (buf[i] & 0x0f);
			}
			Calendar cal = Calendar.getInstance();
			if (type == IsoType.DATE10) {
				//A SimpleDateFormat in the case of dates won't help because of the missing data
				//we have to use the current date for reference and change what comes in the buffer
				//Set the month in the date
				cal.set(Calendar.MONTH, tens[0] - 1);
				cal.set(Calendar.DATE, tens[1]);
				cal.set(Calendar.HOUR_OF_DAY, tens[2]);
				cal.set(Calendar.MINUTE, tens[3]);
				cal.set(Calendar.SECOND, tens[4]);
				if (cal.getTime().after(new Date())) {
					cal.add(Calendar.YEAR, -1);
				}
				return new IsoValue<Date>(type, cal.getTime());
			} else if (type == IsoType.DATE4) {
				cal.set(Calendar.HOUR, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				//Set the month in the date
				cal.set(Calendar.MONTH, tens[0] - 1);
				cal.set(Calendar.DATE, tens[1]);
				if (cal.getTime().after(new Date())) {
					cal.add(Calendar.YEAR, -1);
				}
				return new IsoValue<Date>(type, cal.getTime());
			} else if (type == IsoType.DATE_EXP) {
				cal.set(Calendar.HOUR, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.DATE, 1);
				//Set the month in the date
				cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)
						- (cal.get(Calendar.YEAR) % 100) + tens[0]);
				cal.set(Calendar.MONTH, tens[1] - 1);
				return new IsoValue<Date>(type, cal.getTime());
			} else if (type == IsoType.TIME) {
				cal.set(Calendar.HOUR_OF_DAY, tens[0]);
				cal.set(Calendar.MINUTE, tens[1]);
				cal.set(Calendar.SECOND, tens[2]);
				return new IsoValue<Date>(type, cal.getTime());
			}
			return new IsoValue<Date>(type, cal.getTime());
		}
		return null;
	}
}
