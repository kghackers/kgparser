/**
 * User: 1
 * Date: 17.01.2012
 * Time: 23:38:00
 */
package ru.klavogonki.kgparser;

import org.htmlparser.Node;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class HtmlParserTest
{
	public static void main(String[] args) throws ParserException {
		org.htmlparser.Parser parser = new org.htmlparser.Parser(HTML_PATH); // null means default encoding
		parser.setEncoding(HTML_ENCODING);
		System.out.println("encoding: " + parser.getEncoding());

		NodeList nodeList = parser.parse(null); // contains doctype and html elements
		NodeList divs = nodeList.extractAllNodesThatMatch( new HasAttributeFilter("id", PLAYERS_DIV_ID), true);

		SimpleNodeIterator iterator = divs.elements();
		while (iterator.hasMoreNodes())
		{
			Node node = iterator.nextNode();
			System.out.println("node text: " + node.getText());
		}
	}

	public static final String HTML_PATH = "D:/java/kgparser/html/firefox/1.htm";
	private static final String PLAYERS_DIV_ID = "players";
	private static final String HTML_ENCODING = "UTF8";
}