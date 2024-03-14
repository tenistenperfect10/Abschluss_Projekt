<style>
    body {
        font-family: Arial, sans-serif;
        background-color: #f4f4f4;
        color: #333;
        margin: 0;
        padding: 0;
    }
    h1 {
        text-align: center;
        padding: 20px 0;
        background-color: #007bff;
        color: #fff;
        margin: 0;
    }
    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    table, th, td {
        border: 1px solid #ddd;
    }
    th, td {
        padding: 15px;
        text-align: left;
    }
    th {
        background-color: #007bff;
        color: #fff;
    }
</style>
<script src="/jquery.min.js"></script>
<Script>
    $(document).ready(function () {
        /**
         * message对象的构造方法
         * @constructor
         */
        function MessageBox() {
            this.container = $('<div>', { id: 'message-container' });

            // 初始化消息容器
            this.initContainer = function () {
                this.container.css({
                    position: 'fixed',
                    top: '50%',
                    left: '50%',
                    backgroundColor: '#f8d7da',
                    color: '#721c24',
                    transform: 'translate(-50%, -50%)',
                    padding: '10px',
                    border: '1px solid #f5c6cb',
                    borderRadius: '5px',
                    display: 'none'
                });

                $('body').append(this.container);
            };

            // 显示消息
            this.show = function (message, type) {
                // 设置不同类型的样式
                if (type === 'error') {
                    this.container.css({
                        'background-color': '#f8d7da',
                        'color': '#721c24',
                        'border-color': '#f5c6cb'
                    });
                } else if (type === 'success') {
                    this.container.css({
                        'background-color': '#d4edda',
                        'color': '#155724',
                        'border-color': '#c3e6cb'
                    });
                }

                // 显示消息
                this.container.text(message).fadeIn().delay(2000).fadeOut();
            };

            // 显示错误消息
            this.showError = function (message) {
                this.show(message, 'error');
            };

            // 显示成功消息
            this.showSuccess = function (message) {
                this.show(message, 'success');
            };

            // 调用初始化
            this.initContainer();
        }

        /**
         * 全局挂载message对象
         */
        if (!window.messageBox) {
            window.messageBox = new MessageBox();
        }
    })
</Script>