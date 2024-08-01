<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>wechatpay</title>
</head>
<body>
<h1>微信支付</h1>
<div id = "myQrcode"></div>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
    <script>	jQuery('#myQrcode').qrcode({
            text	: "${codeUrl}"
        });
    </script>
</body>
</html>
