(function() {

  document.getElementById("Submit").onclick = function(event) {
    event.preventDefault();
      var file = $("#input_file").val();
      var text = $("#input_text").val();

      if (file!=""){
        $.get(file, function(data) {
          console.log(data)
        }, 'text');
      }

      if (file != "" && text !="insert text or url"){
         console.log(file);
         console.log(text);
         alert('Please enter only one type of text/url or file');
         return false;
      }

      if ($('#cb_NC').checked){
        var cb_NC = 'True';}
      if ($('#cb_ADJ').checked){
        var cb_ADJ = 'True';}

      if ($('#lang_DE').checked) {
      //TODO put request hook here
          }
      if ($('#lang_EN').checked) {
      //TODO put request hook here
          }
      return false;
  };

  function makeRequest(url) {
    // This function starts the request
    // also shows the loading gif and hides the current table
    // it will be newly shown when the request finishes
    httpRequest = new XMLHttpRequest();
    $("#loading").show();
    $(".freq_table").hide()
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
          createTable(Text);}
      else {
          alert('There was a problem with the request.');
      }
    }
  }

}

)();
