<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>支付</title>
</head>
<body>
<div id = "myQrcode"></div>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
    <script>	jQuery('#myQrcode').qrcode({
            text	: "weixin://wxpay/bizpayurl?pr=OrGF0SXz1"
        });
    </script>
</body>
</html>
