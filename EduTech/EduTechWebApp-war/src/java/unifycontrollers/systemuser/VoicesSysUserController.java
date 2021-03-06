/***************************************************************************************
*   Title:                  VoicesSysUserController.java
*   Purpose:                SERVLET FOR UNIFY COMPANY REVIEW - SYSUSER (EDUBOX)
*   Created & Modified By:  ZHU XINYI
*   Credits:                CHEN MENG, NIGEL LEE TJON YI, TAN CHIN WEE WINSTON, ZHU XINYI
*   Date:                   19 FEBRUARY 2018
*   Code version:           1.0
*   Availability:           === NO REPLICATE ALLOWED. YOU HAVE BEEN WARNED. ===
***************************************************************************************/

package unifycontrollers.systemuser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import unifysessionbeans.systemuser.VoicesSysUserMgrBeanRemote;
import unifysessionbeans.systemuser.UserProfileSysUserMgrBeanRemote;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 50,
        maxRequestSize = 1024 * 1024 * 100
)

public class VoicesSysUserController extends HttpServlet {
    @EJB
    private VoicesSysUserMgrBeanRemote vsmr;
    @EJB
    private UserProfileSysUserMgrBeanRemote usmr;
    String responseMessage = "";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            RequestDispatcher dispatcher;
            ServletContext servletContext = getServletContext();
            String pageAction = request.getParameter("pageTransit");
            String loggedInUsername = getCookieUsername(request);
            
            switch (pageAction) {
                case "goToViewCompanyListingSYS":
                    request.setAttribute("companyListSYS", (ArrayList) vsmr.viewCompanyList());
                    request.setAttribute("industryListSYS", (ArrayList) vsmr.populateCompanyIndustry());
                    request.setAttribute("industryStrSYS", vsmr.populateCompanyIndustryString());
                    request.setAttribute("userMessageListTopThreeSYS", usmr.viewUserMessageListTopThree(loggedInUsername));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "ViewCompanyListingSYS";
                    break;
                case "goToNewReviewSYS":
                    String companyImage = request.getParameter("hiddenCompanyImage");
                    String companyName = request.getParameter("hiddenCompanyName");
                    String companyIndustry = request.getParameter("hiddenCompanyIndustry");
                    request.setAttribute("reviewedCompanyImage", companyImage);
                    request.setAttribute("reviewedCompanyName", companyName);
                    request.setAttribute("reviewedCompanyIndustry", companyIndustry);
                    request.setAttribute("userMessageListTopThreeSYS", usmr.viewUserMessageListTopThree(loggedInUsername));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "NewReviewSYS";
                    break;
                case "createCompanyReviewSYS":
                    responseMessage = createCompanyReview(request);
                    if (responseMessage.endsWith("!")) { request.setAttribute("successMessage", responseMessage); } 
                    else { request.setAttribute("errorMessage", responseMessage); }
                    request.setAttribute("companyListSYS", (ArrayList) vsmr.viewCompanyList());
                    request.setAttribute("industryListSYS", (ArrayList) vsmr.populateCompanyIndustry());
                    request.setAttribute("userMessageListTopThreeSYS", usmr.viewUserMessageListTopThree(loggedInUsername));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "ViewCompanyListingSYS";
                    break;
                case "goToViewCompanyDetailsSYS":
                    long companyID = Long.parseLong(request.getParameter("hiddenCompanyID"));
                    
                    request.setAttribute("companyDetailsSYS", vsmr.viewCompanyDetails(companyID));
                    request.setAttribute("associatedReviewListSYS", vsmr.viewAssociatedReviewList(companyID, loggedInUsername));
                    request.setAttribute("companyListInIndustrySYS", vsmr.viewCompanyInSameIndustry(companyID));
                    request.setAttribute("userMessageListTopThreeSYS", usmr.viewUserMessageListTopThree(loggedInUsername));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "ViewCompanyDetailsSYS";
                    break;
                case "goToViewReviewListSYS":
                    long companyID_ = Long.parseLong(request.getParameter("hiddenCompanyID"));
                    String type = request.getParameter("type");
                    request.setAttribute("companyDetailsSYS", vsmr.viewCompanyDetails(companyID_));
                    request.setAttribute("associatedReviewListSYS", vsmr.viewAssociatedReviewList(companyID_, loggedInUsername));
                    request.setAttribute("companyListInIndustrySYS", vsmr.viewCompanyInSameIndustry(companyID_));
                    request.setAttribute("userMessageListTopThreeSYS", usmr.viewUserMessageListTopThree(loggedInUsername));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    request.setAttribute("tabType", type);
                    pageAction="ViewCompanyDetailsSYS";
                    break;
                case "goToNewCompanyRequestSYS":
                    request.setAttribute("industryStrSYS", vsmr.populateCompanyIndustryString());
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "NewCompanyRequestSYS";
                    break;
                case "createRequestSYS":
                    responseMessage = createCompanyRequest(request);
                    if (responseMessage.endsWith("!")) { request.setAttribute("successMessage", responseMessage); } 
                    else { request.setAttribute("errorMessage", responseMessage); }
                    
                    request.setAttribute("companyListSYS", (ArrayList) vsmr.viewCompanyList());
                    request.setAttribute("industryListSYS", (ArrayList) vsmr.populateCompanyIndustry());
                    request.setAttribute("userMessageListTopThreeSYS", usmr.viewUserMessageListTopThree(loggedInUsername));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "ViewCompanyListingSYS";
                    break;
                case "goToNewReviewReportSYS":
                    String reviewReporter = request.getParameter("hiddenReviewReporter");
                    long hiddenReviewID = Long.parseLong(request.getParameter("hiddenReviewID"));
                    request.setAttribute("hiddenCompanyID", request.getParameter("hiddenCompanyID"));
                    request.setAttribute("hiddenReviewID", request.getParameter("hiddenReviewID"));
                    request.setAttribute("hiddenReviewPoster", request.getParameter("hiddenReviewPoster"));
                    request.setAttribute("hiddenRequest", vsmr.lookupReviewReport(reviewReporter, hiddenReviewID));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "NewReviewReportSYS";
                    break;
                case "createReviewReportSYS":
                    responseMessage = createReviewReport(request);
                    if (responseMessage.endsWith("!")) { request.setAttribute("successMessage", responseMessage); } 
                    else { request.setAttribute("errorMessage", responseMessage); }
                    
                    long returnCompanyID = Long.parseLong(request.getParameter("hiddenCompanyID"));
                    request.setAttribute("companyDetailsSYS", vsmr.viewCompanyDetails(returnCompanyID));
                    request.setAttribute("associatedReviewListSYS", vsmr.viewAssociatedReviewList(returnCompanyID, loggedInUsername));
                    request.setAttribute("companyListInIndustrySYS", vsmr.viewCompanyInSameIndustry(returnCompanyID));
                    request.setAttribute("userMessageListTopThreeSYS", usmr.viewUserMessageListTopThree(loggedInUsername));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "ViewCompanyDetailsSYS";
                    break;
                case "likeReviewListingSYS":
                    long reviewIDHid = Long.parseLong(request.getParameter("reviewIDHid"));
                    
                    responseMessage = vsmr.likeUnlikeReview(reviewIDHid, loggedInUsername);
                    response.setContentType("text/plain");
                    response.getWriter().write(responseMessage);
                    break;
                case "goToReviewDetails":
                    request.setAttribute("userMessageListTopThreeSYS", usmr.viewUserMessageListTopThree(loggedInUsername));
                    request.setAttribute("unreadNotificationCount", usmr.getUnreadNotificationCount(loggedInUsername));
                    pageAction = "ReviewDetails";
                    break;
                default:
                    break;
            }
            dispatcher = servletContext.getNamedDispatcher(pageAction);
            dispatcher.forward(request, response);       
        }
        catch(Exception ex) {
            log("Exception in VoicesSysUserController: processRequest()");
            ex.printStackTrace();
        }
    
    }
    
    private String createCompanyReview(HttpServletRequest request) {
        String companyIndustry = request.getParameter("hiddenCategoryName");
        String companyName = request.getParameter("hiddenCompanyName");
        String reviewTitle = request.getParameter("reviewTitle");
        String reviewPros = request.getParameter("companyPros");
        String reviewCons = request.getParameter("companyCons");
        String reviewRating = request.getParameter("companyRating");
        String employmentStatus = request.getParameter("employmentStatus");
        String salaryRange = request.getParameter("salaryRange");
        String loggedInUsername = getCookieUsername(request);
        
        System.out.println(companyName + "     " + employmentStatus + "    " + salaryRange);
        
        responseMessage = vsmr.createCompanyReview(companyIndustry, companyName, reviewTitle, 
                reviewPros, reviewCons, reviewRating, employmentStatus, salaryRange, loggedInUsername);
        return responseMessage;
    }
    
    private String createCompanyRequest(HttpServletRequest request) {
        String companyIndustry = request.getParameter("companyIndustry");
        String otherIndustry = request.getParameter("otherIndustry");
        String requestCompany = request.getParameter("requestCompany");
        String requestComment = request.getParameter("requestComment");
        String loggedInUsername = getCookieUsername(request);
        
        if(companyIndustry.equals("otherIndustry")) {
            companyIndustry = otherIndustry;
        }
        
        responseMessage = vsmr.createCompanyRequest(requestCompany, companyIndustry, requestComment, loggedInUsername);
        return responseMessage;
    }
    
    private String createReviewReport(HttpServletRequest request) {
        String loggedInUsername = getCookieUsername(request);
        String reportDescription = request.getParameter("reportDescription");
        String reviewPoster = request.getParameter("hiddenReviewPoster");
        String reviewID = request.getParameter("hiddenReviewID");
        
        responseMessage = vsmr.createReviewReport(reviewID, reviewPoster, reportDescription, loggedInUsername);
        return responseMessage;
    }
    
    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
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
    public String getServletInfo() { return "Voices (Shout) System User Servlet"; }
    
    /* MISCELLANEOUS METHODS */
    private String getCookieUsername(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String loggedInUsername = null;
        if(cookies!=null){
            for(Cookie c : cookies){
                if(c.getName().equals("username") && !c.getValue().equals("")){
                    loggedInUsername = c.getValue();
                }
            }
        }
        return loggedInUsername;
    }
}