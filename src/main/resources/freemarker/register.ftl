<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Page</title>
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

        .form-check-input {
            margin-top: 0.25rem;
            margin-right: 0.25rem;
        }
    </style>
    <script>
        $(document).ready(function () {

            $('#key_box').hide();

            $('input[name="userType"]').change(function () {
                var userType = $(this).val();
                if (userType === '1') {
                    $('#key_box').show();
                } else {
                    $('#key_box').hide();
                }
            });

            $("#register-form").on('submit', function (e) {
                e.preventDefault();

                // 获取表单数据
                var username = $('#username').val();
                var password = $('#password').val();
                var confirm_password = $('#confirm_password').val();
                var userType = $('input[name="userType"]:checked').val(); // 获取选中的用户类型
                var verifyCode = $("#confirm_key").val();
                if (userType == 1 && verifyCode.length === 0) {
                    messageBox.showError('Please enter your secret key.');
                    return;
                }

                // 客户端验证逻辑
                if (username.length < 4) {
                    messageBox.showError('The username must have at least 4 characters.');
                    return;
                }

                if (password.length < 6) {
                    messageBox.showError('The password must have at least 6 characters.');
                    return;
                }

                if (password !== confirm_password) {
                    messageBox.showError('Password inconsistency.');
                    return;
                }

                // 发送 Ajax 请求
                $.ajax({
                    url: '/api/register',  // 后端接收注册请求的 URL
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        username,
                        password,
                        userType,
                        verifyCode,
                    }),
                    success: function(response) {
                        // 处理注册成功的逻辑
                        if (response && response.code === 0) {
                            messageBox.showSuccess("Register succeed");
                            window.open("/login", "_self");
                        } else {
                            messageBox.showError(response.message);
                        }
                    },
                    error: function(error) {
                        // 处理注册失败的逻辑
                        messageBox.showError('Register failed');
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
            <h5 class="card-title text-center">Register</h5>
            <form method="post" id="register-form">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
                <div class="mb-3">
                    <label for="confirm_password" class="form-label">Confirm Password</label>
                    <input type="password" class="form-control" id="confirm_password" name="confirm_password" required>
                </div>
                <div class="mb-3" id="key_box">
                    <label for="confirm_key" class="form-label">Your Secret Key</label>
                    <input type="text" class="form-control" id="confirm_key" name="confirm_key">
                </div>
                <div class="mb-3" style="display: flex; margin-bottom: 20px;">
                    <div class="form-check form-check-inline" style="margin-right: 20px;">
                        <input class="form-check-input" type="radio" name="userType" id="userTypeGuest" value="0" checked>
                        <label class="form-check-label" for="userTypeGuest">Regular User</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="userType" id="userTypeAdmin" value="1">
                        <label class="form-check-label" for="userTypeAdmin">Administrator</label>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary btn-block">Register</button>
            </form>
            <div class="text-center mt-3">
                <p>Already have an account? <a href="/login">Login</a></p>
            </div>
        </div>
    </div>
</div>

</body>
</html>
