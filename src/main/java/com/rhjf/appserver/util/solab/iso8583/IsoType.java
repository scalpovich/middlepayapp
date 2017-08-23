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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Defines the possible values types that can be used in the fields. Some types
 * required the length of the value to be specified (NUMERIC and ALPHA). Other
 * types have a fixed length, like dates and times. Other types do not require a
 * length to be specified, like LLVAR and LLLVAR.
 * 
 * @author Enrique Zamudio
 */
public enum IsoType {
	LLLHEXLEN(true,0),
	/** A fixed-length numeric value. It is zero-filled to the left. */
	NUMERIC(true, 0),

	/** 可显示长度的数字类型1 */
	LLLNUMERIC(true, 0), // 如果是false是表示使用这里的指定的长度，比如，date10，是10的长度，如果写0表示不定，比如llvar
	// 如果是true则是前面setvalue那里指定的长度
	/** 可显示长度的数字类型2 */
	LLNUMERIC(true, 0),
	LLLBINARY(true,0),
	/** A variable length alphanumeric value with a 2-digit header length. */
	/**
	 * A fixed-length alphanumeric value. It is filled with spaces to the right.
	 */
	ALPHA(true, 0), ALPHAHEX(true, 0), LLHEX(false, 0), LLLHEX(false, 0),

	/** A variable length alphanumeric value with a 2-digit header length. */
	LLVAR(false, 0),
	/** A variable length alphanumeric value with a 3-digit header length. */
	LLLVAR(false, 0),
	/** A date in format MMddHHmmss */
	DATE10(false, 10),

	/** A date in format MMdd */
	DATE4(false, 4),

	/** A date in format yyMM */
	DATE_EXP(false, 4),

	/** Time of day in format HHmmss */
	TIME(false, 6),
	/** An amount, expressed in cents with a fixed length of 12. */
	AMOUNT(false, 12);

	private boolean needsLen;
	private int length;

	IsoType(boolean flag, int l) {
		needsLen = flag;
		length = l;
	}

	/** Returns true if the type needs a specified length. */
	public boolean needsLength() {
		return needsLen;
	}

	/**
	 * Returns the length of the type if it's always fixed, or 0 if it's
	 * variable.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Formats a Date if the receiver is DATE10, DATE4, DATE_EXP or TIME; throws
	 * an exception otherwise.
	 */
	public String format(Date value) {
		if (this == DATE10) {
			return new SimpleDateFormat("MMddHHmmss").format(value);
		} else if (this == DATE4) {
			return new SimpleDateFormat("MMdd").format(value);
		} else if (this == DATE_EXP) {
			return new SimpleDateFormat("yyMM").format(value);
		} else if (this == TIME) {
			return new SimpleDateFormat("HHmmss").format(value);
		}
		throw new IllegalArgumentException("Cannot format date as " + this);
	}

	/**
	 * Formats the string to the given length (length is only useful if type is
	 * ALPHA).
	 */
	public String format(String value, int length) // 这个函数是为了看是否要在左边或者右边补0
	{
		if (this == ALPHA || this == ALPHAHEX) {
			if (value == null) {
				value = "";
			}
			if (value.length() > length) {
				// 左对齐
				return value.substring(0, length);
			}
			// alpha右边补0
			char[] c = new char[length];
			System.arraycopy(value.toCharArray(), 0, c, 0, value.length());
			for (int i = value.length(); i < c.length; i++) {
				c[i] = ' ';
			}
			return new String(c);
		} 
		else if (this == LLVAR || this == LLLVAR) 
		{
			return value;
		} 
		else if (this == LLHEX || this == LLLHEX) 
		{
			char[] c = new char[length];
			char[] x = value.toCharArray();
			if (this == LLHEX)// 右边补0
			{

				// 右边补一个0

				if (value.length() % 2 == 1) {
					c = new char[length + 1];
					for (int i = value.length(); i < c.length; i++) {
						c[i] = '0';
					}
					System.arraycopy(x, 0, c, 0, x.length);
					return new String(c);
				} else {
					c = new char[length ];
					System.arraycopy(x, 0, c, 0, x.length);
					return new String(c);
				}
			} else if (this == LLLHEX)// 右边补0
			{

				// 右边补一个0

				if (value.length() % 2 == 1) {
					c = new char[length + 1];
					for (int i = value.length(); i < c.length; i++) {
						c[i] = '0';
					}
					System.arraycopy(x, 0, c, 0, x.length);
					return new String(c);
				} else {
					c = new char[length ];
					System.arraycopy(x, 0, c, 0, x.length);
					return new String(c);
				}
			} {
				return new String(x);
			}
		} 
		else if (this == NUMERIC) 
		{
			if(length%2 == 1)
			{
				length += 1 ;
			}
			char[] c = new char[length];
			char[] x = value.toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: "
								+ value + " LEN " + length);
			}// 数字左边补0
			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		}
		// 按照定长右补0
		else if (this == LLLNUMERIC) 
		{
			if(length%2 == 1)
			{
				length += 1 ;
			}
			char[] c = new char[length];
			char[] x = value.toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: "
								+ value + " LEN " + length);
			}

			for (int i = value.length(); i < c.length; i++) // 也可以看字母的方法一样的，都是右边补
			{
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, 0, x.length);
			return new String(c);
		}
		else if(this == LLLBINARY){

			char[] c = new char[length];
			char[] x = value.toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: "
								+ value + " LEN " + length);
			}

			for (int i = value.length(); i < c.length; i++) // 也可以看字母的方法一样的，都是右边补
			{
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, 0, x.length);
			return new String(c);
		
		}
		// 按照定长右补0
		else if (this == LLNUMERIC) {
			char[] c = new char[length];
			char[] x = value.toCharArray();
			// 如果是第2域，就是即使够了长度也要右边补一个0

			if (x.length %2==1) {
				c = new char[length + 1];
				for (int i = value.length(); i < c.length; i++) {
					c[i] = '0';
				}
				System.arraycopy(x, 0, c, 0, x.length);
			} else {
				if (x.length > length) {
					throw new IllegalArgumentException(
							"Numeric value is larger than intended length: "
									+ value + " LEN " + length);
				}

				for (int i = value.length(); i < c.length; i++) {
					c[i] = '0';
				}
				System.arraycopy(x, 0, c, 0, x.length);
			}
			
			return new String(c);
		}
		
		throw new IllegalArgumentException("Cannot format String as " + this);
	}

	/** Formats the integer value as a NUMERIC, an AMOUNT, or a String. */
	public String format(long value, int length) 
	{
		if (this == NUMERIC) 
		{
			if(length%2 == 1)
			{
				length += 1 ;
			}
			char[] c = new char[length];
			char[] x = Long.toString(value).toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: "
								+ value + " LEN " + length);
			}
			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		} else if (this == ALPHA || this == ALPHAHEX || this == LLVAR
				|| this == LLLVAR) {
			return format(Long.toString(value), length);
		} else if (this == AMOUNT) {
			String v = Long.toString(value);
			char[] digits = new char[12];
			for (int i = 0; i < 12; i++) {
				digits[i] = '0';
			}
			// No hay decimales asi que dejamos los dos ultimos digitos como 0
			System.arraycopy(v.toCharArray(), 0, digits, 10 - v.length(), v
					.length());
			return new String(digits);
		} else if (this == LLLNUMERIC) {
			if(length%2 == 1)
			{
				length += 1 ;
			}
			char[] c = new char[length];
			char[] x = Long.toString(value).toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: "
								+ value + " LEN " + length);
			}
			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		} else if (this == LLLBINARY) {
			char[] c = new char[length];
			char[] x = Long.toString(value).toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: "
								+ value + " LEN " + length);
			}
			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		}else if (this == LLNUMERIC) {
			char[] c = new char[length];
			char[] x = Long.toString(value).toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						"Numeric value is larger than intended length: "
								+ value + " LEN " + length);
			}
			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		}
		throw new IllegalArgumentException("Cannot format number as " + this);
	}

	/** Formats the BigDecimal as an AMOUNT, NUMERIC, or a String. */
	public String format(BigDecimal value, int length) {
		if (this == AMOUNT) {
			String v = new DecimalFormat("0000000000.00").format(value);
			return v.substring(0, 10) + v.substring(11);
		} else if (this == NUMERIC) {
			return format(value.longValue(), length);
		} else if (this == ALPHA || this == ALPHAHEX || this == LLVAR
				|| this == LLLVAR) {
			return format(value.toString(), length);
		}
		throw new IllegalArgumentException("Cannot format BigDecimal as "
				+ this);
	}

}
