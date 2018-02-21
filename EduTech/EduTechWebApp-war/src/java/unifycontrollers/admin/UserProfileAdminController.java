/***************************************************************************************
*   Title:                  UserProfileAdminController.java
*   Purpose:                SERVLET FOR UNIFY DASHBOARD & PROFILE - ADMIN (EDUBOX)
*   Created & Modified By:  TAN CHIN WEE WINSTON
*   Credits:                CHEN MENG, NIGEL LEE TJON YI, TAN CHIN WEE WINSTON, ZHU XINYI
*   Date:                   19 FEBRUARY 2018
*   Code version:           1.0
*   Availability:           === NO REPLICATE ALLOWED. YOU HAVE BEEN WARNED. ===
***************************************************************************************/

package unifycontrollers.admin;

import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import unifysessionbeans.admin.MarketplaceAdminMgrBeanRemote;
import unifysessionbeans.admin.ErrandsAdminMgrBeanRemote;
import unifysessionbeans.admin.UserProfileAdminMgrBeanRemote;
import unifysessionbeans.admin.VoicesAdminMgrBeanRemote;

public class UserProfileAdminController extends HttpServlet {
    @EJB
    private MarketplaceAdminMgrBeanRemote mamr;
    @EJB
    private ErrandsAdminMgrBeanRemote eamr;
    @EJB
    private UserProfileAdminMgrBeanRemote uamr;
    @EJB
    private VoicesAdminMgrBeanRemote vamr;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            RequestDispatcher dispatcher;
            ServletContext servletContext = getServletContext();
            String pageAction = request.getParameter("pageTransit");
            
            switch (pageAction) {
                case "goToUnifyAdmin":
                    request.setAttribute("unifyUserCount", uamr.getUnifyUserCount());
                    request.setAttribute("itemTransTodayCount", mamr.getItemTransTodayCount());
                    request.setAttribute("itemListingCount", mamr.getItemListingCount());
                    request.setAttribute("errandsTransTodayCount", eamr.getErrandsTransTodayCount());
                    request.setAttribute("errandsListingCount", eamr.getJobListingCount());
                    request.setAttribute("companyReviewCount", vamr.getCompanyReviewCount());
                    pageAction = "UnifyAdminDashboard";
                    break;
                case "goToUnifyUserList":
                    request.setAttribute("unifyUserCount", uamr.getUnifyUserCount());
                    request.setAttribute("activeUnifyUserCount", uamr.getActiveUnifyUserCount());
                    request.setAttribute("inactiveUnifyUserCount", uamr.getInactiveUnifyUserCount());
                    request.setAttribute("unifyUserList", (ArrayList) uamr.viewUnifyUserList());
                    pageAction = "UnifyUserList";
                    break;
                case "goToUnifyUserProfile":
                    request.setAttribute("unifyUserCount", uamr.getUnifyUserCount());
                    request.setAttribute("activeUnifyUserCount", uamr.getActiveUnifyUserCount());
                    request.setAttribute("inactiveUnifyUserCount", uamr.getInactiveUnifyUserCount());
                    pageAction = "UnifyUserProfile";
                    break;
                default:
                    break;
            }
            dispatcher = servletContext.getNamedDispatcher(pageAction);
            dispatcher.forward(request, response);       
        }
        catch(Exception ex) {
            log("Exception in UserProfileAdminController: processRequest()");
            ex.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    public String getServletInfo() { return "User Profile Admin Servlet"; }
}