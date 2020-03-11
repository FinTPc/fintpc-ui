$(function(){
	
	$('#btnLiveStart').click(function(){
		var btn = $(this);
		
		createConfirmDialog($$confirmLive, function(){
			btn.prop('disabled', true);
			
			reset();
			clearTable();
			
			//ajax get
			$.ajax({
				method : 'GET',
				url : 'component-trace',
				dataType : 'json',
				success : function(data) {
					showListAndInfo(data);
					btn.prop('disabled', false);
				},
				error: function(data) {
					//console.log('error');
					handleAPIError(data);
					btn.prop('disabled', false);
					reset();
				}
			});
		});
	});
	
	$('#reportDate').on('dp.show', function(){
		reset();
	});
	
	$('#reportDate').on('dp.hide', function(e){
		var date = $('#reportDate').val();
		reset();
		
		//ajax get
		$.ajax({
			data : {
				date : date
			},
			method : 'GET',
			url : 'component-timestamps',
			dataType : 'json',
			success : function(data) {
				$.each(data, function(i, item){
					$('#timestamps').append(
						$('<a href="#" class="list-group-item list-group-item-action">')
							.text(item.time)
							.click(function(e){
								e.preventDefault();
								
								$(this).parent().find('a').removeClass('active');
								$(this).addClass('active');
								
								$('#btnSearch').prop('disabled', false);
							})
					);
				});
				
				//$('#timestamps').show();
				if (data.length > 0)
					$('.secondStage').show();
			}
		});
	});
	
	$('#btnSearch').click(function(){
		clearTable();
		
		var date = $('#reportDate').val();
		var time = $('#timestamps a.active').text();
		var name = $('#serviceMaps').val();
		var thread = $('#thread').val();
		
		$.ajax({
			data : {
				timestamp : date + " " + time,
				name: name,
				thread: thread
			},
			method : 'GET',
			url : 'component-report',
			dataType : 'json',
			success : function(data) {
				showListAndInfo(data);
				
				registerShowIntervals();
			}
		});
	});
	
	$('#ToPDF').click(function(){
		//delete row template
		var bypass = $('#exportPDF .dynamicTemplate').clone();
		$('#exportPDF .dynamicTemplate').remove();
		
		var pdf = new jsPDF('l', 'pt', 'a4')

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
		        return true;
		    }
		}
		
		margins = {
		    top: 40,
		    bottom: 40,
		    left: 40,
		    width: 750
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
		    	pdf.save('component-report.pdf');
		    	//clonedSource.remove();
		      },
		    margins
		  );
		
		//put row template back
		$('#exportPDF tr:first').after(bypass);
	});
	
	$('#ToExcel').click(function(){
		var originalTable = $('#result-table');
		var clonedTable = originalTable.clone();
		clonedTable.hide();
		clonedTable.attr('id', 'result-table-copy');
		var pre = originalTable.prev();
		//var post = originalTable.next();
		
		clonedTable.find('tbody > tr:first').before($('<tr><td colspan="7">' + pre.text() + '</td></tr>'));
		//clonedTable.find('tbody > tr:last').after($('<tr><td colspan="2">' + post.text() + '</td></tr>'));
		
		originalTable.after(clonedTable);
		
		var wb = XLSX.utils.table_to_book(document.getElementById('result-table-copy'), {raw: true});
		
		XLSX.writeFile(wb, 'component-report.xlsx');
		
		clonedTable.remove();
	});
	
});

function showListAndInfo(data) {
	showResultList($('#exportPDF .dynamicTemplate'), data);
	//showResultList($('#exportExcel .dynamicTemplate'), data);
	
	if (data[0] !== undefined) {
		var extraInfo = {
				date: data[0].date,
				time: data[0].time
		};
		
		showResult($('#result'), extraInfo);
	}
}

function clearTable() {
	hideResult($('#result'));
	$('#result .dynamicResult').remove();
}

function reset() {
	//$('#timestamps').hide();
	$('.secondStage').hide();
	$('#timestamps').empty();
	
	$('#serviceMaps').val('');
	$('#thread').val('');
	
	$('#btnSearch').prop('disabled', true);
	
	hideResult($('#result'));
}