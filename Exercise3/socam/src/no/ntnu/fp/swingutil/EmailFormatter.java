package no.ntnu.fp.swingutil;

/**
 * Formats an e-mail string on the format <code>&lt;usernamet&gt;@&lt;domain&gt;
 * 
 * @author Hallvard Tr¾tteberg 
 * 
 * @version $Revision: 1.1 $ - $Date: 2008-04-16 12:57:02 $
 */
public class EmailFormatter extends RegexFormatter
{
	/**
	 * Regular expression defining all legal email patterns.
	 */
    public final static String EMAIL_PATTERN_STRING = "\\w+(\\.\\w+)*@(\\w+\\.)+\\w+";

    /**
     * Constructor for objects of class EmailFormatter
     */
    public EmailFormatter()
    {
        super(EMAIL_PATTERN_STRING);
    }
}
