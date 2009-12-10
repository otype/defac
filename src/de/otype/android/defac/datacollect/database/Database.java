package de.otype.android.defac.datacollect.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class Database {
public static final String AUTHORITY = "de.dailab.smartmobile.datacollect.database";
	
	public static final class Category implements BaseColumns {
		//The content Uri
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + "category");
		
		//The content type
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.de.dailab.database.categories";
		
		//the item type
 		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.de.dailab.database.categories";

 		//Column with the name
 		public static final String NAME = "name";
 		
 		//Column with information whether this item is checked
 		public static final String CHECKED = "checked";
 		
	}
	
	public static final class Methods implements BaseColumns {
		//The content Uri
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + "methods");
		
		//The content type
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.de.database.methods";
		
		//the item type
 		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.de.dailab.database.methods";

 		//Column with the name
 		public static final String NAME = "name";
 		
 		//Column with the id of the corresponding category
 		public static final String CID = "cid";
 		
 		//Column with information whether this item is checked
 		public static final String CHECKED = "checked";
	}
}
