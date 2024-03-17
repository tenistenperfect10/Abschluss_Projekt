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

        // 切换面板的显示和隐藏状态
        panel.style.display = (panel.style.display === 'none' || panel.style.display === '') ? 'block' : 'none';

        // 如果面板显示，则发起后台 POST 请求
        if (panel.style.display === 'block') {
            fetchData();
        }
    }

    function fetchData() {
        // 后台 POST 接口的 URL
        var apiUrl = '/logs'

        // 使用 Fetch API 发起异步 POST 请求
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // 可以根据需要添加其他请求头
            }
        })
            .then(response => response.json())
            .then(data => {
                // 处理返回的数据，渲染表格
                renderTable(data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }

    function renderTable(data) {
        var tableBody = document.getElementById('dataBody');
        var tableHeader = document.getElementById('dataHeader');

        // 清空表格内容
        tableBody.innerHTML = '';
        tableHeader.innerHTML = '';

        var headerRow = document.createElement('tr');
        // 遍历数据并创建表格行
        data.header.forEach(item => {
            // 遍历每个属性并创建表格单元格
                var cell = document.createElement('td');
                cell.textContent = item;
                headerRow.appendChild(cell);

            // 将行添加到表格体中
            tableHeader.appendChild(headerRow);
        });


        // 遍历数据并创建表格行
        data.data.forEach(item => {
            var row = document.createElement('tr');

            // 遍历每个属性并创建表格单元格
            for (var key in item) {
                var cell = document.createElement('td');
                cell.textContent = item[key];
                row.appendChild(cell);
            }

            // 将行添加到表格体中
            tableBody.appendChild(row);
        });
    }
</script>
