package org.skycrawl.nosqlshowcase.server.hadoop;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/**
 * MapReduce task that prints out all palindromes found in the input text, along with
 * occurrence count for each of them.
 * 
 * @author SkyCrawl
 */
public class PalindromCount extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>, Reducer<Text, IntWritable, Text, IntWritable>
{
	/**
	 * Each palindrome occurrence counts as "1" so that reduce can accurately compute the overall count.
	 */
	private final IntWritable one = new IntWritable();
	
	@Override
	public void map(LongWritable jobKey, Text input, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException
	{
		// split input text into words
		String[] words = input.toString().split("\\s");
	    for(int i = 0; i < words.length; i++)
	    {
	    	// and if an input words is a palindrome, output its occurrence
	    	if(isPalindrome(words[i]))
	    	{
				// output.collect(new Text(words[i]), one);
	    		output.collect(new Text(words[i]), null);
	    	}
	    }
	}
	
	@Override
	public void reduce(Text mapKey, Iterator<IntWritable> mapValues, OutputCollector<Text, IntWritable> reduceOutput, Reporter reporter) throws IOException
	{
		// count the number of occurrences for {@link mapKey}
		int count = 0;
		while (mapValues.hasNext())
		{
			count++;
		}
		
		// output the computed count
		reduceOutput.collect(mapKey, new IntWritable(count));
	}
	
	public static boolean isPalindrome(String text)
	{
		if((text == null) || text.isEmpty())
		{
			return false;
		}
		else
		{
			int halfSize = text.length() / 2;
			for (int i = 0; i < halfSize; i++)
		    {
		        if (text.charAt(i) != text.charAt(text.length() - 1 - i))
		        {
		        	return false;
		        }
		    }
		    return true;
		}
	}
}