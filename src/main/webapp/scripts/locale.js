function changeLocale(combo) {
	var selectedItem = combo.options[combo.selectedIndex];
	if(document.URL.indexOf("?") > - 1){
		var query = location.search.substring(1);
		var params = query.split("&");
		var containsLang = false;
		for (var i = 0; i < params.length; i++){
			var parameterName = (params[i].split("="))[0];
			if (parameterName == "lang"){
				containsLang = true;
			}
		}
		if(containsLang){
			var text =  window.location.href;
			//replace value between 'lang=' and '&' with new value
			var newUrl =  text.replace(/(lang=).*?(&)/,'$1' + selectedItem.value + '$2');
			//treat the case when lang is last parameter
			if(newUrl == window.location.href){
				text+= "&";
				newUrl =  text.replace(/(lang=).*?(&)/,'$1' + selectedItem.value + '$2');
				newUrl = newUrl.substring(0, newUrl.length -1);
			}	
			window.location.href = newUrl;
		}else{
			window.location.href = document.URL + "&lang=" + selectedItem.value;
		}
		
	}else{
		window.location.href = "?lang=" + selectedItem.value;
	}
}