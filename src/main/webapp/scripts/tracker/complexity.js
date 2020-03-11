$(function(){
	$('#ToPDF').click(function(){
		var pdf = new jsPDF('p', 'pt', 'a4')

		// source can be HTML-formatted string, or a reference
		// to an actual DOM element from which the text will be scraped.
		, source = $('.content')[0]

		// we support special element handlers. Register them with jQuery-style
		// ID selector for either ID or node name. ("#iAmID", "div", "span" etc.)
		// There is no support for any other type of selectors
		// (class, of compound) at this time.
		, specialElementHandlers = {
		    // element with id of "bypass" - jQuery style selector
		    '.bypass-export': function(element, renderer){
		        // true = "handled elsewhere, bypass text extraction"
		        return true
		    }
			//,'printHeaders': false
		}
		
		margins = {
		    top: 20,
		    bottom: 20,
		    left: 40,
		    width: 800
		  };
		  // all coords and widths are in jsPDF instance's declared units
		  // 'inches' in this case
		pdf.fromHTML(
			source // HTML string or DOM elem ref.
		    , margins.left // x coord
		    , margins.top // y coord
		    , {
		        'width': margins.width // max width of content on PDF
		        , 'elementHandlers': specialElementHandlers
		    },
		    function (dispose) {
		      // dispose: object with X, Y of the last line add to the PDF
		      //          this allow the insertion of new lines after html
		    	pdf.save('workflow-complexity.pdf');
		      },
		    margins
		  )
	});
	
	$('#ToExcel').click(function(){
		var clonedTable = $('<table>');
		clonedTable.hide();
		clonedTable.attr('id', 'result-table-copy');
		
		$('.export-row').each(function(){
			$(this).find('.export-table-header').children().each(function(){
				clonedTable.append($('<tr><td colspan="4">' + $(this).text() + '</td></tr>')); //add header
			});
			
			clonedTable.append($(this).find('.export-table tr').clone()); //add row table
		});
		
		$('.form-horizontal').append(clonedTable);
		
		var wb = XLSX.utils.table_to_book(document.getElementById('result-table-copy'), {raw: true});
		
		XLSX.writeFile(wb, 'complexity-report.xlsx');
		
		clonedTable.remove();
	});
});