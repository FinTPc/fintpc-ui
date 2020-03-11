
$(function() {
	/*$('#input').keyup(function (e) {
  	    if (e.keyCode === 13) {
  	    	var txt;
  	    	var t = "";
  	    	var value = $('#input').val();
  	    	var table_size = document.getElementById('eventsTable').getElementsByTagName('tr').length;
  	    	
  	    	for(var i=0; i<table_size; i++){
  	    		txt = t.concat("row").concat(""+i).concat("col2");
  	    		if(document.getElementById(txt).innerHTML != value){
  	    			document.getElementById(""+(i+1)).style.visibility='hidden'
  	    		}
  	    	}
  	    }
	});*/

	
	$('#input1').keyup(function (e) {
  	    if (e.keyCode === 13) {
  	    var value = $('#input1').val();
  	    window.location="events.htm?"+"filter_correlationid="+value;
  	    }
	});
	$('#input2').keyup(function (e) {
  	    if (e.keyCode === 13) {
  	    var value = $('#input2').val();
  	    window.location="events.htm?"+"filter_type="+value;
  	    }
	});
	$('#input3').keyup(function (e) {
  	    if (e.keyCode === 13) {
  	    var value = $('#input3').val();
  	    window.location="events.htm?"+"filter_date="+value;
  	    }
	});
	
	  
}); 

/*$(function() {
	$('#filter').click(function(){
		
		if (x==0){
		document.getElementById("filterEvents").style.visibility='visible';
		x=1;
		}
		else{
			document.getElementById("filterEvents").style.visibility='hidden';
			x=0;}
	
	});
});*/

$(function() {
  	$( "#dialog" ).dialog({
    	autoOpen: false,
    	show: {
        	effect: "blind",
        	duration: 100,
    	},
      	close: function() { 
            $(document).unbind('click');
            $( "#dialog" ).empty();
        }
	});
   $( "*[data-index]" ).click(function() {
   		$( "#dialog" ).dialog( "open" );
        var txt="";
        var txt_info = txt.concat("hide").concat($(this).closest("tr").index());
        $( "#dialog" ).append("<p>");
        $( "#dialog" ).append(""+document.getElementById(txt_info).innerHTML + "<br/>");
       	$( "#dialog" ).append("</p>");
   });
});

