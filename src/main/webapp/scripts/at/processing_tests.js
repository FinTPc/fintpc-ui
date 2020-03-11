$(function() {
			
	$('#processingTestName').on('change',function() {
		
		var processingTestNameId = $(this).val();
		
		$('#processingTestType').find('option').not(':first').remove();			
		$('#inputDataSetType').find('option').not(':first').remove();
		$('#loadFile').remove();
		$('#downLink').remove();
		$('#confirmButton').remove();
		$('#displayDescription').val('');
		
		if(processingTestNameId !== ''){

			$.ajax({
				
				method : 'GET',
				url : './tests/' + processingTestNameId + "/transaction-type",
				dataType : 'json',
				success : function(data) {
					
					for (var key in data){
					    $('#processingTestType').append($('<option>', { 
					        value: key,
					        text : data[key] 
					    }));
					}		
				}
			});
			
			$.each(txProcessingTests, function(index, item){			
				if(item.id == processingTestNameId){
					$('#displayDescription').val(item.description);
				}
			});
		}		
	});
	
	// key = name of file , value = id of inputDataset
	var inputDatasetMap = {};
        
	$('#processingTestType').on('change',function() {
		
		var interfaceConfigId = $(this).val();
		
		$('#inputDataSetType').find('option').not(':first').remove();
		$('#loadFile').remove();
		$('#downLink').remove();
		$('#confirmButton').remove();	
		
		if(interfaceConfigId !== ''){

			$.ajax({
				
				method : 'GET',
				url : './tests/' + interfaceConfigId + "/input-dataset-type",
				dataType : 'json',
				success : function(data) {
					
					$('#inputDataSetType').find('option').not(':first').remove();									
					
					for (var key in data){
						
				        inputDatasetMap[key] = data[key].substring(data[key].indexOf(":") + 1);
				        
					    $('#inputDataSetType').append($('<option>', { 
					        value: key,					        
					        text : data[key].substring(0, data[key].indexOf(":"))
					    }));
					}		
				}
			});
		}		
	});
	
	$('#inputDataSetType').on('change',function() {
		
		// value = the name of the file to download
		var inputDataSet = $(this).val();
		
		$('#loadFile').remove();
		$('#downLink').remove();
		$('#confirmButton').remove();
		 
		if(inputDataSet !== ''){
			
			var loadDataButton = $('<button/>').attr({
                type: "button",
                id: "loadFile",
                'data-value': inputDataSet
			}).html("Load Data");
			
			$('.content').append(loadDataButton); 
			
			$('#loadFile').on('click',function() {
				// value = the name of the file to download
				var inputDataSetFileName = $(this).attr('data-value');	
				
				var transactionType = $('#processingTestType option:selected').text();
				
				var inputType;
				
				$.each(txProcessingTests, function(index, item){
					if(item.txtype === transactionType.trim()){
						inputType = item.interfaceconfig.inputtype						
						return false;
					}
				});

				if(!$('#downLink').length){
					$('.content').append("<div> <a id=\"downLink\" href=\"./tests/download-file/" + inputDataSetFileName + "\">Download File</a> </div>");
				}
				
				// pass in ajax ids for TxProcessingTestEntity si InputDatasetEntity
				var txProcessingTestId;			
				
				$.each(txProcessingTests, function(index, item){			
					if(item.name === $('#processingTestName option:selected').text() && item.txtype === $('#processingTestType option:selected').text()){
						txProcessingTestId = item.id;
						return false;
					}
				});
				
				var inputDatasetId = inputDatasetMap[inputDataSetFileName];
												
				$.ajax({
					
					method : 'GET',
					url : './tests/' + inputType + "/" + inputDataSetFileName + "/" + txProcessingTestId + "/" + inputDatasetId,
					dataType : 'text',
					success : function(data) {																			
						// data o sa fie un String cu msj - daca s-au sters sau nu fisierele (file) --- sau sa insereze in DB info (database)
						console.log("raspuns = " + data);
						
						if(data === "DB"){
														
							var confirmButton = $('<button/>').attr({
				                type: "button",
				                id: "confirmButton"
     
							}).html("Confirm database insert");
									
							$('.content').append(confirmButton); 
							
							alert("Insert in database information from file and the press confirm button");
						}
						else{
							alert(data);
						}
					}
				});
										
			});			
		}
	});
    
})
