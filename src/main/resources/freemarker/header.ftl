<a href="/speaker">speaker</a> | <a href="/speech">speech</a> | <a href="/logout">logout</a> | <button onclick="toggleAndFetch()"> logs </button>
<#--<link rel="stylesheet" href="/font-awasome.min.css">-->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" integrity="sha512-SfTiTlX6kk+qitfevl/7LibUOeJWlt9rbyDn92a1DqWOw9vWG2MFoays0sgObmWazO5BQPiFucnnEAjpAB+/Sw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<div id="panel">
    <table id="dataTable">
        <thead id="dataHeader">
        </thead>
        <tbody id="dataBody">
        <!-- Add rows as needed -->
        </tbody>
    </table>
</div>

<script>
    function toggleAndFetch() {
        var panel = document.getElementById('panel');

        // Toggles the display and hide state of the panel
        panel.style.display = (panel.style.display === 'none' || panel.style.display === '') ? 'block' : 'none';

        // If the panel is displayed, initiate a background POST request
        if (panel.style.display === 'block') {
            fetchData();
        }
    }

    function fetchData() {
        // URL of the backend POST interface
        var apiUrl = '/logs'

        //  Asynchronous POST requests using the Fetch API
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Additional request headers can be added as needed
            }
        })
            .then(response => response.json())
            .then(data => {
                // Processing the returned data and rendering the form
                renderTable(data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }

    function renderTable(data) {
        var tableBody = document.getElementById('dataBody');
        var tableHeader = document.getElementById('dataHeader');

        //  Empty form content
        tableBody.innerHTML = '';
        tableHeader.innerHTML = '';

        var headerRow = document.createElement('tr');
        // Iterate over the data and create table rows
        data.header.forEach(item => {
            // Iterate through each property and create table cells
                var cell = document.createElement('td');
                cell.textContent = item;
                headerRow.appendChild(cell);

            // Adding rows to a table body
            tableHeader.appendChild(headerRow);
        });


        // Iterate over the data and create table rows
        data.data.forEach(item => {
            var row = document.createElement('tr');

            // Iterate through each property and create table cells
            for (var key in item) {
                var cell = document.createElement('td');
                cell.textContent = item[key];
                row.appendChild(cell);
            }

            // Adding rows to a table body
            tableBody.appendChild(row);
        });
    }
</script>
