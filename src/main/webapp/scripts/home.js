/**
 * 
 */
	
$(function(){	 	
		var str = $('#refresh').text();
		var trans = {
				index : ["CstmrCdtTrfInitnSupp", "CstmrCdtTrfInitnVatx", "CstmrCdtTrfInitnOthr", "FinInvc", "CstmrCdtTrfInitnTaxs"],
				label : ["Supplier payments", "VAT payments", "Other payments", "Invoices", "Tax payments"],
				values : [0,0,0,0,0,0],
				valueBar: []
			 }
		$('.span-refresh').css("display", "inline-block");
		var timeRefresh = 1;// minute
		var count = 5;		
		bootstrap_alert = function() {}
		bootstrap_alert.warning = function(message) {
		            $('#alert').html('<div class="alert alert-success fade in right"><span>'+message+'</span></div>')
		        }
		// refresh(0);
		getTransaction();
		
		function drawPie(respons){
			var data = [{
    			  values: respons.values,
    			  labels: respons.label,
    			  type: 'pie',
    			  textposition: 'inside',
    			  hoverinfo: 'value+percent',
    			  textinfo: '+value',
    			}];

    			var layout = {
    			    title: 'TRANSACTIONS',
    			    font: {size: 18},
    				width: '50%',
    			};
    			Plotly.newPlot('myDiv2', data, layout, {showSendToCloud: true, displayModeBar: false});    	
		}
		
		function drawBar(valueBar){
			var titleBar = ["Sent", "Received"]		  
  			var trace1 = {
    			    type: 'bar',
    			    x: titleBar,
    			    y: valueBar,		
    			    
    			    textinfo: 'none',
    			    textposition: 'inside',
    			    marker: {
    			        color: ['#90CCEE', '#442080'],
    			        line: {
    			            width: 2.5,
    			        },
    			        size: 2,		    			        
    			    }
    			};

    		var data = [ trace1 ];

    		var layout = {
    		  title: 'Total transactions: ' + (valueBar[0] + valueBar[1]),
    		  font: {size: 18},
    		  width: '50%',
    		  autosize: false,
    		};
    		Plotly.newPlot('myDiv', data, layout, {displayModeBar: false});	
		}
		
		function refresh(){
				var timer = timeRefresh *60, minutes, seconds;
			    intervalCount = setInterval(function () {
			        minutes = parseInt(timer / 60, 10);
			        seconds = parseInt(timer % 60, 10);

			        minutes = minutes < 10 ? "0" + minutes : minutes;
			        seconds = seconds < 10 ? "0" + seconds : seconds;

			        $('#refresh').html("Refresh in " + minutes + ":" + seconds);

			        if (--timer < 0) {
			        	clearInterval(intervalCount);
			        	getTransaction();			   
			        }
			    }, 1000);
			
		}
		
		function getTransaction(){
			trans.values=[];
			trans.valueBar = [0, 0];
        		$.each(trans.index, function(index, value){
    				$.ajax({
    					type: "GET",
    					url: "./home/transactions?msgType=" + value,
    					beforeSend: function(){
    						if ($("#myDiv2").children().length == 1){
    							$("#loader").show();				
    						}
    					},
    					success: function (data) {
    		            	$("#loader").hide();
    		            	var result = JSON.parse(data);
    		            	if (result > 0 ){
    			            	var no_transaction = true;
    			            	var ind = trans.index.indexOf(value);
    			            	trans.values[ind] = (result);
    			            	drawPie(trans);			      			
    			            	trans.valueBar[0] = trans.valueBar[0] + result;		      			
    			      			drawBar(trans.valueBar);
    		            	}
    		            },
    		            error: function(err){
    						console.log(err);
    		            },
    		            complete: function(){
    		            	count--;
    		            	if (count == 0){    	
    		            		count = 5;
    		            		refresh();
    		            	}
    			            else{
    							$('#refresh').html("loading...");
    						}
    		            }
    				});
    			});

			$.ajax({
				type: "GET",				
				url: "./home/transactions/statements?msgType=BkToCstmrDbtCdtNtfctn",
				beforeSend: function(){
					if ($("#myDiv2").children().length == 1){				
						$("#loader").show();				
					}
				},
				success: function (data) {
	            	$("#loader").css("display", "none");
	            	var result = JSON.parse(data);
	            	trans.valueBar[1] = (result);
	            	trans.values[5] = (result);
	            	//trans.values.push(result);
	            	trans.label.push("Statement transactions");
	            	drawPie(trans);
	            	drawBar(trans.valueBar);	            	
				},				
				complete: function(){
					$("#loader").hide();
	            }
			});
			
			$.ajax({
				type: "GET",
				url: "./queues/count",
				beforeSend: function(){
					console.log();
				},
	            success: function (data) {
	            	var result = JSON.parse(data);
	            	$('#transaction-queue').text(result[0]);
	            	$('#queue-transaction').text(result[-1]);	            		                
	            }
			});

			$.ajax({
				type: "GET",
				url: "./events/count",			
	            success: function (data) {          	
	            	var result = JSON.parse(data);
	            	//count++;
	            	$('#processing-event-error').text(result['error_events']);
	            	$('#processing-event-total').text(result['total_events']);
	            }
			});			
		};
	});
