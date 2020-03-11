$(function(){
	$(".paginator").each(function(){
		loadPaginator($(this));
	});
});




//function loadPaginator(){
//	var totalRecords = $(".paginator").find("input[name='nbRecords']").val();
//	$('.paginator').puipaginator({  
//		totalRecords: $("#total").text() * 1,  
//		rows: $("#limit").val() * 1,
//        page: ($("#offset").val() /$("#limit").val()),
//        paginate: function(event, state) {  
//        	currentPage = $('.pui-paginator-element.ui-state-active').text();
//        	computeOffset();
//        	$("#offset").val(offset);
//    		$("#mainForm").submit();
//        	//here load properly
//        }  
//    });
//}

function loadPaginator(paginatorLocation){
	$(paginatorLocation).empty();
	
	var $paginator = $("<div>");
	
	var page = $(paginatorLocation).parent().find("input[name='page']").val();
	var total = $(paginatorLocation).parent().find("input[name='total']").val();
	var table = $(paginatorLocation).parent().find("table");
	
	$paginator.puipaginator({  
		totalRecords: total,  
		rows: $("#itemsPerPage").val(),
		page : page -1,
        paginate: function(event, state) {  
        	var newPage = state.page + 1;
        	$(paginatorLocation).parent().find("input[name='page']").val(newPage);
        	loadTable(table, false);
        }  
    });
	paginatorLocation.append($paginator);
}