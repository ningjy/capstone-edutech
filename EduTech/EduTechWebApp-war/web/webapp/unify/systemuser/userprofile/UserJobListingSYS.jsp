<%@include file="/webapp/commoninfrastructure/SessionCheck.jspf" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Vector"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Unify - My Job Listing</title>

        <!-- CASCADING STYLESHEET -->
        <link href="css/unify/systemuser/baselayout/bootstrap-v4.min.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/animate-v3.5.2.min.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/font-awesome-v4.7.0.min.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/owl.carousel-v2.2.1.min.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/owl.theme.default.min.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/nouislider-v11.0.3.min.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/style.min.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/iziModal.min.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/weblayout/userprofile/UserItemTransactionCSS.css" rel="stylesheet" type="text/css" />

        <link href="css/unify/systemuser/baselayout/datatable/dataTables.bootstrap.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/datatable/dataTables.responsive.css" rel="stylesheet" type="text/css">
        <link href="css/unify/systemuser/baselayout/datatable/jquery.dataTables.min.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <!-- MOBILE SIDE NAVIGATION -->
        <nav class="offcanvas">
            <div class="offcanvas-content">
                <div id="list-menu" class="list-menu list-group" data-children=".submenu">
                    <a href="ProfileSysUser?pageTransit=goToUnifyUserAccountSYS"><i class="fa fa-fw fa-home"></i>&nbsp;Unify Home</a>
                    <div class="submenu">
                        <a data-toggle="collapse" href="#" data-target="#marketplaceSub" role="button" aria-expanded="false" aria-controls="marketplaceSub"><i class="fa fa-fw fa-file"></i>&nbsp;Marketplace</a>
                        <div id="marketplaceSub" class="collapse" data-parent="#list-menu" role="tabpanel"><a href="MarketplaceSysUser?pageTransit=goToViewItemListingSYS">Item Listing</a></div>
                    </div>
                    <div class="submenu">
                        <a data-toggle="collapse" href="#" data-target="#errandsSub" role="button" aria-expanded="false" aria-controls="errandsSub"><i class="fa fa-fw fa-file"></i>&nbsp;Errands</a>
                        <div id="errandsSub" class="collapse" data-parent="#list-menu" role="tabpanel"><a href="ErrandsSysUser?pageTransit=goToViewJobListingSYS">Errands Listing</a></div>
                    </div>
                    <div class="submenu">
                        <a data-toggle="collapse" href="#" data-target="#companyReviewSub" role="button" aria-expanded="false" aria-controls="companyReviewSub"><i class="fa fa-fw fa-user"></i>&nbsp;Company Review</a>
                        <div id="companyReviewSub" class="collapse" data-parent="#list-menu" role="tabpanel"><a href="VoicesSysUser?pageTransit=goToViewCompanyListingSYS">Company Listing</a></div>
                    </div>
                    <a href="ProfileSysUser?pageTransit=goToUnifyUserAccountSYS"><i class="fa fa-fw fa-home"></i>&nbsp;Unify Home</a>
                </div>
            </div>
        </nav>
        <div class="content-overlay"></div>

        <!-- PAGE TOP HEADER -->
        <div class="top-header">
            <div class="container">
                <div class="row">
                    <div class="col">
                        <div class="d-flex justify-content-between">
                            <nav class="nav">
                                <a class="nav-item nav-link d-sm-block" href="#">Unify @ EduBox</a>
                            </nav>
                            <ul class="nav">
                                <li class="nav-item d-none d-md-block">
                                    <a href="ProfileSysUser?pageTransit=goToViewChatListSYS&assocItemID=" class="nav-link"><i class="fa fa-comment"></i>&nbsp;&nbsp;My Chats</a>
                                </li>
                                <li class="nav-item d-none d-md-block">
                                    <div class="dropdown-container">
                                        <a href="#" class="nav-link" id="dropdown-cart" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="display: block;">
                                            <i class="fa fa-bell"></i>&nbsp;&nbsp;Notifications
                                        </a>
                                        <div class="dropdown-menu dropdown-menu-cart" aria-labelledby="dropdown-cart">
                                            <% 
                                                ArrayList<Vector> userMessageListTopThreeSYS = (ArrayList) request.getAttribute("userMessageListTopThreeSYS");
                                                if (!userMessageListTopThreeSYS.isEmpty()) {
                                                    for (int i = 0; i <= userMessageListTopThreeSYS.size() - 1; i++) {
                                                        Vector v = userMessageListTopThreeSYS.get(i);
                                                        String messageContent = String.valueOf(v.get(0));
                                                        String contentID = String.valueOf(v.get(1));
                                                        String messageType = String.valueOf(v.get(2));
                                                        String messageSenderImage = String.valueOf(v.get(4));
                                                        String messageSentDuration = String.valueOf(v.get(5));
                                            %>
                                            <div id="<%= messageType%><%= contentID%>" class="media messageDIV">
                                                <%  if (messageType.equals("System")) {%>
                                                <img class="img-thumbnail d-flex" src="images/<%= messageSenderImage%>" style="width:35px;height:35px;" />
                                                <%  } else {%>
                                                <img class="img-thumbnail d-flex" src="uploads/commoninfrastructure/admin/images/<%= messageSenderImage%>" style="width:35px;height:35px;" />
                                                <%  }%>
                                                <div class="message-content pl-3">
                                                    <div><%= messageContent%></div>
                                                    <small class="font-weight-normal message-content">
                                                        <i class="fa fa-clock-o"></i>&nbsp;<%= messageSentDuration%>&nbsp;(<%= messageType%>)
                                                    </small>
                                                </div>
                                            </div>
                                            <div class="dropdown-divider"></div>
                                            <%      }   %>
                                            <%  } else {    %>
                                            <p style="text-align:center;">There are no notifications.</p>
                                            <div class="dropdown-divider"></div>
                                            <%  }   %>
                                            <div class="text-center">
                                                <div class="btn-group btn-group-sm" role="group">
                                                    <a href="ProfileSysUser?pageTransit=goToUserNotificationListSYS" role="button" class="btn btn-outline-theme">
                                                        <i class="fa fa-envelope"></i>&nbsp;&nbsp;See All Notifications
                                                    </a>
                                                </div>
                                            </div>
                                            <div class="dropdown-divider"></div>
                                        </div>
                                    </div>
                                </li>
                                <select class="select-dropdown-nav accountNavigation" data-width="120px">
                                    <option value="#" selected data-before='<i class="fa fa-user align-baseline" /></i>'>&nbsp;&nbsp;<%= loggedInUsername%></option>
                                    <option value="CommonInfra?pageTransit=goToCommonLanding" data-before='<i class="fa fa-external-link align-baseline" /></i>'>&nbsp;&nbsp;Landing Page</option>
                                    <option value="ProfileSysUser?pageTransit=goToUnifyUserAccountSYS" data-before='<i class="fa fa-user-circle align-baseline" /></i>'>&nbsp;&nbsp;My Account</option>
                                    <option value="CommonInfra?pageTransit=goToLogout" data-before='<i class="fa fa-sign-out align-baseline" /></i>'>&nbsp;&nbsp;Logout</option>
                                </select>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- PAGE MIDDLE NAVIGATION -->
        <div class="middle-header">
            <div class="container">
                <div class="row py-2 py-lg-0">
                    <div class="col-2 col-sm-1 d-block d-lg-none">
                        <div class="d-flex align-items-center justify-content-center menu-btn-wrapper">
                            <button class="btn btn-lg border-0 btn-link offcanvas-btn p-0" type="button">
                                <i class="fa fa-bars"></i>
                            </button>
                        </div>
                    </div>
                    <div class="col-2 col-sm-1 col-lg-3 pr-0">
                        <div class="d-flex align-items-center logo-wrapper">
                            <a href="index.html" class="d-lg-none">
                                <img src="images/edubox-logo.png" class="logo" />
                            </a>
                            <a href="index.html" class="d-none d-lg-flex mb-2 mb-lg-0">
                                <img src="images/edubox-logo.png" class="logo" />
                            </a>
                        </div>
                    </div>
                    <div class="col-8 col-sm-6 col-md-7 col-lg-6 mt-3">
                        <div class="d-flex align-items-center">
                            <div class="input-group input-group-search">
                                <div class="input-group-prepend d-none d-md-flex">
                                    <select class="select-dropdown">
                                        <option value="all">All Categories</option>
                                        <option value="marketplace">Marketplace</option>
                                        <option value="errands">Errands</option>
                                        <option value="companyReview">Company Review</option>
                                    </select>
                                </div>
                                <input type="text" class="form-control" id="search-input" placeholder="Search here..." aria-label="Search here..." autocomplete="off" />
                                <span class="input-group-append">
                                    <button class="btn btn-theme btn-search" type="button"><i class="fa fa-search"></i></button>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="col-4 col-sm-4 col-md-3 col-lg-3 d-none d-sm-block mt-3">
                        <div class="d-flex align-items-center float-right abg-secondary">
                            <div class="btn-group btn-group-sm mr-3" role="group">
                                <a class="btn btn-outline-theme" href="MarketplaceSysUser?pageTransit=goToNewItemListingSYS" role="button">
                                    <i class="fa fa-user-plus d-none d-lg-inline-block"></i>&nbsp;Sell An Item
                                </a>
                                <a class="btn btn-outline-theme" href="ErrandsSysUser?pageTransit=goToNewJobListingSYS" role="button">
                                    <i class="fa fa-user-plus d-none d-lg-inline-block"></i>&nbsp;Post A Job
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="container">
            <div id="unifyPageNAV"></div>
            <!-- BREADCRUMB -->
            <div class="breadcrumb-container">
                <div class="container">
                    <nav aria-label="breadcrumb" role="navigation">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="ProfileSysUser?pageTransit=goToUnifyUserAccountSYS">Unify Home</a></li>
                            <li class="breadcrumb-item active" aria-current="page">Marketplace Transaction</li>
                        </ol>
                    </nav>
                </div>
            </div>

            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-4 mb-4 mb-md-0">
                        <div class="card user-card">
                            <%
                                Vector userAccountVec = (Vector) request.getAttribute("userAccountVec");
                                String username, userFirstName, userLastName, userImage, userCreationDate;
                                username = userFirstName = userLastName = userImage = userCreationDate = "";

                                if (userAccountVec != null) {
                                    username = (String) userAccountVec.get(0);
                                    userFirstName = (String) userAccountVec.get(1);
                                    userLastName = (String) userAccountVec.get(2);
                                    userImage = (String) userAccountVec.get(3);
                                    userCreationDate = (String.valueOf(userAccountVec.get(4)));
                                }
                            %>
                            <div class="card-body p-2 mb-3 mb-md-0 mb-xl-3">
                                <div class="media">
                                    <img class="img-thumbnail" src="uploads/commoninfrastructure/admin/images/<%= userImage%>" style="width:50px;height:50px;"/>
                                    <div class="media-body ml-2">
                                        <h5 class="user-name"><strong><%= userFirstName%>&nbsp;<%= userLastName%></strong></h5>
                                        <p>@<%= username%></p>
                                        <small class="card-text text-muted mt-2">Joined <%= userCreationDate%></small>
                                    </div>
                                </div>
                            </div>
                            <div class="list-group list-group-flush">
                                <a href="ErrandsSysUser?pageTransit=goToViewJobOfferList&username=<%=loggedInUsername%>" class="list-group-item list-group-item-action">
                                    <i class="fa fa-briefcase" aria-hidden="true"></i>&nbsp;&nbsp;Your Job Offers (I received)
                                </a>
                                <a href="ProfileSysUser?pageTransit=goToViewMyJobOfferSYS" class="list-group-item list-group-item-action">
                                    <i class="fa fa-briefcase" aria-hidden="true"></i>&nbsp;&nbsp;Your Job Offers (I made)
                                </a>
                                <a href="ProfileSysUser?pageTransit=goToMyJobListing" class="list-group-item list-group-item-action">
                                    <i class="fa fa-briefcase" aria-hidden="true"></i>&nbsp;&nbsp;Your Job Listing
                                </a>
                                <a href="ProfileSysUser?pageTransit=goToMarketplaceTrans" class="list-group-item list-group-item-action">
                                    <i class="fa fa-fw fa-user"></i>&nbsp;Marketplace Transaction
                                </a>
                                <a href="account-address.html" class="list-group-item list-group-item-action">
                                    <i class="fa fa-fw fa-map-marker"></i>&nbsp;Errands Transaction
                                </a>
                                <a href="ProfileSysUser?pageTransit=goToCompanyReview" class="list-group-item list-group-item-action">
                                    <i class="fa fa-fw fa-building"></i>&nbsp;Company Review List
                                </a>
                                <a href="ProfileSysUser?pageTransit=goToCompanyRequest" class="list-group-item list-group-item-action">
                                    <i class="fa fa-fw fa-question-circle"></i>&nbsp;Company Request List
                                </a>
                                <a href="ProfileSysUser?pageTransit=goToResume" class="list-group-item list-group-item-action">
                                    <i class="fa fa-fw fa-file"></i>&nbsp;Resume List
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-9 col-md-8">
                        <div class="title"><span>Your Job Listing</span></div>
                        <div class="row">
                            <%
                                ArrayList<Vector> jobListing = (ArrayList) request.getAttribute("userJobListing");
                                if (jobListing != null) {
                                    for (int i = 0; i < jobListing.size(); i++) {
                                        Vector jobInfo = jobListing.get(i);

                                        long jobID = (Long) jobInfo.get(0);
                                        String jobImg = (String) jobInfo.get(1);
                                        String jobTitle = (String) jobInfo.get(2);
                                        String categoryName = (String) jobInfo.get(3);
                                        String userID = (String) jobInfo.get(4);
                                        String postTime = (String) jobInfo.get(6);
                                        String jobRate = (String) jobInfo.get(8);
                                        String rateType = (String) jobInfo.get(9);
                                        String jobRateType;
                                        if (rateType.equals("HR")) {
                                            jobRateType = "hour";
                                        } else {
                                            jobRateType = "Fixed Rate";
                                        }
                            %>
                            <div class="col-sm-6">
                                <div class="card">
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-md-4"><img src="uploads/unify/images/errands/job/<%= jobImg%>" style="height: 100px; width: 100px;"></div>
                                            <div class="col-md-8">
                                                <h5><%= jobTitle%></h5>
                                                <span><%= categoryName%></span><br/>
                                                <span>S$<%= jobRate%>/<%= jobRateType%></span><br/>
                                                <a href="ErrandsSysUser?pageTransit=goToEditJobListing&hiddenJobID=<%= jobID%>" class="btn btn-primary mt-2" style="padding: 5px 5px; font-size: 15px;">Edit</a>
                                                <a href="ErrandsSysUser?pageTransit=deleteJobListingSYS&hiddenJobID=<%= jobID%>" onclick="return confirm('Are you sure to delete the job?')" class="btn btn-danger mt-2" style="padding: 5px 5px; font-size: 15px;">Delete</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%
                                    }
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
            <div id="unifyFooter"></div>
            <div id="itemDetails-iframe"></div>
            
            <div class="chat-main">
                <div class="col-md-12 chat-header">
                    <div class="row header-one text-white p-1">
                        <div class="col-md-6 name pl-2">
                            <i class="fa fa-comment"></i>
                            <h6 class="ml-1 mb-0 mt-1">Unify Bot</h6>
                        </div>
                        <div class="col-md-6 options text-right pr-0">
                            <i class="fa fa-window-minimize hide-chat-box hover text-center"></i>
                        </div>
                    </div>
                </div>
                <div class="chat-content">
                    <div class="col-md-12 chats">
                        <iframe src="ProfileSysUser?pageTransit=goToUnifyBot" width="305" height="285" frameborder="0" ></iframe>
                    </div>
                </div>
            </div>
            
            <a href="#top" class="back-top text-center" onclick="$('body,html').animate({scrollTop: 0}, 500); return false">
                <i class="fa fa-angle-double-up"></i>
            </a>
        </div>


        <!-- #1. jQuery -> #2. Popper.js -> #3. Bootstrap JS -> #4. Other Plugins -->
        <script src="js/unify/systemuser/basejs/jquery-v3.2.1.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/popper.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/bootstrap-v4.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/bootstrap3-typeahead.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/owl.carousel-v2.2.1.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/nouislider-v11.0.3.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/style.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/iziModal.min.js" type="text/javascript"></script>

        <script src="js/unify/systemuser/basejs/datatable/dataTables.bootstrap.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/datatable/dataTables.responsive.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/basejs/datatable/jquery.dataTables.min.js" type="text/javascript"></script>
        <script src="js/unify/systemuser/webjs/userprofile/UserItemTransactionJS.js" type="text/javascript"></script>
    </body>
</html>