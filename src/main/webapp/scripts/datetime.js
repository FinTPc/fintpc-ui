var $$dateFormat = "YYYY-MM-DD";
var $$hourFormat = "HH:mm:ss"; 

$(function(){
	registerDateTime();
});

function registerDateTime() {
	registerDatePicker();
	registerDateTimePicker();
	registerTimePicker();
	registerIntervalDateTimePicker();
	registerIntervalDatePicker();
	registerEventIntervalDateTimePicker();
}

function registerDatePicker() {
	$('.datePicker').each(function() {
		$(this).datetimepicker({
			format: $$dateFormat
		});
		
		$(this).removeClass('datePicker');
	});
}

function registerDateTimePicker() {
    $('.dateTimePicker').each(function(){
    	$(this).datetimepicker({
    		format: $$dateFormat + " " + $$hourFormat,
	    	useStrict: true,
	    	keepInvalid: true,
	    //	debug: true
	    });
    	$(this).off('focus');
    	$(this).click(function() {
    		$(this).datetimepicker('toggle');
    		$(this).focus();
		});
    	$(this).removeClass('dateTimePicker');
    });
}

function registerTimePicker() {
    $('.timePicker').each(function(){
    	$(this).datetimepicker({
    		format: $$hourFormat,
	    	useStrict: true,
    	//	keepInvalid: true,
	    });
    	$(this).off('focus');
    	$(this).click(function() {
    		$(this).datetimepicker('toggle');
    		$(this).focus();
		});
    	$(this).removeClass('timePicker');
    });
}

function registerIntervalDateTimePicker() {
	$('.intervalPicker').each(function(){
		$(this).daterangepicker({
			"showDropdowns": true,
			"timePicker": true,
		    "timePicker24Hour": true,
		    "timePickerSeconds": true,
		    "dateLimit": { //maxSpan in > v3
		        "M": 3
		    },
		    "locale": {
		        "format": $$dateFormat + " " + $$hourFormat,
		        "separator": " - ",
		        "firstDay": 1
		    },
		    "startDate": moment().startOf('day'),
		    "endDate": moment().endOf('day')
		});
		
		$(this).removeClass('intervalPicker');
	});
}

function registerIntervalDatePicker() {
	$('.intervalDatePicker').each(function(){
		$(this).daterangepicker({
			"showDropdowns": true,
			"timePicker": false,
			"autoUpdateInput": false,
		    "locale": {
		        //"format": "DD MM YYYY",
		        //"separator": " - ",
		        "firstDay": 1
		    }
		});
		$(this).on('apply.daterangepicker', function(ev, picker) {
			$(this).val(picker.startDate.format($$dateFormat) + ' - ' + picker.endDate.format($$dateFormat));
		});
		
		$(this).removeClass('intervalDatePicker');
	});
}

function registerEventIntervalDateTimePicker() {
	$('.eventIntervalPicker').each(function(){
		$(this).daterangepicker({
			"showDropdowns": true,
			"timePicker": true,
		    "timePicker24Hour": true,
		    "timePickerSeconds": true,
		    "dateLimit": { //maxSpan in > v3
		        "M": 3
		    },
		    "locale": {
		        "format": $$dateFormat + " " + $$hourFormat,
		        "separator": " - ",
		        "firstDay": 1
		    },
		    "startDate": moment().subtract(1, 'hours'),
		    "endDate": moment(),
		});
		
		$(this).removeClass('intervalPicker');
	});
}

