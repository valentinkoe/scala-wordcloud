(function() {
wordslist = {};

  document.getElementById("Submit").onclick = function(event) {
	event.preventDefault();
	if ($("#input_text").prop("defaultValue") == $("#input_text").val()){
		alert("Please enter another text than the default text")
	} else {
      var text = $("#input_text").val();
      makeRequest(text);
      return false;
	}
  };
  
    function handleFileSelect(evt) {
    var files = evt.target.files; // FileList object
    for (var i = 0, f; f = files[i]; i++) {
      var reader = new FileReader();
      reader.onload = (function(theFile) {
        return function(e) {
       	  $("#loading").hide();
          makeRequest(reader.result)
        };
      })(f);
      reader.readAsText(f);
      $("#loading").show();
    }
  }

  document.getElementById('input_file')
    .addEventListener('change', handleFileSelect, false);

  function makeRequest(fileContent) {

    var cb_NC = 'false';
    var cb_ADJ = 'false';

    if(document.getElementById('cb_NC').checked){
        var cb_NC = 'true';
    }
    if(document.getElementById('cb_ADJ').checked){
        var cb_ADJ = 'true';
    }

    if(document.getElementById('lang_DE').checked){
      if(document.getElementById('cb_NC').checked){
        alert("At this time chunks are not supported for lang", lang )
        }
      var lang = "de";
      }
     else if(document.getElementById('lang_EN').checked){
		var lang = "en"
          }
    else{lang = undefined}

  if (cb_NC == 'true') {
  var url = "ws://127.0.0.1:8080/chunks?lang=" + lang
  } else {
  var url = "ws://127.0.0.1:8080/tokens?lang=" + lang + "&adj=" + cb_ADJ
  }


  if (lang != undefined){
  var socket = new WebSocket(url)

  socket.onopen = function (event) {
    socket.send(fileContent)
    };

  socket.onmessage = function(event) {
    addText(event.data)
    };
  }else {alert ("Could not create socket")
  }

}

  function addText(passedText){

    var stripped = passedText.replace(/[^\w\s]/gi, '')
    var curr_div = $("#" + stripped)
    count = wordslist[stripped]
    if (isNaN(count)){
      wordslist[stripped ]= 1;
      console.log("adding: ", stripped)
    } else {
      wordslist[stripped] += 1
    }

    console.log(stripped, wordslist[stripped], count)

//    var orientation = ['vertical-text', ''];
 //   var rand_orient = orientation[Math.floor(Math.random() * orientation.length)];
if (wordslist[stripped] > 4) {
		 var fontSize = parseInt(curr_div.css("font-size"));
		 if (isNaN(fontSize)){
			 fontSize = 10;
			}
		  curr_div.animate({
                        fontSize: fontSize + 2
                    }, 500);

    if (wordslist[stripped] < 10){
    }
    if (wordslist[stripped] > 10 && wordslist[stripped] <= 25){
      curr_div.css("color", "green");
    } else if (wordslist[stripped] > 25){
      curr_div.css("color", "red");
    }

} else if(wordslist[stripped] ==  3){
    passedText = " " + passedText + " "
//    if (rand_orient != ''){
//      curr_div.css('margin-left',10+'px');
//      curr_div.height(curr_div.width());
//    }
    jQuery('<div/>', {
    id: stripped,
//    class: rand_orient,
    floating: 'right',
    text: passedText,
}).css("display", "inline-block").addClass("word")
        .appendTo('.wc-container');
    }
  }

}

)();

