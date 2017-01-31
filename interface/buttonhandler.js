(function() {

  document.getElementById("Submit").onclick = function(event) {
	event.preventDefault();
	if ($("#input_text").prop("defaultValue") == $("#input_text").val()){
		alert("Please enter another text than the default text")
	} else {  
      var text = $("#input_text").val();
	  addText(text);
      return false;
	}
  };
  
    function handleFileSelect(evt) {
    var files = evt.target.files; // FileList object
    //Looping over files
    for (var i = 0, f; f = files[i]; i++) {
      var reader = new FileReader();
      reader.onload = (function(theFile) {
        return function(e) {
       	  $("#loading").hide();
          //TODO plug request here
		  console.log(e.target.result)
        };
      })(f);

      // Read in the image file as a data URL.
      reader.readAsText(f);
      $("#loading").show();
	  console.log("started text read")
    }
  }
  
  document.getElementById('input_file').addEventListener('change', handleFileSelect, false);
  
  function makeRequest(url) {
	  
	  if ($('#cb_NC').checked){
        var cb_NC = 'True';}
      if ($('#cb_ADJ').checked){
        var cb_ADJ = 'True';}

      if ($('#lang_DE').checked) {
		var lang = "DE";}
      else if ($('#lang_EN').checked) {
		var lang = "EN"
          }
		  
    // This function starts the request
    // also shows the loading gif and hides the current table
    // it will be newly shown when the request finishes
	
    httpRequest = new XMLHttpRequest();
    $("#loading").show();
    if (!httpRequest) {
      alert('Giving up :( Cannot create an XMLHTTP instance');
      return false;
    }
    httpRequest.onreadystatechange = alertContents;
    httpRequest.open('GET', url);
    httpRequest.send();
  }

  function alertContents() {
    // Checks for the ready state and status of the respons
      // throws an error if the .get response is != 200
      // also starts the create_table function from static/js/createTable.js
    if (httpRequest.readyState === XMLHttpRequest.DONE) {
      if (httpRequest.status === 200) {
          $("#loading").hide();
          var Text = JSON.parse(httpRequest.responseText);
          if (Text[0] == undefined){
            alert('The request did not return a result')}
          //TODO hook result function here
      else {
          alert('There was a problem with the request.');
      }
    }
  }
}

  function addText(passedText){
	  if ($("#" + passedText).length) {
		 var fontSize = parseInt($("#" + passedText).css("font-size"));
		 if (isNaN(fontSize)){
			 fontSize = 10;
			}
	     console.log(fontSize);
		 ($("#" + passedText)).css("font-size", fontSize + 20);
		 }
	   else {
		console.log("does not exist")
		jQuery('<div/>', {
		id: passedText,
		text: passedText,		
	    }).appendTo('.outer-container');
	  }
  }

}

)();
