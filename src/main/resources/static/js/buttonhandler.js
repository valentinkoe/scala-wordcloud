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
          makeRequest();
          //TODO plug request here
		  //console.log(e.target.result)
        };
      })(f);

      // Read in the image file as a data URL.
      reader.readAsText(f);
      $("#loading").show();
	  console.log("started text read")
    }
  }
  
  document.getElementById('input_file').addEventListener('change', handleFileSelect, false);
  
  function makeRequest() {
	  
    var cb_NC = 'false';
    var cb_ADJ = 'false';
	  if ($('#cb_NC').checked){
        var cb_NC = 'true';}
      if ($('#cb_ADJ').checked){
        var cb_ADJ = 'true';}

      if ($('#lang_DE').checked) {
		var lang = "de";}
      else if ($('#lang_EN').checked) {
		var lang = "en"
          }
    else{lang = "de"}

//    var url = "http://localhost:8080/tokens?lang=" + lang + "&adj=" + cb_NC +
//    "&noun=" + cb_NC

  var urltwo = "/tokens?lang=de&adj=true&noun=false"
  var url = "ws://127.0.0.1:8080/websocket?lang=de&adj=false&noun=false"
//  var url = "http://localhost:8080/streaming"

  var socket = new WebSocket(url)

  socket.onopen = function (event) {
    socket.send("test test test text text text")
    console.log("socket opened")
  };

  socket.onmessage = function(event) {
    console.log(event.data)
//    addText(event.data)
  }

}

  function addText(passedText){
	  if ($("#" + passedText).length) {
		 var fontSize = parseInt($("#" + passedText).css("font-size"));
		 if (isNaN(fontSize)){
			 fontSize = 10;
			}
	     console.log(fontSize);
		  ($("#" + passedText)).animate({
                        fontSize: fontSize + 20
                    }, 500);
		 //($("#" + passedText)).css("font-size", fontSize + 20);
		 }
	   else {
		console.log("does not exist")
		jQuery('<div/>', {
		id: passedText,
    class: "vertical-text",
		text: passedText,		
	    }).appendTo('.outer-container');
	  }
  }

}

)();

