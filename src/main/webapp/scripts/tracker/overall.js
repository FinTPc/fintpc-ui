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
				url : 'overall-trace',
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
			url : 'overall-timestamps',
			dataType : 'json',
			success : function(data) {
				$.each(data, function(i, item){
					$('#timestamps').append(
						$('<a href="#" class="list-group-item list-group-item-action">')
							.text(item.time)
							.click(function(e){
								e.preventDefault();
								clearTable();
								
								$.ajax({
									data : {
										timestamp : item.date + " " + item.time
									},
									method : 'GET',
									url : 'overall-report',
									dataType : 'json',
									success : function(data) {
										showListAndInfo(data);
									}
								});
							})
					);
				});
				
				//$('#timestamps').show();
				if (data.length > 0)
					$('.secondStage').show();
			}
		});
	});
	
	$('#ToPDF').click(function(){
		//delete row template
		var bypass = $('#exportPDF .dynamicTemplate').clone();
		$('#exportPDF .dynamicTemplate').remove();
		
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
		        return true;
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
		    	pdf.save('overall-report.pdf');
		    	//clonedSource.remove();
		      },
		    margins
		  );
		
		//put row template back
		$('#exportPDF tr:first').after(bypass);
	});
	
	$('#ToExcel').click(function(){
		var originalTable = $('#exportExcel');
		
		var template = [];
		
		//empty template rows
		originalTable.find('.dynamicTemplate').each(function(i){
			item = $(this);
			console.log(item);
			template.push(item.clone());
			item.empty();
		});
		
		var wb = XLSX.utils.table_to_book(document.getElementById('exportExcel'), {raw: true});
		
		XLSX.writeFile(wb, 'overall-report.xlsx');
		
		//put template rows back
		originalTable.find('.dynamicTemplate').each(function(i){
			item = $(this);
			item.replaceWith(template[i]);
		});
	});
	
});

function showListAndInfo(data) {
	showResultList($('#exportPDF .dynamicTemplate'), data);
	showResultList($('#exportExcel .dynamicTemplate'), data);
	
	registerShowIntervals();
	
	if (data[0] !== undefined) {
		var extraInfo = {
				date: data[0].date,
				time: data[0].time,
				events: data[0].events
		};
		
		showResult($('#result'), extraInfo);
	}
}

function clearTable() {
	//hideResult($('#result'));
	$('#result .dynamicResult').remove();
}

function reset() {
	$('.secondStage').hide();
	$('#timestamps').empty();
	
	hideResult($('#result'));
}