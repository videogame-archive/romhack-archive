function onLoad(id, filename) {
    // code to fetch the csv file
    fetch(filename)
      .then(response => response.text())
      .then(data => {
        // Parse the CSV string
        var csvData = CSV.parse(data);

        // Create an HTML table
        var table = document.createElement("table");

        // Create the header row
        var headerRow = document.createElement("tr");
        for (var i = 0; i < csvData[0].length; i++) {
          var headerCell = document.createElement("th");
          headerCell.textContent = csvData[0][i];
          headerRow.appendChild(headerCell);
        }
        table.appendChild(headerRow);

        // Create the data rows
        for (var i = 1; i < csvData.length; i++) {
          var dataRow = document.createElement("tr");
          for (var j = 0; j < csvData[i].length; j++) {
            var dataCell = document.createElement("td");
            if(csvData[i][j] !== undefined) {
                dataCell.textContent = csvData[i][j];
            } else {
                dataCell.textContent = "";
            }
            dataRow.appendChild(dataCell);
          }
          table.appendChild(dataRow);
        }

        // Add the table to the container element by ID
        var container = document.getElementById(id);
        container.appendChild(table);
        });
}