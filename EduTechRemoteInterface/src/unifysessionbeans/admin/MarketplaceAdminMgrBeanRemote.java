/***************************************************************************************
*   Title:                  MarketplaceAdminMgrBeanRemote.java
*   Purpose:                LIST OF REMOTE INTERFACE METHODS FOR UNIFY MARKETPLACE - ADMIN (EDUBOX)
*   Created & Modified By:  TAN CHIN WEE WINSTON
*   Credits:                CHEN MENG, NIGEL LEE TJON YI, TAN CHIN WEE WINSTON, ZHU XINYI
*   Date:                   19 FEBRUARY 2018
*   Code version:           1.0
*   Availability:           === NO REPLICATE ALLOWED. YOU HAVE BEEN WARNED. ===
***************************************************************************************/

package unifysessionbeans.admin;

import java.util.List;
import java.util.Vector;
import javax.ejb.Remote;

@Remote
public interface MarketplaceAdminMgrBeanRemote {
    public List<Vector> viewItemCategoryList();
    public Vector viewItemCategoryDetails(long itemCategoryID);
    public List<Vector> viewAssociatedItemList(long itemCategoryID);
    
    public String createItemCategory(String categoryName, String categoryType, String categoryDescription, 
            String categoryImage);
    public String updateItemCategory(long itemCategoryID, String categoryName, String categoryDescription, 
            String fileName);
    public String activateAnItemCategory(long actItemCategoryID);
    public String deactivateAnItemCategory(long deactItemCategoryID);
    
    public List<Vector> viewItemList();
    public Vector viewItemDetails(long urlItemID);
    public String deleteAnItem(long urlItemID);
    
    /* METHODS FOR UNIFY ADMIN DASHBOARD */
    public Long getItemTransTodayCount();
    public Long getActiveItemCategoryListCount();
    public Long getInactiveItemCategoryListCount();
    public Long getItemListingCount();
    public Long getAvailableItemListingCount();
    public Long getSoldItemListingCount();
    public Long getReservedItemListingCount();
    
    public void createSystemMessage(String hiddenItemName, String hiddenSellerID);
}