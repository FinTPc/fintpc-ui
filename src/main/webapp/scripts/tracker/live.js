$(function(){
	$('#btnLiveStart').click(function(){
		var btn = $(this);
		
		createConfirmDialog($$confirmLive, function(){
			btn.prop('disabled', true);
			
			var time = parseInt($('#timeInterval').val());
			
			reset();
			
			//ajax get
			$.ajax({
				data : {
					time : time
				},
				method : 'GET',
				url : 'live-trace',
				dataType : 'json',
				success : function(data) {
					showResult($('#result'), data);
				},
				error: function(data) {
					//console.log('error');
					handleAPIError(data);
					btn.prop('disabled', false);
					reset();
				}
			});
			
			//animation
			$('#liveProgress').parent().show();
			$('#liveProgress').animate({width: "100%"}, {
				duration: time*1000,
				complete: function() {
					//enable button 
					btn.prop('disabled', false);
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
			url : 'live-intervals',
			dataType : 'json',
			success : function(data) {
				$.each(data, function(i, item){
					$('#intervals').append(
						$('<a href="#" class="list-group-item list-group-item-action">')
							.text(item.interval)
							.click(function(e){
								e.preventDefault();
								var id = item.id;
								
								$.ajax({
									data : {
										id : id
									},
									method : 'GET',
									url : 'live-report',
									dataType : 'json',
									success : function(data) {
										showResult($('#result'), data);
									}
								});
							})
					);
				});
				
				//$('#intervals').show();
				if (data.length > 0)
					$('.secondStage').show();
			}
		});
	});
	
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
		}
		
		margins = {
		    top: 40,
		    bottom: 40,
		    left: 40,
		    width: 522
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
		    	pdf.save('live-report.pdf');
		    	//clonedSource.remove();
		      },
		    margins
		  )
	});
	
	$('#ToExcel').click(function(){
		var originalTable = $('#result-table');
		var clonedTable = originalTable.clone();
		clonedTable.hide();
		clonedTable.attr('id', 'result-table-copy');
		var pre = originalTable.prev();
		var post = originalTable.next();
		
		clonedTable.find('tbody > tr:first').before($('<tr><td colspan="2">' + pre.text() + '</td></tr>'));
		clonedTable.find('tbody > tr:last').after($('<tr><td colspan="2">' + post.text() + '</td></tr>'));
		
		originalTable.after(clonedTable);
		
		var wb = XLSX.utils.table_to_book(document.getElementById('result-table-copy'), {raw: true});
		
		XLSX.writeFile(wb, 'live-report.xlsx');
		
		clonedTable.remove();
	});
});

function reset() {
	$('#liveProgress').parent().hide();
	$('#liveProgress').css({width: "0%"});
	
	//$('#intervals').hide();
	$('.secondStage').hide();
	$('#intervals').empty();
	
	hideResult($('#result'));
}