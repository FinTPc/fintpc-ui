$(function(){
	registerUIControls();
});

function registerUIControls() {
	registerMultiSelect();
}

function registerMultiSelect() {
	$('select[multiple]').multiselect({
		buttonWidth: '80%',
		maxHeight: 200,
		enableFiltering: true,
		//includeSelectAllOption: true
	});
}