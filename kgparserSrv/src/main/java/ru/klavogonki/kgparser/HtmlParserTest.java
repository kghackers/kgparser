package ru.klavogonki.kgparser;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.htmlparser.Node;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

public class HtmlParserTest
{
	private static final Logger logger = LogManager.getLogger(HtmlParserTest.class);

	public static void main(String[] args) throws ParserException {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.DEBUG);

		org.htmlparser.Parser parser = new org.htmlparser.Parser(HTML_PATH); // null means default encoding
		parser.setEncoding(HTML_ENCODING);
		logger.info("encoding: {}", parser.getEncoding());

		NodeList nodeList = parser.parse(null); // contains doctype and html elements
		NodeList divs = nodeList.extractAllNodesThatMatch( new HasAttributeFilter("id", PLAYERS_DIV_ID), true);

		SimpleNodeIterator iterator = divs.elements();
		while (iterator.hasMoreNodes())
		{
			Node node = iterator.nextNode();
			logger.debug("node text: {}", node.getText());
		}
	}

	public static final String HTML_PATH = "D:/java/kgparser/html/firefox/1.htm";
	private static final String PLAYERS_DIV_ID = "players";
	private static final String HTML_ENCODING = "UTF8";
}
