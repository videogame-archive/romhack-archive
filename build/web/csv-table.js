function onLoad(id, filename) {

    var datatable = document.createElement("div");
        datatable.className = "mdc-data-table";

//    var datatableContainer = document.createElement("div");
//        datatableContainer.className = "mdc-data-table__table-container";
//        datatable.appendChild(datatableContainer);

    // code to fetch the csv file
    fetch(filename)
      .then(response => response.text())
      .then(data => {
        // Parse the CSV string
        var csvData = CSV.parse(data);

        // Create an HTML table
        var table = document.createElement("table");
            table.className = "mdc-data-table__table";
        // Create the header row
        var thead = document.createElement("thead");
        var headerRow = document.createElement("tr");
            headerRow.className = "mdc-data-table__header-row";
        for (var i = 0; i < csvData[0].length; i++) {
          var headerCell = document.createElement("th");
            headerCell.className = "mdc-data-table__header-cell";
            headerCell.setAttribute("role", "columnheader");
            headerCell.setAttribute("scope", "col");
          headerCell.textContent = csvData[0][i];
          headerRow.appendChild(headerCell);
        }
        thead.appendChild(headerRow);
        table.appendChild(thead);
        // Create the data rows
        var tbody = document.createElement("tbody");
            tbody.className = "mdc-data-table__content";

        for (var i = 1; i < csvData.length; i++) {
          var dataRow = document.createElement("tr");
              dataRow.className = "mdc-data-table__row";
          for (var j = 0; j < csvData[i].length; j++) {
            var dataCell = document.createElement("td");
                dataCell.className = "mdc-data-table__cell";
            if(csvData[i][j] !== undefined) {
                dataCell.textContent = csvData[i][j];
            } else {
                dataCell.textContent = "";
            }
            dataRow.appendChild(dataCell);
          }
          tbody.appendChild(dataRow);
        }

        table.appendChild(tbody);
        datatable.appendChild(table);


        // Add the table to the container element by ID
        var container = document.getElementById(id);
        container.appendChild(datatable);
        });
}