package com.fisincorporated.democode.handlingxml;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.fisincorporated.interfaces.ICallbackTemplate;
// Android doc
// http://developer.android.com/reference/org/xmlpull/v1/XmlPullParser.html
// Some background and examples of parsing xml and creating Java objects
// http://www.xmlpull.org/history/index.html
// see http://www.xmlpull.org/v1/download/unpacked/doc/quick_intro.html
// example code below taken from above and modified slightly (processAttributes method added)
public class XMLPullParserTemplate {
	private ICallbackTemplate callback = null;
	private static final String TAG = "XMLPullParserTemplate";

	public XMLPullParserTemplate(ICallbackTemplate callback) {
		this.callback = callback;
	}

	public final static String SAMPLE_XML = "<?xml version=\"1.0\"?>\n"
			+ "\n"
			+ "<poem xmlns=\"http://www.megginson.com/ns/exp/poetry\">\n"
			+ "<title name=\"SimplePoem\" length=\"short\">Roses are Red</title>\n"
			+ "<l>Roses are red,</l>\n" 
			+ "<l>Violets are blue;</l>\n"
			+ "<l>Sugar is sweet,</l>\n"
			+ "<l>And I love you.</l>\n" + "</poem>";

	public void startParsing() {
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			outputParsing("Parser implementation class is " + xpp.getClass());
			outputParsing("Parsing simple sample XML");// :\n"+ SAMPLE_XML);
			//xpp.setInput(new FileReader(args[i]));
			xpp.setInput(new StringReader(SAMPLE_XML));
			processDocument(xpp);
		} catch (XmlPullParserException e) {
			outputParsing(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			outputParsing(e.toString());
			e.printStackTrace();
		}
	}
	public void startParsing(InputStream is, String streamSource) {
		XmlPullParserFactory factory;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			outputParsing("Parser implementation class is " + xpp.getClass());
			outputParsing("Parsing XML from input stream: " + streamSource);
					// inputEncoding to come from XML doc
					xpp.setInput(is, null);
					processDocument(xpp);
			
		} catch (XmlPullParserException e) {
			outputParsing(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			outputParsing(e.toString());
			e.printStackTrace();
		}
	}

	public void processDocument(XmlPullParser xpp)
			throws XmlPullParserException, IOException {
		int eventType = xpp.getEventType();
		do {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				outputParsing("Start document");
			} else if (eventType == XmlPullParser.END_DOCUMENT) {
				outputParsing("End document");
			} else if (eventType == XmlPullParser.START_TAG) {
				processStartElement(xpp);
				// added
				processAttributes(xpp);
			} else if (eventType == XmlPullParser.END_TAG) {
				processEndElement(xpp);
			} else if (eventType == XmlPullParser.TEXT) {
				processText(xpp);
			}
			// next()provides access to high level parsing events and advances the
			// parser to the next event.
			// The int value returned from next determines the current parser state
			// and is identical
			// to the value returned from following calls to getEventType ().
			// nextToken() allows access to lower level tokens
			eventType = xpp.next();
		} while (eventType != XmlPullParser.END_DOCUMENT);
	}

	public void processStartElement(XmlPullParser xpp) {
		String name = xpp.getName();
		String uri = xpp.getNamespace();
		if ("".equals(uri)) {
			outputParsing("Start element: " + name);
		} else {
			outputParsing("Start element: {" + uri + "}" + name);
		}
	}

	public void processEndElement(XmlPullParser xpp) {
		String name = xpp.getName();
		String uri = xpp.getNamespace();
		if ("".equals(uri))
			outputParsing("End element: " + name);
		else
			outputParsing("End element:   {" + uri + "}" + name);
	}

	int holderForStartAndLength[] = new int[2];

	public void processText(XmlPullParser xpp) throws XmlPullParserException {
		outputParsing("Text:");
		outputParsing(xpp.getText());
		// or could do as below and get char by char
		// char ch[] = xpp.getTextCharacters(holderForStartAndLength);
		// int start = holderForStartAndLength[0];
		// int length = holderForStartAndLength[1];
		// outputParsing("Characters:    \"");
		// for (int i = start; i < start + length; i++) {
		// switch (ch[i]) {
		// case '\\':
		// outputParsing("\\\\");
		// break;
		// case '"':
		// outputParsing("\\\"");
		// break;
		// case '\n':
		// outputParsing("\\n");
		// break;
		// case '\r':
		// outputParsing("\\r");
		// break;
		// case '\t':
		// outputParsing("\\t");
		// break;
		// default:
		// outputParsing( ch[i] + "");
		// break;
		// }
		// }
		// outputParsing("\"\n");
	}

	// added to template - process attributes of and element
	public void processAttributes(XmlPullParser xpp)
			throws XmlPullParserException {
		for (int i = 0; i < xpp.getAttributeCount(); ++i) {
			outputParsing("Attribute name:" + i + "= " + xpp.getAttributeName(i));
			// for getAttributeValue null is never returned
			outputParsing("Attribute value:" + i + "= " + xpp.getAttributeValue(i));
			// or use verions that allows namespace and attribute name
			outputParsing("Attribute value:" + i + "= "
					+ xpp.getAttributeValue(null, "attributeName"));
			// multiple other methods for attributes available
		}
	}

	public void outputParsing(String info) {
		if (callback != null) {
			callback.callbackInfo(info);
		} else {
			Log.i(TAG, info);
		}

	}
}
