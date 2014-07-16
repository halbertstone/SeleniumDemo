/**
 * 
 */
package demo;

import java.util.List;

/**
 * @author hstone
 *
 * Object to contain info about a Page
 * The page elements maybe WebElement or something else not sure yet
 * Idea: a page representation holding element addressing, expectations?
 */
public abstract class Page {
	
	public String name;
	public List<Object> pgElems;
	

}
