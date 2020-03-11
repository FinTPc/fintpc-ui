/**
 * 
 */
	
$(function(){
	
		var values = [];
		var labels = [];
		
		var str = $('#refresh').text();
		$('.span-refresh').css("display", "inline-block");
		var timeRefresh = 1;// minute
		var count = 0;		
		bootstrap_alert = function() {}
		bootstrap_alert.warning = function(message) {
		            $('#alert').html('<div class="alert alert-success fade in right"><span>'+message+'</span></div>')
		        }
		getTransaction();
		
		function pieTransition(name, value){
			if (values.length > 0){
    			no_transaction = false;
    		}
        	values.push(value);
			console.log(values)
        	labels.push(name);
        	var data = [{
  			  values: values,
  			  labels: labels,
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
  			if (!no_transaction){
  				Plotly.newPlot('myDiv2', data, layout, {showSendToCloud: true, displayModeBar: false});
  			}
  			else{
  				$("#myDiv2").html("No transaction");
  			}
		}
		
		function getTransaction(){
			var value = [];
			var result = [];
			$.each(["CstmrCdtTrfInitnSupp","CstmrCdtTrfInitnVatx","CstmrCdtTrfInitnOthr", "FinInvc", "CstmrCdtTrfInitnTaxs"], function(item, value){
				$.ajax({
					type: "GET",
					url: "./home/transactions?msgType=" + value,
					succes: function( data){
						pieTransition(value, JSON.parse(data));
					},
				});
			});
			
			$.ajax({
				type: "GET",
				url: "./home/transactions/statements?msgType=BkToCstmrDbtCdtNtfctn",
				succes: function( data){
					var result = JSON.parse(data);
					console.log(result);
				},
			});
			
			$.ajax({
				type: "GET",
				url: "./queues/count",
				beforeSend: function(){
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
	            	if (count>0)
	            		bootstrap_alert.warning(update);
	            	count++;
	            	$('#processing-event-error').text(result['error_events']);
	            	$('#processing-event-total').text(result['total_events']);	            	
	            }
			});
			
			var countDown = new Date().getTime() + (timeRefresh * 60 * 1000);
			const p = Promise.resolve(countDown);
			const p1 = new Promise((resolve, reject) => {
				varCountDownFunc = setInterval(() => {
					p.then(value => {
						var now = new Date().getTime();
						var distance = value - now;
						distance = Math.floor((distance % (1000 * 60)) / 1000);
						$('#refresh').text(str + ' ' + (distance<10?'0'+distance:distance));
						var timeRemove = (timeRefresh * 60 - 4);
						if (distance == timeRemove){
							$('#alert').children().remove();
						}
						if (distance <= 0){
							clearInterval(varCountDownFunc);
							getTransaction();					
						}
					})
				}, 1000);
			});
			
			
			
			/*$.ajax({
				type: "GET",				
				url: "./home/transactions_count",
				beforeSend: function(){
					if ($("#myDiv2").children().length == 1){				
						$("#loader").css("display", "block");				
					}
				},
	            success: function (data) {
	            	$("#loader").css("display", "none");
	            	var result = JSON.parse(data);
	            	var values = [];
	            	var labels = [];
	            	var no_transaction = true;
	            	
	      			var valueBar = [];
	      			var titleBar = []
	      			$.each(result["payment"], function(item, value){	       
	            		valueBar.push(value);
	            		titleBar.push(item);
	            	})
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
	            },
	            error: function(err){
					console.log(err);			
			}				
			});	*/			
		};		
	});
