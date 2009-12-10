/**
 * 
 */
package de.otype.android.defac.datacollect.interfaces;

/**
 * @author hgschmidt
 *
 */
public interface IProps {

	public String getIMSI();
	public String getIMEI();
	public String getProperty(String propertyName);
	public String getPropertySoundsLike(String similarToPropertyName);
	public String getAllProperties();
}
