function onLoad(id, filename) {
    fetch(filename)
      .then(response => response.text())
      .then(data => {
            var csvData = CSV.parse(data);
            loadTable(id, csvData)
      });
}

function loadTable(id, csvData) {

    var columnDefinitions = [];

    for (var i = 0; i < csvData[0].length; i++) {
        var columnName = csvData[0][i];
        columnDefinitions.push({
            name : columnName,
            label : columnName,
            getValue : function(params) {
                return params.row[params.column.name];
            }
        });
     }

    var rowData = [];
    for (var i = 1; i < csvData.length; i++) {
          var row = { id : i };
          for (var j = 0; j < csvData[i].length; j++) {
            row[csvData[0][j]] = csvData[i][j];
          }
          rowData.push(row);
    }

    var gridElement = React.createElement(window.NgUiGrid.default.ThemeProvider, {},
    React.createElement(
        window.NgUiGrid.default.DatePickerProvider,
        {},
         React.createElement(window.NgUiGrid.default.Grid, {
        // Mandatory properties
        id: "id",
        columns : columnDefinitions,
        rows :rowData,
        // We only need this property to have export
        exportable : {
            fileFormat: "TSV",
            filePrefix: 'romhacks'
        }
        })
    ));

    ReactDOM.render(gridElement, document.getElementById(id));
}