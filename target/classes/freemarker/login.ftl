<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <#include "common.ftl" />
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            margin-top: 5%;
        }

        .card {
            width: 100%;
            max-width: 400px;
            margin: 0 auto;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .card-body {
            padding: 20px;
        }

        .card-title {
            font-size: 1.5rem;
            text-align: center;
            margin-bottom: 20px;
        }

        .form-label {
            margin-bottom: 0.5rem;
        }

        .form-control {
            width: 100%;
            padding: 0.375rem 0.75rem;
            margin-bottom: 1rem;
            border: 1px solid #ced4da;
            border-radius: 0.25rem;
            box-sizing: border-box;
        }

        .btn-primary {
            color: #fff;
            background-color: #007bff;
            border-color: #007bff;
            padding: 0.375rem 0.75rem;
            font-size: 1rem;
            line-height: 1.5;
            border-radius: 0.25rem;
            cursor: pointer;
            display: block;
            width: 100%;
        }

        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }

        .text-center {
            text-align: center;
        }

        .mt-3 {
            margin-top: 1rem;
        }
    </style>
    <script>
        $(document).ready(function () {
            $("#login-form").on('submit', function (e) {
                e.preventDefault();
                // 获取表单数据
                const formData = {
                    username: $('#username').val(),
                    password: $('#password').val(),
                };
                // 发送 Ajax 请求
                $.ajax({
                    url: '/api/login',  // 后端接收请求的 URL
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(formData),
                    success: function(response) {
                        // 在成功后的操作，根据需要处理后端返回的响应
                        if (response && response.code === 0) {
                            messageBox.showSuccess("Login succeed");
                            window.open("/", "_self");
                        } else {
                            messageBox.showError(response.message);
                        }
                    },
                    error: function(error) {
                        // 处理错误
                        console.log(error);
                    }
                });
            });
        });
    </script>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Login</h5>
            <form id="login-form">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary">Login</button>
            </form>
            <div class="text-center mt-3">
                <p>Don't have an account? <a href="/register">Register</a></p>
                <p><a href="/forget" id="forgot-password-link">Forgot Password?</a></p>
            </div>
        </div>
    </div>
</div>

</body>
</html>
