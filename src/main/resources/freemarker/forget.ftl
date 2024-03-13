<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password Page</title>
    <#include "common.ftl"/>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
        }

        .forgot-password-container {
            background-color: #f8f9fa;
            padding: 50px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            margin: 0 auto;
            text-align: center;
        }

        .forgot-password-title {
            font-size: 24px;
            margin-bottom: 30px;
        }

        .forgot-password-form {
            text-align: left;
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

        .forgot-password-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            color: #007bff;
        }
    </style>
    <script>
        $(document).ready(function () {
            $("#forgot-password-form").on('submit', function (e) {
                e.preventDefault();

                // 获取表单数据
                var username = $('#username').val();
                var newPassword = $('#new_password').val();
                var confirmNewPassword = $('#confirm_new_password').val();

                // 客户端验证逻辑
                if (!username) {
                    messageBox.showError('Please enter username.');
                    return;
                }

                if (!newPassword || newPassword.length < 6) {
                    messageBox.showError('The new password must have at least 6 characters.');
                    return;
                }

                if (newPassword !== confirmNewPassword) {
                    messageBox.showError('Password inconsistency.');
                    return;
                }

                // 发送 Ajax 请求
                $.ajax({
                    url: '/api/updatePwd',  // 后端接收忘记密码请求的 URL
                    type: 'POST',
                    data: {
                        username,
                        newPassword,
                        confirmNewPassword,
                    },
                    success: function (response) {
                        // 处理请求成功的逻辑
                        console.log(response);
                        if (response && response.code === 0) {
                            messageBox.showSuccess('Succeed! Please login.');
                        } else {
                            messageBox.showError(response.message);
                        }
                    },
                    error: function (error) {
                        // 处理请求失败的逻辑
                        messageBox.showError(error);
                    }
                });
            });
        });

    </script>
</head>
<body>

<div class="container mt-5" style="padding-top: 5%">
    <div class="forgot-password-container">
        <h2 class="forgot-password-title">Forgot Password</h2>
        <form id="forgot-password-form" class="forgot-password-form">
            <div class="mb-3">
                <label for="username" class="form-label">Username</label>
                <input type="text" class="form-control" id="username" name="username" required>
            </div>
            <div class="mb-3">
                <label for="new_password" class="form-label">New Password</label>
                <input type="password" class="form-control" id="new_password" name="new_password" required>
            </div>
            <div class="mb-3">
                <label for="confirm_new_password" class="form-label">Confirm New Password</label>
                <input type="password" class="form-control" id="confirm_new_password" name="confirm_new_password" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Confirm Reset</button>
        </form>
        <a href="/login" class="forgot-password-link">Back to Login</a>
    </div>
</div>

</body>
</html>
