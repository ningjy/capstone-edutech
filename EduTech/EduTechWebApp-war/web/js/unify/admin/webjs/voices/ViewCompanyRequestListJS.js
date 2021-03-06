var rowItemName;
function addCompany(companyID) {
    var table = document.getElementById("companyRequestListing");
    for(var i=1;i<table.rows.length;i++) {
        var poster = table.rows.item(i).cells.item(1).innerHTML;
        var requestCompany = table.rows.item(i).cells.item(2).innerHTML;
        if(requestCompany.includes("&")) { requestCompany = requestCompany.replace("&","%26"); }
        var requestIndustry = table.rows.item(i).cells.item(3).innerHTML;
        requestIndustry = requestIndustry.substring(requestIndustry.indexOf("</span>")+7);
        if(requestIndustry.includes("&")) { requestIndustry = requestIndustry.replace("&","%26"); }
        if(requestCompany===companyID) {
            break;
        }
    }
    
    $('iframe').attr('src', 'VoicesAdmin?pageTransit=goToAddCompany&requestPoster=' + poster + '&requestCompany=' + requestCompany + '&requestIndustry=' + requestIndustry);
    $('#newCompany-iframe').iziModal('open', event);
    
}
$(document).ready(function() {

    $('#datatable-responsive tbody').on('click', 'tr', function(event) {
        var $cell= $(event.target).closest('td');
        if($cell.index() > 0 && $cell.index() < 4) {
            var rowData = $(this).children("td").map(function() {
                return $(this).text();
            }).get();
            rowRequestPosterID = $.trim(rowData[1]);
            rowRequestCompany = $.trim(rowData[2]);
            $('iframe').attr('src', 'VoicesAdmin?pageTransit=goToViewCompanyRequestListDetails&requestPosterID=' + rowRequestPosterID + '&requestCompany=' + rowRequestCompany);
            $('#modal-iframe').iziModal('open', event);
        }
        
    });
    
    $("#modal-iframe").iziModal({
        title: 'Request Details',
        iconClass: 'fa fa-cubes',
        transitionIn: 'transitionIn',
        transitionOut: 'transitionOut',
        headerColor: '#337AB7',
        width: 500,
        overlayClose: true,
        iframe : true,
        iframeHeight: 325,
        onClosed: function () { window.location.reload(true); }
    });
    
    $('#newCompany-iframe').iziModal({
        title: 'New Company',
        subtitle: 'Fill in the information of the new company here',
        iconClass: 'fa fa-cubes',
        transitionIn: 'transitionIn',
        transitionOut: 'transitionOut',
        headerColor: '#4D7496',
        width: 775,
        overlayClose: true,
        iframe : true,
        iframeHeight: 350,
        onClosed: function () { window.location.reload(true); }
    });
    
    $('#closeSuccess').click(function() { $('#successPanel').fadeOut(300); });
    $('#closeError').click(function() { $('#errorPanel').fadeOut(300); });
});