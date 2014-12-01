package org.skycrawl.nosqlshowcase;

import junit.framework.TestCase;

import org.skycrawl.nosqlshowcase.server.hadoop.PalindromCount;

public class HadoopTest extends TestCase
{
	public void testPalindrom()
	{
		assertFalse(PalindromCount.isPalindrome(null));
		assertFalse(PalindromCount.isPalindrome(""));
		
		assertTrue(PalindromCount.isPalindrome("a"));
		assertTrue(PalindromCount.isPalindrome("aa"));
		assertTrue(PalindromCount.isPalindrome("aaa"));
		assertTrue(PalindromCount.isPalindrome("radar"));
		assertTrue(PalindromCount.isPalindrome("nam man"));
	}
}