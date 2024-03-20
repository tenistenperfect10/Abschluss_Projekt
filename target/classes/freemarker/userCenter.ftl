<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Center</title>
    <#include "common.ftl" />
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            height: 100vh;
        }

        .container-fluid {
            display: flex;
            flex-direction: column;
            height: 100%;
        }

        .dashboard-title {
            background-color: #3f6995;
            color: #fff;
            text-align: center;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100px;
        }

        .row {
            flex: 1;
            display: flex;
            height: 100%;
        }

        .sidebar {
            width: 200px;
            margin: 20px;
            background-color: #f8f9fa;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .main-content {
            flex: 1;
            margin: 20px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .navList {
            padding-top: 20px;
        }

        .navList li {
            height: 60px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .nav-link {
            width: 100%;
            display: block;
            padding: 10px;
            color: #007bff;
            text-decoration: none;
            border-bottom: 1px solid #ddd;
            transition: background-color 0.3s;
        }

        .nav-link.active {
            background-color: #fff;
            border-right: 4px solid #007bff;
        }

        .nav-link:hover {
            background-color: #f8f9fa;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: left;
        }

        th {
            background-color: #3f6995;
            color: #fff;
        }

        tbody tr:hover {
            background-color: #f5f5f5;
        }

        .btn {
            padding: 8px 12px;
            margin-right: 5px;
            cursor: pointer;
            border: none;
            border-radius: 4px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 14px;
            transition-duration: 0.4s;
        }

        .btn-info {
            background-color: #4CAF50;
            color: white;
            border: 1px solid #4CAF50;
        }

        .btn-info:hover {
            background-color: white;
            color: #4CAF50;
        }

        .btn-danger {
            background-color: #f44336;
            color: white;
            border: 1px solid #f44336;
        }

        .btn-danger:hover {
            background-color: white;
            color: #f44336;
        }
        .modal {
            display: none;
        }
    </style>
    <script>
        var selectedUserName = null;
        function assignPermission(username) {
            selectedUserName = username;
            $("#assignPermissionModal").show();
        }

        function deleteUser(username) {
            selectedUserName = username;
            $("#deleteUserModal").show();
        }

        function confirmAssignUser() {
            $.ajax({
                url: "/api/assign",
                method: "POST",
                data: {username: selectedUserName},
                success: function(response) {
                    // After the successful operation, process the response returned by the backend as needed
                    if (response && response.code === 0) {
                        messageBox.showSuccess("Assign succeed");
                        location.reload();
                    } else {
                        messageBox.showError(response.message);
                    }
                },
                error: function(error) {
                    // process error
                    console.log(error);
                }
            });
        }

        function confirmDeleteUser() {
            $.ajax({
                url: "/api/deleteUser",
                method: "POST",
                data: {username: selectedUserName},
                success: function(response) {
                    // After the successful operation, process the response returned by the backend as needed
                    if (response && response.code === 0) {
                        messageBox.showSuccess("Delete succeed");
                        location.reload();
                    } else {
                        messageBox.showError(response.message);
                    }
                },
                error: function(error) {
                    // process error
                    console.log(error);
                }
            });
        }

    </script>
</head>
<body>
<div class="container-fluid">
    <div class="dashboard-title">
        <h1 style="background: transparent;">User Center
            <#if username?exists>
                <span style="color: coral;">(${username})</span>
            </#if>
        </h1>
    </div>
    <div class="row">
        <nav class="sidebar">
            <ul class="navList">
                <li class="nav-item">
                    <a class="nav-link active" href="#">
                        Permission management
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="/index">
                        View meetings
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="/logout">
                        Logout
                    </a>
                </li>
            </ul>
        </nav>

        <main role="main" class="main-content">
            <table class="table table-striped table-bordered rounded">
                <thead>
                <tr>
                    <th>username</th>
                    <th>operation</th>
                </tr>
                </thead>
                <tbody>
                    <#if users?has_content>
                        <#list users as user >
                            <tr>
                                <td>${user["username"]}</td>
                                <td>
                                    <button class="btn btn-info" onclick="assignPermission('${user["username"]}')">
                                        <#if user.canEdit?exists && user.canEdit == 1>
                                            Release editing permissions
                                        <#else>
                                            Assign editing permissions
                                        </#if>
                                    </button>
                                    <button class="btn btn-danger" onclick="deleteUser('${user["username"]}')">Delete user</button>
                                </td>
                            </tr>
                        </#list>
                    </#if>
                </tbody>
            </table>
        </main>
        <dialog id="assignPermissionModal" class="modal">
            <div class="modal-dialog">
                <div class="modal-title">
                    <h3>Change Permissions</h3>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-info" onclick="confirmAssignUser()">Assign</button>
                    <button class="btn btn-info" onclick="$('#assignPermissionModal').hide()">Cancel</button>
                </div>
            </div>
        </dialog>

        <!-- Delete User Modal -->
        <dialog id="deleteUserModal" class="modal">
            <div class="modal-dialog">
                <div class="modal-title">
                    <h3>Delete User</h3>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-danger" onclick="confirmDeleteUser()">Delete</button>
                    <button class="btn btn-info" onclick="$('#deleteUserModal').hide()">Cancel</button>
                </div>
            </div>
        </dialog>
    </div>
</div>
</body>
</html>
