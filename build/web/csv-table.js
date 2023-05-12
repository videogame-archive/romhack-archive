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
            nowrap : true,
            getValue : function(params) {
                return params.row[params.column.name];
            },
            renderDOMValue : function(params) {
                const value = params.row[params.column.name];
                const valueNode = document.createTextNode(value);
                var node = null;
                if(value.startsWith("http")) {
                    node = document.createElement('a');
                    node.appendChild(valueNode);
                    node.href = value;
                } else {
                    node = document.createTextNode(value);
                }
                params.container.appendChild(node);
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
        header: "Romhack Index",
        columns : columnDefinitions,
        rows : rowData,
        // We only need this property to have export
        exportable : {
            fileFormat: "TSV",
            filePrefix: 'romhacks'
        }
        })
    ));

    ReactDOM.render(gridElement, document.getElementById(id));
}