<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body id = "k">
    <?php
        echo "hello world<br/>";
        echo date('Y-m-d');
    ?>
    <button onclick= "getTime()" id = "k">   The time is?</button>
    <script>
    function getTime(){
        document.getElementById('demo').innerHTML = Date();
        document.getElementById('demo1').innerHTML = "fuck you";
        document.getElementById('k').style.padding = "10px";
    }
    </script>
    <p id = "demo"></p>
    <p id = "demo1"></p>

</body>


</html>